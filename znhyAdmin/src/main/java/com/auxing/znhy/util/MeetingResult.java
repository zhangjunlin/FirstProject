package com.auxing.znhy.util;

import java.util.List;

import com.auxing.znhy.util.meeting.*;

public class MeetingResult {
	
	private Integer success;
	private String username;
	private Integer error_code;
	private String account_token;
	private String cookie;
	private String describe;
	private String department_moid;			//部门id
	private String department_name;			//部门名称
	private String total;
	private List<Accounts> accounts;
	private List<Domain> domains;
	private List<Resouces> virtual_meeting_rooms;
	private VirtualMeetingRoomResource virtual_meeting_room_resource; //虚拟会议室数据
	private List<MeetingOrg> departments;
	private List<Hdus> hdus;//电视墙列表
	private List<UserDomain> userDomains;//用户域列表
	private List<Mts> mts;//终端数组
	
	private String name;
	private String e164;						//虚拟会议号码
	private Integer bitrate;					//会议码率
	private  List<VideoFormat> video_formats;	//主视频格式列表
	private String conf_name;					//会议名称，针对使用中的虚拟会议室，如果空闲则为空
	private Integer conf_type;					//会议类型0-传统会议；1-端口媒体会议；
	private Integer status;						//会议状态0-空闲；1-使用中；	
	private Integer max_join_mt;				//最大与会终端数8-8方视频会议；32-32方视频会议；64-64方视频会议；192-192方视频会议；
	private Integer cascade_mode;				//级联模式 0-简单级联；1-合并级联；
	private Integer cascade_upload;				//	是否级联上传 0-否；1-是；
	private Integer cascade_return;				//	是否级联回传 0-否；1-是；
	private Integer cascade_return_para;		//级联回传带宽参数
	private Integer mute;						//是否开启初始化哑音 0-否；1-是；
	private Integer mute_filter;				//是否开启全场哑音例外 0-不开启；1-开启；
	private Mix mix;							//混音信息
	private Vmt vmp;							//画面合成设置
	private Recorder recorder;					//录像设置
	private Integer preoccpuy_resource;			//预占资源 0-否；1-是；
	private Integer encrypted_type;				//传输加密类型 0-不加密；2-AES加密；3-商密（SM4）；4-商密（SM1）；
	private Integer encrypted_auth;				//	终端双向认证 0-关闭；1-开启；
	private String encrypted_key;				//传输加密AES加密密钥,仅AES加密时返回1.字符限制：仅支持 英文字母(大小写) 数字 下划线（_） 小数点（.）2.最大字符长度：16个字节
	private Integer dual_mode;					//内容共享权限／双流权限1-任意会场；0-发言会场；
	private Integer call_mode;					//呼叫模式0-手动；2-自动；
	private Integer call_times;					//呼叫次数
	private Integer call_interval;				//呼叫间隔(秒)
	private Integer voice_activity_detection;	//是否开启语音激励 0-否；1-是；
	private Integer vacinterval;				//语音激励敏感度(s)
	private Integer one_reforming;				//归一重整
	private Integer fec_mode;					//FEC开关0-关闭；1-开启；
	private Integer exclusive;					//是否是专属虚拟会议室 0-否；1-是；
	private List<MeetingUser> exclusive_users;	//专属人员列表
	private List<Vrs> vrs;//vrs数组
	
