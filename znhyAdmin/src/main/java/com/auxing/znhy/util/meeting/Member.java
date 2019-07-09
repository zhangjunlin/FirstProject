package com.auxing.znhy.util.meeting;

public class Member {
	private Integer chn_idx;//画面合成通道索引，从0开始
	private Integer member_type;//成员类型1-会控指定；2-发言人跟随；3-主席跟随；4-轮询跟随；6-单通道轮询；7-双流跟随
	private String  mt_id;//通道终端号，仅当通道中为会控指定时需要 最大字符长度：48个字节
	private String vmp_id;
	
	private Poll poll;//单通道轮询参数，仅member_type为6时有效

	public Integer getChn_idx() {
		return chn_idx;
	}

	public void setChn_idx(Integer chn_idx) {
		this.chn_idx = chn_idx;
	}

	public Integer getMember_type() {
		return member_type;
	}

	public void setMember_type(Integer member_type) {
		this.member_type = member_type;
	}

	public String getMt_id() {
		return mt_id;
	}

	public void setMt_id(String mt_id) {
		this.mt_id = mt_id;
	}

	public Poll getPoll() {
		return poll;
	}

	public Member() {
		super();
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public Member(String mt_id) {
		super();
		this.mt_id = mt_id;
	}

	public String getVmp_id() {
		return vmp_id;
	}

	public void setVmp_id(String vmp_id) {
		this.vmp_id = vmp_id;
	}
	
	
}
