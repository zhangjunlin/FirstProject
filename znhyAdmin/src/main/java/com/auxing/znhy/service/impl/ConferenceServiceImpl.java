package com.auxing.znhy.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auxing.znhy.entity.Channel;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.service.IConferenceService;
import com.auxing.znhy.service.IUserService;
import com.auxing.znhy.util.HduResult;
import com.auxing.znhy.util.Hdus;
import com.auxing.znhy.util.IpAdd;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.meeting.CascadeMts;
import com.auxing.znhy.util.meeting.Cascades;
import com.auxing.znhy.util.meeting.Member;
import com.auxing.znhy.util.meeting.Mts;
import com.auxing.znhy.util.meeting.OpenConfParam;
import com.auxing.znhy.util.meeting.TargetSource;
import com.auxing.znhy.util.meeting.Terminal;
import com.auxing.znhy.util.meeting.TerminalWatch;
import com.auxing.znhy.util.meeting.Vrs;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/**
 * @auther liuyy
 * @date 2018/10/26 0026 下午 4:17
 */
@Slf4j
@Service
public class ConferenceServiceImpl implements IConferenceService {
    @Autowired
    Environment env;
    
    @Autowired
    IUserService userService;

	@Autowired
	protected HttpServletRequest request;
    //获取指定会议终端列表
    @Override
    public Cascades getAttendee(MeetInterface meetInterface,String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        
        //获取会议级联信息
        MeetingResult total = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+id+"/cascades",params, meetInterface.getCookie());
        //给第一级指定id,为了下级与本级(父级)做树做准备
        UUID uuid = UUID.randomUUID();
        //最高一级
        Cascades cascade = new Cascades(total.getConf_id(),total.getName(),total.getCascade_id(),total.getMt_id(),uuid.toString());
        List<Cascades> listcascades = total.getCascades();//最高级的孩子级
        List<CascadeMts> lists = new ArrayList<CascadeMts>();
        //查询本级会议终端列表
        MeetingResult childrensResult = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+id+"/mts",params, meetInterface.getCookie());
       if(childrensResult.getMts()!= null && childrensResult.getMts().size()>0){
    	    for (Mts mts : childrensResult.getMts()) {
            	if(mts.getType() == 1){//type为1 表示终端用户
            		CascadeMts cascadeMts = new CascadeMts(mts.getMt_id(),uuid.toString(),mts.getAlias(),
                			mts.getIp(),mts.getOnline(),mts.getE164(),mts.getType(),
                			mts.getBitrate(),mts.getSilence(),mts.getMute(),mts.getDual(),
                			mts.getMix(),mts.getVmp(),mts.getInspection(),mts.getRec(),mts.getPoll(),mts.getUpload(),mts.getProtocol(),
                			mts.getCall_mode(),"1");
            		if(mts.getAlias().equals("")){
            			if(mts.getE164() != null && !mts.getE164().equals("")){
            				User user = new User();
            				user.setE164(mts.getE164());
            				QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
            				user = userService.getOne(queryWrapper);
            				if(user != null){
                    			cascadeMts.setAlias(user.getActuralName());
            				}else{
            					cascadeMts.setAlias(mts.getE164());
            				}
            			}else if(mts.getE164().equals("") && !"".equals(mts.getIp())){
            				cascadeMts.setAlias(mts.getIp());
            			}
            		}
            		
            		lists.add(cascadeMts);//将终端存入集合
            	}else if(mts.getType() == 7 && mts.getOnline() == 0){
            		CascadeMts cascadeMts = new CascadeMts(mts.getMt_id(),uuid.toString(),mts.getAlias(),
                			mts.getIp(),mts.getOnline(),mts.getE164(),mts.getType(),
                			mts.getBitrate(),mts.getSilence(),mts.getMute(),mts.getDual(),
                			mts.getMix(),mts.getVmp(),mts.getInspection(),mts.getRec(),mts.getPoll(),mts.getUpload(),mts.getProtocol(),
                			mts.getCall_mode(),"0");
            		if(mts.getAlias().equals("")){
            			if(mts.getE164() != null && !mts.getE164().equals("")){
            				User user = new User();
            				user.setE164(mts.getE164());
            				QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
            				user = userService.getOne(queryWrapper);
            				if(user != null){
                    			cascadeMts.setAlias(user.getActuralName());
            				}else{
            					cascadeMts.setAlias(mts.getE164());
            				}
            			}else if(mts.getE164().equals("") && !"".equals(mts.getIp())){
            				cascadeMts.setAlias(mts.getIp());
            			}
            		}
            		
            		lists.add(cascadeMts);//将终端存入集合        		
            	}
    		} 
       }
          //调用排序
          mycomparator(lists);
        //调用递归方法,查询下级终端
        List<CascadeMts> list = getChild(meetInterface,uuid.toString(),total.getConf_id(),listcascades,lists);
        cascade.setChildrens(list);
        return cascade;
    }
    
  //查询终端树递归循环的通用方法
  	public List<CascadeMts> getChild(MeetInterface meetInterface,String pid,String conf_id, List<Cascades> listcascades,List<CascadeMts> childList
  			) {
  	    // 子菜单
  	    for (Cascades tree : listcascades) {
  	    	 List<Cascades> listcascade = tree.getCascades();
  	    	 UUID uuid = UUID.randomUUID(); 
  	    	 CascadeMts cascadeMt = new CascadeMts(tree.getConf_id(),tree.getName(),tree.getCascade_id(),tree.getMt_id(),pid,uuid.toString(),"0",1,tree.getConf_id(),1);
  	    	 if(tree.getName() == null || tree.getName().equals("")){
  	    		cascadeMt.setAlias(tree.getConf_id());
  	    	 }else{
  	    		cascadeMt.setAlias(tree.getName()); 
  	    	 }
  	        // 遍历所有节点，将父菜单id与传过来的id比较
  	         Map<String, Object> params = new HashMap<String, Object>();
  	         params.put("account_token", meetInterface.getToken());
  	         //插入级联查询的终端
  	         MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/cascades/"+tree.getCascade_id()+"/mts",params, meetInterface.getCookie());
  	         List<CascadeMts> childMts = new ArrayList<>();
  	         if(result.getMts()!= null && result.getMts().size()>0){
  	           for(Mts mts : result.getMts()){
    	        	 if(mts.getType() == 1){
    	        		CascadeMts cascadeMts = new CascadeMts(mts.getMt_id(),uuid.toString(),mts.getAlias(),mts.getIp(),mts.getOnline(),mts.getE164(),mts.getType(),mts.getBitrate(),mts.getSilence(),
    	  	        			mts.getMute(),mts.getDual(),mts.getMix(),mts.getVmp(),mts.getInspection(),mts.getRec(),mts.getPoll(),mts.getUpload(),mts.getProtocol(),mts.getCall_mode(),"1");
    	        		cascadeMts.setCascadesLevel(1);
    	        		if(mts.getAlias().equals("")){
    	        			if(mts.getE164() != null && !mts.getE164().equals("")){
    	        				User user = new User();
    	        				user.setE164(mts.getE164());
    	        				QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
    	        				user = userService.getOne(queryWrapper);
    	        				if(user != null){
    	                			cascadeMts.setAlias(user.getActuralName());
    	        				}else{
                					cascadeMts.setAlias(mts.getE164());
                				}
    	        			}else if(mts.getE164().equals("") && !"".equals(mts.getIp())){
                				cascadeMts.setAlias(mts.getIp());
                			}
    	        		}
    	        		
    	  	        	childMts.add(cascadeMts); 
    	        	 }else if(mts.getType() == 7 && mts.getOnline() == 0){
    	        		CascadeMts cascadeMts = new CascadeMts(mts.getMt_id(),uuid.toString(),mts.getAlias(),
    	            			mts.getIp(),mts.getOnline(),mts.getE164(),mts.getType(),
    	            			mts.getBitrate(),mts.getSilence(),mts.getMute(),mts.getDual(),
    	            			mts.getMix(),mts.getVmp(),mts.getInspection(),mts.getRec(),mts.getPoll(),mts.getUpload(),mts.getProtocol(),
    	            			mts.getCall_mode(),"0");
    	        		cascadeMts.setCascadesLevel(1);
    	        		if(mts.getAlias().equals("")){
    	        			if(mts.getE164() != null && !mts.getE164().equals("")){
    	        				User user = new User();
    	        				user.setE164(mts.getE164());
    	        				QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
    	        				user = userService.getOne(queryWrapper);
    	        				if(user != null){
    	                			cascadeMts.setAlias(user.getActuralName());
    	        				}else{
                					cascadeMts.setAlias(mts.getE164());
                				}
    	        			}else if(mts.getE164().equals("") && !"".equals(mts.getIp())){
                				cascadeMts.setAlias(mts.getIp());
                			}
    	        		}
    	        		childMts.add(cascadeMts);//将终端存入集合        		
    	        	}
    	         }   
  	         }
  	          //调用排序
  	          mycomparator(childMts);
  	         //判断级联是否为空
  	         if(listcascade.size() > 0){
  	        	 //将级联查询的信息插入
  	        	 List<CascadeMts> list = getChild(meetInterface,uuid.toString(),conf_id, listcascade,childMts);
  	        	 cascadeMt.setChildrens(list);
  	         }else{
  	        	 cascadeMt.setChildrens(childMts);
  	         }
  	      
  	       childList.add(cascadeMt);
  	    }
  	    return childList;
  	}

    //开启录像
    @Override
    public MeetingResult OpenConfRecord(String conf_id, String mode, String name, String publishMode, String anonymous,MeetInterface meetInterface, List<Vrs> vrsList){
        //参数
        //开启录像||直播
        Map<String, Object> Params = new HashMap<String, Object>();
        Params.put("account_token", meetInterface.getToken());//token
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("video_name",name);
        map.put("recorder_type",1);
        map.put("publish_mode",publishMode);
        map.put("anonymous",anonymous);
        map.put("recorder_mode",mode);
        map.put("main_stream",1);
        map.put("dual_stream",1);
        map.put("vrs_id",vrsList.get(0).getVrs_id());
        List<Mts> mts = new ArrayList<>();
        map.put("members",mts);
        try {
			Params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//token
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/recorders",Params, meetInterface.getCookie());
        return result;
    }

	   //停止录像
	@Override
	public MeetingResult StopConfRecord(String conf_id, String rec_id,Integer recorder_mode, MeetInterface meetInterface){		
		//参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("recorder_mode",recorder_mode);
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/recorders/"+rec_id,params, meetInterface.getCookie());
		return result;
	}
   //获取画面合成信息
	@Override
	public MeetingResult ConfSynthesis(String conf_id,MeetInterface meetInterface){
	    //参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/vmps/1",params, meetInterface.getCookie());
        return result;
	}
	 //开启画面合成
	@Override
	public MeetingResult OpenConfSynthesis(OpenConfParam openConfParam, MeetInterface meetInterface){
	    //参数		
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(openConfParam),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		return	MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+openConfParam.getConf_id()+"/vmps",params, meetInterface.getCookie());

	}
	 //停止画面合成
	@Override
	public MeetingResult StopConfSynthesis(String conf_id, MeetInterface meetInterface){
	    //参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		//vmp_id默认为1
		return MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/vmps/1",params, meetInterface.getCookie());

	}
	
	//修改画面合成
	@Override
	public MeetingResult UpdatePictureSynthesis(OpenConfParam openConfParam, MeetInterface meetInterface){
	    //参数		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(openConfParam),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+openConfParam.getConf_id()+"/vmps/1",params, meetInterface.getCookie());
		return result;
	}
	//获取录像状态
	@Override
	public MeetingResult GetConfRecord(String conf_id, String rec_id, MeetInterface meetInterface){
	    //参数
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/recorders/"+rec_id,params, meetInterface.getCookie());  
        return result;
	}
	
	//修改录像状态
	@Override
	public MeetingResult UpdateConfRecord(String conf_id, String rec_id,Integer value, Integer recorder_mode, MeetInterface meetInterface){
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("value", value);
		map.put("recorder_mode", recorder_mode);
		params.put("account_token", meetInterface.getToken());
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/recorders/"+rec_id+"/state",params, meetInterface.getCookie());
        return result;
	}
	//获取录像列表
	@Override
	public MeetingResult GetConfRecordList(String conf_id, MeetInterface meetInterface){
	    //参数
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/recorders",params, meetInterface.getCookie());
        return result;
	}
	//获取用户域的vrs地址列表
	public MeetingResult GetVrs() throws Exception {
		MeetInterface meetInterface = (MeetInterface) request.getSession().getAttribute("meetInterface");
		//参数
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/vrs",params, meetInterface.getCookie());
		if(result == null) {
			log.error("会议平台调用失败");
			throw new Exception();
		}
		if(result.getSuccess() != 1) {
			log.error("获取用户域的vrs地址列表-会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
			throw new Exception();
		}      
        return result;
	}

	//延长会议时间
	@Override
	public MeetingResult DelayTime(String conf_id, String delay_time, MeetInterface meetInterface ){
	    //参数
	    Map<String, Object> params = new HashMap<String, Object>();
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("delay_time",delay_time);
		params.put("account_token", meetInterface.getToken());
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/delay",params, meetInterface.getCookie());
        return result;
	}

	//结束会议
	@Override
	public MeetingResult stopConfs(String conf_id, MeetInterface meetInterface){
	    //参数
	    Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/mc/confs/"+conf_id,params, meetInterface.getCookie());
        return result;
	}

	//发送短消息
	@Override
	public MeetingResult SendMessage(String conf_id, Integer type, Integer roll_num, Integer roll_speed, String message, List<String> mtsList, MeetInterface meetInterface){
		//参数
		Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message",message);
        map.put("roll_num",roll_num);
        map.put("roll_speed",roll_speed);
        map.put("type",type);
        List<Mts> ml = new ArrayList<>();
        if (mtsList != null && mtsList.size()!= 0){
            for(String id:mtsList){
                Mts mts = new Mts();
                mts.setMt_id(id);
                ml.add(mts);
            }
        }
        map.put("mts",ml);
		params.put("account_token", meetInterface.getToken());
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/sms",params, meetInterface.getCookie());
		return result;
	}

	//添加本级会议终端
	@Override
	public MeetingResult addThisConfTerminal(String confId, List<String> accountList, MeetInterface meetInterface){
		List<Terminal> mts = new ArrayList<>();
		for (String account: accountList){
		    Terminal terminal = new Terminal();
		    terminal.setAccount(account);
		    if(IpAdd.isIP(account)){
		    	terminal.setAccount_type(7);
		    }
		    mts.add(terminal);
        }
        //参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mts",mts);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mts",params, meetInterface.getCookie());
        return result;
	}

	//添加级联会议终端
	@Override
	public MeetingResult addOtherConfTerminal(String confId, String cascadeId, List<String> accountList, MeetInterface meetInterface){
        List<Terminal> mts = new ArrayList<>();
        for (String account: accountList){
            Terminal terminal = new Terminal();
            terminal.setAccount(account);
            if(IpAdd.isIP(account)){
		    	terminal.setAccount_type(7);
		    }
            mts.add(terminal);
        }
        //参数
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mts",mts);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/cascades/"+cascadeId+"/mts",params, meetInterface.getCookie());
		return result;
	}

	//获取该会议下的级联会议信息
    @Override
    public MeetingResult getConfCascades(String confId, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/cascades",params, meetInterface.getCookie());
        return result;
    }

    //混音设置
    @Override
    public MeetingResult openSoundMixing(Integer number, String confId, List<String> accountList,MeetInterface meetInterface){
       //参数
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        if(number == 1){
            //智能混音
            map.put("mode",number);
            List<Mts> mtsList = new ArrayList<>();
            map.put("members",mtsList);
        }else {
            //定制混音
            map.put("mode",number);
            List<Mts> mtsList = new ArrayList<>();
            for (String account:accountList){
                Mts mts = new Mts();
                mts.setMt_id(account);
                mtsList.add(mts);
            }
            map.put("members",mtsList);
        }
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mixs",params, meetInterface.getCookie());
        return result;
    }

    //添加混音成员
    @Override
    public MeetingResult addSmChannel(String confId, List<String> accountList, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        List<Mts> mtsList = new ArrayList<>();
        for (String account:accountList){
            Mts mts = new Mts();
            mts.setMt_id(account);
            mtsList.add(mts);
        }
        map.put("members",mtsList);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mixs/1/members",params, meetInterface.getCookie());
        return result;
    }

    //删除混音成员
    @Override
    public MeetingResult delSmChannel(String confId, List<String> accountList, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        List<Mts> mtsList = new ArrayList<>();
        for (String account:accountList){
            Mts mts = new Mts();
            mts.setMt_id(account);
            mtsList.add(mts);
        }
        map.put("members",mtsList);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mixs/1/members",params, meetInterface.getCookie());
        return result;
    }

    //会议轮询
    @Override
    public MeetingResult confPolling(String confId, Integer mode, Integer num, Integer keepTime, List<String> accountList, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        params.put("_method", "PUT");
        map.put("mode",mode);
        map.put("num",num);
        map.put("keep_time",keepTime);
        List<Mts> mtsList = new ArrayList<>();
        for (String account:accountList){
            Mts mts = new Mts();
            mts.setMt_id(account);
            mtsList.add(mts);
        }
        map.put("members",mtsList);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/poll",params, meetInterface.getCookie());
        return result;
    }
    
    
    //主席轮询
    @Override
    public MeetingResult confChairmanPolling(String confId, Integer mode, Integer num, Integer keepTime, List<String> accountList, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        map.put("mode",mode);
        map.put("num",num);
        map.put("keep_time",keepTime);
        List<Mts> mtsList = new ArrayList<>();
        for (String account:accountList){
            Mts mts = new Mts();
            mts.setMt_id(account);
            mtsList.add(mts);
        }
        map.put("members",mtsList);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman_poll",params, meetInterface.getCookie());
        return result;
    }
    
    

	//取消终端选看
	@Override
	public MeetingResult delWatchTerminal(String confId, String mt_id, MeetInterface meetInterface){
        //参数
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/inspections/"+mt_id+"/2", params, meetInterface.getCookie());
        result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/inspections/"+mt_id+"/1", params, meetInterface.getCookie());
        return result;
	}

	//获取终端选看列表
    @Override
    public MeetingResult getWatchTerminal(String confId, MeetInterface meetInterface){
       //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/inspections",params, meetInterface.getCookie());
    }

    //开启电视墙批量轮询
    @Override
    public MeetingResult openHdupoll(String confId, List<Channel> list, List<String> members, Integer keepTime, Integer num, Integer showMtName, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("mode",2);
        map.put("num",num);
        map.put("keep_time",keepTime);
        map.put("show_mt_name",showMtName);
        List<Hdus> hdu_id = new ArrayList<>();
        for (Channel c:list){
            Hdus hd = new Hdus();
            hd.setHdu_id(c.getChannelId());
            hdu_id.add(hd);
        }
        map.put("hdus",hdu_id);
        List<TargetSource> targetSourceList = new ArrayList<>();
        for (String me:members){
            TargetSource targetSource = new TargetSource();
            targetSource.setMt_id(me);
            targetSourceList.add(targetSource);
        }
        map.put("members",targetSourceList);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdupoll",params, meetInterface.getCookie());
        return result;
    }

    //修改电视墙批量轮询状态
    @Override
    public MeetingResult changeHdupollState(String confId,Integer value, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",value);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdupoll/state",params, meetInterface.getCookie());
        return result;
    }

	@Override
	public MeetingResult getMeet(MeetInterface meetInterface ,String e164){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+e164, params, meetInterface.getCookie());
        return result;
	}

	@Override
	public MeetingResult openHdu(String confId, String hduId, String mt_id, MeetInterface meetInterface) {
        //参数
        MeetingResult result = null;
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 1);
        
        Map<String, Object> specific = new HashMap<String, Object>();
        specific.put("member_type", "1");
        specific.put("mt_id", mt_id);
        params.put("specific", specific);
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult closeHdu(String confId, String hduId ,MeetInterface meetInterface) {
        //参数
        MeetingResult result = null;
        	Map<String, Object> map = new HashMap<String, Object>();
            map.put("account_token", meetInterface.getToken());
            result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus/"+hduId,map, meetInterface.getCookie());
        return result;
	}
	
	
	@Override
	public MeetingResult getHdus(String confId, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus", map, meetInterface.getCookie());
		return result;
	}

	@Override
	public MeetingResult getHdupoll(String confId,  MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdupoll", map, meetInterface.getCookie());
		return result;
	}
	
	
	//OCX发送监控请求(视频)
	@Override
	public MeetingResult videoMonitors(MeetInterface meetInterface, String confId, String ip, Integer v_port, String members) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mode", 0);
		
		Map<String, Object> src = new HashMap<String, Object>();
		src.put("type", 1);
		src.put("mt_id", members);
		map.put("src", src);
		
		Map<String, Object> video_format = new HashMap<String, Object>();
		video_format.put("format", 4);
		video_format.put("resolution", 3);
		video_format.put("frame", 30);
		video_format.put("bitrate", 8128);
		map.put("video_format", video_format);
		
		Map<String, Object> audio_format = new HashMap<String, Object>();
		audio_format.put("format", 5);
		audio_format.put("chn_num", 2);
		map.put("audio_format", audio_format);
		
		Map<String, Object> dst = new HashMap<String, Object>();
		dst.put("ip", ip);
		dst.put("port", v_port);
		map.put("dst", dst);
		
		try {
			params.put("params",  URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/monitors", params, meetInterface.getCookie());

		return result;
	}
	
	
	//OCX发送监控请求(音频)
	@Override
	public MeetingResult audioMonitors(MeetInterface meetInterface, String confId, String ip, Integer a_port,
			String members) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mode", 1);
		
		Map<String, Object> src = new HashMap<String, Object>();
		src.put("type", 1);
		src.put("mt_id", members);
		map.put("src", src);
		
		Map<String, Object> video_format = new HashMap<String, Object>();
		video_format.put("format", 4);
		video_format.put("resolution", 3);
		video_format.put("frame", 30);
		video_format.put("bitrate", 8128);
		map.put("video_format", video_format);
		
		Map<String, Object> audio_format = new HashMap<String, Object>();
		audio_format.put("format", 5);
		audio_format.put("chn_num", 2);
		map.put("audio_format", audio_format);
		
		Map<String, Object> dst = new HashMap<String, Object>();
		dst.put("ip", ip);
		dst.put("port", a_port);
		map.put("dst", dst);
		
		try {
			params.put("params",  URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/monitors", params, meetInterface.getCookie());

		return result;
	}
	
	
	//OCX获取监控信息
	@Override
	public Map<String,Object> getMonitors(MeetInterface meetInterface, String confId, String ip, Integer v_port) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		JSONObject jsonObject = MeetHttpClient.sendMapGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/monitors/"+ip+"/"+v_port, params, meetInterface.getCookie());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer success =  jsonObject.getInteger("success");
		if(jsonObject != null || success == 1){
			JSONObject src = jsonObject.getJSONObject("src");
			JSONObject rtcp = src.getJSONObject("rtcp");
			Integer port =  rtcp.getInteger("port");
			String v_ip =  rtcp.getString("ip");
			map.put("port", port);
			map.put("ip", v_ip);	
		}
		
		return map;
	}
	
	
	////OCX请求关键帧
	@Override
	public MeetingResult neediframe(Integer v_port, Integer a_port ,String ip, String confId, MeetInterface meetInterface) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		//发送关键帧
		params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> dst = new HashMap<String, Object>();
		dst.put("ip", ip);
		dst.put("port", v_port);
		map.put("dst", dst);
		
		try {
			params.put("params",  URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/neediframe/monitors", params, meetInterface.getCookie());
		
		return result;
	}
	
	
	
	//OCX心跳保活
	@Override
	public MeetingResult heartbeat(Integer v_port, Integer a_port, String ip, MeetInterface meetInterface, String confId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String, Object>> monitors = new ArrayList<Map<String, Object>>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ip", ip);
		map2.put("port", v_port);
		monitors.add(map2);
		map.put("monitors", monitors);
		
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/monitors_heartbeat", params, meetInterface.getCookie());
		return result;
	}

	@Override
	public MeetingResult closeHdupoll(String confId, MeetInterface meetInterface) {
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",0);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdupoll/state",params, meetInterface.getCookie());
        return result;
	}
     //判断是否有级联
	@Override
	public MeetingResult getIsJl(MeetInterface meetInterface, String meetid){
		 Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        //获取会议级联信息
        MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+meetid+"/cascades",params, meetInterface.getCookie());
        return result;
	}
   //全场哑音
	@Override
	public MeetingResult Mute(String conf_id,int value, MeetInterface meetInterface){
		  //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",value);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/mute",params, meetInterface.getCookie());
        return result;
	}
  //全场静音
	@Override
	public MeetingResult KeepQuiet(String conf_id,int value, MeetInterface meetInterface){
		  //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",value);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/silence",params, meetInterface.getCookie());
        return result;
	}
	//呼叫所有离线
	@Override
	public MeetingResult CallOffLine(String conf_id, MeetInterface meetInterface) {
		  //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        //调用查询离线终端接口
        List<String> mtsList = getmt_id(meetInterface,conf_id);      
        List<Mts> listmts = new ArrayList<Mts>();
        for (String str : mtsList) {
			Mts mts = new Mts(str,1);
			listmts.add(mts);
		}
        map.put("mts",listmts);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        //请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/online_mts",params, meetInterface.getCookie());
        return result;
	}

	//调用查询离线终端接口
    public List<String> getmt_id(MeetInterface meetInterface,String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());                   
        //获取会议级联信息
        MeetingResult total = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+id+"/cascades",params, meetInterface.getCookie());
        //最高一级
        List<String> mt_ids = new ArrayList<String>();
        List<Cascades> listcascades = total.getCascades();//最高级的孩子级
        //查询本级会议终端列表
        MeetingResult childrensResult = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+id+"/mts",params, meetInterface.getCookie());
       if(childrensResult.getMts()!= null && childrensResult.getMts().size()>0){
    	    for (Mts mts : childrensResult.getMts()) {
            	if(mts.getType() == 1){//type为1 表示终端用户
            		if(mts.getOnline() == 0){
            			mt_ids.add(mts.getMt_id());
            		}            		
            	}else if(mts.getType() == 7 && mts.getOnline() == 0){
            		mt_ids.add(mts.getMt_id());	
            	}
    		} 
       }    
        //调用递归方法,查询下级终端
         mt_ids = getChilds(meetInterface,listcascades,total.getConf_id(),mt_ids);
        return mt_ids;
    }
  //查询离线终端递归循环的通用方法
  	public List<String> getChilds(MeetInterface meetInterface, List<Cascades> listcascades,String conf_id,List<String> childList) {
  	    // 子菜单
  	    for (Cascades tree : listcascades) {
  	    	 List<Cascades> listcascade = tree.getCascades();  	     	    	
  	        // 遍历所有节点，将父菜单id与传过来的id比较
  	         Map<String, Object> params = new HashMap<String, Object>();
  	         params.put("account_token", meetInterface.getToken());
  	         //插入级联查询的终端
  	         MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/cascades/"+tree.getCascade_id()+"/mts",params, meetInterface.getCookie());
  	         if(result.getMts()!= null && result.getMts().size()>0){
  	           for(Mts mts : result.getMts()){
    	        	 if(mts.getType() == 1 && mts.getOnline() == 0){
    	        		 childList.add(mts.getMt_id());
    	        	 }else if(mts.getType() == 7 && mts.getOnline() == 0){
    	        		 childList.add(mts.getMt_id());	
    	        	}
    	         }   
  	         }
  	         //判断级联是否为空
  	         if(listcascade.size() > 0){
  	        	 //将级联查询的信息插入
  	        	 childList = getChilds(meetInterface,listcascade,conf_id,childList);
  	         }
  	    }
  	    return childList;
  	}  

	//获取会议混音信息
	@Override
	public MeetingResult getMixMesg (String confId, MeetInterface meetInterface){
		//参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mixs/1",params, meetInterface.getCookie());
		return result;
	}
	
	
	@Override
	public MeetingResult getChairman(String confId, MeetInterface meetInterface){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        //获取管理方
        MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman",params, meetInterface.getCookie());
        return result;
	}

	//修改轮询状态
    @Override
    public MeetingResult setPollState(String confId,Integer value, MeetInterface meetInterface ){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",value);
        map.put("mode",3);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/poll/state",params, meetInterface.getCookie());
        return result;
    }
    
    
	//修改主席轮询状态
    @Override
    public MeetingResult setChairmanPollState(String confId,Integer value, MeetInterface meetInterface ,Integer mode){
        //参数
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("value",value);
        map.put("mode",mode);
        try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        MeetingResult result = MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman_poll/state",params, meetInterface.getCookie());
        return result;
    }

	@Override
	public MeetingResult callMtId(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> mts = new ArrayList<Map<String, Object>>();
		Map<String, Object> mt = new HashMap<String, Object>();
		mt.put("mt_id", mt_id);
		mt.put("forced_call", 1);
		mts.add(mt);
		params.put("mts", mts);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/online_mts", map, meetInterface.getCookie());
	}

	@Override
	public MeetingResult hangUpMtId(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> mts = new ArrayList<Map<String, Object>>();
		Map<String, Object> mt = new HashMap<String, Object>();
		mt.put("mt_id", mt_id);
		mts.add(mt);
		params.put("mts", mts);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/online_mts", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult openSilence(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("value", 1);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mts/"+mt_id+"/silence", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult closeSilence(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("value", 0);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mts/"+mt_id+"/silence", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult appointSpeaker(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", mt_id);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/speaker", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult cancelSpeaker(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", "");
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/speaker", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult appointChairman(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", mt_id);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult cancelChairman(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", "");
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult appointDualstream(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", mt_id);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/dualstream", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult cancelDualstream(String confId, String mt_id, MeetInterface meetInterface) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mt_id", mt_id);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/dualstream", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult getMeetChairman(MeetInterface meetInterface, String conf_id, List<CascadeMts> children) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        String chairman = "";
        
        //获取管理方
       return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/chairman",params, meetInterface.getCookie());
	}
	

	@Override
	public MeetingResult getMeetSpeaker(MeetInterface meetInterface, String conf_id) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/speaker",params, meetInterface.getCookie());
	}
	
    //批量删除终端
	@Override
	public MeetingResult deleteMts(String conf_id,List<String> mtsList, MeetInterface meetInterface){
	        Map<String, Object> Params = new HashMap<String, Object>();
	        Params.put("account_token", meetInterface.getToken());//token
	        Map<String, Object> map = new HashMap<String, Object>();	   
	        List<Mts> mts = new ArrayList<>();
	        if (mtsList != null && mtsList.size()!= 0){
	            for(String id:mtsList){
	                Mts mt = new Mts(id);
	                mts.add(mt);
	            }
	        }
	        map.put("mts",mts);
	        try {
				Params.put("params", URLEncoder.encode(JSON.toJSONString(map),"UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}//token
	        MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/mts",Params, meetInterface.getCookie());
	        return result;
	}

	//关闭混音
	@Override
	public MeetingResult closeSoundMixing(String confId, MeetInterface meetInterface){
        Map<String, Object> Params = new HashMap<String, Object>();
        Params.put("account_token", meetInterface.getToken());//token
        MeetingResult result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/mixs/1",Params, meetInterface.getCookie());
        return result;
	}

	//获取会议轮询信息
	@Override
	public MeetingResult getPollDetails(String confId, MeetInterface meetInterface) {
		Map<String, Object> Params = new HashMap<String, Object>();
		Params.put("account_token", meetInterface.getToken());//token
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/poll",Params, meetInterface.getCookie());
		return result;
	}
	
	//获取会议主席轮询信息
	@Override
	public MeetingResult getChairmanPollDetails(String confId, MeetInterface meetInterface) {
		Map<String, Object> Params = new HashMap<String, Object>();
		Params.put("account_token", meetInterface.getToken());//token
		MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/chairman_poll",Params, meetInterface.getCookie());
		return result;
	}
	
	

	//终端在线排序
	  private  void  mycomparator(List<CascadeMts> Lists) {
		   Collections.sort(Lists, new Comparator<CascadeMts>() {
		      @Override 
		     public int compare(CascadeMts  o1,CascadeMts o2) {
		         try {
		            if (o1.getOnline()< o2.getOnline()) {
		               return 1;
		            } else if (o1.getOnline() > o2.getOnline()) {
		               return -1;
		            } else {
		               return 0;
		            }
		         } catch (Exception e) {
		            e.printStackTrace();
		         }
		         return 0;

		      }
		   });
		}

	@Override
	public MeetingResult setWatchTerminal(String confId, MeetInterface meetInterface, String src, String dst, Integer mode) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());//token
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mode", mode);
		
		Map<String, Object> srcmt = new HashMap<String, Object>();
		srcmt.put("type", 1);
		srcmt.put("mt_id", src);
		params.put("src", srcmt);
		
		Map<String, Object> dstmt = new HashMap<String, Object>();
		dstmt.put("mt_id", dst);
		params.put("dst", dstmt);
		try {
			map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        return MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/inspections",map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult delWatchTerminalAll(String confId, MeetInterface meetInterface,List<TerminalWatch> list) {
		MeetingResult result = null;
		if(list != null && list.size() > 0){
			for (TerminalWatch terminalWatch : list) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("account_token", meetInterface.getToken());//token
				result = MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/inspections/"+terminalWatch.getDst().getMt_id()+"/"+terminalWatch.getMode()+"", map, meetInterface.getCookie());
			}
		}
		return result;
	}

	@Override
	public MeetingResult getVrsParams(MeetInterface meetInterface) {
        Map<String, Object> vrsParams = new HashMap<String, Object>();
        vrsParams.put("account_token", meetInterface.getToken());//token
        return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/vrs",vrsParams, meetInterface.getCookie());
	}
	
	
	@Override
	public MeetingResult openHduVmps(MeetInterface meetInterface, OpenConfParam openConfParam) {
		String conf_id = openConfParam.getConf_id();
		openConfParam.setConf_id(null);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(openConfParam),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		return	MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/hduvmps",params, meetInterface.getCookie());

	}

	@Override
	public MeetingResult getHduVmps(MeetInterface meetInterface, String conf_id, String hdu_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/hduvmps/"+hdu_id, params, meetInterface.getCookie());
	}
	
	
	@Override
	public MeetingResult cancelHduVmps(MeetInterface meetInterface, String conf_id, String hdu_id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		return MeetHttpClient.sendDelete(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/hduvmps/"+hdu_id, params, meetInterface.getCookie());
	}
	
	
	//根据会议号和电视墙通道获取通道信息
	@Override
	public HduResult getHdu(MeetInterface meetInterface, String confId, String channelId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		return MeetHttpClient.sendHduGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus/"+channelId, params, meetInterface.getCookie());
	}

	@Override
	public MeetingResult openHduChairman(String confId, String hduId, MeetInterface meetInterface) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 1);
        
        Map<String, Object> specific = new HashMap<String, Object>();
        specific.put("member_type", "3");
        specific.put("mt_id", "");
        params.put("specific", specific);
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult openHduSpeaker(String confId, String hduId, MeetInterface meetInterface) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 1);
        
        Map<String, Object> specific = new HashMap<String, Object>();
        specific.put("member_type", "2");
        specific.put("mt_id", "");
        params.put("specific", specific);
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult openHduDualstream(String confId, String hduId, MeetInterface meetInterface) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 1);
        
        Map<String, Object> specific = new HashMap<String, Object>();
        specific.put("member_type", "10");
        params.put("specific", specific);
        specific.put("mt_id", "");
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult openHduMeetPoll(String confId, String hduId, MeetInterface meetInterface) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 1);
        
        Map<String, Object> specific = new HashMap<String, Object>();
        specific.put("member_type", "4");
        params.put("specific", specific);
        specific.put("mt_id", "");
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult openHduChannelPoll(String confId, String hduId, MeetInterface meetInterface, Integer num,
			Integer keepTime, List<String> accountList) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 3);
        
        Map<String, Object> poll = new HashMap<String, Object>();
        poll.put("num", num);
        poll.put("keep_time", keepTime);
        poll.put("mode", 3);
        poll.put("num", num);
        List<Mts> mtsList = new ArrayList<>();
        for (String account:accountList){
            Mts mts = new Mts();
            mts.setMt_id(account);
            mtsList.add(mts);
        }
        poll.put("members",mtsList);
        params.put("poll", poll);
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult openHduSplitScreen(String confId, String hduId, MeetInterface meetInterface,
			List<String> accountList, List<Integer> chnIdxList) {
        //参数
    	Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> params = new HashMap<String, Object>();
        map.put("account_token", meetInterface.getToken());
        params.put("hdu_id", hduId);
        params.put("mode", 2);
        
        Map<String, Object> spilt = new HashMap<String, Object>();
        List<Member> mtsList = new ArrayList<>();
        for (int i = 0; i < accountList.size(); i++) {
        	Member mts = new Member();
            mts.setMt_id(accountList.get(i));
            mts.setChn_idx(chnIdxList.get(i));
            mtsList.add(mts);
		}

        spilt.put("members",mtsList);
        params.put("spilt", spilt);
        try {
        	map.put("params", URLEncoder.encode(JSON.toJSONString(params),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
        MeetingResult result = MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/vc/confs/"+confId+"/hdus",map, meetInterface.getCookie());
    
        return result;
	}

	@Override
	public MeetingResult getHduVmpsMts(MeetInterface meetInterface, String conf_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/mtvmps", map, meetInterface.getCookie());
	}

	@Override
	public MeetingResult getHduVmpsHdus(MeetInterface meetInterface, String conf_id) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("account_token", meetInterface.getToken());
		return MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/hduvmps", map, meetInterface.getCookie());

	}

	@Override
	public MeetingResult updateHduVmps(MeetInterface meetInterface, OpenConfParam openConfParam) {
		String conf_id = openConfParam.getConf_id();
		openConfParam.setConf_id(null);
		String hdu_id = openConfParam.getHdu_id();
		openConfParam.setHdu_id(null);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());//token
		try {
			params.put("params", URLEncoder.encode(JSON.toJSONString(openConfParam),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}//请求内容，以JSON形式发送，需进行UrlEncode
		return	MeetHttpClient.sendPut(env.getProperty("meetIp")+"/api/v1/vc/confs/"+conf_id+"/hduvmps/"+hdu_id,params, meetInterface.getCookie());

	} 
}
