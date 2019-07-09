package com.auxing.znhy.common;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.ClientTransport;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

import com.auxing.znhy.entity.Domainuser;
import com.auxing.znhy.util.MeetHttpClient;
import com.auxing.znhy.util.MeetInterface;
import com.auxing.znhy.util.MeetingResult;



public class MeetingPush extends Thread{
	
	private String token; // 获取的token
	private ClientSessionChannel.MessageListener msglistener; // 回调订阅内容
	private ClientSessionChannel.MessageListener sublistener; // 回调订阅是否成功
	private Boolean isStatus = true;
	private BayeuxClient bayeuxclient;
	private String domain_id;
	private String svrIp;
	private String oauthConsumerKey;
	private String oauthConsumerCecret;
	private Map<String,Object> map;
	
	public static String jdbcDriver;
	public static String jdbcUrl;
	public static String jdbcUserName;
	public static String jdbcPassword;
	
	
	

	public MeetingPush() {
	}

	public MeetingPush(String token, BayeuxClient bayeuxclient, String svrIp, String jdbcDriver, String jdbcUrl,
			String jdbcUserName, String jdbcPassword,String oauthConsumerKey ,String oauthConsumerCecret, Map<String, Object> map) {
		this.token = token;
		this.bayeuxclient = bayeuxclient;
		this.svrIp = svrIp;
		this.oauthConsumerKey = oauthConsumerKey;
		this.oauthConsumerCecret = oauthConsumerCecret;
		MeetingPush.jdbcDriver = jdbcDriver;
		MeetingPush.jdbcUrl = jdbcUrl;
		MeetingPush.jdbcUserName = jdbcUserName;
		MeetingPush.jdbcPassword = jdbcPassword;
		this.map = map;
	}





	// 握手回调
	private class HshakeListener implements ClientSessionChannel.MessageListener {
		public synchronized void onMessage(ClientSessionChannel channel, Message message) {
			if (message.isSuccessful()) {
				// 握手成功，获取domain_id
				Map<String, Object> map = message.getExt();
				domain_id = map.get("user_domain_moid").toString();
				System.err.println("userdomain_id: " + domain_id);
				System.err.println("handshanke success!");
				
				String subchannl = "/userdomains/" + domain_id + "/confs/**";
				
				bayeuxclient.getChannel(subchannl).subscribe(msglistener, sublistener);
			} else {
				System.err.println("handshake failed!");
			}
		}
	}

	// 订阅是否成功
	private class SubListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			if (message.isSuccessful()) {
				System.err.println("Subsription successful!");

