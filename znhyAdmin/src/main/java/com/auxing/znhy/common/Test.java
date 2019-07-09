package com.auxing.znhy.common;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.cometd.websocket.client.JettyWebSocketTransport;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liuxiaolin on 2016/11/28.
 */


public class Test {

	private static String svrIp; // 服务器ip
	private static String domain_id; // 用户域id
	private static String token; // 获取的token
	private static List<String> channlList; // 通道列表，用来存已订阅的通道
	private static BayeuxClient bayeuxclient;
	private static ClientSessionChannel.MessageListener msglistener; // 回调订阅内容
	private static ClientSessionChannel.MessageListener sublistener; // 回调订阅是否成功

	public static void init() {
		svrIp = "http://112.4.82.115";
		channlList = new ArrayList<String>();

		// 创建一个CookieManager，设置接受所有的Cookie
		CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
	}

	public static void main(String[] args) throws Exception {
		String str = " SSO_COOKIE_KEY=f3534d4f-5c10-4f67-a7ee-6b302c75fae6";
		System.out.println(str.substring(16, str.length()));
		 
		init();

		// httpmsg用来发送http消息
		Httpmsg httpMsg = new Httpmsg();
		httpMsg.SetAppId("test");
		httpMsg.SetAppKey("test");
		httpMsg.SetUserName("admin1");
		httpMsg.SetPassWord("keda8888");

		// 登录
		token = httpMsg.login(svrIp);
		System.out.println("get token : " + token);

		// 获取cookiekey
		CookieManager handle = (CookieManager) CookieHandler.getDefault();
		CookieStore store = handle.getCookieStore();
		List<HttpCookie> lstCookie = store.getCookies();

		System.out.println(lstCookie);
		
		
		msglistener = new MsgListener();
		sublistener = new SubListener();
		
		HttpClient httpclient = new HttpClient();
		httpclient.start();
		ClientTransport httptransport = new LongPollingTransport(null,httpclient);
		ClientSessionChannel.MessageListener hslistener = new HshakeListener();

		String url = svrIp + "/api/v1/publish";
		boolean bHttp = true;
		if (bHttp) {
			bayeuxclient = new BayeuxClient(url, httptransport);
		} else{// WebSocket
			WebSocketClient webSocketClient = new WebSocketClient();
			webSocketClient.start();
			JettyWebSocketTransport wsTransport = new JettyWebSocketTransport(null, null, webSocketClient);
			bayeuxclient = new BayeuxClient(url, wsTransport, httptransport);
		}
		System.out.println(lstCookie);
		for(HttpCookie cookie : lstCookie) {
			System.out.println("get cookie： " + cookie);
			bayeuxclient.putCookie(cookie); // 携带cookiekey
		}
		
		
		
		bayeuxclient.putCookie(lstCookie.get(2));
		bayeuxclient.handshake(hslistener); // 握手
		
		while(true){
			url = "http://" + svrIp + "/api/v1/system/heartbeat";
			String param = "account_token=" + token;
			httpMsg.HttpPostMsg(url, param);
			Thread.sleep(30 * 60 * 1000);		
		}
		


		// 心跳保护


	}

	// 握手回调
	private static class HshakeListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			if (message.isSuccessful()) {
				// 握手成功，获取domain_id
				Map<String, Object> map = message.getExt();
				domain_id = map.get("user_domain_moid").toString();
				System.out.println("userdomain_id: " + domain_id);
				System.out.println("handshanke success!");

				// 订阅通道，若是断链重连后握手成功，同时重订阅channlList中保存的通道
				// String subchannl = "/userdomains/" + domain_id +
				// "/confs/7773217/vmps/1";
				
				
				String subchannl = "/userdomains/" + domain_id + "/confs/**";
				
				
				bayeuxclient.getChannel(subchannl).subscribe(msglistener, sublistener);
				
				
/*				String subchannl = "/userdomains/" + domain_id + "/eqp/hdus";
				String subchannl2 = "/userdomains/" + domain_id
						+ "/eqp/hdus/23_0";
				String subchannl3 = "/userdomains/" + domain_id
						+ "/eqp/hdus/23_1";
				String subchannl4 = "/userdomains/" + domain_id
						+ "/eqp/hdus/44_0";
				String subchannl5 = "/userdomains/" + domain_id
						+ "/eqp/hdus/44_1";

				channlList.add(subchannl);
				channlList.add(subchannl2);
				channlList.add(subchannl3);
				channlList.add(subchannl4);
				channlList.add(subchannl5);

				for (int i = 0; i < channlList.size(); i++) {
					subchannl = channlList.get(i).toString();
					bayeuxclient.getChannel(subchannl).subscribe(msglistener,sublistener);
				}*/
			} else {
				System.out.println("handshake failed!");
			}
		}
	}

	// 订阅是否成功
	private static class SubListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			if (message.isSuccessful()) {
				System.out.println("Subsription successful!");

				// 获取订阅的通道并保存
				String strChannel = message.get("subscription").toString();
				channlList.add(strChannel);
			} else {
				System.out.println("Subsription failed!");
			}
		}
	}

	// 订阅消息内容
	private static class MsgListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			// Here you received a message on the channel
			System.out.println("subcontent: " + message.getJSON());

			System.out.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date()));

			// 获取订阅返回的通道消息
			String strChannel = message.getChannel();
			Map<String, Object> data = message.getDataAsMap();
			String method = data.get("method").toString();

			System.out.println("subscribe channl: " + strChannel);
			System.out.println("subscribe method: " + method);

			String subChannel = strChannel;
			// 获取第三个“/”后面的内容,去掉域信息的通道
			// eg:/userdomains/xxx/xxx/xxx/xxx
			for (int i = 0; i < 3; i++) {
				subChannel = subChannel.substring(subChannel.indexOf("/") + 1);
			}
			subChannel = "/" + subChannel;
			System.out.println("str: " + subChannel);

		}
	}

	// 发送http请求
	public static void SendHttpGetMsg(String channel) throws Exception {
		String url = "http://" + svrIp + "/api/v1/vc" + channel;
		String param = "account_token=" + token;
		Httpmsg httpMsg = new Httpmsg();
		String result = httpMsg.HttpGetMsg(url, param);
		System.out.println("Http get msg info : " + result);
	}
	
	

}