	//画面合成
	private Integer mode;//画面合成模式 1-定制画面合成；2-自动画面合成；3-自动画面合成批量轮询；4-定制画面合成批量轮询；
	private Integer layout;//
	private Integer broadcast;//是否广播 0-否；1-是；
	private Integer voice_hint;//是否识别声音来源 0-否；1-是;
	private Integer show_mt_name;//是否显示别名 0-否；1-是；
	private MtNameStyle mt_name_style;	
	private List<Member> members ;
	//录像
	private String video_name;//录像保存的文件名称1.字符限制：a.不支持输入特殊字符 % : & * ^ ~ ' "" ? / \ < > | ` " $b.首字符和尾字符，不允许输入下划线（_） 减号（-） @ 小数点（.）2.最大字符长度：64个字节
	private Integer recorder_type;//录像类型1-会议录像；2-终端录像；
	private Integer publish_mode;//发布模式 0-不发布；1-发布；
	private Integer anonymous;//是否支持免登陆观看直播 0-不支持；1-支持；
	private Integer recorder_mode;//录像模式 1-录像；2-直播；3-录像+直播；
	private Integer main_stream;//是否录主格式码流（视频+音频） 0-否；1-是；
	private Integer dual_stream;//是否录双流（仅双流） 0-否；1-是；
	private String vrs_id;//vrs的id信息,用于指定录像的vrs
	private Integer state;//录像状态 0-未录像；1-正在录像；2-暂停；3-正在呼叫实体；4-准备录像；
	private Integer current_progress;//当前录像进度, 单位为: 秒
	private String rec_id;//录像机id
	
	
	private List<Recorders>recorders;//会议记录集合
	
	private String conf_id;					//会议号
    private String cascade_id;              //级联号 最大字符长度：48个字节
    private String mt_id;                   //终端号 最大字符长度：48个字节
    private List<Cascades> cascades;        //级联数组
    private List<Mts> children;
	private String description;				//结果描述
	private List<TerminalWatch> inspections;//选看终端列表返回
	
	
	private String meeting_room_name;	//虚拟会议室名称
	private Integer conf_level;			//会议等级
	private String start_time;			//开始时间
	private String end_time;			//结束时间
	private Integer duration;			//会议时间
	private Integer closed_conf;		//会议免打扰
	private Integer safe_conf;			//会议安全
	private Integer silence;			//初始化静音 
	private Integer video_quality;		//视频质量
	private Integer public_conf;		//是否公共会议室 
	private Integer auto_end;			//开始时间
	private Integer preoccupy_resource;			//开始时间
	private Integer force_broadcast;			//开始时间
	private Integer vmp_enable;			//开始时间
	private Integer mix_enable;			//开始时间
	private Integer poll_enable;			//开始时间
	private Integer need_password;			//开始时间
	private Integer doubleflow;			//开始时间
	private String platform_id;			//开始时间
	private MeetingUser creator;			//开始时间
	
	
	private Integer keep_time;
	private Integer num;
	
	private Poll poll;
	
	private List<Meeting> confs;	//会议列表
	
	
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getError_code() {
		return error_code;
	}
	public void setError_code(Integer error_code) {
		this.error_code = error_code;
	}
	public String getAccount_token() {
		return account_token;
	}
	public void setAccount_token(String account_token) {
		this.account_token = account_token;
	}
	public String getCookie() {
		return cookie;
	}
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getDepartment_moid() {
		return department_moid;
	}
	public void setDepartment_moid(String department_moid) {
		this.department_moid = department_moid;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public List<Accounts> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Accounts> accounts) {
		this.accounts = accounts;
	}
	public List<Domain> getDomains() {
		return domains;
	}
	public void setDomains(List<Domain> domains) {
		this.domains = domains;
	}
	public List<Resouces> getVirtual_meeting_rooms() {
		return virtual_meeting_rooms;
	}

