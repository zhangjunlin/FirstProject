package com.auxing.znhy.util.meeting;

/**
 * 	会议用户类
 */
public class MeetingUser {
	private String name;		// 名称 最大字符长度：128个字节 仅当跟随类型为会控指定时才需要输入 
	private String account;		//帐号 最大字符长度：128个字节 仅当跟随类型为会控指定时才需要输入
	private Integer  account_type;	//帐号类型 1-moid；4-非系统邮箱；5-e164号；6-电话；7-ip地址；8-别名@ip(监控前端)；
	private Integer member_type;	// 跟随类型 1-会控指定；2-发言人跟随；3-管理方跟随；4-会议轮询跟随；7-内容共享跟随； 
	private Integer chn_idx;		//在画画合成中的位置
	private String mobile;			//手机号
	private String telephone;		//座机
	private String mt_id;			//通道终端号，仅当通道中为会控指定时需要 最大字符长度：48个字节
	private String ip;
	private String alias;
	private Integer protocol;			// 呼叫协议 
	private Integer bitrate;			//呼叫码率
	
	public MeetingUser() {
	}

	
	
	
	public MeetingUser(String name, String account, Integer account_type, Integer protocol) {
		this.name = name;
		this.account = account;
		this.account_type = account_type;
		this.protocol = protocol;
	}





	public MeetingUser(String name, String account, Integer account_type) {
		this.name = name;
		this.account = account;
		this.account_type = account_type;
	}
	
	
	
	
	public MeetingUser(Integer member_type, Integer chn_idx, String mt_id) {
		this.member_type = member_type;
		this.chn_idx = chn_idx;
		this.mt_id = mt_id;
	}


	public MeetingUser(String name, String account, Integer account_type, Integer member_type, Integer chn_idx) {
		this.name = name;
		this.account = account;
		this.account_type = account_type;
		this.member_type = member_type;
		this.chn_idx = chn_idx;
	}

	
	
	public MeetingUser(String account, Integer account_type, Integer protocol, Integer bitrate) {
		super();
		this.account = account;
		this.account_type = account_type;
		this.protocol = protocol;
		this.bitrate = bitrate;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getAccount_type() {
		return account_type;
	}
	public void setAccount_type(Integer account_type) {
		this.account_type = account_type;
	}
	public Integer getMember_type() {
		return member_type;
	}
	public void setMember_type(Integer member_type) {
		this.member_type = member_type;
	}
	public Integer getChn_idx() {
		return chn_idx;
	}
	public void setChn_idx(Integer chn_idx) {
		this.chn_idx = chn_idx;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}


	public String getMt_id() {
		return mt_id;
	}


	public void setMt_id(String mt_id) {
		this.mt_id = mt_id;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getAlias() {
		return alias;
	}


	public void setAlias(String alias) {
		this.alias = alias;
	}


	public Integer getProtocol() {
		return protocol;
	}


	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}


	public Integer getBitrate() {
		return bitrate;
	}


	public void setBitrate(Integer bitrate) {
		this.bitrate = bitrate;
	}
	
	
}
