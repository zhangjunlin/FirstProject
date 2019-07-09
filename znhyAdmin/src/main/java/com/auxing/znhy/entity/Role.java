package com.auxing.znhy.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author auxing
 * @since 2018-10-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Role extends Model<Role> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 描述
     */
    @TableField("DES")
    private String des;

    /**
     * 排序
     */
    @TableField("SORT")
    private String sort;

    /**
     * 类型
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR",fill = FieldFill.INSERT)
    private String creator;

    /**
     * 创建时间
     */
    @TableField(value = "CREAT_TIME",fill = FieldFill.INSERT)
    private String creatTime;

    /**
     * 更新时间
     */
    @TableField(value = "UPDATE_TIME",fill = FieldFill.INSERT_UPDATE)
    private String updateTime;

    /**
     * 角色ID
     */
    @TableField("ROLE_ID")
    private String roleId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
