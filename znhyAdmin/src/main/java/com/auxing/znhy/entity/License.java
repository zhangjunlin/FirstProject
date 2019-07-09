package com.auxing.znhy.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
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
 * @since 2018-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class License extends Model<License> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @TableField("START_TIME")
    private LocalDateTime startTime;

    @TableField("END_TIME")
    private LocalDateTime endTime;

    @TableField("REMAIN_TIME")
    private String remainTime;

    @TableField("ALL_TIME")
    private String allTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
