package com.auxing.znhy.mapper;

import com.auxing.znhy.entity.Channel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-15
 */
public interface ChannelMapper extends BaseMapper<Channel> {
	
	void updateByConfId(@Param("confId")String confId);

	void updateByConfIdHdu(@Param("confId")String confId, @Param("hduId")String hduId);
	
    List<Channel> getByTvId(@Param("televisionId") String televisionId);

	void empty(@Param("confId")String confId);
}
