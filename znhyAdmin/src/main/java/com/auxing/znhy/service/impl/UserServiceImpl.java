package com.auxing.znhy.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.License;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.mapper.UserMapper;
import com.auxing.znhy.service.IDepartmentService;
import com.auxing.znhy.service.IDomainuserService;
import com.auxing.znhy.service.ILicenseService;
import com.auxing.znhy.service.IUserService;
import com.auxing.znhy.util.Accounts;
import com.auxing.znhy.util.MD5Utils;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	protected HttpServletRequest request;
	
	@Autowired
	private IDepartmentService depservice;
	
	 @Autowired
	 private IDomainuserService domainUserService;
	 
	 @Autowired
	 private ILicenseService licenseService;
	 @Resource
	 private UserMapper userMapper;
	
	/**
	 * 登录业务验证
	 */
	public JSONObject login(String account, String password) {
		JSONObject respJson = new JSONObject();
	   try{
			if(account != null && !"".equals(account)){
				if(password != null && !"".equals(password)){//
					License license = licenseService.getById("1");
					if( Integer.valueOf(license.getRemainTime()) > 0){
						MeetInterface meetInterface = new MeetInterface(account,password, env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
						if(meetInterface.getSuccess() ==1){
							User u = new User();
							u.setAccount(account);
							QueryWrapper<User> wrapper = new QueryWrapper<User>(u);
							User user = this.getOne(wrapper);
							if (user == null) {
								respJson.put("code",ResultCode.ERROR);
								respJson.put("msg", "登录名不存在");
							} else if (!user.getPassword().equals(MD5Utils.pwdEncrypt(password))) {
								respJson.put("code",ResultCode.ERROR);
								respJson.put("msg","密码不正确");
							} else if (user.getEnable() == 0) {
								respJson.put("code",ResultCode.ERROR);
								respJson.put("msg","已被管理员禁用");
							} else {
								user.setPassword(password);
								//设置session的最大过期时间
								request.getSession().setMaxInactiveInterval(10080);//单位为秒
								System.out.println("登录请求session:"+request.getSession().getId());
								request.getSession().setAttribute("user", user);
								request.getSession().setAttribute("meetInterface", meetInterface);
								respJson.put("code",ResultCode.SUCCESS);
								respJson.put("msg","登录成功!");
								respJson.put("data",user);
							}
						}else if(meetInterface.getCode() == 20008){
							respJson.put("code",ResultCode.ERROR);
							respJson.put("msg","用户名或密码错误!");
						}else{
							respJson.put("code",ResultCode.ERROR);
							respJson.put("msg","登录失败!");
						}
					
					}else{
						respJson.put("code",ResultCode.ERROR);
						respJson.put("msg", "license已过期,请联系系统管理员!");
					}
			}else{
					respJson.put("code",ResultCode.ERROR);
					respJson.put("msg", "密码不能为空!");
				}
			}else{
				respJson.put("code",ResultCode.ERROR);
				respJson.put("msg", "登录名不能为空!");
			}
			
		}catch(Exception e){
			respJson.put("code",ResultCode.INTERNAL_SERVER_ERROR);
			respJson.put("msg","系统异常!");
			e.printStackTrace();
		}
		return respJson;
	}
	//调用接口获取会议系统表中的用户
	@Override
	public MeetingResult getCommonUser(String username,String password){
        MeetInterface meetInterface = null;
		try {
			meetInterface = new MeetInterface(username,password, env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		params.put("count",0);
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/amc/accounts",params, meetInterface.getCookie());
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
		}
		return result;
	}
	//调用接口更新会议系统表的用户密码
	public MeetingResult updateCommonPaw(User user ,MeetInterface meetInterface){
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("value", user.getPassword());
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(userMap),"UTF-8"));//请求内容，以JSON形式发送，需进行UrlEncode
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		MeetingResult result =  MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/amc/accounts/"+user.getMoid()+"/password",params, meetInterface.getCookie());
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
		}
		return result;
	}
	//自动同步用户到本地表
	@Override
	public JSONObject SynchronousUser(){
		JSONObject respJson = new JSONObject();
		//查询域域用户的关系记录
		Domainuser doma = new Domainuser("1");
		QueryWrapper<Domainuser> wrappers = new QueryWrapper<Domainuser>(doma);
		List<Domainuser> listDomainusers = domainUserService.list(wrappers);
		for (Domainuser domainuser : listDomainusers) {
			
			//调用接口获取会议系统表中的用户
			MeetingResult result = this.getCommonUser(domainuser.getUsername(),domainuser.getPassword());
			List<Accounts> lists = result.getAccounts();
			//循环从会议系统账户表中捞的所有数据 
			for (int i = 0; i < lists.size(); i++) {
					User user = new User();
					user.setMoid(lists.get(i).getAccount_moid().trim());
					QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
					//根据moid在本地表中查询该用户是否存
					User u = this.getOne(wrapper);
					User us = new User(lists.get(i).getAccount(),lists.get(i).getPassword().trim(),lists.get(i).getName(),lists.get(i).getJob_num(),lists.get(i).getE164(),lists.get(i).getAccount_moid().trim(),lists.get(i).getSex(),lists.get(i).getDate_of_birth(),
					lists.get(i).getMobile(),lists.get(i).getEmail(),lists.get(i).getDepartments().get(0).getDepartment_moid().trim(),
					lists.get(i).getLimited(),lists.get(i).getEnable(),domainuser.getMoid());
					if(domainuser.getUsername().equals(lists.get(i).getAccount())){
						us.setUserType(2);
					}else if ("admingd".equals(lists.get(i).getAccount())){
						us.setUserType(1);
					}else{
						us.setUserType(3);
					}
					//判断该用户是否存在
					if(u == null) {
						//不存在,重新插入
						this.save(us);
					}else {
						this.update(us,wrapper);
					}
			  }
		}
		respJson.put("code", ResultCode.SUCCESS);
		respJson.put("msg","同步成功!");
		return respJson;
	}
	//手动同步用户到本地表
		@Override
		public JSONObject SynchronousUserByHand(){
			//先删除所有用户,再重新插入
			User user = new User();
			QueryWrapper<User> wrapper = new QueryWrapper<User>(user);
			wrapper.ne("ACCOUNT","admin1");
			wrapper.ne("ACCOUNT","admingd");
			this.remove(wrapper);
			JSONObject respJson = new JSONObject();
			//查询域域用户的关系记录
			Domainuser doma = new Domainuser("1");
			QueryWrapper<Domainuser> wrappers = new QueryWrapper<Domainuser>(doma);
			List<Domainuser> listDomainusers = domainUserService.list(wrappers);
			for (Domainuser domainuser : listDomainusers) {
				MeetingResult result = this.getCommonUser(domainuser.getUsername(),domainuser.getPassword());
				List<Accounts> lists = result.getAccounts();
				//循环从会议系统账户表中捞的所有数据
				for (int i = 0; i < lists.size(); i++) {
					User us = new User(lists.get(i).getAccount(),lists.get(i).getPassword().trim(),lists.get(i).getName(),lists.get(i).getJob_num(),lists.get(i).getE164(),lists.get(i).getAccount_moid().trim(),lists.get(i).getSex(),lists.get(i).getDate_of_birth(),
							lists.get(i).getMobile(),lists.get(i).getEmail(),lists.get(i).getDepartments().get(0).getDepartment_moid().trim(),
							lists.get(i).getLimited(),lists.get(i).getEnable(),domainuser.getMoid());

					if(domainuser.getUsername().equals(lists.get(i).getAccount())){
						us.setUserType(2);
					}else if ("admingd".equals(lists.get(i).getAccount())){
						us.setUserType(1);
					}else{
						us.setUserType(3);
					}
												//重新插入用户
						if("admin1".equals(us.getAccount())||"admingd".equals(us.getAccount())){
							User u = new User();
							 u.setMoid(lists.get(i).getAccount_moid().trim());
							 QueryWrapper<User> wrapper1 = new QueryWrapper<User>(u);
							 this.update(us,wrapper1);
						}else{
							this.save(us);
						}
						
				}
			}
			respJson.put("code", ResultCode.SUCCESS);
			respJson.put("msg","同步成功!");
			return respJson;
		}
	//同步更新本地密码和会议系统表中用户密码
	@Override
	public synchronized JSONObject updates(User user, MeetInterface meetInterface){
		JSONObject respJson = new JSONObject();
		String p = user.getPassword();
		user.setPassword(p);
		//更新会议系统表
		this.updateCommonPaw(user , meetInterface);
		
		user.setPassword(MD5Utils.pwdEncrypt(user.getPassword()));
		//更新本地表
		this.updateById(user);
		
		//更新domainuser表的密码
		QueryWrapper<Domainuser> updateWrapper = new QueryWrapper<Domainuser>();
		Domainuser duser = new Domainuser();
		duser.setName(user.getAccount());
		updateWrapper.setEntity(duser);
		Domainuser domainuser = domainUserService.getOne(updateWrapper);
		if(domainuser != null) {
			domainuser.setPassword(p);
			updateWrapper.setEntity(domainuser);
			this.domainUserService.update(domainuser,updateWrapper);
		}
		respJson.put("code", ResultCode.SUCCESS);
		respJson.put("msg", "密码更新成功!");
		return respJson;
	}
	
	//同时同步部门和用户
	public synchronized JSONObject CommonUserDeps(MeetInterface meetInterface){
		
		JSONObject respJson = new JSONObject();
		//手动同步用户
		this.SynchronousUserByHand();
		//手动同步域与部门
		//depservice.SynchronousDomain();
		try {
			depservice.SynchronousDomainByHand();
		} catch (Exception e) {
			e.printStackTrace();
		}
		respJson.put("code", ResultCode.SUCCESS);
		respJson.put("msg","同步成功!");
		return respJson;
		
	}
	@Override
	public List<User> selectUserPage(Page<User> page,User user) {
		return userMapper.selectUserPage(page, user);
	}
	@Override
	public long selectTotalCount(User user) {
		return userMapper.selectTotalCount(user);
	}
}
