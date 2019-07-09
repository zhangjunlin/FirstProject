package com.auxing.znhy.util;

import java.util.List;

/**
 * @auther liuyy
 * @date 2018/10/13 0013 上午 11:19
 * 虚拟会议室数据
 */

public class VirtualMeetingRoomResource {
    private String user_domain_moid;//用户域moid
    private String user_domain_name;//用户域名称
    private String resource_type;//资源类型:1-仅限制方数；2-同时限制方数及分辨率；
    private List<Resouces> resources;//资源列表
    private List<VirtualMeetingRoomResource> sub_user_domains;//子级用户域及虚拟会议室信息

    public String getUser_domain_moid() {
        return user_domain_moid;
    }

    public void setUser_domain_moid(String user_domain_moid) {
        this.user_domain_moid = user_domain_moid;
    }

    public String getUser_domain_name() {
        return user_domain_name;
    }

    public void setUser_domain_name(String user_domain_name) {
        this.user_domain_name = user_domain_name;
    }

    public String getResource_type() {
        return resource_type;
    }

    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    public List<Resouces> getResources() {
        return resources;
    }

    public void setResources(List<Resouces> resources) {
        this.resources = resources;
    }

    public List<VirtualMeetingRoomResource> getSub_user_domains() {
        return sub_user_domains;
    }

    public void setSub_user_domains(List<VirtualMeetingRoomResource> sub_user_domains) {
        this.sub_user_domains = sub_user_domains;
    }
}
