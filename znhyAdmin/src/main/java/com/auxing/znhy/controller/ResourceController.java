package com.auxing.znhy.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.Resouces;
import com.auxing.znhy.util.ResultCode;
import com.auxing.znhy.util.VirtualMeetDetails;
import com.auxing.znhy.util.VirtualMeetingRoomResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @auther liuyy
 * @date 2018/10/11 0011 下午 4:32
 */
@Slf4j
@Api(tags = "会议资源接口",value = "/rsc")
@RestController
@RequestMapping("/rsc")
public class ResourceController extends BaseController{
	
    //资源获取
    @ApiOperation(value = "虚拟会议室资源获取接口")
    @RequestMapping(value = "/getRsc", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Resouces.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getRsc(){
        JSONObject respJson = new JSONObject();
        try {
        	User loginUser = (User) request.getSession().getAttribute("user");
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	Map<String, Object> params = new HashMap<String, Object>();
            params.put("account_token", meetInterface.getToken());
            params.put("count",0);
            params.put("order",0);
            params.put("start",0);
            MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/mc/virtual_meeting_rooms",params, meetInterface.getCookie());
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取会议资源:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取会议资源失败");
    			return respJson;
    		}
            //List<Resouces> lists = result.getVirtual_meeting_rooms();
            List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
            for (Resouces resouces : result.getVirtual_meeting_rooms()) {
            	Map<String,Object> map = new HashMap<String,Object>();
            	map.put("name", resouces.getName());
            	map.put("e164", resouces.getE164());
				String bitrate = resouces.getBitrate() > 1023?(resouces.getBitrate()/1024)+"M":resouces.getBitrate()+"Kbps";
				String bitrate1 = meetService.getResolutionStr(resouces.getResolution());
            	
            	map.put("scale", resouces.getMax_join_mt()+"方"+bitrate1);
            	map.put("mediaAbility", meetService.getResolutionStr1(resouces.getResolution())+"("+bitrate+bitrate1+")");
            	map.put("id", resouces.getId());
            	Domainuser domainuser = new Domainuser();
            	domainuser.setMoid(loginUser.getDomainId());
            	QueryWrapper<Domainuser> wrapper = new QueryWrapper<Domainuser>(domainuser);
            	domainuser = domainService.getOne(wrapper);
            	map.put("area", domainuser.getName());
            	mapList.add(map);
			}
            
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data",mapList);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //修改会议资源名称
    @ApiOperation(value = "修改会议资源名称接口")
    @RequestMapping(value = "/changeName", method = RequestMethod.POST)
    @ApiResponses({
            @ApiResponse(code = 200, message = "修改成功", response = Resouces.class),
            @ApiResponse(code = 500, message = "修改失败")
    })
    public JSONObject changeName(@RequestParam @ApiParam(value = "虚拟会议室id", required = true) String id, @RequestParam @ApiParam(value = "名称", required = true) String name){
        JSONObject respJson = new JSONObject();
        try {
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    Map<String, Object> params = new HashMap<String, Object>();
            params.put("account_token", meetInterface.getToken());
            VirtualMeetDetails details= MeetHttpClient.virtualSendGet(env.getProperty("meetIp")+"/api/v1/mc/virtual_meeting_rooms/"+id+"",params,meetInterface.getCookie());
            MeetingResult result = this.resourceService.changName(env.getProperty("meetIp"),id,name,meetInterface.getToken(),meetInterface.getCookie(),details);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("修改会议资源:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "修改会议资源失败");
    			return respJson;
    		}
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "修改成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "修改失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //获取用户域及下级用户域各种会议规模数量及已使用数量
    @ApiOperation(value = "获取用户域及下级用户域各种会议规模数量及已使用数量")
    @RequestMapping(value = "/getRscCount", method = RequestMethod.GET)
    @ApiResponses({
            @ApiResponse(code = 200, message = "获取成功", response = Resouces.class),
            @ApiResponse(code = 500, message = "获取失败")
    })
    public JSONObject getRscCount(@RequestParam @ApiParam(value = "虚区域moid", required = true) String domain_id){
        JSONObject respJson = new JSONObject();
        try {
        	
        	//查询域域用户的关系记录
        	Domainuser doma = new Domainuser(domain_id,"1");
			QueryWrapper<Domainuser> wrappers = new QueryWrapper<Domainuser>(doma);
			Domainuser domainuser = domainService.getOne(wrappers);
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			Map<String, Object> params = new HashMap<String, Object>();
            params.put("account_token", meetInterface.getToken());
            MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/mc/virtual_meeting_room_count",params,meetInterface.getCookie());           
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取用户域及下级用户域各种会议规模数量及已使用数量:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
            VirtualMeetingRoomResource virtual_meeting_room_resource = result.getVirtual_meeting_room_resource();
        	List<Object> listBzt = new ArrayList<Object>();
        	List<Object> listZzt = new ArrayList<Object>();
        	Map  mapfb = new HashMap<String, Object>();
        	Map  mapfz = new HashMap<String, Object>();
        	//调取公共方法
        	Map<String,Object> map = this.getCommon(virtual_meeting_room_resource);
        	
        	//饼状图
        	mapfb.put("name", virtual_meeting_room_resource.getUser_domain_name());
        	mapfb.put("value", map.get("total"));
        	listBzt.add(mapfb);
        	//树状图
        	mapfz.put("name", virtual_meeting_room_resource.getUser_domain_name());
        	mapfz.put("usedValue",  map.get("usedValue"));
        	mapfz.put("UnuseValue", map.get("UnuseValue"));
        	listZzt.add(mapfz);
        	
        	List<VirtualMeetingRoomResource> listRoomResource = virtual_meeting_room_resource.getSub_user_domains();
        	if(listRoomResource.size()>0){
        		for (VirtualMeetingRoomResource virtualMeetingRoomResource : listRoomResource) {
        			Map  mapsb = new HashMap<String, Object>();
        			Map  mapsz = new HashMap<String, Object>();
        			//调取公共方法
        			Map<String,Object> maps = this.getCommon(virtualMeetingRoomResource);
                	//饼状图
        			mapsb.put("name", virtualMeetingRoomResource.getUser_domain_name());
                	mapsb.put("value",maps.get("total"));
                	listBzt.add(mapsb);
                	//柱状图
                	mapsz.put("name", virtualMeetingRoomResource.getUser_domain_name());
                	mapsz.put("usedValue", maps.get("usedValue"));
                	mapsz.put("UnuseValue",maps.get("UnuseValue"));
                	listZzt.add(mapsz);
				}
        	}
            respJson.put("dataBzt", listBzt);
            respJson.put("dataZzt", listZzt);
            respJson.put("domName", domainuser.getName());
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }
    
    public Map<String,Object>getCommon(VirtualMeetingRoomResource virtualMeetingRoomResource){
    	Map<String,Object> mapCommon = new HashMap<String, Object>();
    	Map  mapfb = new HashMap<String, Object>();
    	Map  mapfz = new HashMap<String, Object>();
    	List<Resouces> firstResources =virtualMeetingRoomResource.getResources(); 
    	int total = 0;
    	int usedValue = 0;
    	int UnuseValue = 0;
    	for(int i = 0;i<firstResources.size();i++ ){
    		total += firstResources.get(i).getTotal();
    		usedValue += firstResources.get(i).getUsed();
    		UnuseValue += firstResources.get(i).getTotal() -firstResources.get(i).getUsed();
    	}
    	mapCommon.put("total", total);
    	mapCommon.put("usedValue", usedValue);
    	mapCommon.put("UnuseValue", UnuseValue);
		return mapCommon;
    	
    }
}
