package com.auxing.znhy.util;

import com.auxing.znhy.util.meeting.*;

import java.util.List;

/**
 * @auther liuyy
 * @date 2018/10/12 0012 上午 10:24
 */

public class VirtualMeetDetails {
    private Integer success;//是否成功
    private String name;//虚拟会议室名称
    private String e164;//虚拟会议号码
    private Integer bitrate;//会议码率，目前以主视频格式中的码率为准
    private List<VideoFormat>  video_formats;//主视频格式列表
    private String conf_name;//会议名称，针对使用中的虚拟会议室，如果空闲则为空
    private Integer conf_type;//会议类型:0-传统会议;1-端口媒体会议；
    private Integer status;//会议状态:0-空闲；1-使用中；
    private Integer max_join_mt;//最大与会终端数:8-8方视频会议；32-32方视频会议；64-64方视频会议；192-192方视频会议；
    private Integer cascade_mode;//级联模式 :0-简单级联；1-合并级联；
    private Integer cascade_upload;//是否级联上传:0-否；1-是；
    private Integer cascade_return;//是否级联回传:0-否；1-是；
    private Integer cascade_return_para;//级联回传带宽参数
    private Integer mute;//是否开启初始化哑音:0-否；1-是；
    private Integer mute_filter;//是否开启全场哑音例外 :0-不开启；1-开启；
    private Mix mix;//混音信息
    private Vmt vmp;//画面合成设置
    private Recorder recorder;//录像设置
    private Integer preoccpuy_resource;//预占资源:0-否；1-是；
    private Integer encrypted_type;//传输加密类型 :0-不加密；2-AES加密；3-商密（SM4）；4-商密（SM1）；
    private Integer encrypted_auth;//终端双向认证 :0-关闭；1-开启；
    private String encrypted_key;//传输加密AES加密密钥,仅AES加密时返回:1.字符限制：仅支持 英文字母(大小写) 数字 下划线（_） 小数点（.）2.最大字符长度：16个字节
    private Integer dual_mode;//内容共享权限／双流权限:1-任意会场；0-发言会场；
    private Integer call_mode;//呼叫模式:0-手动；2-自动；
    private Integer call_times;//呼叫次数
    private Integer call_interval;//呼叫间隔(秒)
    private Integer voice_activity_detection;//是否开启语音激励 0-否；1-是；
    private Integer vacinterval;//语音激励敏感度(s)
    private Integer one_reforming;//归一重整
    private Integer fec_mode;//FEC开关:0-关闭；1-开启；
    private Integer exclusive;//是否是专属虚拟会议室 :0-否；1-是；
    private List<MeetingUser> exclusive_users;//专属人员列表

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
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
}
