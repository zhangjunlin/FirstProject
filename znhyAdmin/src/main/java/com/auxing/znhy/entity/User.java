package com.auxing.znhy.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author auxing
 * @since 2018-10-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID",type = IdType.AUTO)
    private Integer id;

    /**
     * 登录名
     */
    @TableField("ACCOUNT")
    private String account;

    /**
     * 密码
     */
    @TableField("PASSWORD")
    private String password;

    /**
     * 用户名
     */
    @TableField("ACTURAL_NAME")
    private String acturalName;

    /**
     * 工号
     */
    @TableField("JOB_NUMBER")
    private String jobNumber;

    /**
     * E164
     */
    @TableField("E164")
    private String e164;
    /**
     * 用户类型
     */
    @TableField("USER_TYPE")
    private Integer userType;

    /**
     * 账号moid
     */
    @TableField("MOID")
    private String moid;

    /**
     * 性别
     */
    @TableField("SEX")
    private Integer sex;

    /**
     * 出生日期
     */
    @TableField("BIRTH")
    private String birth;

    /**
     * 手机
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 邮箱
     */
    @TableField("EMAIL")
    private String email;

    /**
     * 部门
     */
    @TableField("DEPARTMENT")
    private String department;

    /**
     * 账号类型
     */
    @TableField("ACCOUNT_TYPE")
    private Integer accountType;

    /**
     * 是否启用
     */
    @TableField("ENABLE")
    private Integer enable;
    
    /**
     * 域id
     */
    @TableField("DOMAIN_ID")
    private String domainId;

    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

	

	public User() {
		super();
	}



	public User(String account, String password, String acturalName,
			String jobNumber, String e164, Integer userType, String moid,
			Integer sex, String birth, String mobile, String email,
			String department, Integer accountType, Integer enable) {
		super();
		this.account = account;
		this.password = password;
		this.acturalName = acturalName;
		this.jobNumber = jobNumber;
		this.e164 = e164;
		this.userType = userType;
		this.moid = moid;
		this.sex = sex;
		this.birth = birth;
		this.mobile = mobile;
		this.email = email;
		this.department = department;
		this.accountType = accountType;
		this.enable = enable;
	}



	public User(String account, String password, String acturalName,
			String jobNumber, String e164, Integer userType, String moid,
			Integer sex, String birth, String mobile, String email,
			String department, Integer accountType, Integer enable,
			String domainId) {
		super();
		this.account = account;
		this.password = password;
		this.acturalName = acturalName;
		this.jobNumber = jobNumber;
		this.e164 = e164;
		this.userType = userType;
		this.moid = moid;
		this.sex = sex;
		this.birth = birth;
		this.mobile = mobile;
		this.email = email;
		this.department = department;
		this.accountType = accountType;
		this.enable = enable;
		this.domainId = domainId;
	}



	public User(String acturalName, Integer userType, String department) {
		super();
		this.acturalName = acturalName;
		this.userType = userType;
		this.department = department;
	}



	public User(String account, String password, String acturalName,
			String jobNumber, String e164, String moid, Integer sex,
			String birth, String mobile, String email, String department,
			Integer accountType, Integer enable, String domainId) {
		super();
		this.account = account;
		this.password = password;
		this.acturalName = acturalName;
		this.jobNumber = jobNumber;
		this.e164 = e164;
		this.moid = moid;
		this.sex = sex;
		this.birth = birth;
		this.mobile = mobile;
		this.email = email;
		this.department = department;
		this.accountType = accountType;
		this.enable = enable;
		this.domainId = domainId;
	}

}
