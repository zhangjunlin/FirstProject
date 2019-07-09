package com.auxing.znhy.service;

import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.VirtualMeetDetails;

/**
 * @auther liuyy
 * @date 2018/10/12 0012 上午 11:17
 */

public interface IResourceService {

      MeetingResult changName(String ip,String id, String name, String token, String cookie, VirtualMeetDetails details) throws Exception;

	MeetingResult getIrtualMeetById(String fictitiousId, MeetInterface meetInterface);
}
