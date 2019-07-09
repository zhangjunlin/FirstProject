package com.auxing.znhy.util;

import java.util.List;

public class Accounts {

	private String account;
	private Integer binded;
	private String date_of_birth;
	private String e164;
	private String email;
	private Integer enable;
	private String ext_num;
	private String fax;
	private String job_num;
	private Integer limited;
	private String mobile;
	private String account_moid;
	private String account_jid;
	private String name;
	private String office_location;
	private String password;
	private Integer sex;
	
	private List<MeetingOrg> departments;	//部门

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Integer getBinded() {
		return binded;
	}

	public void setBinded(Integer binded) {
		this.binded = binded;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getE164() {
		return e164;
	}

	public void setE164(String e164) {
		this.e164 = e164;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEnable() {
		return enable;
	}

	public void setEnable(Integer enable) {
		this.enable = enable;
	}

	public String getExt_num() {
		return ext_num;
	}

	public void setExt_num(String ext_num) {
		this.ext_num = ext_num;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getJob_num() {
		return job_num;
	}

	public void setJob_num(String job_num) {
		this.job_num = job_num;
	}

	public Integer getLimited() {
		return limited;
	}

	public void setLimited(Integer limited) {
		this.limited = limited;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAccount_moid() {
		return account_moid;
	}

	public void setAccount_moid(String account_moid) {
		this.account_moid = account_moid;
	}

	public String getAccount_jid() {
		return account_jid;
	}

	public void setAccount_jid(String account_jid) {
		this.account_jid = account_jid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOffice_location() {
		return office_location;
	}

	public void setOffice_location(String office_location) {
		this.office_location = office_location;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public List<MeetingOrg> getDepartments() {
		return departments;
	}

	public void setDepartments(List<MeetingOrg> departments) {
		this.departments = departments;
	}
	
	
}
