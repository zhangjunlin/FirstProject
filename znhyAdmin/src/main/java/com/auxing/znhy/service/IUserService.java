package com.auxing.znhy.service;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-10-09
 */
public interface IUserService extends IService<User> {
	long selectTotalCount(User user);
	List<User> selectUserPage(Page<User> page,User user);
	JSONObject login(String account, String password);//验证登录
	MeetingResult getCommonUser(String username,String password);//调用接口获取会议系统的所有账户信息
	MeetingResult updateCommonPaw(User user ,MeetInterface meetInterface);//调用接口更新会议系统的账户密码
	JSONObject SynchronousUser();//同步账户到本地数据库
	JSONObject SynchronousUserByHand();//手动同步账户到本地数据库
	JSONObject updates(User user, MeetInterface meetInterface);//同步更新本地数据库用户密码和会议系统数据库账户密码
	JSONObject CommonUserDeps(MeetInterface meetInterface);//同时同步部门和用户

}
