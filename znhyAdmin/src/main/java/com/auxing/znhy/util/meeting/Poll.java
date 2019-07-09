package com.auxing.znhy.util.meeting;

import java.util.ArrayList;
import java.util.List;



/**
 *	轮询设置
 */
public class Poll {
	private Integer mode;		//轮询模式 1-视频轮询；3-音视频轮询；
	private Integer num;		//轮询次数，0无限次轮询
	private Integer keep_time;	// 轮询间隔时间(秒) 
	private List<MeetingUser> members;	//轮询成员列表
	
	
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getKeep_time() {
		return keep_time;
	}
	public void setKeep_time(Integer keep_time) {
		this.keep_time = keep_time;
	}
	public List<MeetingUser> getMembers() {
		return members;
	}
	public void setMembers(List<MeetingUser> members) {
		this.members = members;
	}
	
	
	
	
}
