package com.auxing.znhy.util.meeting;

import java.util.List;

public class Message {
	private String message;
	private Integer type;
	private Integer roll_num;
	private Integer roll_speed;
	private List<Mts> mts;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getRoll_num() {
		return roll_num;
	}
	public void setRoll_num(Integer roll_num) {
		this.roll_num = roll_num;
	}
	public Integer getRoll_speed() {
		return roll_speed;
	}
	public void setRoll_speed(Integer roll_speed) {
		this.roll_speed = roll_speed;
	}
	public List<Mts> getMts() {
		return mts;
	}
	public void setMts(List<Mts> mts) {
		this.mts = mts;
	}
	
}
