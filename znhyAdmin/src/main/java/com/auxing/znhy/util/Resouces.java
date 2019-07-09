package com.auxing.znhy.util;

/**
 * @auther liuyy
 * @date 2018/10/11 0011 下午 5:56
 * 会议资源
 */

public class Resouces {
    //虚拟会议室id
    private String id;
    //虚拟会议室名称 最大字符长度：64个字符
    private String name;
    //会议码率
    private Integer bitrate;
    //是否是专属会议室
    private Integer exclusive;
    //虚拟会议号码
    private String e164;
    //会议主题，针对使用中的虚拟会议室，如果空闲则为空
    private String conf_name;
    //会议状态
    private String status;
    //最大与会终端数
    private Integer max_join_mt;
    //视频分辨率
    private Integer resolution;
    //传输加密AES加密密钥,仅AES加密时返回
    private String encrypted_key;
    //资源总数 最大字符长度：8个字节
    private Integer total;
    //资源已占用数
    private Integer used;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBitrate() {
        return bitrate;
    }

    public void setBitrate(Integer bitrate) {
        this.bitrate = bitrate;
    }

    public Integer getExclusive() {
        return exclusive;
    }

    public void setExclusive(Integer exclusive) {
        this.exclusive = exclusive;
    }

    public String getE164() {
        return e164;
    }

    public void setE164(String e164) {
        this.e164 = e164;
    }

    public String getConf_name() {
        return conf_name;
    }

    public void setConf_name(String conf_name) {
        this.conf_name = conf_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMax_join_mt() {
        return max_join_mt;
    }

    public void setMax_join_mt(Integer max_join_mt) {
        this.max_join_mt = max_join_mt;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public String getEncrypted_key() {
        return encrypted_key;
    }

    public void setEncrypted_key(String encrypted_key) {
        this.encrypted_key = encrypted_key;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }
}
