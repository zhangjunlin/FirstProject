package com.auxing.znhy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MD5Utils;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


/**
 * 单点登录 Controller
 */
@Api(tags = "登录操作接口")
@RestController
@RequestMapping("/login")
public class LoginController extends BaseController {

	/**
	 * 检测用户是否登录
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkIsLogin", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject checkIsLogin(String account,String password) {
		JSONObject respJson = new JSONObject();
		try {
			User u = new User();
			u.setAccount(account);
			QueryWrapper<User> wrapper = new QueryWrapper<User>(u);
			User user = userService.getOne(wrapper);
			if (user == null) {
				respJson.put("msg","登录名不存在");
			} else if (!user.getPassword().equals(MD5Utils.pwdEncrypt(password))) {
				respJson.put("msg","密码不正确");
			} else if (user.getEnable() == 0) {
				respJson.put("msg","已被管理员禁用");
			} else {
				HttpSession session = request.getSession();
				ServletContext application = session.getServletContext(); // 获取application
				if (application.getAttribute(account) == null) {// 说明还没有用户登录
					respJson.put("code", 0);
					respJson.put("msg", "用户未登录");
				} else {
					respJson.put("data", true);
					respJson.put("msg", "用户已登录");
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return respJson;
	}

	/**
	 * 检测用户是否离线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/checkIsOffline", method = RequestMethod.POST)
	@ResponseBody
	public boolean checkIsOffline() {
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			return true;
		}
		return false;
	}

	/**
	 * T掉已经登录的用户重新登录
	 */
	@RequestMapping(value = "/clearAlreadyLoginUser", method = RequestMethod.GET)
	@ResponseBody
	public JSONObject clearAlreadyLoginUser(String account) {
		JSONObject respJson = new JSONObject();
		try {
			// 判断用户是否登录
			HttpSession session = request.getSession();
			ServletContext application = session.getServletContext(); // 获取application
			if (application.getAttribute(account) != null) {// 说明还没有用户登录
				String sessionId = (String) application.getAttribute(account); // 根据account获取上一个用户的sessionId
				HttpSession oldSession = (HttpSession) application.getAttribute(sessionId); // 根据sessionId获取上一个用户的session
				application.removeAttribute(oldSession.getId()); // 将oldSession从application中移除
				application.removeAttribute(account); // 将oldSession的id从application中移除
				oldSession.invalidate();
			}
		} catch (Exception e) {
			respJson.put("data", false);
			e.printStackTrace();
		}
		return respJson;
	}

	/**
	 * 登录验证
	 * 
	 * @return
	 */
    @ApiOperation(value = "登录验证接口")
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	public JSONObject checkLogin(@RequestParam @ApiParam(value = "登录账户", required = true)String account,@RequestParam @ApiParam(value = "登录密码", required = true)String password){
    	JSONObject respJson = new JSONObject();
    	try{
    	   respJson = this.userService.login(account,password);
	   }catch(Exception e){
		    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
			respJson.put("msg", "系统异常!");
			e.printStackTrace();
	   }
	   return respJson;
	}

	/**
	 * 系统登出
	 * 
	 * @param backUrl
	 * @param appCode
	 * @return
	 */
    @ApiOperation(value = "系统登出接口")
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public JSONObject logout(@RequestParam @ApiParam(value = "登录账户", required = true)String account) {
		JSONObject respJson = new JSONObject();
		try{
			this.request.getServletContext().removeAttribute(account);
			this.request.getSession().invalidate();
			respJson.put("code", ResultCode.SUCCESS);
			respJson.put("msg","退出成功");
		}catch(Exception e){
			    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "系统异常!");
				e.printStackTrace();
		}
		return respJson;
	}
}
