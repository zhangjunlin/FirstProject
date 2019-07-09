package com.auxing.znhy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
public class Auth extends Model<Auth> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名称
     */
    @TableField("AUTH_NAME")
    private String authName;

    /**
     * 上级菜单
     */
    @TableField("SUPER_AUTH")
    private String superAuth;

    /**
     * URL
     */
    @TableField("URL")
    private String url;

    /**
     * 排序
     */
    @TableField("SORT")
    private Integer sort;

    /**
     * 描述
     */
    @TableField("DES")
    private String des;

    /**
     * 图标
     */
    @TableField("ICON")
    private String icon;

    /**
     * 菜单ID
     */
    @TableField("AUTH_ID")
    private String authId;

    /**
     * 父ID
     */
    @TableField("PARENT_ID")
    private String parentId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
