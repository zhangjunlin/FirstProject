package com.auxing.znhy.util.meeting;

import java.util.List;

/**
 * 会议录像设置
 */
public class Recorders {
	private String video_name;			//录像保存的文件名称
	private Integer recorder_type;		//录像类型 1-会议录像；2-终端录像；
	private Integer publish_mode;		//发布模式 0-不发布；1-发布；
	private Integer anonymous;			//是否支持免登陆观看直播 0-不支持；1-支持； 
	private Integer recorder_mode;		//录像模式 1-录像；2-直播；3-录像+直播； 
	private Integer main_stream;		//是否录主格式码流（视频+音频） 0-否；1-是；
	private Integer dual_stream;		//是否录双流（仅双流） 0-否；1-是； 
	private String vrs_id;              //vrs的id信息,用于指定录像的vrs
	//终端id, 仅当开启终端录像需要 最大字符长度：48个字节(使用时只需向MeetingUser类set mt_id属性)
	private List<Member>members;
	
	//返回录像列表补充信息
	private String rec_id;//录像机id 最大字符长度：48字节
    private Integer state;//1-正在录像；2-暂停；3-正在呼叫实体；4-准备录像；
    private Integer current_progress;//当前录像进度, 单位为: 秒

	
	public Recorders() {
	}

	public Recorders(String video_name, Integer recorder_type, Integer publish_mode, Integer anonymous,
			Integer recorder_mode, Integer main_stream, Integer dual_stream) {
		this.video_name = video_name;
		this.recorder_type = recorder_type;
		this.publish_mode = publish_mode;
		this.anonymous = anonymous;
		this.recorder_mode = recorder_mode;
		this.main_stream = main_stream;
		this.dual_stream = dual_stream;
	}

	public String getVideo_name() {
		return video_name;
	}

	public void setVideo_name(String video_name) {
		this.video_name = video_name;
	}

	public Integer getRecorder_type() {
		return recorder_type;
	}

	public void setRecorder_type(Integer recorder_type) {
		this.recorder_type = recorder_type;
	}

	public Integer getPublish_mode() {
		return publish_mode;
	}

	public void setPublish_mode(Integer publish_mode) {
		this.publish_mode = publish_mode;
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

	public Integer getMain_stream() {
		return main_stream;
	}

	public void setMain_stream(Integer main_stream) {
		this.main_stream = main_stream;
	}

	public Integer getDual_stream() {
		return dual_stream;
	}

	public void setDual_stream(Integer dual_stream) {
		this.dual_stream = dual_stream;
	}

	public String getVrs_id() {
		return vrs_id;
	}

	public void setVrs_id(String vrs_id) {
		this.vrs_id = vrs_id;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public String getRec_id() {
		return rec_id;
	}

	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getCurrent_progress() {
		return current_progress;
	}

	public void setCurrent_progress(Integer current_progress) {
		this.current_progress = current_progress;
	}

	
}
