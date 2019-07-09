package com.auxing.znhy.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.management.ServiceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;


/**
 * 会议平台接口
 */
@Slf4j
public class MeetInterface {
	
	private String token;
	private String cookie;
	private String meetIp;
	private String oauthConsumerKey;
	private String oauthConsumerCecret;
	private Integer success;
	private Integer code;
	
	public MeetInterface() {
	}

	
	public MeetInterface(String username, String wpd, String meetIp, String oauthConsumerKey,
			String oauthConsumerCecret) throws Exception {
		this.meetIp = meetIp;
		this.oauthConsumerKey = oauthConsumerKey;
		this.oauthConsumerCecret = oauthConsumerCecret;
		login(username ,wpd);
	}

	/**
	 * 会议平台登录
	 * @param userName
	 * @param passWord
	 * @throws Exception 
	 * @throws ServiceNotFoundException 
	 */
	private void login(String userName , String passWord) throws Exception{
		
		//获取token
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("oauth_consumer_key", oauthConsumerKey);
		params.put("oauth_consumer_secret", oauthConsumerCecret);
		
		MeetingResult result = MeetHttpClient.sendPost(meetIp+"/api/v1/system/token", params, null);
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
			throw new Exception();
		}
		token = result.getAccount_token();
		
		params = new HashMap<String, Object>();
		params.put("account_token", token);
		params.put("username", userName);
		params.put("password", passWord);
		result = MeetHttpClient.sendPost(meetIp+"/api/v1/system/login", params, null);
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
			code = result.getError_code();
		}
		cookie = result.getCookie();
		success = result.getSuccess();
	}
	

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public String getMeetIp() {
		return meetIp;
	}

	public void setMeetIp(String meetIp) {
		this.meetIp = meetIp;
	}

	public String getOauthConsumerKey() {
		return oauthConsumerKey;
	}

	public void setOauthConsumerKey(String oauthConsumerKey) {
		this.oauthConsumerKey = oauthConsumerKey;
	}

	public String getOauthConsumerCecret() {
		return oauthConsumerCecret;
	}

	public void setOauthConsumerCecret(String oauthConsumerCecret) {
		this.oauthConsumerCecret = oauthConsumerCecret;
	}


	public Integer getSuccess() {
		return success;
	}


	public void setSuccess(Integer success) {
		this.success = success;
	}


	public Integer getCode() {
		return code;
	}


	public void setCode(Integer code) {
		this.code = code;
	}
	
	
}
