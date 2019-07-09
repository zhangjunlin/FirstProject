package com.auxing.znhy.util.meeting;

import java.util.List;

public class HduPoll {
	private String num;
	private Integer mode;
	private String keep_time;
	private List<MeetingUser> members;	//轮询成员列表
	private PollMt cur_poll_mt;
	private Integer poll_index;
	
	public String getNum() {
		return num;
	}
	public void setNum(String num) {
		this.num = num;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getKeep_time() {
		return keep_time;
	}
	public void setKeep_time(String keep_time) {
		this.keep_time = keep_time;
	}
	public List<MeetingUser> getMembers() {
		return members;
	}
	public void setMembers(List<MeetingUser> members) {
		this.members = members;
	}
	public PollMt getCur_poll_mt() {
		return cur_poll_mt;
	}
	public void setCur_poll_mt(PollMt cur_poll_mt) {
		this.cur_poll_mt = cur_poll_mt;
	}
	public Integer getPoll_index() {
		return poll_index;
	}
	public void setPoll_index(Integer poll_index) {
		this.poll_index = poll_index;
	}
	
	
}
