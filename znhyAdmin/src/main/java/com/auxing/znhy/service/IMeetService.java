package com.auxing.znhy.service;

import java.util.List;
import java.util.Map;

import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Meetroom;
import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
public interface IMeetService extends IService<Meet> {

	MeetingResult createMeet(User user, Template template, String fictitiousId, MeetInterface meetInterface, String ids);	//创建会议
	

	void saveMeetUser(Map<String, Object> meetUserMap);
	
	List<Meet> getMeetList(String userId);
	

	void saveMeet(Meet meet);


	String getResolutionStr(int i);
	
	String getResolutionStr1(int i);

    List<Meet> getMrUseDetails(String year, String month, List<Meetroom> list);


	MeetingResult getResources(User loginUser) throws Exception;

    Meet getDetailsByMeetNumber(String meetId);
    
	void synchronizedSubscribe(User loginUser);
	
	Meet selectByMeetNum(String meetId);
	
	 List<Meet> getMrLists(String startTime,String endTime, List<Meetroom> list);


	long selectTotalCount(Meet meet, String meetRoom);

	List<Meet> page(Page<Meet> page, Meet meet, String meetRoom);
}
