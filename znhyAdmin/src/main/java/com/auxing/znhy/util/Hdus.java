package com.auxing.znhy.util;

/**
 * @auther liuyy
 * @date 2018/10/15 0015 上午 10:35
 * 电视墙列表
 */

public class Hdus {
    private String hdu_id;//电视墙id 最大字符长度：48个字节
    private String hdu_name;//电视墙名称 最大字符长度：16个字节
    private Integer occupy;//是否被占用:0-否；1-是；
    private Integer online; //是否在线:0-否；1-是；
    private Integer layout;
    private Integer show_mt_name;

    public String getHdu_id() {
        return hdu_id;
    }

    public void setHdu_id(String hdu_id) {
        this.hdu_id = hdu_id;
    }

    public String getHdu_name() {
        return hdu_name;
    }

    public void setHdu_name(String hdu_name) {
        this.hdu_name = hdu_name;
    }

    public Integer getOccupy() {
        return occupy;
    }

    public void setOccupy(Integer occupy) {
        this.occupy = occupy;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

	public Integer getLayout() {
		return layout;
	}

	public void setLayout(Integer layout) {
		this.layout = layout;
	}

	public Integer getShow_mt_name() {
		return show_mt_name;
	}

	public void setShow_mt_name(Integer show_mt_name) {
		this.show_mt_name = show_mt_name;
	}
    
    
}
