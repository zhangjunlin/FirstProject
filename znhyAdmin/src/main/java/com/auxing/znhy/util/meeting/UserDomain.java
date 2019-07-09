package com.auxing.znhy.util.meeting;

public class UserDomain {
	private String parentId;// 父用户域的moid，未返回代表为顶级用户域
	private String  moid;//用户域moid
	private String name;//用户域名称
	private String groupName;//集团名称
	private String version;//版本号，该组织架构有更新时，版本号会更新
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getMoid() {
		return moid;
	}
	public void setMoid(String moid) {
		this.moid = moid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	


}
