package com.auxing.znhy.util.meeting;

/**
 * 录像设置
 *
 */
public class Recorder {
	
	private Integer publish_mode;		//发布模式 0-不发布；1-发布；
	private Integer dual_stream;		//是否内容共享录像0-否；1-是；
	private Integer anonymous;			//是否支持免登陆观看直播 0-不支持；1-支持；
	private Integer recorder_mode;		//录像模式 1-录像；2-直播；3-录像+直播；
	private String vrs_id;				//VRS的moid
	public Integer getPublish_mode() {
		return publish_mode;
	}
	public void setPublish_mode(Integer publish_mode) {
		this.publish_mode = publish_mode;
	}
	public Integer getDual_stream() {
		return dual_stream;
	}
	public void setDual_stream(Integer dual_stream) {
		this.dual_stream = dual_stream;
	}
	public Integer getAnonymous() {
		return anonymous;
	}
	public void setAnonymous(Integer anonymous) {
		this.anonymous = anonymous;
	}
	public Integer getRecorder_mode() {
		return recorder_mode;
	}
	public void setRecorder_mode(Integer recorder_mode) {
		this.recorder_mode = recorder_mode;
	}
	public String getVrs_id() {
		return vrs_id;
	}
	public void setVrs_id(String vrs_id) {
		this.vrs_id = vrs_id;
	}
	
	
	
}
