package com.auxing.znhy.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Meet extends Model<Meet> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String resourcesId;		//会议资源ID(虚拟会议室)
    
    private String resourcesName;	//会议资源ID(虚拟会议室)
    
    private String meetRoom;		//会议室ID
    
    private Integer duration;		//会议时长，0为手动结束
    
    private String theme;			//会议主题
    
    private String msg;				//会议摘要
    
    private String meetNumber;		//会议号
    
    private String startTime;		//开始时间

    private String susHour;		//持续小时

    private String susMinute;		//持续分钟
    
    private String endTime;			//结束时间
    
    private Integer status;			//会议状态1开启中 0结束
    
    private String createId;		//创建人ID
    
    private String createName;		//创建人名称
    
    private String userDomain;		//用户域
    
    private Integer bitrate;		//会议码率
    private String e164;			//虚拟会议室号码
    private Integer joinMt;			//与会方
    private Integer resolution;		//	视频分辨率 1-QCIF;2-CIF;3-4CIF;12-720P;13-1080P;14-WCIF;15-W4CIF;16-4k;
    private Integer frame;			//帧率
    private Integer maxJoinMt;		//最大与会方 
    private Integer meetRange;		//会议范围
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    
    
}
