package com.auxing.znhy.common;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.entity.Meet;
import com.auxing.znhy.mapper.ChannelMapper;
import com.auxing.znhy.mapper.DomainuserMapper;
import com.auxing.znhy.mapper.MeetMapper;
import com.auxing.znhy.mapper.MeetroomMapper;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;
import com.auxing.znhy.util.meeting.Meeting;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Configuration
public class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent>{
	
	
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			try {
				MeetMapper meetMapper = (MeetMapper) event.getApplicationContext().getBean("meetMapper");
				DomainuserMapper domainuserMapper = (DomainuserMapper) event.getApplicationContext().getBean("domainuserMapper");
				ChannelMapper channelMapper = (ChannelMapper) event.getApplicationContext().getBean("channelMapper");
				MeetroomMapper meetroomMapper = (MeetroomMapper) event.getApplicationContext().getBean("meetroomMapper");
				Environment env = event.getApplicationContext().getEnvironment();
				List<Meet> meets = meetMapper.getMeetListByStatus();
				domainuserMapper.updateAllSubsription();
				if(meets != null){
					for (Meet meet : meets) {
						Boolean falg = true;
						Domainuser domainuser = new Domainuser();
						domainuser.setMoid(meet.getUserDomain());
						QueryWrapper<Domainuser> queryWrapper = new QueryWrapper<Domainuser>(domainuser);
						domainuser = domainuserMapper.selectOne(queryWrapper);
						MeetInterface meetInterface = null;
						try {
							meetInterface = new MeetInterface(domainuser.getUsername(), domainuser.getPassword(), env.getProperty("meetIp"), env.getProperty("oauthConsumerKey"), env.getProperty("oauthConsumerSecret"));
						} catch (Exception e) {
							e.printStackTrace();
						}
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("account_token", meetInterface.getToken());
						map.put("start", 0);
						map.put("count", 0);
						
						MeetingResult result = MeetHttpClient.sendGet(env.getProperty("meetIp")+"/api/v1/vc/confs", map, meetInterface.getCookie());
						if(result != null && result.getSuccess() == 1){
							List<Meeting> confs = result.getConfs();
							if(confs != null && confs.size() > 0){
								for (Meeting meeting : confs) {
									if(meeting.getName().equals(meet.getTheme()) && meeting.getConf_id().equals(meet.getMeetNumber())){
										falg = false;
										subscribe(meet.getUserDomain(),domainuserMapper,env);
									}
								}
							}
							
							if(falg){
								meetMapper.updateStatus(meet.getId(),0);
								channelMapper.empty(meet.getMeetNumber());
								meetroomMapper.updateStatus(Integer.parseInt(meet.getMeetRoom()));
							}
								
						}
						
					}			
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void subscribe(String domainId, DomainuserMapper domainuserMapper, Environment env){
		Domainuser domainuser = new Domainuser();
		domainuser.setSubsription(0);
		domainuser.setMoid(domainId);
		QueryWrapper<Domainuser> wrapper = new QueryWrapper<Domainuser>(domainuser);
		Domainuser domain = domainuserMapper.selectOne(wrapper);
		
		if(domain != null){
			//当前用户域没有进行订阅
			String svrIp = env.getProperty("meetIp");
			
			CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
			Httpmsg httpMsg = new Httpmsg();
			httpMsg.SetAppId(env.getProperty("oauthConsumerKey"));
			httpMsg.SetAppKey(env.getProperty("oauthConsumerSecret"));
			httpMsg.SetUserName(domain.getUsername());
			httpMsg.SetPassWord(domain.getPassword());
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
			MeetingPush push = new MeetingPush(token, bayeuxclient, svrIp, jdbcDriver, jdbcUrl, jdbcUserName, jdbcPassword, oauthConsumerKey, oauthConsumerCecret, ext);
			push.start();
			domainuserMapper.updateSubsription(domainId,1);
		}		
	}
	

}
