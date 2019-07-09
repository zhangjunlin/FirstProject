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
 * @since 2018-10-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Domainuser extends Model<Domainuser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField("NAME")
    private String name;

    @TableField("MOID")
    private String moid;

    @TableField("USERNAME")
    private String username;

    @TableField("PASSWORD")
    private String password;

    @TableField("ENABLED")
    private String enabled;
    
    @TableField("SUBSRIPTION")
    private Integer subsription;
    
    @Override
    protected Serializable pkVal() {
        return this.id;
    }

	public Domainuser(String enabled) {
		super();
		this.enabled = enabled;
	}

	public Domainuser() {
		super();
	}

	public Domainuser(String moid, String enabled) {
		super();
		this.moid = moid;
		this.enabled = enabled;
	}

	

}
