package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.TemplateUser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.util.DateUtil;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-10-12
 */
@Api(tags = "会议模板接口",value = "/template")
@Controller
@RequestMapping("/template")
@Slf4j
public class TemplateController extends BaseController {
	
	@ResponseBody
	@ApiOperation(value = "分页查询会议模板")
	@RequestMapping(value = "/selectPage", method = RequestMethod.GET)
	public JSONObject selectPage(Long size,Long current) {
		JSONObject respJson = new JSONObject();
	try{
		User loginUser = (User) request.getSession().getAttribute("user");
			Page<Template> page = new Page<Template>();
			if(size != null){
				page.setSize(size);
			}
			if(current != null){
				page.setCurrent(current);
			}
			/*Template template = new Template();
			List<ZTreeNode> selectDepsByUser = departmentService.listDepsByUser(loginUser.getDepartment(),loginUser.getDomainId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("list", selectDepsByUser);
			map.put("template", template);
			long count = templateService.selectTotalCount(map);
			List<Template> templates = templateService.page(page, template ,selectDepsByUser);*/
			
			Template template = new Template();
			template.setDepartmentId(loginUser.getDepartment());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("template", template);
			long count = templateService.selectTotalCount(map);
			map.put("page", page);
			List<Template> templates = templateService.page(map);
			
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			for (Template t : templates) {
				Map<String,Object> map1 = new HashMap<>();
				map1.put("id", t.getTId());
				map1.put("name", t.getTName());
				map1.put("time", "手动结束");
                map1.put("creator", t.getCreateName());
                map1.put("passWord", t.getTPassword());
                
                
                if(t.getSpeaker() != null && !"".equals(t.getSpeaker())){
                	Map<String, Object> speakerMap = new HashMap<String, Object>();
    				User user = new User();
    				user.setMoid(t.getSpeaker());
    				QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
    				User speakerUser = userService.getOne(wrapper);
    				if(speakerUser != null){
    					speakerMap.put("id", speakerUser.getMoid());
        				speakerMap.put("name", speakerUser.getActuralName());
        				map1.put("speaker", speakerMap);
    				}
    				
                }
                
				
                if(t.getChairMan() != null && !"".equals(t.getChairMan())){
    				Map<String, Object> chairManMap = new HashMap<String, Object>();
    				User user = new User();
    				user.setMoid(t.getChairMan());
    				QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
    				wrapper = new QueryWrapper<User>(user);
    				User chairManUser = userService.getOne(wrapper);
    				if(chairManUser != null){
    					chairManMap.put("id", chairManUser.getMoid());
        				chairManMap.put("name", chairManUser.getActuralName());
        				map1.put("chairMan", chairManMap);
    				}
    				
                }
				
				
			
				
				List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
				List<Map<String,Object>> userList2 = new ArrayList<Map<String,Object>>();
				List<TemplateUser> idList = this.templateService.getUsersById(t.getTId());
				map1.put("joinMt", idList.size());
				if(idList != null && idList.size() > 0){
					for (TemplateUser mo : idList) {
						User u = new User();
						u.setMoid(mo.getUserId());
						QueryWrapper<User> queryWrapper = new QueryWrapper<User>(u);
						u = this.userService.getOne(queryWrapper);
						Map<String, Object> userMap = new HashMap<String, Object>();
						if(u != null) {
							userMap.put("id", u.getMoid());
							userMap.put("name", u.getActuralName());
							userList.add(userMap);
						}else {
							userMap.put("id", mo.getUserId());
							userMap.put("name", mo.getUserId());
							userList2.add(userMap);
						}
						
					}
				}

				map1.put("userList", userList);
				map1.put("terminalList", userList2);
				mapList.add(map1);
			}
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("records", mapList);
			resultMap.put("total", count);
			resultMap.put("size", page.getSize());
			resultMap.put("current", current);
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", resultMap);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
	         log.error(String.valueOf(ResultCode.INTERNAL_SERVER_ERROR), e);
		}
		return respJson;
	}
	
