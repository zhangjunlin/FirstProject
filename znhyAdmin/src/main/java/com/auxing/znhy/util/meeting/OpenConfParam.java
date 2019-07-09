package com.auxing.znhy.util.meeting;

import java.util.List;
//画面合成参数映射对象
public class OpenConfParam {
	private String conf_id;
	private String hdu_id;
	private Integer mode;
	private Integer layout;
	private Integer broadcast;
	private Integer voice_hint;
	private Integer show_mt_name;
	private MtNameStyle mt_name_style;
	private List<Member> members;
	private Poll poll;
	
	
	public String getConf_id() {
		return conf_id;
	}
	public void setConf_id(String conf_id) {
		this.conf_id = conf_id;
	}
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
	public Integer getBroadcast() {
		return broadcast;
	}
	public void setBroadcast(Integer broadcast) {
		this.broadcast = broadcast;
	}
	public Integer getVoice_hint() {
		return voice_hint;
	}
	public void setVoice_hint(Integer voice_hint) {
		this.voice_hint = voice_hint;
	}
	public Integer getShow_mt_name() {
		return show_mt_name;
	}
	public void setShow_mt_name(Integer show_mt_name) {
		this.show_mt_name = show_mt_name;
	}
	public MtNameStyle getMt_name_style() {
		return mt_name_style;
	}
	public void setMt_name_style(MtNameStyle mt_name_style) {
		this.mt_name_style = mt_name_style;
	}
	public List<Member> getMembers() {
		return members;
	}
	public void setMembers(List<Member> members) {
		this.members = members;
	}
	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public String getHdu_id() {
		return hdu_id;
	}
	public void setHdu_id(String hdu_id) {
		this.hdu_id = hdu_id;
	}
	
	
}
