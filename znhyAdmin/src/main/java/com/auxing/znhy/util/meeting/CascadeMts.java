package com.auxing.znhy.util.meeting;

import java.util.List;

public class CascadeMts {
	    private String conf_id;					//会议号
	    private String name;                    //会议名称 最大字符长度：64个字节
	    private String cascade_id;              //级联号 最大字符长度：48个字节
	    private String mt_id;                   //终端号 最大字符长度：48个字节
	    private List<Cascades> cascades;        //级联数组
	    private String pid;	//父id
	    private String id;//id
	    
	    private String alias;//终端别名 最大字符长度：128个字节
	    private String ip;//终端IP
	    private Integer online;//是否在线
	    private String e164;//终端E164号
	    private Integer type;//终端类型
	    private Integer bitrate;//呼叫码率
	    private Integer silence;//是否静音
	    private Integer mute;//是否哑音
	    private Integer dual;//是否在发送双流
	    private Integer mix;//是否在混音
	    private Integer vmp;//是否在合成
	    private Integer inspection;//是否在选看
	    private Integer rec;//是否在录像
	    private Integer poll;//是否在轮询
	    private Integer upload;//是否在上传
	    private Integer protocol;//呼叫协议
	    private Integer call_mode;//呼叫模式
	    private String style;//0: 级联1:会议终端
	    List <CascadeMts> childrens;//集合
	    private Integer speaker;	//发言方0否1是
	    private Integer chairman;	//管理方0否1是
	    private Integer cascadesLevel = 0;	//终端级别 0本级终端 1级联终端
	    
		public String getConf_id() {
			return conf_id;
		}
		public void setConf_id(String conf_id) {
			this.conf_id = conf_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
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
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public Integer getOnline() {
			return online;
		}
		public void setOnline(Integer online) {
			this.online = online;
		}
		public String getE164() {
			return e164;
		}
		public void setE164(String e164) {
			this.e164 = e164;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Integer getBitrate() {
			return bitrate;
		}
		public void setBitrate(Integer bitrate) {
			this.bitrate = bitrate;
		}
		public Integer getSilence() {
			return silence;
		}
		public void setSilence(Integer silence) {
			this.silence = silence;
		}
		public Integer getMute() {
			return mute;
		}
		public void setMute(Integer mute) {
			this.mute = mute;
		}
		public Integer getDual() {
			return dual;
		}
		public void setDual(Integer dual) {
			this.dual = dual;
		}
		public Integer getMix() {
			return mix;
		}
		public void setMix(Integer mix) {
			this.mix = mix;
		}
		public Integer getVmp() {
			return vmp;
		}
		public void setVmp(Integer vmp) {
			this.vmp = vmp;
		}
		public Integer getInspection() {
			return inspection;
		}
		public void setInspection(Integer inspection) {
			this.inspection = inspection;
		}
		public Integer getRec() {
			return rec;
		}
		public void setRec(Integer rec) {
			this.rec = rec;
		}
		public Integer getPoll() {
			return poll;
		}
		public void setPoll(Integer poll) {
			this.poll = poll;
		}
		public Integer getUpload() {
			return upload;
		}
		public void setUpload(Integer upload) {
			this.upload = upload;
		}
		public Integer getProtocol() {
			return protocol;
		}
		public void setProtocol(Integer protocol) {
			this.protocol = protocol;
		}
		public Integer getCall_mode() {
			return call_mode;
		}
		public void setCall_mode(Integer call_mode) {
			this.call_mode = call_mode;
		}
		public String getStyle() {
			return style;
		}
		public void setStyle(String style) {
			this.style = style;
		}
		public CascadeMts(String mt_id, String pid,String alias,
				String ip, Integer online, String e164, Integer type,
				Integer bitrate, Integer silence, Integer mute, Integer dual,
				Integer mix, Integer vmp, Integer inspection, Integer rec,
				Integer poll, Integer upload, Integer protocol,
				Integer call_mode, String style) {
			super();
			this.mt_id = mt_id;
			this.pid = pid;
			this.alias = alias;
			this.ip = ip;
			this.online = online;
			this.e164 = e164;
			this.type = type;
			this.bitrate = bitrate;
			this.silence = silence;
			this.mute = mute;
			this.dual = dual;
			this.mix = mix;
			this.vmp = vmp;
			this.inspection = inspection;
			this.rec = rec;
			this.poll = poll;
			this.upload = upload;
			this.protocol = protocol;
			this.call_mode = call_mode;
			this.style = style;
		}
		public CascadeMts(String conf_id, String name, String cascade_id,
				String mt_id, String pid, String id, String style,Integer online,String e164,Integer cascadesLevel) {
			super();
			this.conf_id = conf_id;
			this.name = name;
			this.cascade_id = cascade_id;
			this.mt_id = mt_id;
			this.pid = pid;
			this.id = id;
			this.style = style;
			this.online = online;
			this.e164 = e164;
			this.cascadesLevel = cascadesLevel;
		}
		public List<CascadeMts> getChildrens() {
			return childrens;
		}
		public void setChildrens(List<CascadeMts> childrens) {
			this.childrens = childrens;
		}
		public Integer getSpeaker() {
			return speaker;
		}
		public void setSpeaker(Integer speaker) {
			this.speaker = speaker;
		}
		public Integer getChairman() {
			return chairman;
		}
		public void setChairman(Integer chairman) {
			this.chairman = chairman;
		}
		public Integer getCascadesLevel() {
			return cascadesLevel;
		}
		public void setCascadesLevel(Integer cascadesLevel) {
			this.cascadesLevel = cascadesLevel;
		}
		
	    
}
