package com.auxing.znhy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Meetroom;
import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.TemplateUser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.entity.ZTreeNode;
import com.auxing.znhy.util.DateUtil;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.Resouces;
import com.auxing.znhy.util.ResultCode;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Maps;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
@Api(tags = "会议操作接口",value = "/meet")
@RestController
@RequestMapping("/meet")
@Slf4j
public class MeetController extends BaseController {
	
	@ResponseBody
	@ApiOperation(value = "查询会议接口")
	@RequestMapping(value = "selectPage", method = RequestMethod.GET)
	public JSONObject caveMeet(Long size,Long current,String resources ,String meetRoom) {
		JSONObject respJson = new JSONObject();
		User loginUser = (User) request.getSession().getAttribute("user");
		Page<Meet> page = new Page<Meet>();
		if(size != null){
			page.setSize(size);
		}
		if(current != null){
			page.setCurrent(current);
		}
		try{
			List<Meet> meets = null;
			long count;
			Meet meet = new Meet();
			meet.setResourcesName(resources);
			if(loginUser.getUserType() == 2){
				meet.setUserDomain(loginUser.getDomainId());
				count = meetService.selectTotalCount(meet,meetRoom);
				meets = meetService.page(page, meet, meetRoom);
			}else{
				meet.setCreateId(loginUser.getMoid());
				count = meetService.selectTotalCount(meet,meetRoom);
				meets = meetService.page(page, meet, meetRoom);
			}
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			for (Meet m : meets) {
				Map<String,Object> map = new HashMap<>();
				map.put("theme", m.getTheme());
				map.put("resources", m.getResourcesName());
				map.put("create", m.getCreateName());
				map.put("meetRoom", m.getMeetRoom());
				map.put("time", m.getDuration() == 0?"手动结束":DateUtil.getStartTime(m.getStartTime(), m.getDuration()));
				map.put("status", m.getStatus());
				String bitrate = m.getBitrate() > 1023?(m.getBitrate()/1024)+"M":m.getBitrate()+"Kbps";
				String bitrate1 = meetService.getResolutionStr(m.getResolution());
				map.put("scale", m.getMaxJoinMt()+"方"+bitrate1);
				map.put("mediaAbility", meetService.getResolutionStr1(m.getResolution())+"("+bitrate+bitrate1+"@"+m.getFrame()+"fps)");
				map.put("meetRange", m.getMeetRange());
				mapList.add(map);
			}
			
			Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put("records", mapList);
			resultMap.put("total", count);
			resultMap.put("size", page.getSize());
			resultMap.put("current", page.getCurrent());
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", resultMap);
            
            
		}catch(Exception e){
		 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
		}
		return respJson;
	}
	
	
	/**
	 * 
	 * @param msg			会议摘要
	 * @param templateId	会议模板ID
	 * @param fictitiousId	会议资源ID(虚拟会议室)
	 * @param meetRoomId	会议室ID
	 * @return
	 */
	@ResponseBody
	@ApiOperation(value = "创建会议接口")
	@RequestMapping(value = "createMeet",  method = RequestMethod.POST)
	public JSONObject createMeet(String msg, Integer templateId, String resourcesId,String meetRoomId,String meetTheme,String startTime,String hour,String minutes,Integer meetRange,
			 String ids) {
		User loginUser = (User) request.getSession().getAttribute("user");
		JSONObject respJson = new JSONObject();
		try{
			if(templateId == null){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议模板不能为空");
		         return respJson;
			}
			if(resourcesId == null || "".equals(resourcesId)){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议资源不能为空");
		         return respJson;
			}
			if(meetRoomId == null){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议地点不能为空");
		         return respJson;
			}
			if(meetTheme == null){
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "会议主题不能为空");
				return respJson;
			}
			if(startTime == null || "".equals(startTime)){
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "开始时间不能为空");
				return respJson;
			}
			if((hour == null || "".equals(hour)) && (minutes == null || "".equals(minutes))){
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "预计时长不能为空");
				return respJson;
			}
			if(meetRange == null){
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "会议范围不能为空");
				return respJson;
			}
			
			
			Meet m = new Meet();
			m.setStatus(1);
			m.setTheme(meetTheme);
			QueryWrapper<Meet> queryWrapper = new QueryWrapper<Meet>(m);
			m = this.meetService.getOne(queryWrapper);
			if(m != null){
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
				respJson.put("msg", "会议主题已存在");
				return respJson;
			}
			
			
			Template template = templateService.getTemplateById(templateId);
			
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult irtualResult = resourceService.getIrtualMeetById(resourcesId, meetInterface);
			if(irtualResult == null || irtualResult.getSuccess() != 1) {
				log.error("会议平台调用失败:error_code="+irtualResult.getError_code()+";msg="+irtualResult.getDescription());
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "系统异常");
		         return respJson;
			}
			if(irtualResult.getE164() == null || irtualResult.getE164().equals("")){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议资源异常");
		         return respJson;
			}
			if(irtualResult.getStatus() == null || irtualResult.getStatus() == 1){
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "会议资源被占用");
		         return respJson;
			}

			//重新设置会议主题
			template.setTName(meetTheme);

			MeetingResult result = meetService.createMeet(loginUser ,template ,resourcesId, meetInterface,ids);
			if(result == null || result.getSuccess() != 1) {
				log.error("会议平台创建会议失败:error_code="+result.getError_code()+";msg="+result.getDescription());
				 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		         respJson.put("msg", "系统异常");
		         return respJson;
			}
			
			Meet meet = new Meet();
			if(!StringUtils.isEmpty(msg)) {
				meet.setMsg(msg);
			}
			meet.setMeetNumber(result.getConf_id());
			meet.setResourcesId(resourcesId);
			meet.setResourcesName(irtualResult.getName());
			meet.setFrame(irtualResult.getVideo_formats().get(0).getFrame());
			meet.setStatus(1);
			meet.setCreateId(loginUser.getMoid());
			meet.setCreateName(loginUser.getActuralName());
			meet.setDuration(0);//会议时长-手动结束
			meet.setTheme(template.getTName());
			meet.setStartTime(startTime);//获得开始时间
			meet.setEndTime(DateUtil.getEndDate(startTime, hour, minutes));
            meet.setSusHour(hour);//获得持续时间-小时
            meet.setSusMinute(minutes);//获得开始时间-分钟
			meet.setBitrate(irtualResult.getBitrate());
			meet.setE164(irtualResult.getE164());
			List<String> list = java.util.Arrays.asList(ids.split(","));
			meet.setJoinMt(list.size());
			meet.setResolution(irtualResult.getVideo_formats().get(0).getResolution());
			meet.setMeetRoom(meetRoomId);
			meet.setUserDomain(loginUser.getDomainId());
			meet.setMaxJoinMt(irtualResult.getMax_join_mt());
			meet.setMeetRange(meetRange);
			meetService.saveMeet(meet);
			
			Meetroom mr = meetroomService.getById(meetRoomId);
			mr.setStatus(1);
			mr.setUseBegin(startTime);
			mr.setUseEnd(DateUtil.getEndDate(startTime, hour, minutes));
			meetroomService.updateById(mr);
			meetService.synchronizedSubscribe(loginUser);
			respJson.put("code", ResultCode.SUCCESS);
	        respJson.put("msg", "创建会议成功");
		}catch(Exception e){
			 respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	         respJson.put("msg", "系统异常");
	         e.printStackTrace();
	         log.error("创建会议系统异常",e);
		}
		return respJson;
	}
	
	@ApiOperation(value = "查询个人会议")
	@RequestMapping(value = "getOwnMeet",method = RequestMethod.GET)
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功", response = Meet.class),
        @ApiResponse(code = 500, message = "系统异常")})
	public JSONObject getOwnMeet(){
		JSONObject respJson = new JSONObject();
		User us = (User) request.getSession().getAttribute("user");
		try {
			List<Meet> meets = meetService.getMeetList(us.getMoid());
			
			List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
			for (Meet m : meets) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("theme", m.getTheme());
				map.put("resources", m.getResourcesName());
				map.put("e164", m.getE164());
				map.put("meetRoom", m.getMeetRoom());
				map.put("time", m.getDuration() == 0?"手动结束":DateUtil.getStartTime(m.getStartTime(), m.getDuration()));
				map.put("status", m.getStatus());
				String bitrate1 = meetService.getResolutionStr(m.getResolution());
				map.put("scale", m.getMaxJoinMt()+"方"+bitrate1);
				map.put("joinMt", m.getJoinMt());
				mapList.add(map);
			}
			
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", mapList);
		} catch (Exception e) {
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
		}
		return respJson;
	}
	

	@ApiOperation(value = "查询会议详情")
	@RequestMapping(value = "selectByMeetNum",method = RequestMethod.GET)
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功", response = Meet.class),
        @ApiResponse(code = 500, message = "系统异常")})
	public JSONObject selectByMeetNum(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
		JSONObject respJson = new JSONObject();
		try {
			Meet meet = meetService.selectByMeetNum(confId);
			Map<String,Object> map = new HashMap<>();
			map.put("theme", meet.getTheme());//主题
			map.put("resources", meet.getResourcesName());//虚拟会议室
			map.put("create", meet.getCreateName());//发起人
			map.put("meet_num", meet.getMeetNumber());//会议号
			map.put("meetRoom", meet.getMeetRoom());//会议地点
			map.put("time", meet.getDuration() == 0?"手动结束":DateUtil.getStartTime(meet.getStartTime(), meet.getDuration()));//会议时间
			map.put("status", meet.getStatus());//会议状态
			String bitrate = meet.getBitrate() > 1023?(meet.getBitrate()/1024)+"M":meet.getBitrate()+"Kbps";
			String bitrate1 = meetService.getResolutionStr(meet.getResolution());
			map.put("bitrate", bitrate);//会议码率
			map.put("resolution", bitrate1);//分辨率
			map.put("scale", meet.getMaxJoinMt()+"方"+bitrate1);//会议规模
			map.put("mediaAbility", meetService.getResolutionStr1(meet.getResolution())+"("+bitrate+bitrate1+"@"+meet.getFrame()+"fps)");
			map.put("meetRange", meet.getMeetRange());//会议范围
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", map);
		} catch (Exception e) {
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
		}
		return respJson;
	}
	
	@ApiOperation(value = "查询会议室使用情况")
	@RequestMapping(value = "selectMeetRoom",method = RequestMethod.GET)
    @ApiResponses({
        @ApiResponse(code = 200, message = "获取成功", response = Meet.class),
        @ApiResponse(code = 500, message = "系统异常")})
	public JSONObject selectMeetRoom(@RequestParam @ApiParam(value = "年份",required = true) String year,@RequestParam @ApiParam(value = "月份",required = true) String month){
		JSONObject respJson = new JSONObject();
		try {
			
            User loginUser = (User) request.getSession().getAttribute("user");
            List<ZTreeNode> selectDepsByUser = departmentService.listDepsByUser(loginUser.getDepartment(),loginUser.getDomainId());
            QueryWrapper<Meetroom> wrapper = new QueryWrapper<Meetroom>();
            List<String> ids = new ArrayList<String>();
            for (ZTreeNode zTreeNode : selectDepsByUser) {
            	ids.add(zTreeNode.getId());
			}
            wrapper.in("DEPARTMENT_ID", ids);
            List<Meetroom> list = this.meetroomService.list(wrapper);
			
            Map<String,Object> map = Maps.newLinkedHashMap();
            
			if(list == null || list.isEmpty()) {//add by zhangjl 2019-01-20
				respJson.put("code", ResultCode.SUCCESS);
	            respJson.put("msg", "获取成功");
	            respJson.put("data", map);
	            return respJson;
			}
            
			//获取该月份有多少天
			String firstday = year+"-"+month+"-01";//拼接当前月份的第一天
			String kssj = getKssj(firstday);//获取当前月份所在周的周一
			String jssj = getAddDay(kssj,41);
			String startTime = kssj+" 00:00:00";
			String endTime = jssj+" 23:59:59";
			List<Meet> meetList = meetService.getMrLists(startTime, endTime,list);
			for(int i = 0;i<= 41 ;i++){
				List<Meet> meets = new ArrayList<Meet>();
				String time = getAddDay(kssj,i);
				String name = time.substring(0,4)+time.substring(5,7)+time.substring(8,10);
				if(meetList.size()>0){
					for (Meet meet : meetList) {
						if(meet != null && !"".equals(meet.getStartTime())&& meet.getStartTime() != null){
							String str = meet.getStartTime().substring(0,10);
							if(time.equals(str)){
								meets.add(meet);
							}
						}
					}
				}
				
				map.put("D"+name, meets);
			}
			
			respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", map);
		} catch (Exception e) {
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
		}
		return respJson;
	}
	
	
	@ResponseBody
	@ApiOperation(value = "获取会议参数接口")
	@RequestMapping(value = "getMeetParameter",  method = RequestMethod.GET)
	public JSONObject getMeetParameter(){
		JSONObject respJson = new JSONObject();
		Map<String,Object> resultMap = new HashMap<String,Object>();
		List<Map<String,Object>> resourcesList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> templateList = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> meetRoomList = new ArrayList<Map<String,Object>>();
		
		try {
			User loginUser = (User) request.getSession().getAttribute("user");
			MeetingResult result = meetService.getResources(loginUser);
			List<Resouces> lists = result.getVirtual_meeting_rooms();
			for (Resouces resouces : lists) {
				if(resouces.getStatus().equals("0")){
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", resouces.getId());
					map.put("name", resouces.getName());
					resourcesList.add(map);
				}
			}
			//查询会议模板以及与会方
			//List<ZTreeNode> selectDepsByUser = departmentService.listDepsByUser(loginUser.getDepartment(),loginUser.getDomainId());
			//List<Template> templates = templateService.findListByDep(selectDepsByUser);
			List<Template> templates = templateService.templateLists(loginUser.getDepartment());
			if(templates.size() > 0){
				for (Template t : templates) {
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("id", t.getTId());
					map.put("name", t.getTName());
					if(t.getDuration() == 0){
						map.put("hourTime", 0);
						map.put("minTime", 0);
					}else{
						map.put("hourTime", (int)Math.floor(t.getDuration() / 60));
						map.put("minTime", t.getDuration() % 60);
					}
					List<Map<String,Object>> userList = new ArrayList<Map<String,Object>>();
					
					List<TemplateUser> idList = this.templateService.getUsersById(t.getTId());
					for (TemplateUser mo : idList) {
						User u = new User();
						u.setMoid(mo.getUserId());
						QueryWrapper<User> queryWrapper = new QueryWrapper<User>(u);
						u = this.userService.getOne(queryWrapper);
						Map<String,Object> userMap = new HashMap<>();
						if(u != null) {
							userMap.put("id", u.getMoid());
							userMap.put("name", u.getActuralName());
						}else {
							userMap.put("id", mo.getUserId());
							userMap.put("name", mo.getUserId());
						}
						userList.add(userMap);
					}
					map.put("userList", userList);
					templateList.add(map);
				}
			}
			List<Meetroom> meetroomList = meetroomService.getFreeMeetRoom(loginUser.getDepartment());
			for (Meetroom m : meetroomList) {
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("id", m.getId());
				map.put("name", m.getName());
				meetRoomList.add(map);
			}
			
			resultMap.put("resourcesList", resourcesList);
			resultMap.put("templateList", templateList);
			resultMap.put("meetRoomList", meetRoomList);
			
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", resultMap);
			
		} catch (Exception e) {
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常");
            e.printStackTrace();
		}
		
		return respJson;
	}	
		// 获取当前日期所在周的周一对应的日期
	public String getKssj(String date) throws java.text.ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 设置时间格式
		Calendar cal = Calendar.getInstance();
		// 如果传递参数，那么应该加上下面语句
		try {
			cal.setTime(sdf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		String kssj = sdf.format(cal.getTime());
		return kssj;
	   }
		 
		   //获取当前日期的后N天
			public static String getAddDay(String strTime,int addday) throws java.text.ParseException {
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");  
				Calendar cal = Calendar.getInstance();
				cal.setTime(sd.parse(strTime));
			    cal.add(Calendar.DAY_OF_MONTH, addday);
			    return sd.format(cal.getTime());
			}
			
}
