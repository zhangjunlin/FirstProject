package com.auxing.znhy.util.meeting;

import java.util.ArrayList;
import java.util.List;

/**
 *	 混音信息 
 */
public class Mix {
	private Integer mode; 										//混音模式 1-智能混音；2-定制混音； 
	List<MeetingUser> members = new ArrayList<MeetingUser>();		//制定混音时的混音成员列表
	
	
	public Integer getMode() {
		return mode;
	}
	
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
	public List<MeetingUser> getMembers() {
		return members;
	}
	
	
	public void setMembers(List<MeetingUser> members) {
		this.members = members;
	}
	
}
