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
public class TemplateUser extends Model<TemplateUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "T_ID", type = IdType.AUTO)
    private Integer tId;

    @TableField("USER_ID")
    private String userId;

    @TableField("TEMPLATE_ID")
    private String chairMan;

    @TableField("TYPE")
    private Integer type;
    
    @Override
    protected Serializable pkVal() {
        return this.tId;
    }

}
