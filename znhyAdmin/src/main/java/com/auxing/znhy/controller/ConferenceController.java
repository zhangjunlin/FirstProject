package com.auxing.znhy.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.common.Httpmsg;
import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Tvwall;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.util.DateUtil;
import com.auxing.znhy.util.HduResult;
import com.auxing.znhy.util.Hdus;
import com.auxing.znhy.util.HttpUtil;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.ResultCode;
import com.auxing.znhy.util.meeting.Cascades;
import com.auxing.znhy.util.meeting.MeetingUser;
import com.auxing.znhy.util.meeting.Member;
import com.auxing.znhy.util.meeting.OpenConfParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

/**
 * @auther liuyy
 * @date 2018/10/26 0026 上午 11:14
 * 会议控制
 */
@Slf4j
@Api(tags = "会议控制接口")
@RequestMapping(value = "/confControl")
@RestController
public class ConferenceController extends BaseController{
	
	//开启画面合成接口
    @ApiOperation(value = "开启画面合成接口")
	@RequestMapping(value = "/OpenConfSynthesis", method = RequestMethod.POST)
	public JSONObject OpenPictureSynthesis(@RequestBody @ApiParam(value = "合成画面参数", required = true)OpenConfParam openConfParam){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.OpenConfSynthesis(openConfParam,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("开启画面合成失败："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "画面合成开启失败");
    			return respJson;
    		}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
  //停止画面合成接口
    @ApiOperation(value = "停止画面合成接口")
	@RequestMapping(value = "/StopConfSynthesis", method = RequestMethod.POST)
	public JSONObject StopConfSynthesis(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    //vmp_id默认为1
			MeetingResult result = conferenceService.StopConfSynthesis(conf_id,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("停止画面合成失败："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "画面合成停止失败");
    			return respJson;
    		}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
  //修改画面合成接口
    @ApiOperation(value = "修改画面合成接口")
	@RequestMapping(value = "/UpdatePictureSynthesis", method = RequestMethod.POST)
	public JSONObject UpdatePictureSynthesis(@RequestBody  @ApiParam(value = "合成画面参数", required = true)OpenConfParam openConfParam){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.UpdatePictureSynthesis(openConfParam,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("修改画面合成失败："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "画面合成修改失败");
    			return respJson;
    		}else{
    			respJson.put("data", result);	
    			respJson.put("msg", "获取成功");
    			respJson.put("code", ResultCode.SUCCESS);
    		}
			
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
	//画面合成信息接口
    @ApiOperation(value = "画面合成信息接口")
	@RequestMapping(value = "/ConfSynthesis", method = RequestMethod.POST)
	public JSONObject ConfSynthesis(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.ConfSynthesis(conf_id,meetInterface);
			Map<String, Object> map = new HashMap<String, Object>();
			if(result.getSuccess() == 1){
				map.put("type", 1);			//开启了画面合成
				map.put("mode", result.getMode());
				map.put("layout", result.getLayout());
				map.put("broadcast", result.getBroadcast());
				map.put("voice_hint", result.getVoice_hint());
				map.put("show_mt_name", result.getShow_mt_name());
				map.put("layout", result.getLayout());
				
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				for (Member member : result.getMembers()) {
					Map<String, Object> memberMap = new HashMap<String, Object>();
					memberMap.put("mt_id", member.getMt_id());
					memberMap.put("chn_idx", member.getChn_idx());
					memberMap.put("member_type", member.getMember_type());
					list.add(memberMap);
				}
				map.put("members", list);
				
				if(result.getMode() == 3 || result.getMode() == 4){
					map.put("num", result.getPoll().getNum());
					map.put("keep_time", result.getPoll().getKeep_time());
				}
				
			}else{
				if(result.getError_code() == 20823){
					map.put("type", 0);		//画面合成未开启
				}else{
					log.error("获取画面合成失败："+result);
					respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
			        respJson.put("msg", "画面合成获取失败");
			        return respJson;
				}
			}
			
			
			respJson.put("data", map);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    //获取用户域vrs地址接口
    @ApiOperation(value = "获取用户域vrs地址列表")
	@RequestMapping(value = "/getVrsParams", method = RequestMethod.POST)
	public JSONObject getVrsParams(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "模式：1,录像 2,直播") String mode,@ApiParam(value = "录像保存文件名") @RequestParam(value = "name",required = false) String name,@RequestParam @ApiParam(value = "发布模式",required = true) String publishMode,@RequestParam @ApiParam(value = "是否支持免登陆观看直播 0-不支持；1-支持；",required = true) String anonymous){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.getVrsParams(meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取用户域vrs地址："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取用户域vrs地址失败");
    			return respJson;
    		}
			respJson.put("data", result.getVrs());	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    //开启录像接口
    @ApiOperation(value = "开启录像接口")
	@RequestMapping(value = "/OpenConfRecord", method = RequestMethod.POST)
	public JSONObject OpenConfRecord(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "模式：1,录像 2,直播") String mode,@ApiParam(value = "录像保存文件名") @RequestParam(value = "name",required = false) String name,@RequestParam @ApiParam(value = "发布模式",required = true) String publishMode,@RequestParam @ApiParam(value = "是否支持免登陆观看直播 0-不支持；1-支持；",required = true) String anonymous){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.getVrsParams(meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取用户域vrs地址："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取用户域vrs地址失败");
    			return respJson;
    		}
    		if(result.getVrs().size() < 1){
    			log.error("用户域vrs地址为空");
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "没有找到对应的vrs地址");
    			return respJson;
    		}
		    result = conferenceService.OpenConfRecord(conf_id,mode,name,publishMode,anonymous,meetInterface,result.getVrs());
    		if (result == null || result.getSuccess() != 1) {
    			log.error("开启录像："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启录像失败");
    			return respJson;
    		}
		    respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    
    //停止录像接口
    @ApiOperation(value = "停止录像接口")
	@RequestMapping(value = "/StopConfRecord", method = RequestMethod.POST)
	public JSONObject StopConfRecord(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "录像机id", required = true)String rec_id,@RequestParam @ApiParam(value = "录像模:1：录像；2：直播；3：录像+直播 ", required = true)Integer recorder_mode){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.StopConfRecord(conf_id, rec_id, recorder_mode,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("停止录像："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "停止录像失败");
    			return respJson;
    		}
			respJson.put("code", ResultCode.SUCCESS);
			respJson.put("msg", "获取成功");
			respJson.put("data", result);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    //获取录像状态接口
    @ApiOperation(value = "获取录像状态接口")
	@RequestMapping(value = "/GetConfRecord", method = RequestMethod.GET)
	public JSONObject GetConfRecord(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id, @RequestParam @ApiParam(value = "录像机id", required = true)String rec_id){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.GetConfRecord(conf_id,rec_id,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取录像状态："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取录像状态失败");
    			return respJson;
    		}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}    
  //获取录像列表接口
    @ApiOperation(value = "获取录像列表接口")
	@RequestMapping(value = "/GetConfRecordList", method = RequestMethod.GET)
	public JSONObject GetConfRecordList(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.GetConfRecordList(conf_id,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取录像列表："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取录像列表失败");
    			return respJson;
    		}
			if(result.getRecorders().size() == 0){
                respJson.put("msg", "没有录像");
                respJson.put("code", ResultCode.ACCEPTED);
            }else {
                respJson.put("data", result);
                respJson.put("msg", "获取成功");
                respJson.put("code", ResultCode.SUCCESS);
            }
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}    
  //修改录像状态接口
    @ApiOperation(value = "修改录像状态接口")
	@RequestMapping(value = "/UpdateConfRecord", method = RequestMethod.POST)
	public JSONObject UpdateConfRecord(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "录像机id", required = true)String rec_id,@RequestParam @ApiParam(value = "录像状态 1-暂停录像；2-继续录像；", required = true)Integer value,@RequestParam @ApiParam(value = "录像模式 1-录像；2-直播；3-录像+直播；", required = true)Integer recorder_mode){
		JSONObject respJson = new JSONObject();
		try{
		    MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		    MeetingResult result = conferenceService.UpdateConfRecord(conf_id,rec_id,value,recorder_mode,meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("修改录像状态："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "修改录像状态失败");
    			return respJson;
    		}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}    
    
    @ApiOperation(value = "根据会议号获取会议详情")
    @RequestMapping(value = "/getMetDetail",method = RequestMethod.GET)
    public JSONObject getMetDetail(@RequestParam @ApiParam(value = "会议号",required = true) String meetId){
        JSONObject respJson = new JSONObject();
        try {
            Meet meet = this.meetService.getDetailsByMeetNumber(meetId);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
            respJson.put("data", meet);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "获取指定会议终端列表")
    @RequestMapping(value = "/getAttendee",method = RequestMethod.GET)
    public JSONObject getAttendee(@RequestParam @ApiParam(value = "会议号",required = true) String meetId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			Cascades jilianresult = this.conferenceService.getAttendee(meetInterface,meetId);
            respJson.put("data",jilianresult);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "判断是否有级联")
    @RequestMapping(value = "/getIsJl",method = RequestMethod.GET)
    public JSONObject getIsJl(@RequestParam @ApiParam(value = "会议号",required = true) String meetId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result  = this.conferenceService.getIsJl(meetInterface,meetId);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取会议级联信息："+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取会议级联信息失败");
    			return respJson;
    		}
            if(result.getCascades().size() >0){
            	result.setConf_type(1);//1:有级联
            }else{
            	result.setConf_type(0);//无级联
            }
            respJson.put("data",result);
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("msg", "获取成功");
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }

    //延长会议接口
    @ApiOperation(value = "延长会议时间接口")
    @RequestMapping(value = "/delayTime",method = RequestMethod.POST)
    public JSONObject delayTime(@RequestParam @ApiParam(value = "会议号", required = true)String confId,@RequestParam @ApiParam(value = "延长的时间,单位:分钟", required = true)String delayTime){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.DelayTime(confId,delayTime,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("延迟会议："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "延迟会议失败");
                return respJson;
			}
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
  //结束会议接口
    @ApiOperation(value = "结束会议接口")
    @RequestMapping(value = "/stopConfs",method = RequestMethod.POST)
    public JSONObject stopConfs(@RequestParam @ApiParam(value = "会议号", required = true)String confId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.stopConfs(confId, meetInterface);
			if(result == null) {
				log.error("结束会议失败："+result);
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		        respJson.put("msg", "结束会议失败!");
		        return respJson;
			}
			if(result.getSuccess() != 1) {
				if(result.getSuccess() == 12103){
					respJson.put("msg", "会议已结束");
					respJson.put("code", ResultCode.SUCCESS);
				}else{
					log.error("结束失败:"+result);
					respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
			        respJson.put("msg", "会议结束失败");
			        return respJson;					
				}
			}
			respJson.put("msg", "结束成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    //发送短消息接口
    @ApiOperation(value = "发送短消息接口")
    @RequestMapping(value = "/SendMessage",method = RequestMethod.POST)
    public JSONObject SendMessage(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "短消息类型",required = true) Integer type,@RequestParam @ApiParam(value = "滚动次数",required = true) Integer roll_num,@RequestParam @ApiParam(value = "滚动速度",required = true) Integer roll_speed, @RequestParam @ApiParam(value = "消息内容", required = true)String message, @ApiParam @RequestParam(value = "mtsList", required = false)List<String> mtsList){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.SendMessage(conf_id,type,roll_num,roll_speed,message,mtsList,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("发送短消息："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "发送短消息失败");
                return respJson;
			}
			respJson.put("data", result);
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }

    @ApiOperation(value = "批量添加与会终端")
    @RequestMapping(value = "/addConfTerminal",method = RequestMethod.POST)
    public JSONObject addConfTerminal(@RequestParam @ApiParam(value = "选择添加本级终端还是级联终端(1:本级终端，2：级联终端)",required = true) Integer id, @ApiParam(value = "级联会议号(当选择为添加级联终端时必须)") @RequestParam(value = "cascadeId",required = false) String cascadeId, @RequestParam @ApiParam(value = "会议号",required = true) String confId, @RequestParam @ApiParam(value = "终端list",required = true)List<String> accountList){
        JSONObject respJson = new JSONObject();
        try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			if(id==1){
                //添加本级会议终端
                MeetingResult result = conferenceService.addThisConfTerminal(confId,accountList,meetInterface);
                if (result == null || result.getSuccess() != 1) {
                	log.error("批量添加本级终端："+result);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "批量添加本级终端失败");
                    return respJson;
    			}
                respJson.put("data", result);
                respJson.put("msg", "添加成功");
                respJson.put("code", ResultCode.SUCCESS);
            }else{
                //添加级联会议终端
                MeetingResult result = conferenceService.addOtherConfTerminal(confId,cascadeId,accountList,meetInterface);
                if (result == null || result.getSuccess() != 1) {
                	log.error("批量添加级联终端："+result);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "批量添加级联终端失败");
                    return respJson;
    			}
                respJson.put("data", result);
                respJson.put("msg", "添加成功");
                respJson.put("code", ResultCode.SUCCESS);
            }
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "获取该会议级联会议信息")
    @RequestMapping(value = "/getConfCascades",method = RequestMethod.GET)
    public JSONObject getConfCascades(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getConfCascades(confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取该会议级联会议信息："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取会议级联会议信息失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "获得会议混音信息")
    @RequestMapping(value = "/getMixMesg",method = RequestMethod.GET)
    public JSONObject getMixMesg(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getMixMesg(confId, meetInterface);
            if (result == null ) {
            	log.error("获得会议混音信息："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获得会议混音信息失败");
                return respJson;
			}
            if(result.getSuccess() == 1){
	            respJson.put("data", result);
	            respJson.put("msg", "获取成功");
	            respJson.put("code", ResultCode.SUCCESS);	
	            return respJson;
            }else if(result.getError_code() == 24509){
                respJson.put("msg", "未混音");
                respJson.put("code", ResultCode.ACCEPTED);
                return respJson;
            }else{
            	log.error("获得会议混音信息："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获得会议混音信息失败");
                return respJson;
            }
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "混音设置")
    @RequestMapping(value = "/openSoundMixing",method = RequestMethod.POST)
    public JSONObject openSoundMixing(@RequestParam @ApiParam(value = "混音模式(1:智能混音，2:定制混音)",required = true)Integer number,@RequestParam @ApiParam(value = "会议号",required = true) String confId, @ApiParam(value = "混音列表:设备的mt_id") @RequestParam(value = "accountList",required = false) List<String> accountList){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openSoundMixing(number,confId,accountList, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("混音设置："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "混音设置失败");
                return respJson;
			}
		    respJson.put("data", result);
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "关闭混音")
    @RequestMapping(value = "/closeSoundMixing",method = RequestMethod.POST)
    public JSONObject closeSoundMixing(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.closeSoundMixing(confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("关闭混音："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "关闭混音失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "关闭成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "已开起的定制混音会议添加混音成员")
    @RequestMapping(value = "/addSmChannel",method = RequestMethod.POST)
    public JSONObject addSmChannel(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "混音列表:设备的mt_id",required = true) List<String> accountList){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.addSmChannel(confId,accountList, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("添加混音成员："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "添加混音成员失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "添加成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "已开起的定制混音会议删除混音成员")
    @RequestMapping(value = "/delSmChannel",method = RequestMethod.POST)
    public JSONObject delSmChannel(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "混音列表:设备的mt_id",required = true) List<String> accountList){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.delSmChannel(confId,accountList, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("删除混音成员："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "删除混音成员失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "删除成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "会议轮询设置")
    @RequestMapping(value = "/setConfPolling",method = RequestMethod.POST)
    public JSONObject setConfPolling(
    		@RequestParam @ApiParam(value = "会议号",required = true) String confId,
    		@RequestParam @ApiParam(value = "轮询模式：1：会议轮询(视频轮询),3:会议轮询(音视频轮询),10：主席轮询（视频轮询）,11：主席轮询（音视频轮询）")Integer mode,
    		@RequestParam @ApiParam(value = "轮询次数，0为一直轮询",required = true) Integer num,
    		@RequestParam @ApiParam(value = "轮询时间，单位：秒",required = true)Integer keepTime, 
    		@RequestParam @ApiParam(value = "轮询终端号:设备的mt_id",required = true) List<String> accountList){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.confPolling(confId,mode,num,keepTime,accountList,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("会议轮询："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议轮询失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "主席轮询设置")
    @RequestMapping(value = "/setConfChairmanPolling",method = RequestMethod.POST)
    public JSONObject setConfChairmanPolling(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "轮询模式：1：会议轮询(视频轮询),3:会议轮询(音视频轮询),10：主席轮询（视频轮询）,11：主席轮询（音视频轮询）")Integer mode,@RequestParam @ApiParam(value = "轮询次数，0为一直轮询",required = true) Integer num,@RequestParam @ApiParam(value = "轮询时间，单位：秒",required = true)Integer keepTime, @RequestParam @ApiParam(value = "轮询终端号:设备的mt_id",required = true) List<String> accountList){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getChairman(confId, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取管理方："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取管理方失败");
                return respJson;
			}
		    if(result.getMt_id().isEmpty()){
                respJson.put("msg", "请先指定管理方，再开启主席轮询！");
                respJson.put("code", ResultCode.ACCEPTED);
            }else{
                result = conferenceService.confChairmanPolling(confId,mode,num,keepTime,accountList,meetInterface);
                if (result == null || result.getSuccess() != 1) {
                	log.error("主席轮询："+result);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "主席轮询失败");
                    return respJson;
    			}
                respJson.put("data", result);
                respJson.put("msg", "设置成功");
                respJson.put("code", ResultCode.SUCCESS);
            }
    
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    

    @ApiOperation(value = "修改会议轮询状态")
    @RequestMapping(value = "/setPollState",method = RequestMethod.POST)
    public JSONObject setPollState(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "轮询状态：0-停止轮询；1-暂停轮询；2-继续轮询；",required = true) Integer value){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.setPollState(confId,value, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("修改会议轮询状态："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "修改会议轮询状态失败");
                return respJson;
			}
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "修改会议主席轮询状态")
    @RequestMapping(value = "/setChairmanPollState",method = RequestMethod.POST)
    public JSONObject setChairmanPollState(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "轮询状态：0-停止轮询；1-暂停轮询；2-继续轮询；",required = true) Integer value, @RequestParam @ApiParam(value = "轮询模式：10：主席轮询（视频轮询）,11：主席轮询（音视频轮询）")Integer mode){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.setChairmanPollState(confId,value, meetInterface,mode);
            if (result == null || result.getSuccess() != 1) {
            	log.error("修改会议主席轮询状态："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "修改会议轮询状态失败");
                return respJson;
			}
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    

    @ApiOperation(value = "获取会议轮询信息")
    @RequestMapping(value = "/getPollDetails",method = RequestMethod.GET)
    public JSONObject getPollDetails(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getPollDetails(confId, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取会议轮询状态："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取会议轮询状态失败");
                return respJson;
			}
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("data", result);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "获取会议主席轮询信息")
    @RequestMapping(value = "/getChairmanPollDetails",method = RequestMethod.GET)
    public JSONObject getChairmanPollDetails(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getChairmanPollDetails(confId, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取会议轮询状态："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取会议轮询状态失败");
                return respJson;
			}
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
            respJson.put("data", result);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    

    @ApiOperation(value = "开启终端选看")
    @RequestMapping(value = "/setWatchTerminal",method = RequestMethod.POST)
    public JSONObject setWatchTerminal(@RequestParam @ApiParam(value = "会议号",required = true) String confId, @RequestParam @ApiParam(value = "被选看方",required = true) String src, @RequestParam @ApiParam(value = "选看方",required = true) String dst){
        JSONObject respJson = new JSONObject();
        try {
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.setWatchTerminal(confId,meetInterface,src,dst,1);
            result = conferenceService.setWatchTerminal(confId,meetInterface,src,dst,2);
            if (result == null || result.getSuccess() != 1) {
            	log.error("开启终端选看："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "开启终端选看失败");
                return respJson;
			}
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }

    @ApiOperation(value = "取消终端选看")
    @RequestMapping(value = "/delWatchTerminal",method = RequestMethod.POST)
    public JSONObject delWatchTerminal(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "终端mt_id",required = true) String mt_id){
        JSONObject respJson = new JSONObject();
        try {
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.delWatchTerminal(confId, mt_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("取消终端选看："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "取消终端选看失败");
                return respJson;
			}
            respJson.put("msg", "设置成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    @ApiOperation(value = "取消全部终端选看")
    @RequestMapping(value = "/delWatchTerminalAll",method = RequestMethod.POST)
    public JSONObject delWatchTerminalAll(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.getWatchTerminal(confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取终端选看："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取终端选看失败");
                return respJson;
			}
    		result = conferenceService.delWatchTerminalAll(confId,meetInterface, result.getInspections());
            if (result == null || result.getSuccess() != 1) {
            	log.error("取消终端选看："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "取消终端选看失败");
                return respJson;
			}
            respJson.put("msg", "取消成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    

    @ApiOperation(value = "获取终端选看列表")
    @RequestMapping(value = "/getWatchTerminal",method = RequestMethod.GET)
    public JSONObject getWatchTerminal(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
            MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
            MeetingResult result = conferenceService.getWatchTerminal(confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取终端选看："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取终端选看失败");
                return respJson;
			}
            respJson.put("data", result.getInspections());
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "开启电视墙")
    @RequestMapping(value = "/openHdu",method = RequestMethod.POST)
    public JSONObject openHdu(
    		@RequestParam @ApiParam(value = "会议号",required = true) String confId,
    		@RequestParam @ApiParam(value = "通道ID",required = true)String hdus,
    		@RequestParam @ApiParam(value = "终端",required = true)String mt_id){
        JSONObject respJson = new JSONObject();
        try {
            MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
            MeetingResult result = conferenceService.openHdu(confId,hdus,mt_id,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("开启电视墙："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "开启电视墙失败");
                return respJson;
			}else{
				Channel channel = new Channel();
				channel.setChannelId(hdus);
				QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
				Channel entity = new Channel();
				entity.setAlias(mt_id);
				entity.setOccupy(1);
				entity.setConfId(confId);
				this.channelService.update(entity, queryWrapper);
			}

            respJson.put("msg", "开启成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    
    @ApiOperation(value = "开启电视墙轮询")
    @RequestMapping(value = "/openHduPoll",method = RequestMethod.POST)
    public JSONObject openHduPoll(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "轮询次数",required = true)Integer num,
    		@RequestParam @ApiParam(value = "轮询间隔",required = true)Integer keepTime,@RequestParam @ApiParam(value = "是否显示终端别名",required = true)Integer showMtName ,@RequestParam @ApiParam(value = "电视墙ID",required = true)String tvwallId,
    		@ApiParam @RequestParam(value = "mtIdList", required = false)List<String> mtIdList){
        JSONObject respJson = new JSONObject();
        try {
            MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
            Channel channel = new Channel();
        	channel.setTelevisionId(tvwallId);
        	QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
        	List<Channel> list = this.channelService.list(queryWrapper);
        	List<Channel> channels = new ArrayList<>();
        	for (int i = 0; i < list.size(); i++) {
				if(!list.get(i).getChannelId().equals("")){
					channels.add(list.get(i));
				}
			}
            MeetingResult result = conferenceService.openHdupoll(confId,channels,mtIdList,keepTime,num,showMtName, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("开启电视墙轮询："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "开启电视墙轮询失败");
                return respJson;
			}else{
				for (int i = 0; i < mtIdList.size(); i++) {
					if(list.size() > i){
						if(!list.get(i).getChannelId().equals("")){
							Channel channel1 = new Channel();
							channel1.setChannelId(list.get(i).getChannelId());
							QueryWrapper<Channel> wrapper = new QueryWrapper<Channel>(channel1);
							Channel entity = new Channel();
							entity.setAlias(mtIdList.get(i));
							entity.setOccupy(1);
							entity.setConfId(confId);
							this.channelService.update(entity, wrapper);
						}
					}
				}
				
				for (Channel c : list) {
					if(!c.getChannelId().equals("")){
						Channel channel1 = new Channel();
						channel1.setChannelId(c.getChannelId());
						QueryWrapper<Channel> wrapper = new QueryWrapper<Channel>(channel1);
						Channel entity = new Channel();
						entity.setOccupy(1);
						entity.setConfId(confId);
						this.channelService.update(entity, wrapper);
					}
				}
				
			}	

            respJson.put("msg", "开启成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "关闭电视墙轮询")
    @RequestMapping(value = "/closeHdupoll",method = RequestMethod.POST)
    public JSONObject closeHdupoll(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
            MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
            MeetingResult result = conferenceService.closeHdupoll(confId ,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("关闭电视墙轮询："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "关闭电视墙轮询失败");
                return respJson;
			}
            
            this.channelService.updateByConfId(confId);
            
            respJson.put("msg", "关闭成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    
    @ApiOperation(value = "获取电视墙通道")
    @RequestMapping(value = "/getChannels",method = RequestMethod.GET)
    public JSONObject getChannels(
    		@RequestParam @ApiParam(value = "电视墙ID",required = true) String tvId,
    		@RequestParam @ApiParam(value = "会议ID",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	Tvwall tvwall = new Tvwall();
        	tvwall.setTelevisionId(tvId);
        	QueryWrapper<Tvwall> wrapper = new QueryWrapper<Tvwall>(tvwall);
        	tvwall = this.tvwallService.getOne(wrapper);
        	
        	Channel channel = new Channel();
        	channel.setTelevisionId(tvId);
        	QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
        	List<Channel> list = this.channelService.list(queryWrapper);
        	
        	Map<String, Object> map = new HashMap<String, Object>();
        	map.put("line", tvwall.getLine());
        	map.put("col", tvwall.getCol());
        	
        	//增加延迟等待
        	Thread.sleep(3000);
        	
            MeetingResult result = conferenceService.getHdus(confId, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取电视墙："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取电视墙失败");
                return respJson;
			}
            
        	List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
            
            if(result.getHdus().size() < 1){
            	//未开启
            	for (Channel c : list) {
            		Map<String, Object> m = new HashMap<String, Object>();
            		m.put("channelId", c.getChannelId());
            		m.put("line", c.getLine());
            		m.put("col", c.getCol());
            		m.put("mt_id", "");
            		m.put("occupy", c.getOccupy());
            		m.put("confId", c.getConfId());
            		m.put("member_type", 1);
            		mapList.add(m);
    			}
            }else{
            	//获取电视墙批量轮询信息
            	MeetingResult meetingResult = conferenceService.getHdupoll(confId,meetInterface);
                if (meetingResult == null || meetingResult.getSuccess() != 1) {
                	log.error("获取电视墙批量轮询信息："+meetingResult);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "获取失败");
                    return respJson;
    			}
                if(meetingResult.getState() == 1){
                	//开启了批量轮询
                	for (Channel c : list) {
                		Map<String, Object> m = new HashMap<String, Object>();
                		m.put("channelId", c.getChannelId());
                		m.put("line", c.getLine());
                		m.put("col", c.getCol());
                		m.put("occupy", c.getOccupy());
                		m.put("confId", c.getConfId());
                		m.put("member_type", 1);
                		if(c.getChannelId() != null && !"".equals(c.getChannelId())){
                    		if(meetingResult.getHdus() != null && meetingResult.getMembers() != null){
                        		String mt_id = "";
                    			for (int i = 0; i < meetingResult.getHdus().size(); i++) {
        							if(meetingResult.getHdus().get(i).getHdu_id().equals(c.getChannelId())){
        								if(meetingResult.getMembers() != null && meetingResult.getMembers().size() > i){
        									mt_id = meetingResult.getMembers().get(i).getMt_id();
        								}
        								
        							}
        						}
                        		m.put("mt_id", mt_id);
                    		}else{
                    			m.put("mt_id", "");
                    		}
                		}else{
                			m.put("mt_id", "");
                		}

                		mapList.add(m);
        			}
                	
                }else{
                	
                	MeetingResult result2 = conferenceService.getHduVmpsHdus(meetInterface,confId);
                	List<Hdus> hdus = new ArrayList<Hdus>();
                	if(result2.getSuccess() == 1){
                		hdus = result2.getHdus();
                	}
                	
                	for (Channel c : list) {
                		Map<String, Object> m = new HashMap<String, Object>();
                		m.put("channelId", c.getChannelId());
                		m.put("line", c.getLine());
                		m.put("col", c.getCol());
                		m.put("occupy", c.getOccupy());
                		m.put("confId", c.getConfId());
                		if(c.getChannelId() != null && !"".equals(c.getChannelId())){
                			String mt_id = "";
                			int member_type = 0;
                			for (int i = 0; i < result.getHdus().size(); i++) {
								if(c.getChannelId().equals(result.getHdus().get(i).getHdu_id())){
									
									//判断会议有没有使用电视墙自主画面合成
									Boolean flag = true;
									if(hdus != null && hdus.size() > 0){
										for (Hdus h : hdus) {
											if(h.getHdu_id().equals(c.getChannelId())){
												member_type = 13;
												flag = false;
												break;
											}
										}
									}
									
									if(flag){
										HduResult hduResult = conferenceService.getHdu(meetInterface,confId,c.getChannelId());
										if (result == null || result.getSuccess() != 1) {
						                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
						                    respJson.put("msg", "获取失败");
						                    return respJson;
										}else{
											if(hduResult.getMode() != null){
												if(hduResult.getMode() == 1){
													//电视墙通道的模式
													member_type = hduResult.getSpecific().getMember_type();
													if(hduResult.getSpecific().getMember_type() == 1){
														//选看终端id，仅member_type为 1-指定 时生效
														mt_id = hduResult.getSpecific().getMt_id();
													}
												}else if(hduResult.getMode() == 2){
													member_type = 12;
												}else if(hduResult.getMode() == 3){
													member_type = 11;
													Integer poll_index = hduResult.getPoll().getPoll_index();
													if(poll_index != null){
														if(hduResult.getPoll().getMembers() != null && hduResult.getPoll().getMembers().size() > poll_index){
															mt_id = hduResult.getPoll().getMembers().get(poll_index).getMt_id();
														}
													}
												}
											}else{
												member_type = 0;
											}
											
										}
									}

								}
							}
                			m.put("member_type", member_type);
                			m.put("mt_id", mt_id);
                		}else{
                			m.put("mt_id", "");
                		}
                		
                		mapList.add(m);
        			}
                }
                
            }
        	
        	map.put("channels", mapList);
            respJson.put("data", map);
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "获取电视墙轮询")
    @RequestMapping(value = "/getHdupoll",method = RequestMethod.POST)
    public JSONObject getHdupoll(@RequestParam @ApiParam(value = "会议ID",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
            MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
            MeetingResult result = conferenceService.getHdupoll(confId, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取电视墙轮询："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取电视墙轮询失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "开启成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    
    
    
    @ApiOperation(value = "获取会议电视墙下拉列表")
    @RequestMapping(value = "/getHdus",method = RequestMethod.GET)
    public JSONObject getHdus(@RequestParam @ApiParam(value = "会议号",required = true) String confId){
        JSONObject respJson = new JSONObject();
        try {
        	User loginUser = (User) request.getSession().getAttribute("user");
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	//获取会议电视墙列表
            MeetingResult result = conferenceService.getHdus(confId, meetInterface);
            
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取电视墙："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取电视墙失败");
                return respJson;
			}
            
            //获取用户创建的电视墙
        	Tvwall tvwall = new Tvwall();
        	tvwall.setCreateId(loginUser.getMoid());
        	QueryWrapper<Tvwall> wrapper = new QueryWrapper<Tvwall>(tvwall);
        	List<Tvwall> tvwalls = this.tvwallService.list(wrapper);
            
            if(result.getHdus().size() < 1){
            	//会议未调用电视墙
            	Map<String, Object> resultMap = new HashMap<String, Object>();
            	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            	for (int i = 0; i < tvwalls.size(); i++) {
            		Map<String, Object> map = new HashMap<String, Object>();
            		if(i == 0){
            			resultMap.put("tvwallId", tvwalls.get(i).getTelevisionId());
            		}
            		map.put("id", tvwalls.get(i).getTelevisionId());
            		map.put("name", tvwalls.get(i).getName());
            		list.add(map);
				}
            	
            	resultMap.put("tvwalls", list);
            	resultMap.put("type", 0);
            	respJson.put("data", resultMap);
            }else{
            	//会议开启了电视墙
            	Channel channel = new Channel();
            	channel.setChannelId(result.getHdus().get(0).getHdu_id());
            	QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
            	channel = this.channelService.getOne(queryWrapper);
            	
            	//获取电视墙批量轮询信息
            	MeetingResult meetingResult = conferenceService.getHdupoll(confId,meetInterface);
                if (meetingResult == null || meetingResult.getSuccess() != 1) {
                	log.error("获取电视墙："+meetingResult);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "获取电视墙失败");
                    return respJson;
    			}
            	if(meetingResult.getState() == 1){
            		//会议开启了批量电视墙轮询
            		
            		Map<String, Object> resultMap = new HashMap<String, Object>();                	
                	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                	
                	for (int i = 0; i < tvwalls.size(); i++) {
                		Map<String, Object> map = new HashMap<String, Object>();
                		if(channel.getTelevisionId().equals(tvwalls.get(i).getTelevisionId())){
                			resultMap.put("tvwallId", tvwalls.get(i).getTelevisionId());
                		}
                		map.put("id", tvwalls.get(i).getTelevisionId());
                		map.put("name", tvwalls.get(i).getName());
                		list.add(map);	
                	}
            		
                	resultMap.put("type", 1);
            		resultMap.put("state", 1);	//批量轮询开关1开0关
            		resultMap.put("keep_time", meetingResult.getKeep_time());	//轮询间隔
            		resultMap.put("num", meetingResult.getNum());	//轮询次数
            		resultMap.put("show_mt_name", meetingResult.getShow_mt_name());
            		resultMap.put("tvwalls", list);
            		respJson.put("data", resultMap);            		
            	}else{
            		Map<String, Object> resultMap = new HashMap<String, Object>();
            		//会议开启了定制电视墙               	
                	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                	for (int i = 0; i < tvwalls.size(); i++) {
                		Map<String, Object> map = new HashMap<String, Object>();
                		if(channel.getTelevisionId().equals(tvwalls.get(i).getTelevisionId())){
                			resultMap.put("tvwallId", tvwalls.get(i).getTelevisionId());
                		}
                		map.put("id", tvwalls.get(i).getTelevisionId());
                		map.put("name", tvwalls.get(i).getName());
                		list.add(map);
    				}
                	resultMap.put("type", 0);
                	resultMap.put("tvwalls", list);
                	respJson.put("data", resultMap);
            	}

            }
            respJson.put("msg", "获取成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    @ApiOperation(value = "关闭电视墙")
    @RequestMapping(value = "/closeHdu",method = RequestMethod.POST)
    public JSONObject closeHdu(
    		@RequestParam @ApiParam(value = "会议号",required = true) String confId ,
    		@RequestParam @ApiParam(value = "电视墙通道",required = false) String hduId){
        JSONObject respJson = new JSONObject();
        try {
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	MeetingResult result = conferenceService.closeHdu(confId ,hduId ,meetInterface);
            if(result == null){
            	log.error("关闭电视墙："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "关闭电视墙失败");
                return respJson;
            }else if(result.getSuccess() != 1){
            	if(result.getError_code() != 20052){
            		log.error("关闭电视墙："+result);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "关闭电视墙失败");
                    return respJson;
            	}
            }
            
            this.channelService.updateByConfIdHdu(confId,hduId);
            respJson.put("msg", "关闭成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "关闭电视墙所有通道")
    @RequestMapping(value = "/closeHduAll",method = RequestMethod.POST)
    public JSONObject closeHduAll(@RequestParam @ApiParam(value = "会议号",required = true) String confId ,@RequestParam @ApiParam(value = "电视墙ID",required = true) String tvwallId){
        JSONObject respJson = new JSONObject();
        try {
        	
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	MeetingResult result = conferenceService.getHdus(confId, meetInterface);
            if (result == null) {
            	log.error("获取电视墙："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "获取电视墙失败");
                return respJson;
			}
            if(result.getHdus().size() > 0){
            	MeetingResult meetingResult = conferenceService.getHdupoll(confId,meetInterface);
                if (meetingResult == null) {
                	log.error("获取电视墙轮询："+result);
                    respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                    respJson.put("msg", "获取电视墙轮询失败");
                    return respJson;
    			}
                
                if(meetingResult.getState() == 1){
                	//关闭电视墙轮询
                	result = conferenceService.closeHdupoll(confId ,meetInterface);
                }else{
                	Channel channel = new Channel();
                	channel.setTelevisionId(tvwallId);
                	QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
                	List<Channel> list = this.channelService.list(queryWrapper);
                	for (Channel c : list) {
						for (Hdus hdus : result.getHdus()) {
							if(hdus.getHdu_id().equals(c.getChannelId())){
			            		conferenceService.closeHdu(confId ,c.getChannelId() ,meetInterface);
							}
						}
					}
                }
                
                this.channelService.updateByConfId(confId);
                
            }
            respJson.put("msg", "关闭成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    

    @ApiOperation(value = "修改电视墙批量轮询状态")
    @RequestMapping(value = "/changeHdupollState",method = RequestMethod.POST)
    public JSONObject changeHdupollState(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "电视墙批量轮询状态 0-停止轮询；1-暂停轮询；2-继续轮询；",required = true) Integer value){
        JSONObject respJson = new JSONObject();
        try{
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	MeetingResult result = conferenceService.changeHdupollState(confId,value,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("修改电视墙轮询："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            respJson.put("data", result);
            respJson.put("msg", "修改成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    @ApiOperation(value = "获取推送参数")
    @RequestMapping(value = "/getPush",method = RequestMethod.GET)
    public JSONObject getPush(){
        JSONObject respJson = new JSONObject();
        try {
        	User loginUser = (User) request.getSession().getAttribute("user");
        	
			CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
			Httpmsg httpMsg = new Httpmsg();
			httpMsg.SetAppId(env.getProperty("oauthConsumerKey"));
			httpMsg.SetAppKey(env.getProperty("oauthConsumerSecret"));
			httpMsg.SetUserName(loginUser.getAccount());
			httpMsg.SetPassWord(loginUser.getPassword());
			httpMsg.login(env.getProperty("meetIp"));
			
			HttpClient httpclient = new HttpClient();
			httpclient.start();
			ClientTransport httptransport = new LongPollingTransport(null,httpclient);
			CookieManager handle = (CookieManager) CookieHandler.getDefault();
			CookieStore store = handle.getCookieStore();
			List<HttpCookie> lstCookie = store.getCookies();
        	System.err.println(lstCookie);
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("ip", env.getProperty("meetIp"));
            for (HttpCookie httpCookie : lstCookie) {
            	map.put(httpCookie.getName(), httpCookie.getValue());
            }
        	respJson.put("msg", "获取成功");
            respJson.put("data", map);
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "获取失败");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
	@ResponseBody
	@ApiOperation(value = "获取会议信息接口")
	@RequestMapping(value = "/getMeet",  method = RequestMethod.GET)
	public JSONObject getMeet(String conf_id){
		JSONObject respJson = new JSONObject();
		try {
			Map<String,Object> resultMap = new HashMap<String,Object>();
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	MeetingResult result = conferenceService.getMeet(meetInterface,conf_id);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取会议信息："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            
            Cascades jilianresult = this.conferenceService.getAttendee(meetInterface,conf_id);
            resultMap.put("name", result.getName());	//会议名称
            resultMap.put("joinMt", jilianresult.getChildrens().size());				//与会方
            resultMap.put("silence", result.getSilence());								//全场静音
            resultMap.put("mute", result.getMute());									//全场哑音
            resultMap.put("children", jilianresult.getChildrens());
            resultMap.put("confType", result.getConf_type());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
        	Date endDate = new Date();
        	Date nowDate = sdf.parse(result.getStart_time());
            long nd = 1000 * 24 * 60 * 60;
            long nh = 1000 * 60 * 60;
            long nm = 1000 * 60;
            long diff = endDate.getTime() - nowDate.getTime();
            long hour = diff % nd / nh;
            long min = diff % nd % nh / nm;
            resultMap.put("startHour", hour);
            resultMap.put("startMin", min);
            resultMap.put("endTime", result.getDuration());            
            result = conferenceService.getMeetChairman(meetInterface,conf_id,jilianresult.getChildrens());
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取会议管理方："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            resultMap.put("chairman", result.getMt_id());
            result = conferenceService.getMeetSpeaker(meetInterface,conf_id);
            if (result == null || result.getSuccess() != 1) {
            	log.error("获取会议发言方："+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            resultMap.put("speaker", result.getMt_id());
            
            result = conferenceService.getWatchTerminal(conf_id,meetInterface);
            
            
            resultMap.put("terminalCTS", result.getInspections());
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
    
    ////OCX发送监控请求
    @ApiOperation(value = "OCX发送监控请求")
    @RequestMapping(value = "/monitors",method = RequestMethod.POST)
    public JSONObject monitors(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "视频端口",required = true) Integer v_port,@RequestParam @ApiParam(value = "音频端口",required = true) Integer a_port,@RequestParam @ApiParam(value = "终端号",required = true) String mt_id){
        JSONObject respJson = new JSONObject();
        try{
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	String ip = HttpUtil.getClientIp(request);
        	MeetingResult result = conferenceService.videoMonitors(meetInterface,confId,ip,v_port,mt_id);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX发送监控请求(视频)"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
        	
            result = conferenceService.audioMonitors(meetInterface,confId,ip,a_port,mt_id);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX发送监控请求(音频)"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            
            Map<String, Object> map = conferenceService.getMonitors(meetInterface,confId,ip,v_port);
            if(map.get("port") == null){
            	log.error("OCX获取监控信息"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;	
            }else{
            	respJson.put("data", map);
            }
            
            result = conferenceService.neediframe(v_port,a_port ,ip,confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX发送关键帧"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            
            
            result = conferenceService.heartbeat(v_port,a_port,ip,meetInterface,confId);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX保活"+result);
            	System.out.println();
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            respJson.put("msg", "修改成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
	
	
    ////OCX请求关键帧
    @ApiOperation(value = "OCX请求关键帧")
    @RequestMapping(value = "/neediframe",method = RequestMethod.POST)
    public JSONObject neediframe(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "视频端口",required = true) Integer v_port,@RequestParam @ApiParam(value = "音频端口",required = true) Integer a_port){
        JSONObject respJson = new JSONObject();
        try{
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	String ip = HttpUtil.getClientIp(request);
        	MeetingResult result = conferenceService.neediframe(v_port,a_port ,ip,confId,meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX请求关键帧"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            respJson.put("msg", "请求成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
    
    
    ////OCX心跳保活
    @ApiOperation(value = "OCX心跳保活")
    @RequestMapping(value = "/heartbeat",method = RequestMethod.POST)
    public JSONObject heartbeat(@RequestParam @ApiParam(value = "会议号",required = true) String confId,@RequestParam @ApiParam(value = "视频端口",required = true) Integer v_port,@RequestParam @ApiParam(value = "音频端口",required = true) Integer a_port){
        JSONObject respJson = new JSONObject();
        try{
        	MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
        	String ip = HttpUtil.getClientIp(request);
        	MeetingResult result = conferenceService.heartbeat(v_port,a_port,ip,meetInterface,confId);
            if (result == null || result.getSuccess() != 1) {
            	log.error("OCX心跳保活"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "会议平台调用失败");
                return respJson;
			}
            respJson.put("msg", "操作成功");
            respJson.put("code", ResultCode.SUCCESS);
        }catch (Exception e){
            respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
            respJson.put("msg", "系统异常!");
            e.printStackTrace();
        }
        return respJson;
    }
  //全场哑音
    @ApiOperation(value = "全场哑音")
	@RequestMapping(value = "/Mute", method = RequestMethod.POST)
	public JSONObject Mute(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "状态0-停止静音；1-静音；", required = true)int value){
		JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.Mute(conf_id,value, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("全场哑音"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "全场哑音失败");
                return respJson;
			}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
  //全场静音
    @ApiOperation(value = "全场静音")
	@RequestMapping(value = "/KeepQuiet", method = RequestMethod.POST)
	public JSONObject Keep_Quiet(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@RequestParam @ApiParam(value = "状态0-停止静音；1-静音；", required = true)int value){
		JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.KeepQuiet(conf_id,value, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("全场静音:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "全场静音失败");
                return respJson;
			}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    //呼叫离线终端
    @ApiOperation(value = "呼叫离线终端")
	@RequestMapping(value = "/CallOffLine", method = RequestMethod.POST)
	public JSONObject Call_Off_Line(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id){
		JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.CallOffLine(conf_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("呼叫离线终端:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "呼叫离线终端失败");
                return respJson;
			}
			respJson.put("data", result);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    @ApiOperation(value = "获取剩余时长")
	@RequestMapping(value = "/getSurplusTime", method = RequestMethod.GET)
	public JSONObject getSurplusTime(@RequestParam @ApiParam(value = "会议号", required = true)String confId){
		JSONObject respJson = new JSONObject();
		try{
			Meet meet = new Meet();
			meet.setMeetNumber(confId);
			meet.setStatus(1);
			QueryWrapper<Meet> queryWrapper = new QueryWrapper<Meet>(meet);
			meet = this.meetService.getOne(queryWrapper);
			String surplusTime = null;
			if(meet != null){
				if(meet.getDuration() == 0){
					surplusTime = "手动结束";
				}else{
					surplusTime = DateUtil.getSurplusTime(meet.getStartTime(), meet.getDuration());
				}
			}else{
				respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
		        respJson.put("msg", "会议不存在或已结束!");
		        return respJson;
			}
			respJson.put("data", surplusTime);	
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    //呼叫终端
    @ApiOperation(value = "呼叫终端")
	@RequestMapping(value = "/callMtId", method = RequestMethod.POST)
	public JSONObject callMtId(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
		JSONObject respJson = new JSONObject();
		try{
			
	        MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
	        MeetingResult result = conferenceService.callMtId(confId, mt_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("呼叫终端:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "呼叫终端失败");
                return respJson;
			}
			respJson.put("msg", "呼叫成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    @ApiOperation(value = "挂断终端")
	@RequestMapping(value = "/hangUpMtId", method = RequestMethod.POST)
	public JSONObject hangUpMtId(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
		JSONObject respJson = new JSONObject();
		try{
			
	        MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
	        MeetingResult result = conferenceService.hangUpMtId(confId, mt_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("挂断终端:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "挂断终端失败");
                return respJson;
			}
			respJson.put("msg", "挂断成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    @ApiOperation(value = "开启静音")
	@RequestMapping(value = "/openSilence", method = RequestMethod.POST)
	public JSONObject openMute(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
		JSONObject respJson = new JSONObject();
		try{
			
	        MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
	        MeetingResult result = conferenceService.openSilence(confId, mt_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("开启静音:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "开启静音失败");
                return respJson;
			}
			respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    @ApiOperation(value = "关闭静音")
	@RequestMapping(value = "/closeSilence", method = RequestMethod.POST)
	public JSONObject closeSilence(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
		JSONObject respJson = new JSONObject();
		try{
			
	        MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
	        MeetingResult result = conferenceService.closeSilence(confId, mt_id, meetInterface);
            if (result == null || result.getSuccess() != 1) {
            	log.error("关闭静音:"+result);
                respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
                respJson.put("msg", "关闭静音失败");
                return respJson;
			}
			respJson.put("msg", "关闭成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
	}
    
    
    @ApiOperation(value = "获取发言方")
    @RequestMapping(value = "/getSpeaker", method = RequestMethod.GET)
    public JSONObject getSpeaker(@RequestParam @ApiParam(value = "会议号", required = true)String confId){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.getMeetSpeaker(meetInterface,confId);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取发言方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取发言方失败");
    			return respJson;
    		}
    		respJson.put("data", result.getMt_id());
    		respJson.put("msg", "获取成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    @ApiOperation(value = "获取管理方")
    @RequestMapping(value = "/getChairman", method = RequestMethod.GET)
    public JSONObject getChairman(@RequestParam @ApiParam(value = "会议号", required = true)String confId){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.getChairman(confId, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("获取管理方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取管理方失败");
    			return respJson;
    		}
    		respJson.put("data", result.getMt_id());
    		respJson.put("msg", "获取成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    @ApiOperation(value = "指定发言方")
    @RequestMapping(value = "/appointSpeaker", method = RequestMethod.POST)
    public JSONObject appointSpeaker(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "终端mt_id", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.appointSpeaker(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("指定发言方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "指定发言方失败");
    			return respJson;
    		}
    		respJson.put("msg", "挂断成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    
    @ApiOperation(value = "取消发言方")
    @RequestMapping(value = "/cancelSpeaker", method = RequestMethod.POST)
    public JSONObject cancelSpeaker(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.cancelSpeaker(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("取消发言方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "取消发言方失败");
    			return respJson;
    		}
    		respJson.put("msg", "指定成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    
    @ApiOperation(value = "指定管理方")
    @RequestMapping(value = "/appointChairman", method = RequestMethod.POST)
    public JSONObject appointChairman(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.appointChairman(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("指定管理方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "指定管理方失败");
    			return respJson;
    		}
    		respJson.put("msg", "指定成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    @ApiOperation(value = "取消管理方")
    @RequestMapping(value = "/cancelChairman", method = RequestMethod.POST)
    public JSONObject cancelChairman(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.cancelChairman(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("取消管理方:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "取消管理方失败");
    			return respJson;
    		}
    		respJson.put("msg", "取消成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    }
    
    
    @ApiOperation(value = "指定双流源")
    @RequestMapping(value = "/appointDualstream", method = RequestMethod.POST)
    public JSONObject appointDualstream(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.appointDualstream(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("指定双流源:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "指定双流源失败");
    			return respJson;
    		}
    		respJson.put("msg", "指定成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    } 
    
    @ApiOperation(value = "取消双流源")
    @RequestMapping(value = "/cancelDualstream", method = RequestMethod.POST)
    public JSONObject cancelDualstream(@RequestParam @ApiParam(value = "会议号", required = true)String confId, @RequestParam @ApiParam(value = "会议号", required = true)String mt_id){
    	JSONObject respJson = new JSONObject();
    	try{
    		
    		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
    		MeetingResult result = conferenceService.cancelDualstream(confId, mt_id, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("取消双流源:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "取消双流源失败");
    			return respJson;
    		}
    		respJson.put("msg", "取消成功");
    		respJson.put("code", ResultCode.SUCCESS);
    	}catch(Exception e){
    		respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    		respJson.put("msg", "系统异常!");
    		e.printStackTrace();
    	}
    	return respJson;
    } 
    
    //发送短消息接口
    @ApiOperation(value = "删除终端接口")
    @RequestMapping(value = "/deleteMts",method = RequestMethod.POST)
    public JSONObject deleteMts(@RequestParam @ApiParam(value = "会议号", required = true)String conf_id,@ApiParam @RequestParam(value = "mtsList", required = false)List<String> mtsList){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.deleteMts(conf_id,mtsList, meetInterface);
    		if (result == null || result.getSuccess() != 1) {
    			log.error("删除终端:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "删除终端失败");
    			return respJson;
    		}
			respJson.put("data", result);
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "开启电视墙自主画面合成")
    @RequestMapping(value = "/openHduVmps",method = RequestMethod.POST)
    public JSONObject openHduVmps(
    		@RequestBody @ApiParam(value = "合成画面参数", required = true)OpenConfParam openConfParam){
    	JSONObject respJson = new JSONObject();
		try{
			String conf_id = openConfParam.getConf_id();
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduVmps(meetInterface,openConfParam);
	        
	        
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙自动画面合成:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(openConfParam.getHdu_id());
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(conf_id);
			this.channelService.update(entity, queryWrapper);
	        
	        
			respJson.put("data", result);
			respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "修改电视墙自主画面合成")
    @RequestMapping(value = "/updateHduVmps",method = RequestMethod.POST)
    public JSONObject updateHduVmps(
    		@RequestBody @ApiParam(value = "合成画面参数", required = true)OpenConfParam openConfParam){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.updateHduVmps(meetInterface,openConfParam);
	        
	        
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙自动画面合成:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(openConfParam.getHdu_id());
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(openConfParam.getConf_id());
			this.channelService.update(entity, queryWrapper);
	        
	        
			respJson.put("data", result);
			respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "获取电视墙自主画面合成")
    @RequestMapping(value = "/getHduVmps",method = RequestMethod.GET)
    public JSONObject getHduVmps(
    		@RequestParam @ApiParam(value = "conf_id:会议号", required = true)String conf_id,
    		@RequestParam @ApiParam(value = "hdu_id:电视墙通道ID", required = true)String hdu_id){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getHduVmpsHdus(meetInterface,conf_id);
	        Map<String, Object> map = new HashMap<String, Object>();
	        
	        if(result != null && result.getSuccess() == 1){
	        	if(result.getHdus() != null && result.getHdus().size() > 0){
	        		boolean flag = false;
	        		for (Hdus hdus : result.getHdus()) {
						if(hdus.getHdu_id().equals(hdu_id)){
							flag = true;
						}
					}
	        		if(flag){
	        			map.put("type", 1);			//开启了画面合成	      
	        			result = conferenceService.getHduVmps(meetInterface,conf_id,hdu_id);
	        			if(result.getMembers() != null && result.getMembers().size() > 0){
	        				//自动画面合成
	        				if(result.getMembers().get(0).getMember_type() == 5){
	        					map.put("mode", 2);
	        				}else{
	        					map.put("mode", 1);	
	        				}
	        			}else{
	        				map.put("mode", 1);	
	        			}
	        			map.put("layout", result.getLayout());
	        			map.put("show_mt_name", result.getShow_mt_name());
	        			
	        			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	        			for (Member member : result.getMembers()) {
	        				Map<String, Object> memberMap = new HashMap<String, Object>();
	        				memberMap.put("mt_id", member.getMt_id());
	        				memberMap.put("chn_idx", member.getChn_idx());
	        				memberMap.put("member_type", member.getMember_type());
	        				list.add(memberMap);
	        			}
	        			map.put("members", list);
	        			
	        			
	        		}else{
	        			map.put("type", 0);	
	        		}
	        	}else{
	        		map.put("type", 0);	
	        	}
	        }else{
	        	map.put("type", 0);			
	        }
	        
			respJson.put("data", map);
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "获取开启自主画面合成的终端列表")
    @RequestMapping(value = "/getHduVmpsMts",method = RequestMethod.GET)
    public JSONObject getHduVmpsMts(
    		@RequestParam @ApiParam(value = "conf_id:会议号", required = true)String conf_id){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getHduVmpsMts(meetInterface,conf_id);
	        
	        
	        if (result == null || result.getSuccess() != 1) {
    			log.error("获取开启自主画面合成的终端列表:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
			respJson.put("data", result);
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "获取开启自主画面合成的电视墙列表")
    @RequestMapping(value = "/getHduVmpsHdus",method = RequestMethod.GET)
    public JSONObject getHduVmpsHdus(
    		@RequestParam @ApiParam(value = "conf_id:会议号", required = true)String conf_id){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.getHduVmpsHdus(meetInterface,conf_id);
	        
	        
	        if (result == null || result.getSuccess() != 1) {
    			log.error("获取开启自主画面合成的电视墙列表:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
			respJson.put("data", result);
			respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    
    @ApiOperation(value = "停止电视墙自主画面合成")
    @RequestMapping(value = "/cancelHduVmps",method = RequestMethod.POST)
    public JSONObject cancelHduVmps(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.cancelHduVmps(meetInterface,confId,hduId);
	        
	        
	        if (result == null || result.getSuccess() != 1) {
    			log.error("停止电视墙自主画面合成:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "停止失败");
    			return respJson;
    		}
			respJson.put("data", result);
			respJson.put("msg", "停止成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    
    @ApiOperation(value = "开启电视墙管理方跟随")
    @RequestMapping(value = "/openHduChairman",method = RequestMethod.POST)
    public JSONObject openHduChairman(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduChairman(confId,hduId,meetInterface);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙管理方跟随:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "开启电视墙发言方跟随")
    @RequestMapping(value = "/openHduSpeaker",method = RequestMethod.POST)
    public JSONObject openHduSpeaker(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduSpeaker(confId,hduId,meetInterface);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙管理方跟随:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "开启电视墙内容共享跟随")
    @RequestMapping(value = "/openHduDualstream",method = RequestMethod.POST)
    public JSONObject openHduDualstream(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduDualstream(confId,hduId,meetInterface);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙管理方跟随:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    
    @ApiOperation(value = "开启电视墙会议轮询跟随")
    @RequestMapping(value = "/openHduMeetPoll",method = RequestMethod.POST)
    public JSONObject openHduMeetPoll(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduMeetPoll(confId,hduId,meetInterface);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙管理方跟随:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "开启电视墙单通道轮询")
    @RequestMapping(value = "/openHduChannelPoll",method = RequestMethod.POST)
    public JSONObject openHduChannelPoll(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId,
    		@RequestParam @ApiParam(value = "轮询次数，0为一直轮询",required = true) Integer num,
    		@RequestParam @ApiParam(value = "轮询时间，单位：秒",required = true)Integer keepTime, 
    		@RequestParam @ApiParam(value = "轮询终端号:设备的mt_id",required = true) List<String> accountList){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduChannelPoll(confId,hduId,meetInterface,num,keepTime,accountList);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启电视墙单通道轮询:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "获取电视墙单通道轮询")
    @RequestMapping(value = "/getHduChannelPoll",method = RequestMethod.GET)
    public JSONObject getHduChannelPoll(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			Map<String, Object> data = new HashMap<String, Object>();
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			HduResult hduResult = conferenceService.getHdu(meetInterface,confId,hduId);
	        if (hduResult == null) {
    			log.error("获取电视墙单通道轮询失败");
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
	        if(hduResult.getSuccess() == 0 || hduResult.getMode() == null || hduResult.getMode() == 1 || hduResult.getMode() == 2){
	        	data.put("state", 0);
	        }else if(hduResult.getMode() == 3){
	        	data.put("state", 1);
	        	data.put("num", Integer.parseInt(hduResult.getPoll().getNum()));
	        	data.put("keep_time", Integer.parseInt(hduResult.getPoll().getKeep_time()));
	        	List<Map<String, Object>> members = new ArrayList<Map<String, Object>>();
	        	for (MeetingUser meetingUser : hduResult.getPoll().getMembers()) {
	        		Map<String, Object> mts = new HashMap<String, Object>();
	        		mts.put("mt_id", meetingUser.getMt_id());
	        		members.add(mts);
				}
	        	
	        	data.put("members", members);
	        }
	        respJson.put("data", data);        
	        respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    
    @ApiOperation(value = "开启四分屏")
    @RequestMapping(value = "/openHduSplitScreen",method = RequestMethod.POST)
    public JSONObject openHduSplitScreen(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId,
    		@RequestParam @ApiParam(value = "四分屏通道号0-3:chn_idx",required = true) List<Integer> chnIdxList, 
    		@RequestParam @ApiParam(value = "轮询终端号:设备的mt_id",required = true) List<String> accountList){
    	JSONObject respJson = new JSONObject();
		try{
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			MeetingResult result = conferenceService.openHduSplitScreen(confId,hduId,meetInterface,accountList,chnIdxList);
	        if (result == null || result.getSuccess() != 1) {
    			log.error("开启四分屏:"+result);
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "开启失败");
    			return respJson;
    		}
			Channel channel = new Channel();
			channel.setChannelId(hduId);
			QueryWrapper<Channel> queryWrapper = new QueryWrapper<Channel>(channel);
			Channel entity = new Channel();
			entity.setOccupy(1);
			entity.setConfId(confId);
			this.channelService.update(entity, queryWrapper);
	        respJson.put("msg", "开启成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
    
    @ApiOperation(value = "获取电视墙四分屏")
    @RequestMapping(value = "/getHduSplitScreen",method = RequestMethod.GET)
    public JSONObject getHduSplitScreen(
    		@RequestParam @ApiParam(value = "confId:会议号", required = true)String confId,
    		@RequestParam @ApiParam(value = "hduId:电视墙通道ID", required = true)String hduId){
    	JSONObject respJson = new JSONObject();
		try{
			Map<String, Object> data = new HashMap<String, Object>();
			MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
			HduResult hduResult = conferenceService.getHdu(meetInterface,confId,hduId);
	        if (hduResult == null) {
    			log.error("获取电视墙失败");
    			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
    			respJson.put("msg", "获取失败");
    			return respJson;
    		}
	        if(hduResult.getSuccess() == 0 || hduResult.getMode() == 1 || hduResult.getMode() == 3){
	        	data.put("state", 0);
	        }else if(hduResult.getMode() == 2){
	        	data.put("state", 1);
	        	data.put("members", hduResult.getSpilt().getMembers());
	        }
	        respJson.put("data", data);        
	        respJson.put("msg", "获取成功");
			respJson.put("code", ResultCode.SUCCESS);
		}catch(Exception e){
			respJson.put("code", ResultCode.INTERNAL_SERVER_ERROR);
	        respJson.put("msg", "系统异常!");
	        e.printStackTrace();
		}
        return respJson;
    }
    
}
