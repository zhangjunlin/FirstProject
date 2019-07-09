package com.auxing.znhy.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.FieldFill;
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
 * @since 2018-10-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Tvwall extends Model<Tvwall> {

    private static final long serialVersionUID = 1L;

    /**
     * 电视墙ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 电视墙名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 画面风格：行
     */
    @TableField("LINE")
    private Integer line;

    /**
     * 画面风格：列
     */
    @TableField("COL")
    private Integer col;

    /**
     * 电视墙ID
     */
    @TableField("TELEVISION_ID")
    private String televisionId;

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
     * 创建人ID
     */
    @TableField("CREATE_ID")
    private String createId;

    /**
     * 预留字段2
     */
    @TableField(value = "DEPARTMENT_ID",fill = FieldFill.INSERT)
    private String departmentId;

    /**
     * 预留字段3
     */
    @TableField("YLZD3")
    private String ylzd3;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
