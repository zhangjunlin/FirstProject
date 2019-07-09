package com.auxing.znhy.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

public class MeetHttpClient {
	
	/**
	 * post请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static MeetingResult sendPost(String url, Map<String, Object> params, String cookie){
		PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        StringBuffer param = new StringBuffer();  
        HttpURLConnection httpURLConnection = null;  
        Map<String, List<String>> map = null;
        StringBuffer result = new StringBuffer();;
        Iterator it = params.entrySet().iterator();  
        while (it.hasNext()) {  
            Map.Entry element = (Map.Entry) it.next();  
            param.append(element.getKey());  
            param.append("=");  
            param.append(element.getValue());  
            param.append("&");  
        }  
        if (param.length() > 0) {  
        	param.deleteCharAt(param.length() - 1);  
        }  
        try {  
            URL realUrl = new URL(url);  
            httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
            
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setDefaultUseCaches(false);
            httpURLConnection.setRequestProperty("contentType", "utf-8");
            httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (compatible) ");
            httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			
            httpURLConnection.setRequestProperty("accept", "*/*");  
            httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));  
            httpURLConnection.setDoOutput(true);  
            httpURLConnection.setDoInput(true); 
            httpURLConnection.setConnectTimeout(10000*10);  //设置连接超时为10s  
            httpURLConnection.setReadTimeout(10000*10);     //读取数据超时也是10s
            httpURLConnection.setRequestMethod("POST");
            if(cookie != null){
            	httpURLConnection.setRequestProperty("Cookie",cookie);
            }
            
            printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
            printWriter.write(param.toString());  
            printWriter.flush();  
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));  
            
            map = httpURLConnection.getHeaderFields();
            
            String line;  
            while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {  
            	result.append(line);  
            }  
        } catch (IOException e) {  
        	e.printStackTrace();
        } finally {  
            httpURLConnection.disconnect();  
            try {  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
            } catch (IOException ex) {  
                ex.printStackTrace();  
            }  
        }
        
        MeetingResult meetResult = JSONObject.parseObject(result.toString(), MeetingResult.class);
        
        //登录获取cookie
        if(cookie == null){
            // 遍历所有的响应头字段
            Iterator iter = map.entrySet().iterator(); // 获得map的Iterator
            while (iter.hasNext()) {
            	Entry entry = (Entry) iter.next();   
            	if("Set-Cookie".equals(entry.getKey())){
            		if(entry.getValue().toString().contains("SSO_COOKIE_KEY")){
                		String ssoCookie = entry.getValue().toString();
                		ssoCookie = ssoCookie.substring(ssoCookie.indexOf("SSO_COOKIE_KEY"), ssoCookie.indexOf(";",ssoCookie.indexOf("SSO_COOKIE_KEY")));
                		meetResult.setCookie(ssoCookie);
            		}
            	}
            }
        }
        
		return meetResult;
	}
	
	
	/**
	 * put请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static MeetingResult sendPut(String url, Map<String, Object> params, String cookie){
		PrintWriter printWriter = null;  
		BufferedReader bufferedReader = null;  
		StringBuffer param = new StringBuffer();  
		HttpURLConnection httpURLConnection = null;  
		Map<String, List<String>> map = null;
		StringBuffer result = new StringBuffer();;
		Iterator it = params.entrySet().iterator();  
		while (it.hasNext()) {  
			Map.Entry element = (Map.Entry) it.next();  
			param.append(element.getKey());  
			param.append("=");  
			param.append(element.getValue());  
			param.append("&");  
		}  
		if (param.length() > 0) {  
			param.deleteCharAt(param.length() - 1);  
		}  
		try {  
			URL realUrl = new URL(url);  
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
			
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDefaultUseCaches(false);
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (compatible) ");
			httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			
			httpURLConnection.setRequestProperty("accept", "*/*");  
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));  
			httpURLConnection.setDoOutput(true);  
			httpURLConnection.setDoInput(true); 
			httpURLConnection.setConnectTimeout(10000*10);  //设置连接超时为10s  
			httpURLConnection.setReadTimeout(10000*10);     //读取数据超时也是10s
			httpURLConnection.setRequestMethod("PUT");
			if(cookie != null){
				httpURLConnection.setRequestProperty("Cookie",cookie);
			}
			
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
			printWriter.write(param.toString());  
			printWriter.flush();  
			bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));  
			
			map = httpURLConnection.getHeaderFields();
			
			String line;  
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {  
				result.append(line);  
			}  
		} catch (IOException e) {  
			e.printStackTrace();
		} finally {  
			httpURLConnection.disconnect();  
			try {  
				if (printWriter != null) {  
					printWriter.close();  
				}  
				if (bufferedReader != null) {  
					bufferedReader.close();  
				}  
			} catch (IOException ex) {  
				ex.printStackTrace();  
			}  
		}
		
		MeetingResult meetResult = JSONObject.parseObject(result.toString(), MeetingResult.class);

		return meetResult;
	}
	
	/**
	 * delete请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static MeetingResult sendDelete(String url, Map<String, Object> params, String cookie) {
		PrintWriter printWriter = null;  
		BufferedReader bufferedReader = null;  
		StringBuffer param = new StringBuffer();  
		HttpURLConnection httpURLConnection = null;  
		Map<String, List<String>> map = null;
		StringBuffer result = new StringBuffer();;
		Iterator it = params.entrySet().iterator();  
		while (it.hasNext()) {  
			Map.Entry element = (Map.Entry) it.next();  
			param.append(element.getKey());  
			param.append("=");  
			param.append(element.getValue());  
			param.append("&");  
		}  
		if (param.length() > 0) {  
			param.deleteCharAt(param.length() - 1);  
		}  
		try {  
			URL realUrl = new URL(url);  
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();  
			
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setDefaultUseCaches(false);
			httpURLConnection.setRequestProperty("contentType", "utf-8");
			httpURLConnection.setRequestProperty("User-Agent","Mozilla/5.0 (compatible) ");
			httpURLConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			
			httpURLConnection.setRequestProperty("accept", "*/*");  
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");  
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));  
			httpURLConnection.setDoOutput(true);  
			httpURLConnection.setDoInput(true); 
			httpURLConnection.setConnectTimeout(10000*10);  //设置连接超时为10s  
			httpURLConnection.setReadTimeout(10000*10);     //读取数据超时也是10s
			httpURLConnection.setRequestMethod("DELETE");
			if(cookie != null){
				httpURLConnection.setRequestProperty("Cookie",cookie);
			}
			
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());  
			printWriter.write(param.toString());  
			printWriter.flush();  
			bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"utf-8"));  
			
			map = httpURLConnection.getHeaderFields();
			
			String line;  
			while (bufferedReader != null && (line = bufferedReader.readLine()) != null) {  
				result.append(line);  
			}  
		} catch (IOException e) {  
			e.printStackTrace();
		} finally {  
			httpURLConnection.disconnect();  
			try {  
				if (printWriter != null) {  
					printWriter.close();  
				}  
				if (bufferedReader != null) {  
					bufferedReader.close();  
				}  
			} catch (IOException ex) {  
				ex.printStackTrace();  
			}  
		}
		
		MeetingResult meetResult = JSONObject.parseObject(result.toString(), MeetingResult.class);
	
		return meetResult;
	}
	
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static MeetingResult sendGet(String url,Map<String, Object> params,String cookie) {
        String result = "";
        BufferedReader in = null;
        try {
            
            StringBuffer param = new StringBuffer();
            Iterator it = params.entrySet().iterator();  
            while (it.hasNext()) {  
                Map.Entry element = (Map.Entry) it.next();  
                param.append(element.getKey());  
                param.append("=");  
                param.append(element.getValue());  
                param.append("&");  
            }  
            if (param.length() > 0) {  
            	param.deleteCharAt(param.length() - 1);  
            }
            
            String urlNameString = url+"?"+param;
            
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Accept-Encoding", " identity");
            connection.setRequestProperty("connection", "Keep-close");
            connection.setRequestProperty("user-agent","Python-urllib/3.5");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","58");
            
            connection.setConnectTimeout(10000*10);  //设置连接超时为10s  
            connection.setReadTimeout(10000*10);     //读取数据超时也是10s
            
            if(cookie != null){
            	connection.setRequestProperty("Cookie",cookie);
            }
            
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
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
        System.out.println(result);
        return JSONObject.parseObject(result,MeetingResult.class);
    }

	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static JSONObject sendMapGet(String url,Map<String, Object> params,String cookie) {
        String result = "";
        BufferedReader in = null;
        try {
            
            StringBuffer param = new StringBuffer();
            Iterator it = params.entrySet().iterator();  
            while (it.hasNext()) {  
                Map.Entry element = (Map.Entry) it.next();  
                param.append(element.getKey());  
                param.append("=");  
                param.append(element.getValue());  
                param.append("&");  
            }  
            if (param.length() > 0) {  
            	param.deleteCharAt(param.length() - 1);  
            }
            
            String urlNameString = url+"?"+param;
            
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Accept-Encoding", " identity");
            connection.setRequestProperty("connection", "Keep-close");
            connection.setRequestProperty("user-agent","Python-urllib/3.5");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","58");
            
            connection.setConnectTimeout(10000*10);  //设置连接超时为10s  
            connection.setReadTimeout(10000*10);     //读取数据超时也是10s
            
            if(cookie != null){
            	connection.setRequestProperty("Cookie",cookie);
            }
            
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
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
        System.out.println(result);
        return JSONObject.parseObject(result);
    }
	
	
	
	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static VirtualMeetDetails virtualSendGet(String url,Map<String, Object> params,String cookie) {
		String result = "";
		BufferedReader in = null;
		try {

			StringBuffer param = new StringBuffer();
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry element = (Map.Entry) it.next();
				param.append(element.getKey());
				param.append("=");
				param.append(element.getValue());
				param.append("&");
			}
			if (param.length() > 0) {
				param.deleteCharAt(param.length() - 1);
			}

			String urlNameString = url+"?"+param;

			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("Accept-Encoding", " identity");
			connection.setRequestProperty("connection", "Keep-close");
			connection.setRequestProperty("user-agent","Python-urllib/3.5");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length","58");

			connection.setConnectTimeout(10000*10);  //设置连接超时为10s
			connection.setReadTimeout(10000*10);     //读取数据超时也是10s

			if(cookie != null){
				connection.setRequestProperty("Cookie",cookie);
			}

			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
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

		return JSONObject.parseObject(result,VirtualMeetDetails.class);
	}


	/**
	 * get请求
	 * @param url
	 * @param params
	 * @param cookie
	 * @return
	 */
	public static HduResult sendHduGet(String url,Map<String, Object> params,String cookie) {
        String result = "";
        BufferedReader in = null;
        try {
            
            StringBuffer param = new StringBuffer();
            Iterator it = params.entrySet().iterator();  
            while (it.hasNext()) {  
                Map.Entry element = (Map.Entry) it.next();  
                param.append(element.getKey());  
                param.append("=");  
                param.append(element.getValue());  
                param.append("&");  
            }  
            if (param.length() > 0) {  
            	param.deleteCharAt(param.length() - 1);  
            }
            
            String urlNameString = url+"?"+param;
            
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("Accept-Encoding", " identity");
            connection.setRequestProperty("connection", "Keep-close");
            connection.setRequestProperty("user-agent","Python-urllib/3.5");
            connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length","58");
            
            connection.setConnectTimeout(10000*10);  //设置连接超时为10s  
            connection.setReadTimeout(10000*10);     //读取数据超时也是10s
            
            if(cookie != null){
            	connection.setRequestProperty("Cookie",cookie);
            }
            
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
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
        return JSONObject.parseObject(result,HduResult.class);
    }
}
