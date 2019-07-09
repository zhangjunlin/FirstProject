package com.auxing.znhy.common;

import net.sf.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxiaolin on 2016/12/6.
 */
public class Httpmsg {
	private String appid;
	private String appkey;
	private String username;
	private String password;

	public void SetAppId(String id) {
		appid = id;
	}

	public String GetAppId() {
		return appid;
	}

	public void SetAppKey(String key) {
		appkey = key;
	}

	public String GetAppKey() {
		return appkey;
	}

	public void SetUserName(String userName) {
		username = userName;
	}

	public String GetUserName() {
		return username;
	}

	public void SetPassWord(String passWord) {
		password = passWord;
	}

	public String GetPassWord() {
		return password;
	}

	public String login(String svrIp) throws Exception {
		// 通过APP ID和APP KEY获取APP TOKEN
		String param = "oauth_consumer_key=" + appid
				+ "&oauth_consumer_secret=" + appkey;
		System.out.println("param: " + param);

		String url = svrIp + "/api/v1/system/token";
		String result = HttpPostMsg(url, param);
		System.out.println("get token result: " + result);

		// result是json格式，带有token
		JSONObject jsonResult = JSONObject.fromObject(result);
		String token = jsonResult.getString("account_token");
		System.out.println("token: " + token);

		// 使用用户名及密码登录
		String loginparam = "account_token=" + token + "&username=" + username
				+ "&password=" + password;
		System.out.println("loginparam: " + loginparam);

		url = svrIp + "/api/v1/system/login";

		// result中带有是否成功与用户名信息
		result = HttpPostMsg(url, loginparam);

		return token;
	}

	public String HttpPostMsg(String svrurl, String param) throws Exception {
		String result = "";
		BufferedReader br = null;

		try {
			URL url = new URL(svrurl);
			HttpURLConnection httpconn = (HttpURLConnection) url
					.openConnection();
			httpconn.setDoInput(true);
			httpconn.setDoOutput(true);
			httpconn.setRequestMethod("POST");
			httpconn.setRequestProperty("connection", "Keep-Alive");
			httpconn.connect();

			// 向指向的URL传入参数
			OutputStreamWriter osw = new OutputStreamWriter(
					httpconn.getOutputStream());
			osw.write(param);
			osw.flush();
			osw.close();

			// 获得响应
			int state = httpconn.getResponseCode();
			if (state == HttpURLConnection.HTTP_OK) {
				httpconn.setInstanceFollowRedirects(false);// test
				InputStream in = httpconn.getInputStream();
				br = new BufferedReader(new InputStreamReader(in));

				String line = br.readLine();
				while (line != null) {
					result += line;
					line = br.readLine();
					System.out.println("result:" + result);
				}

				br.close();
				in.close();
			}

		} catch (Exception e) {
			System.out.println("Send POST MSG error!");
		}

		System.out.println("Send httpmsg over!");

		return result;
	}

	public String HttpGetMsg(String url, String param) throws Exception {
		String result = "";
		BufferedReader in = null;

		try {
			// GET请求直接在链接后面拼上请求参数
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);

			// 打开和URL之间的连接
			HttpURLConnection connection = (HttpURLConnection) realUrl
					.openConnection();

			// 设置通用的请求属性
			connection.setRequestMethod("GET");
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setDoInput(true);
			connection.setDoOutput(false);

			// 建立实际的连接
			connection.connect();

			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();

			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}

		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;

	}

}
