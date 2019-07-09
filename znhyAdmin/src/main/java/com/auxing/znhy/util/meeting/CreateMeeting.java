package com.auxing.znhy.util.meeting;

import java.util.ArrayList;
import java.util.List;

/**
 *	创建会议类
 */
public class CreateMeeting {
	
	private Integer create_type = 4;					//创建会议类型，默认为1 1-及时会议；4-根据虚拟会议室创会；
	private String template_id;							//当creat_type不为1时必填，分别对应模板id和虚拟会议室id
	private String name;								//会议名称
	private Integer duration;							//会议时长 0为永久会议
	private Integer bitrate;							//会议码率，目前以主视频格式中的码率为准
	private Integer closed_conf;						//会议免打扰 0-关闭；1-开启；
	private Integer safe_conf;							//会议安全 0-公开会议；1-隐藏会议；
	private String password;							//会议密码
	private Integer encrypted_type = 0;					//传输加密类型 0-不加密；2-AES加密；
	private Integer conf_type = 0;						//会议类型 0-传统会议；1-端口会议；
	private Integer call_mode = 0;						// 呼叫模式，不填则默认为2 0-手动；2-自动； 
	private Integer call_times = 0;						// 呼叫次数 
	private Integer call_interval = 0;					// 呼叫间隔(秒) 
	private Integer fec_mode = 0;						// FEC开关，默认为00-关闭；1-开启；
	private Integer mute_filter = 0;					//是否开启全场哑音例外，默认为0 0-不开启；1-开启；
	private Integer mute = 0;							// 是否开启初始化哑音 0-否；1-是； 
	private Integer silence = 0;						//是否开启初始化静音 0-否；1-是；
	private Integer video_quality = 1;					// 视频质量,其中租赁环境默认设为质量优先，自建环境以api下参为准 0-质量优先；1-速度优先； 
	private String encrypted_key = "";					// 传输加密AES加密密钥 
	private Integer dual_mode = 0;							//双流权限 0-发言会场；1-任意会场；2-指定会场；
	private Integer doubleflow = 0;							//成为发言人后立即发起内容共享，默认为0 0否1是
	private Integer voice_activity_detection = 0;			// 是否开启语音激励 0-否；1-是； 
	private Integer vacinterval = 0;				    	//语音激励敏感度(s),支持5、15、30、60
	private Integer cascade_mode = 1;						//级联模式 0-简单级联；1-合并级联；
	private Integer cascade_upload = 1;						// 是否级联上传 0-否；1-是； 
	private Integer cascade_return = 0;						// 是否级联回传 0-否；1-是； 
	private Integer cascade_return_para = 0;				//级联回传带宽参数
	private Integer public_conf = 0;						//是否来宾会议室 0-否；1-是；
	private Integer max_join_mt;						//8-小型8方会议；32-32方会议；64-64方会议；192-大型192方会议；
	private Integer auto_end = 1;							//会议中无终端时，是否自动结会，永久会议时默认为0  0-否；1-是；
	private Integer preoccpuy_resource = 0;					//预占资源 0-否；1-是；
	private Integer one_reforming = 0;						//归一重整 0-不使用；1-使用；
	private String platform_id;							//创会平台moid，不填则根据用户权限取默认值
	
