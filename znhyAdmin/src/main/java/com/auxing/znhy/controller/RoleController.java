package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Role;
import com.auxing.znhy.entity.Roleauth;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.HttpUtil;
import com.auxing.znhy.util.JsonHelperUtil;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
@Api(tags = "角色操作接口",value = "/role")
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    //新增角色
    @ApiOperation(value = "新增角色并绑定菜单接口")
    @RequestMapping(value = "/saveRoles", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增角色成功", response = Role.class),
            @ApiResponse(code = 500, message = "新增失败")
    })
    public JSONObject saveRoles(@RequestParam @ApiParam(value = "角色名称", required = true) String name, @RequestParam @ApiParam(value = "排序", required = true) String sort, @ApiParam(value = "备注") @RequestParam(value = "des",required = false) String des, @ApiParam(value = "绑定菜单的ID") @RequestParam(value = "list",required = false) List<String> list){
        JSONObject respJson = new JSONObject();
        try {
        	Role r = new Role();
        	r.setName(name);
        	QueryWrapper<Role> wrapper = new QueryWrapper<Role>(r);
        	r = this.roleService.getOne(wrapper);
        	if(r != null){
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "角色名称已存在");
                return respJson;
        	}
        	
            Role role = new Role();
            role.setName(name);
            role.setSort(sort);
            role.setDes(des);
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
            role.setRoleId(uuid);
            roleService.save(role);
            if(list != null && list.size() != 0){
                for(String id:list){
                    Roleauth roleauth = new Roleauth();
                    roleauth.setRoleId(uuid);
                    roleauth.setAuthId(id);
                    roleauthService.save(roleauth);
                }
            }
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "新增角色成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "新增失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //测试
    //新增角色
    @ApiOperation(value = "测试测试测试测试测试测试测试测试接口")
    @RequestMapping(value = "/saveTestRoles", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "新增角色成功", response = Role.class),
            @ApiResponse(code = 500, message = "新增失败")
    })
    public ResponseEntity<JSONObject> saveTestRoles(HttpServletRequest request, HttpServletResponse response){
        //接受请求，定义响应
        JSONObject reqJson = HttpUtil.ProcessRequest(request);
        JSONObject respJson = new JSONObject();
        try {
            respJson.put("code", ResultCode.SUCCESS);
            System.out.println(reqJson.getString("des"));
        }catch (Exception e){
            reqJson.put("code",ResultCode.ERROR);
        }
        return new ResponseEntity<>(respJson,
                JsonHelperUtil.getJSONHeaders(), HttpStatus.OK);
    }

    //修改角色
    @ApiOperation(value = "修改角色接口")
    @RequestMapping(value = "/updateRoles", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "角色修改成功", response = Role.class),
            @ApiResponse(code = 500, message = "修改失败")
    })
    public  JSONObject updateRoles(@RequestParam @ApiParam(value = "应该修改的角色的roleId",required = true) String roleId,@RequestParam @ApiParam(value = "角色名称", required = true) String name, @RequestParam @ApiParam(value = "排序", required = true) String sort, @ApiParam(value = "备注") @RequestParam(value = "des",required = false) String des, @ApiParam(value = "绑定菜单的ID") @RequestParam(value = "list",required = false) List<String> list){
        JSONObject respJson = new JSONObject();
        try {
        	Role r = new Role();
        	r.setName(name);
        	QueryWrapper<Role> wrapper = new QueryWrapper<Role>(r);
        	r = this.roleService.getOne(wrapper);
        	if(r != null && !r.getRoleId().equals(roleId)){
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "角色名称已存在");
                return respJson;
        	}
        	
            Role role = new Role();
            role.setRoleId(roleId);
            role.setName(name);
            role.setSort(sort);
            role.setDes(des);
            roleService.updateByRoleId(role);
            roleauthService.removeByRoleId(roleId);
            if(list != null && list.size() != 0){
                for (String changeId : list){
                    Roleauth roleauth = new Roleauth();
                    roleauth.setRoleId(roleId);
                    roleauth.setAuthId(changeId);
                    roleauthService.save(roleauth);
                }
            }
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "修改角色成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "修改失败");
        }
        return respJson;
    }

    //删除角色
    @ApiOperation(value = "删除角色接口")
    @RequestMapping(value = "/delRoles", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "角色删除成功", response = Role.class),
            @ApiResponse(code = 500, message = "删除失败")
    })
    public JSONObject delRoles(@RequestParam @ApiParam (value = "需要删除角色的id", required = true) List<Integer> list){
        JSONObject respJson = new JSONObject();
        try {
            for (Integer id : list){
                Role role = roleService.getById(id);
                roleService.removeById(id);
                roleauthService.removeByRoleId(role.getRoleId());
                userroleService.removeByRoleId(role.getRoleId());
            }
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "删除成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
        }
        return respJson;
    }

    //查询角色列表
    @ApiOperation(value = "查询角色列表接口")
    @RequestMapping(value = "getRoles",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Role.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getRoles(@RequestParam @ApiParam(value = "当前页", required = true) Long currentPage){
        JSONObject respJson = new JSONObject();
        try {
            Page page = new Page();
            page.setSize(10);
            page.setCurrent(currentPage);
            User loginUser = (User) request.getSession().getAttribute("user");
            Role role = new Role();
            role.setCreator(loginUser.getAccount());
            QueryWrapper<Role> wrapper = new QueryWrapper<Role>(role);
            wrapper.orderByDesc("CREAT_TIME");
            IPage<Role> pageRole = roleService.page(page, wrapper);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data",pageRole);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    //查询所有角色
    @ApiOperation(value = "查询所有角色接口")
    @RequestMapping(value = "getAllRoles",method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Role.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    
    public JSONObject getAllRoles(@RequestParam @ApiParam(value = "用户的moid", required = true)String moid){
        JSONObject respJson = new JSONObject();
        User loginUser = (User) request.getSession().getAttribute("user");
        try {   //用户已经存在的
	        	List<Role> listrReadyroles = roleService.findRolesByUserId(moid);
	        	//查询所有角色
	            Role role = new Role();
	            role.setCreator(loginUser.getAccount());
	            QueryWrapper<Role> wrapper = new QueryWrapper<Role>(role);
	            List<Role> listroles = roleService.list(wrapper);
	            respJson.put("code", ResultCode.SUCCESS);
	            respJson.put("msg", "获取成功");
	            respJson.put("data",listroles);
	            respJson.put("Readydata",listrReadyroles);//用户已经存在的角色
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
  //分配角色保存接口
    @ApiOperation(value = "分配角色保存接口")
    @RequestMapping(value = "getSaveUserRoles",method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "保存成功", response = Role.class),
            @ApiResponse(code = 500, message
                    = "保存失败")
    })
    public JSONObject getSaveUserRoles(@RequestParam @ApiParam(value = "用户的moid", required = true)String UserId,@RequestParam(required = false, value = "listroleIds[]")List<String>listroleIds){
        JSONObject respJson = new JSONObject();
        try {
        	   respJson = userroleService.AaveAndDelete(UserId,listroleIds);
	           
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
}
