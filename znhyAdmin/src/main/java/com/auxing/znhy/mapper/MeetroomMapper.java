package com.auxing.znhy.mapper;

import com.auxing.znhy.entity.Meetroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-10-16
 */
public interface MeetroomMapper extends BaseMapper<Meetroom> {

    List<Meetroom> getFreeMeetRoom(@Param("department") String department);

	void updateStatus(@Param("id") Integer id);
}
