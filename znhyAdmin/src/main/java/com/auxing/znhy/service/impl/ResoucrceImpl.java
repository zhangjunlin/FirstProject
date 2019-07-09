package com.auxing.znhy.service.impl;

import com.alibaba.fastjson.JSON;
import com.auxing.znhy.service.IResourceService;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.VirtualMeetDetails;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther liuyy
 * @date 2018/10/12 0012 上午 11:18
 */
@Slf4j
@Service
public class ResoucrceImpl implements IResourceService {
	
	@Autowired
	private Environment env;
	
    @Override
    public MeetingResult changName(String ip,String id, String name, String token, String cookie, VirtualMeetDetails details) throws Exception {
        MeetingResult result = null;
        Map<String, Object> resMap = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        resMap.put("name",name);
        resMap.put("bitrate",details.getBitrate());
        resMap.put("conf_type",details.getConf_type());
        resMap.put("status",details.getStatus());
        resMap.put("cascade_mode",details.getCascade_mode());
        resMap.put("cascade_upload",details.getCascade_upload());
        resMap.put("cascade_return",details.getCascade_return());
        resMap.put("cascade_return_para",details.getCascade_return_para());
        resMap.put("mute",details.getMute());
        resMap.put("mute_filter",details.getMute_filter());
        resMap.put("preoccpuy_resource",details.getPreoccpuy_resource());
        resMap.put("encrypted_type",details.getEncrypted_type());
        resMap.put("encrypted_auth",details.getEncrypted_auth());
        resMap.put("encrypted_key",details.getEncrypted_key());
        resMap.put("dual_mode",details.getDual_mode());
        resMap.put("call_mode",details.getCall_mode());
        resMap.put("call_times",details.getCall_times());
        resMap.put("call_interval",details.getCall_interval());
        resMap.put("voice_activity_detection",details.getVoice_activity_detection());
        resMap.put("vacinterval",details.getVacinterval());
        resMap.put("one_reforming",details.getOne_reforming());
        resMap.put("fec_mode",details.getFec_mode());
        resMap.put("exclusive",details.getExclusive());
        resMap.put("video_formats",details.getVideo_formats());
        resMap.put("max_join_mt",details.getMax_join_mt());
        Map<String, Object> mixMap = new HashMap<String, Object>();
        mixMap.put("mode",1);
        resMap.put("mix",mixMap);
        Map<String, Object> vmpMap = new HashMap<String, Object>();
        vmpMap.put("mode",2);
        resMap.put("vmp",vmpMap);
        resMap.put("recorder",details.getRecorder());
        resMap.put("exclusive_users",details.getExclusive_users());
        params.put("account_token", token);
        System.out.println(resMap);
        try {
            params.put("params", URLEncoder.encode(JSON.toJSONString(resMap),"UTF-8"));
        }catch (Exception e){
            e.printStackTrace();
        }
        result = MeetHttpClient.sendPost(ip+"/api/v1/mc/virtual_meeting_rooms/"+id+"",params, cookie);
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
			throw new Exception();
		}
        return result;
    }
    
	@Override
	public MeetingResult getIrtualMeetById(String fictitiousId, MeetInterface meetInterface){
        MeetingResult result = null;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/mc/virtual_meeting_rooms/"+fictitiousId, params, meetInterface.getCookie());
        return result;
	}
}
