package com.auxing.znhy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Template extends Model<Template> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "T_ID", type = IdType.AUTO)
    private Integer tId;

    /**
     * 会议主题
     */
    @TableField("T_NAME")
    private String tName;


    /**
     * 会议密码 
1.字符限制：仅支持 英文字母(大小写) 数字 下划线（_） 小数点（.）
2.最大字符长度：32个字节
     */
    @TableField("T_PASSWORD")
    private String tPassword;


    /**
     * 发言方
     */
    @TableField("SPEAKER")
    private String speaker;

    /**
     * 会议管理方
     */
    @TableField("CHAIR_MAN")
    private String chairMan;

    /**
     * 会议中无终端时，是否自动结会 0-否；1-是；
     */
    @TableField("AUTO_END")
    private Integer autoEnd;

    /**
     * 创建人ID
     */
    @TableField("CREATE_ID")
    private String createId;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private String createTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private String updateTime;

    /**
     * 会议时长（0为手动结束，单位：分钟）
     */
    @TableField("DURATION")
    private Integer duration;
    
    /**
     * 部门MOID
     */
    @TableField("DEPARTMENT_ID")
    private String departmentId;
    
    /**
     * 创建人name
     */
    @TableField("CREATE_NAME")
    private String createName;
    
    
    /**
     * 与会方
     */
    private List<User> userList = new ArrayList<User>();
    
    @Override
    protected Serializable pkVal() {
        return this.tId;
    }

}
