package com.auxing.znhy.util.meeting;

public class Domain {
	private String moid;//域的moid
	private String parent_moid;//上级域的moid
	private String type;//域的类型：kernel(核心域), service(服务域), platform(平台域), user(用户域)
	private String name;//域的名称
	public String getMoid() {
		return moid;
	}
	public void setMoid(String moid) {
		this.moid = moid;
	}
	public String getParent_moid() {
		return parent_moid;
	}
	public void setParent_moid(String parent_moid) {
		this.parent_moid = parent_moid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
