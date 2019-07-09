package com.auxing.znhy.controller;


import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Auth;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
@Api(tags = "菜单操作接口",value = "/auth")
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    //新增菜单
    @ApiOperation(value = "新增菜单接口")
    @RequestMapping(value = "/saveAuths", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增菜单成功", response = Auth.class),
            @ApiResponse(code = 500, message = "新增失败")
    })
    public JSONObject saveAuths(@RequestParam @ApiParam(value = "上级菜单", required = true) String superAuth,@RequestParam @ApiParam(value = "父ID", required = true) String parentId,@RequestParam @ApiParam(value = "菜单名称", required = true) String name,@RequestParam @ApiParam(value = "URL", required = true) String url,@RequestParam @ApiParam(value = "排序", required = true) Integer sort,@ApiParam(value = "备注") @RequestParam(value = "des",required = false) String des,@RequestParam @ApiParam(value = "图标", required = true) String icon){
        JSONObject respJson = new JSONObject();
        try {
        	Auth a = new Auth();
        	a.setAuthName(name);
        	QueryWrapper<Auth> wrapper = new QueryWrapper<Auth>(a);
        	a = this.authService.getOne(wrapper);
        	if(a != null){
        		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "菜单名称已存在");
                return respJson;
        	}
        	
            Auth auth = new Auth();
            auth.setSuperAuth(superAuth);
            auth.setParentId(parentId);
            auth.setAuthName(name);
            auth.setUrl(url);
            auth.setSort(sort);
            auth.setDes(des);
            auth.setIcon(icon);
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            auth.setAuthId(uuid);
            authService.save(auth);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "新增菜单成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "新增失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //查询菜单列表
    @ApiOperation(value = "查询菜单列表接口")
    @RequestMapping(value = "/getAuthList", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getAuthList(@RequestParam @ApiParam(value = "当前页", required = true)Long currentPage){
        JSONObject respJson = new JSONObject();
        try {
            Page page = new Page();
            page.setSize(10);
            page.setCurrent(currentPage);
            IPage<Auth> pageAuth = authService.page(page, null);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", pageAuth);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //查询菜单列表
    @ApiOperation(value = "新增角色查询所有菜单树接口(新增用)")
    @RequestMapping(value = "/getAuthTree", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getAuthTree(){
        JSONObject respJson = new JSONObject();
        try {
            //List<Auth> authList = this.authService.list(null);
            //原始数据
            List<ZTreeNode> zTreeNodeList = this.authService.findTreeDetails();
            //最终数据
            List<ZTreeNode> authList = new ArrayList<ZTreeNode>();
            // 先找到所有的一级菜单
            for (int i = 0; i < zTreeNodeList.size(); i++) {
                // 一级菜单没有parentId
                if (zTreeNodeList.get(i).getpId() == null) {
                    authList.add(zTreeNodeList.get(i));
                }
            }
            // 为一级菜单设置子菜单，getChild是递归调用的
            for (ZTreeNode tree : authList) {
                List<ZTreeNode> list = departmentService.getChild(tree.getId(), zTreeNodeList);
                tree.setChildren(list);
            }
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", authList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //查询菜单列表
    @ApiOperation(value = "新增角色查询所有菜单树接口(修改用)")
    @RequestMapping(value = "/getChangeAuthTree", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getChangeAuthTree(@RequestParam @ApiParam(value = "角色Id",required = true) String roleId){
        JSONObject respJson = new JSONObject();
        try {
            //List<Auth> authList = this.authService.list(null);
            //该角色已经绑定的菜单的list
            List<Auth> bindAuthList = roleauthService.getBindAuthList(roleId);

            //原始数据
            List<ZTreeNode> zTreeNodeList = this.authService.findTreeDetails();
            //最终数据
            List<ZTreeNode> authList = new ArrayList<ZTreeNode>();
            // 先找到所有的一级菜单
            for (int i = 0; i < zTreeNodeList.size(); i++) {
                // 一级菜单没有parentId
                if (zTreeNodeList.get(i).getpId() == null) {
                    authList.add(zTreeNodeList.get(i));
                }
            }
            // 为一级菜单设置子菜单，getChild是递归调用的
            for (ZTreeNode tree : authList) {
                List<ZTreeNode> list = departmentService.getChild(tree.getId(), zTreeNodeList);
                tree.setChildren(list);
            }
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("allList",authList);
            resultMap.put("readyList",bindAuthList);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", resultMap);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //更新菜单
    @ApiOperation(value = "更新菜单接口")
    @RequestMapping(value = "/updateAuth",method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功", response = Auth.class),
            @ApiResponse(code = 500, message = "修改失败")
    })
    public JSONObject updateAuth(@RequestParam @ApiParam(value = "应该修改的id",required = true)Integer id,
    		@RequestParam @ApiParam(value = "上级菜单", required = true) String superAuth,
    		@RequestParam @ApiParam(value = "菜单名称", required = true) String name,
    		@RequestParam @ApiParam(value = "URL", required = true) String url,
    		@RequestParam @ApiParam(value = "排序", required = true) Integer sort,
    		@ApiParam(value = "备注") @RequestParam(value = "des", required = false) String des,
    		@RequestParam @ApiParam(value = "图标", required = true) String icon,
    		@RequestParam @ApiParam(value = "上级菜单ID", required = true) String parentId
    		){
        JSONObject respJson = new JSONObject();
        try {
        	Auth a = new Auth();
        	a.setAuthName(name);
        	QueryWrapper<Auth> wrapper = new QueryWrapper<Auth>(a);
        	a = this.authService.getOne(wrapper);
        	if(a != null && a.getId() != id){
        		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "菜单名称已存在");
                return respJson;
        	}
            Auth auth = new Auth();
            auth.setId(id);
            auth.setParentId(parentId);
            auth.setSuperAuth(superAuth);
            auth.setAuthName(name);
            auth.setUrl(url);
            auth.setSort(sort);
            auth.setDes(des);
            auth.setIcon(icon);
            authService.updateById(auth);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "修改成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "修改失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //删除菜单
    @ApiOperation(value = "删除菜单接口")
    @RequestMapping(value = "/delAuth",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "删除成功", response = Auth.class),
            @ApiResponse(code = 500, message = "删除失败")
    })
    public JSONObject delAuth(@RequestParam @ApiParam(value = "需要删除的菜单的id",required = true) List<Integer> list){
        JSONObject respJson = new JSONObject();
        try {
            for (Integer id:list){
                Auth auth = authService.getById(id);
                roleauthService.removeByAuthId(auth.getAuthId());
            }
            authService.removeByIds(list);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "删除成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "根据登录用户查询该用户可见所有菜单")
    @RequestMapping(value = "/getAdminAuth",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getAdminAuth(@RequestParam @ApiParam(value ="登录用户的ID",required = true) String modId){
        JSONObject respJson = new JSONObject();
        try{
            List<Auth> authList = authService.getBindList(modId);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", authList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "根据登录用户查询该用户可见一级菜单(主页面)")
    @RequestMapping(value = "/getAdminLevelOneAuth",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getAdminLevelOneAuth(){
    	User loginUser = (User) request.getSession().getAttribute("user");
        JSONObject respJson = new JSONObject();
        try{
            List<Auth> authList = authService.getBindLevelOneList(loginUser.getMoid());
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", authList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "根据登录用户查询该用户可见非一级菜单(菜单页面)")
    @RequestMapping(value = "/getAdminLevelOtherAuth",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Auth.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getAdminLevelOtherAuth(@RequestParam @ApiParam(value ="登录用户的ID",required = true) String modId){
        JSONObject respJson = new JSONObject();
        try{
            List<Auth> authList = authService.getBindLevelOtherList(modId);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", authList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }
}
