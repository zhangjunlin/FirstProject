package com.auxing.znhy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Department;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MD5Utils;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
@Api(tags = "用户操作接口",value = "/user")
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

	@ApiOperation(value = "查询用户接口")
	@RequestMapping(value = "selectPage", method = RequestMethod.GET)
	public JSONObject selectPage(@RequestParam @ApiParam(value = "当前页数", required = true)long current,
			@RequestParam @ApiParam(value = "每页显示数据", required = true)long size,
		String acturalName,
		@RequestParam @ApiParam(value = "部门moid", required = true)String department,Integer userType) {
		JSONObject respJson = new JSONObject();
		try{
			if(department != null && !"".equals(department)){
				User loginUser = (User) request.getSession().getAttribute("user");
				Domainuser domain = new Domainuser();
				domain.setUsername(loginUser.getAccount());
				QueryWrapper<Domainuser> wrapper = new QueryWrapper<Domainuser>(domain);
				Domainuser domains = domainService.getOne(wrapper);
				
				User user = null;
				if(domains != null){
					user = new User(acturalName,userType,department);
				}else{
					Department dep = this.departmentService.getDepByChildren(loginUser.getDepartment(),department);
					if(dep != null){
						user = new User(acturalName,userType,dep.getDepartmentMoid());
					}else{
						user = new User(acturalName,userType,department);
					}
				}
				
				long count = userService.selectTotalCount(user);
				Page<User>page = new Page<User>(current,size);
				List<User> listusers = userService.selectUserPage(page, user);
				
				Map<String, Object> resultMap = new HashMap<String, Object>();
				resultMap.put("records", listusers);
				resultMap.put("total", count);
				resultMap.put("size", page.getSize());
				resultMap.put("current", current);
				
				respJson.put("code", ResultCode.SUCCESS);
	            respJson.put("msg", "获取用户成功");
	            respJson.put("data", resultMap);
			}else{
				respJson.put("code", ResultCode.ERROR);
	            respJson.put("msg", "部门id为空!");
			}
			
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
	         e.printStackTrace();
		}
		return respJson;
	}

	//手动同步用户数据
    @ApiOperation(value = "手动同步部门与用户接口")
	@RequestMapping(value = "/SynchronousUser", method = RequestMethod.POST)
	public JSONObject SynchronousUser(){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    respJson = userService.CommonUserDeps(meetInterface);//手动同步部门和用户
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    

	/**
	 * 修改密码
	 * 
	 * @param backUrl
	 * @param appCode
	 * @return
	 */
    @ApiOperation(value = "修改密码接口")
	@RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject modifyPwd(@RequestParam @ApiParam(value = "旧密码", required = true)String password,@RequestParam @ApiParam(value = "新密码", required = true)String newPwd) {
		JSONObject respJson = new JSONObject();
		User loginUser = (User) request.getSession().getAttribute("user");
		try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			// 此处实现修改密码
			User user = new User();
			user.setAccount(loginUser.getAccount());
			user.setPassword(MD5Utils.pwdEncrypt(password));
			QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
			User u = this.userService.getOne(wrapper);
			if (u == null) {
				respJson.put("msg","原密码不正确。");
				respJson.put("code",ResultCode.DAO_ERROR);
			}else{
				user.setMoid(u.getMoid().trim());
				user.setId(u.getId());
				user.setPassword(newPwd);
				respJson = userService.updates(user, meetInterface);
				respJson.put("msg","密码修改成功。");
				respJson.put("code",ResultCode.SUCCESS);
			}
		} catch (Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
			respJson.put("msg", "系统异常！");
			e.printStackTrace();
		}
		return respJson;
	}   
    
	/**
	 * 修改密码

	 * @return
	 */
    @ApiOperation(value = "个人信息")
	@RequestMapping(value = "/myMsg", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject myMsg() {
		JSONObject respJson = new JSONObject();
		User loginUser = (User) request.getSession().getAttribute("user");
		Department department = new Department();
		department.setDepartmentMoid(loginUser.getDepartment());
		QueryWrapper<Department> wrapper = new QueryWrapper<Department>(department);
		department = this.departmentService.getOne(wrapper);
		loginUser.setDepartment(department.getDepartmentName());
		try {
			respJson.put("data", loginUser);
			respJson.put("msg","获取成功");
			respJson.put("code",ResultCode.SUCCESS);
		} catch (Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
			respJson.put("msg", "系统异常！");
			e.printStackTrace();
		}
		return respJson;
	}   
}
