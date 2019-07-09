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
 * @since 2018-10-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Channel extends Model<Channel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 电视墙ID
     */
    @TableField("TELEVISION_ID")
    private String televisionId;

    /**
     * 设备名称
     */
    @TableField("DEVICE_NAME")
    private String deviceName;

    /**
     * 通道ID
     */
    @TableField("CHANNEL_ID")
    private String channelId;

    /**
     * 创建人
     */
    @TableField(value = "CREATOR",fill= FieldFill.INSERT)
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
     * 行
     */
    @TableField("LINE")
    private Integer line;

    /**
     * 列
     */
    @TableField("COL")
    private Integer col;

    /**
     * 终端MTID
     */
    @TableField("ALIAS")
    private String alias;
   
    /**
     * 是否被占用1-是；0-否；
     */
    @TableField("OCCUPY")
    private Integer occupy;
    
    /**
     * 被占用的会议号
     */
    @TableField("CONF_ID")
    private String confId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
