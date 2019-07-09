package com.auxing.znhy.util.meeting;

import java.util.ArrayList;
import java.util.List;


/**
 * 会议画面合成设置
 */
public class Vmt {
	private Integer mode;			//画面合成模式 1-定制画面合成；2-自动画面合成；
	private Integer layout;			//画面合成风格:参考API
	private Integer voice_hint;		//是否识别声音来源 0-否；1-是；
	private Integer broadcast;		//是否向终端广播 0-否；1-是；
	private Integer show_mt_name;	//是否显示别名 0-否；1-是；
	
	private MtNameStyle mt_name_style = new MtNameStyle();					//画面合成参数
	private List<MeetingUser> members = new ArrayList<MeetingUser>();		//画面合成人员
	
	
	public Integer getMode() {
		return mode;
	}
	public void setMode(Integer mode) {
		this.mode = mode;
	}
	public Integer getLayout() {
		return layout;
	}
	public void setLayout(Integer layout) {
		this.layout = layout;
	}
	public Integer getVoice_hint() {
		return voice_hint;
	}
	public void setVoice_hint(Integer voice_hint) {
		this.voice_hint = voice_hint;
	}
	public Integer getBroadcast() {
		return broadcast;
	}
	public void setBroadcast(Integer broadcast) {
		this.broadcast = broadcast;
	}
	public Integer getShow_mt_name() {
		return show_mt_name;
	}
	public void setShow_mt_name(Integer show_mt_name) {
		this.show_mt_name = show_mt_name;
	}
	public List<MeetingUser> getMembers() {
		return members;
	}
	public void setMembers(List<MeetingUser> members) {
		this.members = members;
	}
	public MtNameStyle getMt_name_style() {
		return mt_name_style;
	}
	public void setMt_name_style(MtNameStyle mt_name_style) {
		this.mt_name_style = mt_name_style;
	}
	
	
	
}
