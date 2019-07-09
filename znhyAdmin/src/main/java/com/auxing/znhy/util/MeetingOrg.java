package com.auxing.znhy.util;

import java.util.List;


/**
 * 会议平台数据下发需要的部门
 * @author Joe
 *
 */
public class MeetingOrg  {
	
	private String department_moid;//部门序号（UUID格式）
	private String department_name;//部门名称
	private Integer seqencing;
	private List<MeetingOrg> departments;
	public String getDepartment_moid() {
		return department_moid;
	}
	public void setDepartment_moid(String department_moid) {
		this.department_moid = department_moid;
	}
	public String getDepartment_name() {
		return department_name;
	}
	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}
	public Integer getSeqencing() {
		return seqencing;
	}
	public void setSeqencing(Integer seqencing) {
		this.seqencing = seqencing;
	}
	public List<MeetingOrg> getDepartments() {
		return departments;
	}
	public void setDepartments(List<MeetingOrg> departments) {
		this.departments = departments;
	}
	
}
