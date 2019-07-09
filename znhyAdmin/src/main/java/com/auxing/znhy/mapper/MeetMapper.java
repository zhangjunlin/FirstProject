package com.auxing.znhy.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Meetroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
public interface MeetMapper extends BaseMapper<Meet> {

	List<Meet> page(@Param("page")Page<Meet> page,@Param("meet")Meet meet, @Param("meetRoom")String meetRoom);

	long selectTotalCount(@Param("meet")Meet meet, @Param("meetRoom")String meetRoom);

	void saveMeet(Meet meet);

	void saveMeetUser(Map<String, Object> meetUserMap);

	List<Meet> getMeetListById(@Param("userId")String userId);
	
    List<Meet> getMrUseDetails(@Param("year") String year, @Param("month") String month,@Param("list") List<Meetroom> list);

	Meet getDetailsByMeetNumber(@Param("meetId") String meetId);
	
	Meet selectByMeetNum(@Param("meetId") String meetId);
	
	List<Meet> getMeetListByStatus();
	
	void updateStatus(@Param("id") Integer id, @Param("status") Integer status);
	
	 List<Meet> getMrLists(@Param("startTime") String startTime, @Param("endTime") String endTime ,@Param("list")List<Meetroom> list);
}