	public void setVirtual_meeting_rooms(List<Resouces> virtual_meeting_rooms) {
		this.virtual_meeting_rooms = virtual_meeting_rooms;
	}
	public String getConf_id() {
		return conf_id;
	}
	public void setConf_id(String conf_id) {
		this.conf_id = conf_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    public VirtualMeetingRoomResource getVirtual_meeting_room_resource() {
        return virtual_meeting_room_resource;
    }

    public void setVirtual_meeting_room_resource(VirtualMeetingRoomResource virtual_meeting_room_resource) {
        this.virtual_meeting_room_resource = virtual_meeting_room_resource;
    }
	public List<MeetingOrg> getDepartments() {
		return departments;
	}
	public void setDepartments(List<MeetingOrg> departments) {
		this.departments = departments;
	}

    public List<Hdus> getHdus() {
        return hdus;
    }

    public void setHdus(List<Hdus> hdus) {
        this.hdus = hdus;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getE164() {
		return e164;
	}
	public void setE164(String e164) {
		this.e164 = e164;
	}
	public Integer getBitrate() {
		return bitrate;
	}
	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	public List<VideoFormat> getVideo_formats() {
		return video_formats;
	}
	public void setVideo_formats(List<VideoFormat> video_formats) {
		this.video_formats = video_formats;
	}
	public String getConf_name() {
		return conf_name;
	}
	public void setConf_name(String conf_name) {
		this.conf_name = conf_name;
	}
	public Integer getConf_type() {
		return conf_type;
	}
	public void setConf_type(Integer conf_type) {
		this.conf_type = conf_type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getMax_join_mt() {
		return max_join_mt;
	}
	public void setMax_join_mt(Integer max_join_mt) {
		this.max_join_mt = max_join_mt;
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
	public Integer getMute() {
		return mute;
	}
	public void setMute(Integer mute) {
		this.mute = mute;
	}
	public Integer getMute_filter() {
		return mute_filter;
	}
	public void setMute_filter(Integer mute_filter) {
		this.mute_filter = mute_filter;
	}
	public Mix getMix() {
		return mix;
	}
	public void setMix(Mix mix) {
		this.mix = mix;
	}
	public Vmt getVmp() {
		return vmp;
	}
	public void setVmp(Vmt vmp) {
		this.vmp = vmp;
	}
	public Recorder getRecorder() {
		return recorder;
	}
	public void setRecorder(Recorder recorder) {
		this.recorder = recorder;
	}
	public Integer getPreoccpuy_resource() {
		return preoccpuy_resource;
	}
	public void setPreoccpuy_resource(Integer preoccpuy_resource) {
		this.preoccpuy_resource = preoccpuy_resource;
	}
	public Integer getEncrypted_type() {
		return encrypted_type;
	}
	public void setEncrypted_type(Integer encrypted_type) {
		this.encrypted_type = encrypted_type;
	}
	public Integer getEncrypted_auth() {
		return encrypted_auth;
	}
	public void setEncrypted_auth(Integer encrypted_auth) {
		this.encrypted_auth = encrypted_auth;
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
	public Integer getCall_interval() {
		return call_interval;
	}
	public void setCall_interval(Integer call_interval) {
		this.call_interval = call_interval;
	}
	public Integer getVoice_activity_detection() {
		return voice_activity_detection;
	}
	public void setVoice_activity_detection(Integer voice_activity_detection) {
		this.voice_activity_detection = voice_activity_detection;
	}
	public Integer getVacinterval() {
		return vacinterval;
	}
	public void setVacinterval(Integer vacinterval) {
		this.vacinterval = vacinterval;
	}
	public Integer getOne_reforming() {
		return one_reforming;
	}
	public void setOne_reforming(Integer one_reforming) {
		this.one_reforming = one_reforming;
	}
	public Integer getFec_mode() {
		return fec_mode;
	}
	public void setFec_mode(Integer fec_mode) {
		this.fec_mode = fec_mode;
	}
	public Integer getExclusive() {
		return exclusive;
	}
	public void setExclusive(Integer exclusive) {
		this.exclusive = exclusive;
	}
	public List<MeetingUser> getExclusive_users() {
		return exclusive_users;
	}
	public void setExclusive_users(List<MeetingUser> exclusive_users) {
		this.exclusive_users = exclusive_users;
	}
	public List<UserDomain> getUserDomains() {
		return userDomains;
	}
	public void setUserDomains(List<UserDomain> userDomains) {
		this.userDomains = userDomains;
	}

    public List<Mts> getMts() {
        return mts;
    }

    public void setMts(List<Mts> mts) {
        this.mts = mts;
    }

    public String getCascade_id() {
        return cascade_id;
    }

    public void setCascade_id(String cascade_id) {
        this.cascade_id = cascade_id;
    }

    public String getMt_id() {
        return mt_id;
    }

    public void setMt_id(String mt_id) {
        this.mt_id = mt_id;
    }

    public List<Cascades> getCascades() {
        return cascades;
    }

    public void setCascades(List<Cascades> cascades) {
        this.cascades = cascades;
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
	public String getRec_id() {
		return rec_id;
	}
	public void setRec_id(String rec_id) {
		this.rec_id = rec_id;
	}
	public List<Recorders> getRecorders() {
		return recorders;
	}
	public void setRecorders(List<Recorders> recorders) {
		this.recorders = recorders;
	}

    public List<Mts> getChildren() {
        return children;
    }

    public void setChildren(List<Mts> children) {
        this.children = children;
    }
	public List<Vrs> getVrs() {
		return vrs;
	}
	public void setVrs(List<Vrs> vrs) {
		this.vrs = vrs;
	}

    public List<TerminalWatch> getInspections() {
        return inspections;
    }

    public void setInspections(List<TerminalWatch> inspections) {
        this.inspections = inspections;
    }
	public String getMeeting_room_name() {
		return meeting_room_name;
	}
	public void setMeeting_room_name(String meeting_room_name) {
		this.meeting_room_name = meeting_room_name;
	}
	public Integer getConf_level() {
		return conf_level;
	}
	public void setConf_level(Integer conf_level) {
		this.conf_level = conf_level;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
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
	public Integer getPublic_conf() {
		return public_conf;
	}
	public void setPublic_conf(Integer public_conf) {
		this.public_conf = public_conf;
	}
	public Integer getAuto_end() {
		return auto_end;
	}
	public void setAuto_end(Integer auto_end) {
		this.auto_end = auto_end;
	}
	public Integer getPreoccupy_resource() {
		return preoccupy_resource;
	}
	public void setPreoccupy_resource(Integer preoccupy_resource) {
		this.preoccupy_resource = preoccupy_resource;
	}
	public Integer getForce_broadcast() {
		return force_broadcast;
	}
	public void setForce_broadcast(Integer force_broadcast) {
		this.force_broadcast = force_broadcast;
	}
	public Integer getVmp_enable() {
		return vmp_enable;
	}
	public void setVmp_enable(Integer vmp_enable) {
		this.vmp_enable = vmp_enable;
	}
	public Integer getMix_enable() {
		return mix_enable;
	}
	public void setMix_enable(Integer mix_enable) {
		this.mix_enable = mix_enable;
	}
	public Integer getPoll_enable() {
		return poll_enable;
	}
	public void setPoll_enable(Integer poll_enable) {
		this.poll_enable = poll_enable;
	}
	public Integer getNeed_password() {
		return need_password;
	}
	public void setNeed_password(Integer need_password) {
		this.need_password = need_password;
	}
	public Integer getDoubleflow() {
		return doubleflow;
	}
	public void setDoubleflow(Integer doubleflow) {
		this.doubleflow = doubleflow;
	}
	public String getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(String platform_id) {
		this.platform_id = platform_id;
	}
	public MeetingUser getCreator() {
		return creator;
	}
	public void setCreator(MeetingUser creator) {
		this.creator = creator;
	}
	public Integer getKeep_time() {
		return keep_time;
	}
	public void setKeep_time(Integer keep_time) {
		this.keep_time = keep_time;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Poll getPoll() {
		return poll;
	}
	public void setPoll(Poll poll) {
		this.poll = poll;
	}
	public List<Meeting> getConfs() {
		return confs;
	}
	public void setConfs(List<Meeting> confs) {
		this.confs = confs;
	}
	@Override
	public String toString() {
		return "MeetingResult [success=" + success + ", error_code=" + error_code + ", describe=" + describe + "]";
	}
	
	
}
