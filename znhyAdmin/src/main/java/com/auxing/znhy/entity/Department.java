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
 * @since 2018-10-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Department extends Model<Department> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 部门名称
     */
    @TableField("DEPARTMENT_NAME")
    private String departmentName;

    /**
     * 部门ID
     */
    @TableField("DEPARTMENT_MOID")
    private String departmentMoid;

    /**
     * 父部门ID
     */
    @TableField("PARENT_ID")
    private String parentId;

    /**
     * 父部门ID
     */
    @TableField("TYPE")
    private String type;
    
    
    /**
     * 等级level
     */
    @TableField("LEVEL")
    private String level;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }


	public Department(String departmentName, String departmentMoid,
			String parentId) {
		super();
		this.departmentName = departmentName;
		this.departmentMoid = departmentMoid;
		this.parentId = parentId;
	}


	public Department() {
		super();
	}


	public Department(String departmentName, String departmentMoid,
			String parentId, String type) {
		super();
		this.departmentName = departmentName;
		this.departmentMoid = departmentMoid;
		this.parentId = parentId;
		this.type = type;
	}


	public Department(String departmentMoid) {
		super();
		this.departmentMoid = departmentMoid;
	}

}
