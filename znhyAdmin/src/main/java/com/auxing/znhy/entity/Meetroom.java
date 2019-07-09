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
 * @since 2018-10-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Meetroom extends Model<Meetroom> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 会议室名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 会议室容量
     */
    @TableField("CAPACITY")
    private String capacity;

    /**
     * 会议室面积
     */
    @TableField("ACREAGE")
    private String acreage;

    /**
     * 所在区域
     */
    @TableField("AREA")
    private String area;

    /**
     * 会议室类型
     */
    @TableField("TYPE")
    private Integer type;

    /**
     * 会议室状态
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 备注
     */
    @TableField("DES")
    private String des;

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
     * 预留字段1
     */
    @TableField(value = "DEPARTMENT_ID",fill = FieldFill.INSERT)
    private String departmentId;

    /**
     * 使用开始时间
     */
    @TableField("USE_BEGIN")
    private String useBegin;

    /**
     * 使用结束时间
     */
    @TableField("USE_END")
    private String useEnd;

    @TableField(exist = false)
    private String susTime;//持续时间


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
