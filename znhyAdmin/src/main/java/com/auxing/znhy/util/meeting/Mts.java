package com.auxing.znhy.util.meeting;


/**
 * @auther liuyy
 * @date 2018/10/26 0026 下午 3:26
 */

public class Mts {
    private String alias;//终端别名 最大字符长度：128个字节
    private String mt_id;//终端号 最大字符长度：48个字节
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
    private String account;//终端E164号,IP或电话号码
    private Integer account_type;//终端类型
    private Integer forced_call;//是否强制呼叫，默认是0 0-不强呼；1-强呼；
    
    private Integer mode;
    private Integer layout;
    private Integer voice_hint;
    private Integer show_mt_name;
    
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

	public Integer getForced_call() {
		return forced_call;
	}

	public void setForced_call(Integer forced_call) {
		this.forced_call = forced_call;
	}

	public Mts(String mt_id, Integer forced_call) {
		super();
		this.mt_id = mt_id;
		this.forced_call = forced_call;
	}

	public Mts(String mt_id) {
		super();
		this.mt_id = mt_id;
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

	public Mts() {
		super();
	}
    
}
