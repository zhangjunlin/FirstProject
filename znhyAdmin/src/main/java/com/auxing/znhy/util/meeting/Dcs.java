package com.auxing.znhy.util.meeting;

/**
 * 数据协作
 */
public class Dcs {
	private Integer mode = 0; 	//数据协作模式 0-关闭数据协作；1-管理方控制；2-自由协作；

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}
	
}