	//private List<MeetingUser> keep_calling_members = new ArrayList<MeetingUser>();	//追呼成员数组
	private MeetingUser speaker;													//发言人 用户对象
	private MeetingUser chairman;													//主席 用户对象
	//private Mix mix = new Mix();													//混音信息 
	//private List<VideoFormat> video_formats = new ArrayList<VideoFormat>();			//主视频格式列表
	private List<MeetingUser> invite_members = new ArrayList<MeetingUser>();		//参会人员
	//private Vmt vmp = new Vmt();													//画面合成设置
	//private List<MeetingUser> vips = new ArrayList<MeetingUser>();					//vip成员列表
	//private Poll poll = new Poll();													//轮询设置
	//private Recorder recorder = new Recorder();										//录像设置
	//private Dcs dcs = new Dcs();													//数据协作
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public Integer getClosed_conf() {
		return closed_conf;
	}
	public void setClosed_conf(Integer closed_conf) {
		this.closed_conf = closed_conf;
	}
	public Integer getSafe_conf() {
		return safe_conf;
	}
	public void setSafe_conf(Integer safe_conf) {
		this.safe_conf = safe_conf;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getEncrypted_type() {
		return encrypted_type;
	}
	public void setEncrypted_type(Integer encrypted_type) {
		this.encrypted_type = encrypted_type;
	}
	public Integer getConf_type() {
		return conf_type;
	}
	public void setConf_type(Integer conf_type) {
		this.conf_type = conf_type;
	}
	public Integer getCall_mode() {
		return call_mode;
	}
	public void setCall_mode(Integer call_mode) {
		this.call_mode = call_mode;
	}
	public Integer getCall_times() {
		return call_times;
	}
	public void setCall_times(Integer call_times) {
		this.call_times = call_times;
	}

	public Integer getMute() {
		return mute;
	}
	public void setMute(Integer mute) {
		this.mute = mute;
	}
	public Integer getSilence() {
		return silence;
	}
	public void setSilence(Integer silence) {
		this.silence = silence;
	}
	public Integer getVideo_quality() {
		return video_quality;
	}
	public void setVideo_quality(Integer video_quality) {
		this.video_quality = video_quality;
	}
	public String getEncrypted_key() {
		return encrypted_key;
	}
	public void setEncrypted_key(String encrypted_key) {
		this.encrypted_key = encrypted_key;
	}
	public Integer getDual_mode() {
		return dual_mode;
	}
	public void setDual_mode(Integer dual_mode) {
		this.dual_mode = dual_mode;
	}
	public Integer getVoice_activity_detection() {
		return voice_activity_detection;
	}
	public void setVoice_activity_detection(Integer voice_activity_detection) {
		this.voice_activity_detection = voice_activity_detection;
	}
	public Integer getCascade_mode() {
		return cascade_mode;
	}
	public void setCascade_mode(Integer cascade_mode) {
		this.cascade_mode = cascade_mode;
	}
	public Integer getCascade_upload() {
		return cascade_upload;
	}
	public void setCascade_upload(Integer cascade_upload) {
		this.cascade_upload = cascade_upload;
	}
	public Integer getCascade_return() {
		return cascade_return;
	}
	public void setCascade_return(Integer cascade_return) {
		this.cascade_return = cascade_return;
	}
	public Integer getCascade_return_para() {
		return cascade_return_para;
	}
	public void setCascade_return_para(Integer cascade_return_para) {
		this.cascade_return_para = cascade_return_para;
	}
	public Integer getPublic_conf() {
		return public_conf;
	}
	public void setPublic_conf(Integer public_conf) {
		this.public_conf = public_conf;
	}
	public Integer getMax_join_mt() {
		return max_join_mt;
	}
	public void setMax_join_mt(Integer max_join_mt) {
		this.max_join_mt = max_join_mt;
	}
	public Integer getAuto_end() {
		return auto_end;
	}
	public void setAuto_end(Integer auto_end) {
		this.auto_end = auto_end;
	}
	public Integer getPreoccpuy_resource() {
		return preoccpuy_resource;
	}
	public void setPreoccpuy_resource(Integer preoccpuy_resource) {
		this.preoccpuy_resource = preoccpuy_resource;
	}
	public MeetingUser getSpeaker() {
		return speaker;
	}
	public void setSpeaker(MeetingUser speaker) {
		this.speaker = speaker;
	}
	public MeetingUser getChairman() {
		return chairman;
	}
	public void setChairman(MeetingUser chairman) {
		this.chairman = chairman;
	}
//	public Mix getMix() {
//		return mix;
//	}
//	public void setMix(Mix mix) {
//		this.mix = mix;
//	}
//	public List<VideoFormat> getVideo_formats() {
//		return video_formats;
//	}
//	public void setVideo_formats(List<VideoFormat> video_formats) {
//		this.video_formats = video_formats;
//	}
	public List<MeetingUser> getInvite_members() {
		return invite_members;
	}
	public void setInvite_members(List<MeetingUser> invite_members) {
		this.invite_members = invite_members;
	}
//	public Vmt getVmp() {
//		return vmp;
//	}
//	public void setVmp(Vmt vmp) {
//		this.vmp = vmp;
//	}
//	public List<MeetingUser> getVips() {
//		return vips;
//	}
//	public void setVips(List<MeetingUser> vips) {
//		this.vips = vips;
//	}
//	public Poll getPoll() {
//		return poll;
//	}
//	public void setPoll(Poll poll) {
//		this.poll = poll;
//	}
	public Integer getCall_interval() {
		return call_interval;
	}
	public void setCall_interval(Integer call_interval) {
		this.call_interval = call_interval;
	}
	public Integer getVacinterval() {
		return vacinterval;
	}
	public void setVacinterval(Integer vacinterval) {
		this.vacinterval = vacinterval;
	}
	public Integer getCreate_type() {
		return create_type;
	}
	public void setCreate_type(Integer create_type) {
		this.create_type = create_type;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public Integer getFec_mode() {
		return fec_mode;
	}
	public void setFec_mode(Integer fec_mode) {
		this.fec_mode = fec_mode;
	}
	public Integer getMute_filter() {
		return mute_filter;
	}
	public void setMute_filter(Integer mute_filter) {
		this.mute_filter = mute_filter;
	}
	public Integer getDoubleflow() {
		return doubleflow;
	}
	public void setDoubleflow(Integer doubleflow) {
		this.doubleflow = doubleflow;
	}
	public Integer getOne_reforming() {
		return one_reforming;
	}
	public void setOne_reforming(Integer one_reforming) {
		this.one_reforming = one_reforming;
	}
	public String getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(String platform_id) {
		this.platform_id = platform_id;
	}
//	public List<MeetingUser> getKeep_calling_members() {
//		return keep_calling_members;
//	}
//	public void setKeep_calling_members(List<MeetingUser> keep_calling_members) {
//		this.keep_calling_members = keep_calling_members;
//	}
//	public Recorder getRecorder() {
//		return recorder;
//	}
//	public void setRecorder(Recorder recorder) {
//		this.recorder = recorder;
//	}
//	public Dcs getDcs() {
//		return dcs;
//	}
//	public void setDcs(Dcs dcs) {
//		this.dcs = dcs;
//	}
	
	
}