				// 获取订阅的通道并保存
				String strChannel = message.get("subscription").toString();
				System.err.println(strChannel);
			} else {
				System.err.println("Subsription failed!");
			}
		}
	}

	// 订阅消息内容
	private class MsgListener implements ClientSessionChannel.MessageListener {
		public void onMessage(ClientSessionChannel channel, Message message) {
			// Here you received a message on the channel
			System.err.println("subcontent: " + message.getJSON());

			System.err.println(new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss:SSS").format(new Date()));

			// 获取订阅返回的通道消息
			String strChannel = message.getChannel();
			Map<String, Object> data = message.getDataAsMap();
			String method = data.get("method").toString();

			System.err.println("subscribe channl: " + strChannel);
			System.err.println("subscribe method: " + method);

			String[] str = strChannel.split("/");
			
			if(method.equals("delete") && str.length == 5){
				//修改会议状态
				String id = selectRoom(str[4]);
				updateRoomStatus(id);
				updateMeetStatus(str[4]);
				updateTvwallStatus(str[4]);
				isStatus = getMeetStatusByDomainid(domain_id);
			}
			
			if(method.equals("update") && str.length == 5){
				Domainuser domainuser = getDomainuserByConfId(str[2]);
				MeetInterface meetInterface = null;
				try {
					meetInterface = new MeetInterface(domainuser.getUsername(), domainuser.getPassword(), svrIp, oauthConsumerKey, oauthConsumerCecret);
				} catch (Exception e) {
					e.printStackTrace();
				}
		        Map<String, Object> params = new HashMap<String, Object>();
		        params.put("account_token", meetInterface.getToken());
		        
				MeetingResult result = MeetHttpClient.sendGet(svrIp+"/api/v1/vc/confs/"+str[4], params, meetInterface.getCookie());
				updateMeetDuration(result.getDuration(), str[4]);
		        
			}
			
			
			if(str.length == 9 && str[5].equals("cascades") && str[6].equals("0") && str[7].equals("mts")){
				System.out.println(str[2]);
				Domainuser domainuser = getDomainuserByConfId(str[2]);
				MeetInterface meetInterface = null;
				try {
					meetInterface = new MeetInterface(domainuser.getUsername(), domainuser.getPassword(), svrIp, oauthConsumerKey, oauthConsumerCecret);
				} catch (Exception e) {
					e.printStackTrace();
				}
		        Map<String, Object> params = new HashMap<String, Object>();
		        params.put("account_token", meetInterface.getToken());
				MeetingResult result = MeetHttpClient.sendGet(svrIp+"/api/v1/vc/confs/"+str[4]+"/mts",params, meetInterface.getCookie());
				
				updateMeetJoin(result.getMts().size(),str[4]);
				
			}
		}

	}

	// 发送http请求
	public void SendHttpGetMsg(String channel) throws Exception {
		String url = svrIp + "/api/v1/vc" + channel;
		String param = "account_token=" + token;
		Httpmsg httpMsg = new Httpmsg();
		String result = httpMsg.HttpGetMsg(url, param);
		System.err.println("Http get msg info : " + result);
	}


	@Override
	public void run() {
		ClientSessionChannel.MessageListener hslistener = new HshakeListener();
		bayeuxclient.handshake(map,hslistener); // 握手	
		
		msglistener = new MsgListener();
		sublistener = new SubListener();
		
		while(isStatus){
			try {
			Thread.sleep(29 * 60 * 1000);
			String url = svrIp + "/api/v1/system/heartbeat";
			String param = "account_token=" + token;
			Httpmsg httpMsg = new Httpmsg();
				httpMsg.HttpPostMsg(url, param);
				isStatus = getMeetStatusByDomainid(domain_id);
				if(!isStatus){
					updateDomainidStatus(domain_id);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
	
	/**
	 * 修改会议状态
	 */
	public static void updateMeetStatus(String e164){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		int status = 0;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "UPDATE meet SET"
					+ " STATUS = ?"
					+ " WHERE E164 = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,status);
			pstmt.setString(2, e164);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * 修改会议参会人数
	 */
	public static void updateMeetJoin(Integer size ,String confid){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		int status = 1;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "UPDATE meet SET"
					+ " JOIN_MT = ?"
					+ " WHERE E164 = ?"
					+ "	AND STATUS = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,size);
			pstmt.setString(2, confid);
			pstmt.setInt(3, status);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	/**
	 * 修改会议时长
	 */
	public static void updateMeetDuration(Integer duration ,String confid){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		int status = 1;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "UPDATE meet SET"
					+ " DURATION = ?"
					+ " WHERE E164 = ?"
					+ "	AND STATUS = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,duration == null ? 0 : duration);
			pstmt.setString(2, confid);
			pstmt.setInt(3, status);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	
	/**
	 * 查询创建人
	 */
	public static Domainuser getDomainuserByConfId(String moid){
		Domainuser domainuser = null;
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "SELECT * FROM domainuser WHERE"
					+ " MOID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,moid);
			rs = pstmt.executeQuery();
			domainuser = new Domainuser();
	        while (rs.next()) {
	        	domainuser.setUsername(rs.getString("USERNAME"));
	        	domainuser.setPassword(rs.getString("PASSWORD"));
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}	
 		return domainuser;
	}
	
	
	/**
	 * 查询会议室ID
	 */
	public static String selectRoom(String confId){
		String id = null;
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "SELECT m.MEET_ROOM"
					+ " FROM meet m"
					+ " WHERE m.STATUS = 1"
					+ " AND m.E164 = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, confId);
			rs = pstmt.executeQuery(); 
			while(rs.next()) {
				id = rs.getString("MEET_ROOM");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
 		return id;
	}
	
	
	/**
	 * 修改会议室状态
	 */
	public static void updateRoomStatus(String confId){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "UPDATE meetroom SET"
					+ " STATUS = 0,"
					+ "	USE_BEGIN = NULL,"
					+ " USE_END = NULL"
					+ " WHERE ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(confId));
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	/**
	 * 修改电视墙通道信息
	 */
	public static void updateTvwallStatus(String confId){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);
			String sql = "UPDATE channel SET"
					+ " ALIAS = NULL,"
					+ "OCCUPY = 0,"
					+ "CONF_ID = NULL"
					+ " WHERE CONF_ID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, confId);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}		
	}
	
	
	
	
	/**
	 * 修改用户域订阅状态
	 */
	public static void updateDomainidStatus(String domain_id){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
		int status = 0;
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);			
			String sql = "UPDATE domainuser SET"
					+ " SUBSRIPTION = ?"
					+ " WHERE MOID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1,0);
			pstmt.setString(2, domain_id);
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
					
					e.printStackTrace();
				}
			}
		}		
	}
	
	/**
	 * 根据用户域，查询用户域下会议
	 */
	public static Boolean getMeetStatusByDomainid(String userDomain){
		Connection conn = null;
		PreparedStatement pstmt = null ;
		ResultSet rs = null ;
 		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, jdbcUserName, jdbcPassword);			
			String sql = "SELECT ID FROM meet WHERE STATUS = 1 AND USER_DOMAIN = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userDomain);
			rs = pstmt.executeQuery(); 
			while(rs.next()) { 
				System.err.println(rs.getString("ID"));
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(rs !=null){
				try {
					rs.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(pstmt !=null){
				try {
					pstmt.close();
				} catch (SQLException e2) {
					e2.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				
					e.printStackTrace();
				}
			}
		}
 		return false;
	}
	
	  public static void main(String args[]) throws Exception {
			String id = selectRoom("1112029");
			updateRoomStatus(id);

			String svrIp = "SSO_COOKIE_KEY=458d0a8d-d881-45be-82fc-0c1e96b554e6";
			
			System.out.println(svrIp.substring(0,14));
			
			CookieHandler.setDefault(new CookieManager(null,CookiePolicy.ACCEPT_ALL));
			Httpmsg httpMsg = new Httpmsg();
			httpMsg.SetAppId("test");
			httpMsg.SetAppKey("test");
			httpMsg.SetUserName("admin1");
			httpMsg.SetPassWord("keda8888");
			String token = httpMsg.login(svrIp);
			
			String url = svrIp + "/api/v1/publish";
			HttpClient httpclient = new HttpClient();
			httpclient.start();
			ClientTransport httptransport = new LongPollingTransport(null,httpclient);
			BayeuxClient bayeuxclient = new BayeuxClient(url, httptransport);
			CookieManager handle = (CookieManager) CookieHandler.getDefault();
			CookieStore store = handle.getCookieStore();
			List<HttpCookie> lstCookie = store.getCookies();
			System.err.println(lstCookie);
	  }
}
