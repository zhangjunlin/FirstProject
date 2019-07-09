package com.auxing.znhy.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.auxing.znhy.common.Httpmsg;
import com.auxing.znhy.common.MeetingPush;
import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.entity.Meetroom;
import com.auxing.znhy.entity.Template;
import com.auxing.znhy.entity.User;
import com.auxing.znhy.mapper.MeetMapper;
import com.auxing.znhy.service.IDomainuserService;
import com.auxing.znhy.service.IMeetService;
import com.auxing.znhy.service.IUserService;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.meeting.CreateMeeting;
import com.auxing.znhy.util.meeting.MeetingUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auxing
 * @since 2018-09-17
 */
@Slf4j
@Service
public class MeetServiceImpl extends ServiceImpl<MeetMapper, Meet> implements IMeetService {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private IDomainuserService domainService;
	
	@Resource
	private IUserService userService;
	
    @Resource
    private MeetMapper meetMapper;
	
	
	@Override
	public MeetingResult createMeet(User user, Template template ,String fictitiousId,MeetInterface meetInterface, String ids){
		CreateMeeting createMeeting = new CreateMeeting();
		createMeeting.setTemplate_id(fictitiousId);
		createMeeting.setName(template.getTName());					//会议主题
		createMeeting.setDuration(template.getDuration());			//会议时长
		createMeeting.setPassword(template.getTPassword());			//会议密码
		
		if(template.getSpeaker() != null && !"".equals(template.getSpeaker())){							//发言人
			MeetingUser speaker = new MeetingUser();	
			User u = new User();
			u.setMoid(template.getSpeaker());
			QueryWrapper<User> wrapper = new QueryWrapper<User>(u);
			User speakerUser = userService.getOne(wrapper);
			if(speakerUser != null){
				speaker.setName(speakerUser.getAccount());
				speaker.setAccount(speakerUser.getE164());
				speaker.setAccount_type(5);
				createMeeting.setSpeaker(speaker);
			}
			
		}
		
		if(template.getChairMan() != null && !"".equals(template.getChairMan())){
			MeetingUser chairman = new MeetingUser();				//主席
			User u = new User();
			u.setMoid(template.getChairMan());
			QueryWrapper<User> wrapper = new QueryWrapper<User>(u);
			User chairmanUser = userService.getOne(wrapper);
			if(chairmanUser != null){
				chairman.setName(chairmanUser.getAccount());
				chairman.setAccount(chairmanUser.getE164());
				chairman.setAccount_type(5);
				createMeeting.setChairman(chairman);
			}
			
		}
		
		List<MeetingUser> invite_members = new ArrayList<MeetingUser>();	//参会人
		List<String> list = java.util.Arrays.asList(ids.split(","));
		for (String id : list) {
			User u = new User();
			u.setMoid(id);
			QueryWrapper<User> queryWrapper = new QueryWrapper<User>(u);
			u = this.userService.getOne(queryWrapper);
			if(u!= null){
				invite_members.add(new MeetingUser(u.getAccount(),u.getE164(),5,0));
			}else {
				invite_members.add(new MeetingUser(id,id,5,0));
			}
			
		}
		createMeeting.setInvite_members(invite_members);
		
		String json = JSON.toJSONString(createMeeting);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account_token", meetInterface.getToken());
		try {
			params.put("params", URLEncoder.encode(json,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return MeetHttpClient.sendPost(env.getProperty("meetIp")+"/api/v1/mc/confs", params, meetInterface.getCookie());
	}




	@Override
	public void saveMeet(Meet meet) {
		meetMapper.saveMeet(meet);
	}


	@Override
	public void saveMeetUser(Map<String, Object> meetUserMap) {
		meetMapper.saveMeetUser(meetUserMap);
	}

	@Override
	public List<Meet> getMeetList(String userId) {
		List<Meet> mList = meetMapper.getMeetListById(userId);
		return mList;
	}


	@Override
	public String getResolutionStr1(int resolution) {
    	switch (resolution) {
		case 1:
			return "QCIF";
		case 2:
			return "CIF";
		case 3:
			return "标清";
		case 12:
			return "高清";
		case 13:
			return "超清";
		case 14:
			return "WCIF";
		case 15:
			return "W4CIF";
		case 16:
			return "4k";
    	}
		return null;
    
	}
	
	@Override
	public String getResolutionStr(int resolution) {
    	switch (resolution) {
		case 1:
			return "QCIF";
		case 2:
			return "CIF";
		case 3:
			return "4CIF";
		case 12:
			return "720P";
		case 13:
			return "1080P";
		case 14:
			return "WCIF";
		case 15:
			return "W4CIF";
		case 16:
			return "4k";
    	}
		return null;
    
	}

	@Override
	public List<Meet> getMrUseDetails(String year, String month, List<Meetroom> list) {
		return meetMapper.getMrUseDetails(year,month, list);
	}


	@Override
	public MeetingResult getResources(User loginUser) throws Exception {
		MeetInterface meetInterface = new MeetInterface(loginUser.getAccount(),loginUser.getPassword(), env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("account_token", meetInterface.getToken());
        params.put("count",0);
        params.put("order",0);
        params.put("start",0);
		
        MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/mc/virtual_meeting_rooms",params, meetInterface.getCookie());
        
		if(result == null || result.getSuccess() != 1) {
			log.error("会议平台调用失败:error_code="+result.getError_code()+";msg="+result.getDescription());
			throw new Exception();
		}
		return result;
	}

	@Override
	public Meet getDetailsByMeetNumber(String meetId) {
		return meetMapper.getDetailsByMeetNumber(meetId);
	}
	
	@Override
	public synchronized void synchronizedSubscribe(User loginUser){
		Domainuser domainuser = new Domainuser();
		domainuser.setSubsription(0);
		domainuser.setMoid(loginUser.getDomainId());
		QueryWrapper<Domainuser> wrapper = new QueryWrapper<Domainuser>(domainuser);
		Domainuser domain = domainService.getOne(wrapper);
		
		if(domain != null){
			//当前用户域没有进行订阅
			String svrIp = env.getProperty("meetIp");
			
			CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
			Httpmsg httpMsg = new Httpmsg();
			httpMsg.SetAppId(env.getProperty("oauthConsumerKey"));
			httpMsg.SetAppKey(env.getProperty("oauthConsumerSecret"));
			httpMsg.SetUserName(loginUser.getAccount());
			httpMsg.SetPassWord(loginUser.getPassword());
			String token = null;
			try {
				token = httpMsg.login(svrIp);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String url = svrIp + "/api/v1/publish";
			HttpClient httpclient = new HttpClient();
			try {
				httpclient.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
			ClientTransport httptransport = new LongPollingTransport(null,httpclient);
			BayeuxClient bayeuxclient = new BayeuxClient(url, httptransport);
			CookieManager handle = (CookieManager) CookieHandler.getDefault();
			CookieStore store = handle.getCookieStore();
			List<HttpCookie> lstCookie = store.getCookies();
			Map<String, Object> ext = new HashMap<String, Object>();
			Map<String, Object> cookies = new HashMap<String, Object>();
			for (HttpCookie httpCookie : lstCookie) {
				bayeuxclient.putCookie(httpCookie);
				cookies.put(httpCookie.getName(), httpCookie.getValue());
			}
			ext.put("ext", cookies);
			String jdbcDriver = env.getProperty("jdbcDriver");
			String jdbcUrl = env.getProperty("jdbcUrl");
			String jdbcUserName = env.getProperty("jdbcUserName");
			String jdbcPassword = env.getProperty("jdbcPassword");
			String oauthConsumerKey = env.getProperty("oauthConsumerKey");
			String oauthConsumerCecret = env.getProperty("oauthConsumerSecret");
			
			MeetingPush push = new MeetingPush(token, bayeuxclient, svrIp, jdbcDriver, jdbcUrl, jdbcUserName, jdbcPassword,oauthConsumerKey, oauthConsumerCecret,ext);
			push.start();
			
			domainuser = new Domainuser();
			domainuser.setMoid(loginUser.getDomainId());
			QueryWrapper<Domainuser> queryWrapper = new QueryWrapper<Domainuser>(domainuser);
			domain = domainService.getOne(queryWrapper);
			domain.setSubsription(1);
			domainService.update(domain, queryWrapper);
		}		
	}

	@Override
	public Meet selectByMeetNum(String meetId) {
		return meetMapper.selectByMeetNum(meetId);
	}

	@Override
	public List<Meet> getMrLists(String startTime, String endTime, List<Meetroom> list) {
		return meetMapper.getMrLists(startTime, endTime, list);
	}




	@Override
	public long selectTotalCount(Meet meet, String meetRoom) {
		return meetMapper.selectTotalCount(meet, meetRoom);
	}


	@Override
	public List<Meet> page(Page<Meet> page, Meet meet, String meetRoom) {
		return meetMapper.page(page,meet, meetRoom);
	}	
}
