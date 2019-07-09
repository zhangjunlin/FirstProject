package com.auxing.znhy.util;

import com.auxing.znhy.util.meeting.HduPoll;
import com.auxing.znhy.util.meeting.Member;

public class HduResult {
	
	private Integer success;
	private Integer mode;
	private String hdu_id;
	private Member specific;
	private HduPoll poll;
	private HduPoll spilt;

	
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public String getHdu_id() {
		return hdu_id;
	}
	public void setHdu_id(String hdu_id) {
		this.hdu_id = hdu_id;
	}
	public Member getSpecific() {
		return specific;
	}
	public void setSpecific(Member specific) {
		this.specific = specific;
	}
	public HduPoll getPoll() {
		return poll;
	}
	public void setPoll(HduPoll poll) {
		this.poll = poll;
	}
	public HduPoll getSpilt() {
		return spilt;
	}
	public void setSpilt(HduPoll spilt) {
		this.spilt = spilt;
	}
	
	
}
