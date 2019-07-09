package com.auxing.znhy.util.meeting;

/**
 * @auther liuyy
 * @date 2018/10/30 0030 下午 2:23
 */

public class Terminal {
    private String account;//终端E164号,IP或电话号码
    private Integer account_type = 5;//终端类型
    private Integer bitrate = 1024;//呼叫码率
    private Integer protocol = 0;//呼叫协议
    private Integer forced_call = 0;//是否强制呼叫，默认是0
    private Integer call_mode = 0;//呼叫模式，默认为创会时设置的呼叫模式

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

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getProtocol() {
        return protocol;
    }

    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    public Integer getForced_call() {
        return forced_call;
    }

    public void setForced_call(Integer forced_call) {
        this.forced_call = forced_call;
    }

    public Integer getCall_mode() {
        return call_mode;
    }

    public void setCall_mode(Integer call_mode) {
        this.call_mode = call_mode;
    }

}