	@ResponseBody
	@ApiOperation(value = "新增会议模板接口")
	@RequestMapping(value = "saveTemplate", method = RequestMethod.POST)
	public JSONObject saveTemplate(@RequestParam @ApiParam(value = "名称",required = true)String name, String password,String speaker,String chairMan, @RequestParam @ApiParam(value = "与会方",required = true)String ids, @RequestParam @ApiParam(value = "常用终端")String terminals) {
		JSONObject respJson = new JSONObject();
		User loginUser = (User) request.getSession().getAttribute("user");
		try{
			if(name == null || name.equals("")){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议主题不能为空");
		         return respJson;
			}
			
			if(ids == null || ids.equals("")){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "至少选择一位与会方");
		         return respJson;
			}
			
			Template template = new Template();
			template.setTName(name);
			template.setTPassword(password);
			if(speaker != null && !speaker.equals("0") && !speaker.equals("")){
				template.setSpeaker(speaker);	
			}
			if(chairMan != null && !chairMan.equals("0") && !chairMan.equals("")){
				template.setChairMan(chairMan);
			}
			template.setDuration(0);//会议模板默认手动结束
			template.setCreateId(loginUser.getMoid());
			template.setCreateName(loginUser.getActuralName());
			template.setDepartmentId(loginUser.getDepartment());
			template.setCreateTime(DateUtil.getCurrentTime());
			templateService.saveTemplate(template);
			List<String> list = java.util.Arrays.asList(ids.split(","));
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", template.getTId());
			map.put("list", list);
			map.put("type", 1);
			templateService.saveTemplateUser(map);
			//添加常用终端
			List<String> terminalList = java.util.Arrays.asList(terminals.split(","));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("id", template.getTId());
			map2.put("list", terminalList);
			map2.put("type", 2);
			templateService.saveTemplateUser(map2);
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "新增会议模板成功");
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
	         log.error(String.valueOf(ResultCode.INTERNAL_SERVER_ERROR), e);
		}
		return respJson;
	}
	
	
	@ResponseBody
	@ApiOperation(value = "修改会议模板接口")
	@RequestMapping(value = "updateTemplate", method = RequestMethod.POST)
	public JSONObject updateTemplate(Integer id, String name, String password, String speaker,String chairMan, @RequestParam @ApiParam(value = "与会方")String ids, @RequestParam @ApiParam(value = "常用终端")String terminals) {
		JSONObject respJson = new JSONObject();
		try{
			Template template = new Template();
			template.setTId(id);
			template.setTName(name);
			template.setTPassword(password);
			if(speaker != null && !speaker.equals("0") && !speaker.equals("")){
				template.setSpeaker(speaker);	
			}
			if(chairMan != null && !chairMan.equals("0") && !chairMan.equals("")){
				template.setChairMan(chairMan);
			}
			
			List<String> list = java.util.Arrays.asList(ids.split(","));
			if(!list.contains(speaker)){
				template.setSpeaker("");
			}
			if(!list.contains(chairMan)){
				template.setChairMan("");
			}
			
			template.setUpdateTime(DateUtil.getCurrentTime());
			templateService.updateTemplate(template);
			templateService.deleteTemplateUser(template.getTId());
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", template.getTId());
			map.put("list", list);
			map.put("type", 1);
			templateService.saveTemplateUser(map);
			//更新常用终端
			List<String> terminalList = java.util.Arrays.asList(terminals.split(","));
			Map<String, Object> map2 = new HashMap<String, Object>();
			map2.put("id", template.getTId());
			map2.put("list", terminalList);
			map2.put("type", 2);
			templateService.saveTemplateUser(map2);
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "修改会议模板成功");
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
	         log.error(String.valueOf(ResultCode.INTERNAL_SERVER_ERROR), e);
		}
		return respJson;
	}
	
	
	@ResponseBody
	@ApiOperation(value = "根据ID查询会议模板接口")
	@RequestMapping(value = "getTemplate", method = RequestMethod.GET)
	public JSONObject getTemplate(Integer id) {
		JSONObject respJson = new JSONObject();
		try{
			Template template = templateService.getTemplateById(id);
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", template.getTId());
			map.put("name", template.getTName());
			if(template.getDuration() == 0){
				map.put("hourTime", 0);
				map.put("minTime", 0);
			}else{
				map.put("hourTime", (int)Math.floor(template.getDuration() / 60));
				map.put("minTime", template.getDuration() % 60);
			}
			Map<String, Object> speakerMap = new HashMap<String, Object>();
			User user = new User();
			user.setMoid(template.getSpeaker());
			QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
			User speakerUser = userService.getOne(wrapper);
			speakerMap.put("id", speakerUser.getMoid());
			speakerMap.put("name", speakerUser.getActuralName());
			map.put("speaker", speakerMap);
			
			Map<String, Object> chairManMap = new HashMap<String, Object>();
			user.setMoid(template.getSpeaker());
			wrapper = new QueryWrapper<User>(user);
			User chairManUser = userService.getOne(wrapper);
			chairManMap.put("id", chairManUser.getMoid());
			chairManMap.put("name", chairManUser.getActuralName());
			map.put("chairMan", chairManMap);
			
			
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			List<Map<String,Object>> mapList2 = new ArrayList<Map<String,Object>>();
			List<TemplateUser> idList = this.templateService.getUsersById(template.getTId());
			for (TemplateUser mo : idList) {
				User u = new User();
				u.setMoid(mo.getUserId());
				QueryWrapper<User> queryWrapper = new QueryWrapper<User>(u);
				u = this.userService.getOne(queryWrapper);
				Map<String, Object> userMap = new HashMap<String, Object>();
				if(u != null) {
					userMap.put("id", u.getMoid());
					userMap.put("name", u.getActuralName());
					mapList.add(userMap);
				}else {
					userMap.put("id", mo.getUserId());
					userMap.put("name", mo.getUserId());
					mapList2.add(userMap);
				}
			}
			map.put("userList", mapList);
			map.put("terminalList", mapList2);
			//原始数据
			List<ZTreeNode> listAlldeps = departmentService.listAlldeps();
			 // 最后的结果
		    List<ZTreeNode> departmentList = new ArrayList<ZTreeNode>();
			// 先找到所有的一级菜单
		    for (int i = 0; i < listAlldeps.size(); i++) {
		        // 一级菜单没有parentId
		        if (listAlldeps.get(i).getpId() == null) {
		        	departmentList.add(listAlldeps.get(i));
		        }
		    }
		    List<String> ids = null;
		    if(id != null){
		    	ids	= templateService.findUserIds(id);
		    }
		     
		    
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (ZTreeNode tree : departmentList) {
		    	List<ZTreeNode> list = departmentService.getUserChild(tree.getId(), listAlldeps, ids);
		    	tree.setChildren(list);
		    }
			
		    map.put("zTreeNode", departmentList);
			
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", map);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
		}
		return respJson;
	}
	
	
	@ResponseBody
	@ApiOperation(value = "删除会议模板接口")
	@RequestMapping(value = "deleteTemplate",  method = RequestMethod.POST)
	public JSONObject deleteTemplate(Integer[] list) {
		JSONObject respJson = new JSONObject();
		try{
			for (Integer id : list) {
				templateService.deleteTemplate(id);
				templateService.deleteTemplateUser(id);
			}
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "删除成功");
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
		}
		return respJson;
	}
	
	@ResponseBody
	@ApiOperation(value = "查询所有部门用户树形结构接口")
	@RequestMapping(value = "getNodes", method = RequestMethod.GET)
	public JSONObject getNodes() {
		JSONObject respJson = new JSONObject();
		try{
			//原始数据
			List<ZTreeNode> listAlldeps = departmentService.listAlldeps();
			 // 最后的结果
		    List<ZTreeNode> departmentList = new ArrayList<ZTreeNode>();
			// 先找到所有的一级菜单
		    for (int i = 0; i < listAlldeps.size(); i++) {
		        // 一级菜单没有parentId
		        if (listAlldeps.get(i).getpId() == null) {
		        	departmentList.add(listAlldeps.get(i));
		        }
		    }
		    
		    // 为一级菜单设置子菜单，getChild是递归调用的
		    for (ZTreeNode tree : departmentList) {
		    	List<ZTreeNode> list = departmentService.getUserChild(tree.getId(), listAlldeps, null);
		    	tree.setChildren(list);
		    }
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", departmentList);
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常!");
	         e.printStackTrace();
		}
		return respJson;
	}
	
	
	
}
