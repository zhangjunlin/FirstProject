package com.auxing.znhy.filter;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.ResultCode;

import lombok.extern.slf4j.Slf4j;
 
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
	
	 @Autowired
	 private Environment env;
	
    //这个方法是在访问接口之前执行的，我们只需要在这里写验证登陆状态的业务逻辑，就可以在用户调用指定接口之前验证登陆状态了
	 public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
	        //每一个项目对于登陆的实现逻辑都有所区别，我这里使用最简单的Session提取User来验证登陆。
	        HttpSession session = request.getSession();
	        System.out.println(session.getId());
	        //这里的User是登陆时放入session的
	        User user = (User) session.getAttribute("user");
	        //如果session中没有user，表示没登陆
	        if (user == null){
	            //这个方法返回false表示忽略当前请求，如果一个用户调用了需要登陆才能使用的接口，如果他没有登陆这里会直接忽略掉
	            //当然你可以利用response给用户返回一些提示信息，告诉他没登陆
	        	//解决登录页面内嵌在iframe问题
	        	response.setContentType("text/json;charset=UTF-8");// 解决中文乱码
				PrintWriter out = response.getWriter();  
				JSONObject obj = new JSONObject();
				obj.put("code",ResultCode.NO_LOGIN_ERROR);
				obj.put("msg","session已过期,请重新登录");
				out.println(obj);
				return false;
	        }else {
	        	//保持用户心跳
	        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
	        	if(meetInterface != null){
	        		Map<String, Object> params = new HashMap<String, Object>();
	        		params.put("account_token", meetInterface.getToken());
	        		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/system/heartbeat",params, meetInterface.getCookie());
	        		if(result == null){
	        			log.error("会议平台调用失败");
	        		}
	        		if(result.getSuccess() != 1){
	        			if(result.getError_code() != null && result.getError_code() == 10102){
	        				meetInterface = new MeetInterface(user.getAccount(),user.getPassword(), env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
	        				if(meetInterface.getSuccess() ==1){
	        					request.getSession().setAttribute("meetInterface", meetInterface);
	        					return true;
	        				}
	        			}else{
	        				log.error("会议平台调用失败");
	        			}
	        		}else{
	        			return true;    //如果session里有user，表示该用户已经登陆，放行，用户即可继续调用自己需要的接口 
	        		}
	        	}
	    		
	        }
	        return false;
	    }
 
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    
    }
 
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }
}
