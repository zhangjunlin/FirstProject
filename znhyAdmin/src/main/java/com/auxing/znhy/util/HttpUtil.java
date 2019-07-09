package com.auxing.znhy.util;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.zip.GZIPInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;


/**
 * Created by GY on 2015/12/8.
 */
public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送HTTP请求POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 发送到远程主机的正文数据
     * @return String
     */
    public static String ReqRtnStr(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap, boolean useProxy) {

        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cdqlln", "cdqlln111".toCharArray());
            }
        };

        Authenticator.setDefault(authenticator);
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        String ip = null;
        Integer port = null;
        try {
            url = new URL(urlStr);

            if (useProxy == true) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
//            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
//            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setConnectTimeout(15000);// 设置连接超时
            connection.setReadTimeout(20000);// 设置读取超时

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            if (params != null) {
                paramParseUtf8 = params.getBytes(charset);// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), charset);

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            } else {
                logger.error("urlStr={},respCode={}", urlStr, connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!,ip={},port={},errmsg={},url={}", ip, port, e.getMessage(), urlStr);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!，errmsg={}", e.getMessage());
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP请求POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @return String
     */
    public static String ReqRtnStr(JSONObject jsonObject) {

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cdqlln", "cdqlln111".toCharArray());
            }
        };

        Authenticator.setDefault(authenticator);
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        String ip = null;
        Integer port = null;
        try {
            url = new URL(jsonObject.getString("url"));

            if (jsonObject.getBooleanValue("useProxy")) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(jsonObject.getString("reqMethod"));// 请求方式
            connection.setConnectTimeout(jsonObject.getIntValue("connectTimeout"));// 设置连接超时
            connection.setReadTimeout(jsonObject.getIntValue("readTimeout"));// 设置读取超时


            JSONObject headMap = jsonObject.getJSONObject("headMap");
            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (int i = 0; i < listKey.length; i++) {
                    connection.setRequestProperty(listKey[i].toString(), headMap.getString(listKey[i].toString()));
                }
            }
            connection.connect();

            String params = jsonObject.getString("params");
            if (StringUtils.isNoneBlank(params)) {
                paramParseUtf8 = params.getBytes(jsonObject.getString("charset"));// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), jsonObject.getString("charset"));

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            } else {
                logger.error("urlStr={},respCode={}", jsonObject.getString("url"), connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!,ip={},port={},msg={},url={}", ip, port, e.getMessage(), jsonObject.getString("url"));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!", e);
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP请求POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @return String
     */
    public static JSONObject ReqRtnJSON(JSONObject jsonObject) {

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cdqlln", "cdqlln111".toCharArray());
            }
        };

        Authenticator.setDefault(authenticator);
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        String ip = null;
        Integer port = null;
        try {
            url = new URL(jsonObject.getString("url"));

            if (jsonObject.getBooleanValue("useProxy")) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));

//                ip = "127.0.0.1";
//                port = 8888;
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(jsonObject.getString("reqMethod"));// 请求方式
            connection.setConnectTimeout(jsonObject.getIntValue("connectTimeout"));// 设置连接超时
            connection.setReadTimeout(jsonObject.getIntValue("readTimeout"));// 设置读取超时


            JSONObject headMap = jsonObject.getJSONObject("headMap");
            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (int i = 0; i < listKey.length; i++) {
                    connection.setRequestProperty(listKey[i].toString(), headMap.getString(listKey[i].toString()));
                }
            }
            connection.connect();

            String params = jsonObject.getString("params");
            if (StringUtils.isNoneBlank(params)) {
                paramParseUtf8 = params.getBytes(jsonObject.getString("charset"));// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), jsonObject.getString("charset"));

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }

                StringBuilder stringBuilder = new StringBuilder();
                List<String> list = connection.getHeaderFields().get("Set-Cookie");
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        if (i == list.size() - 1) {
                            stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";")));
                        } else {
                            stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";"))).append(";");
                        }
                    }
                }

                JSONObject respJson = new JSONObject();
                respJson.put("Content", resultData.toString());
                respJson.put("Cookie", stringBuilder.toString());
                return respJson;

            } else {
                logger.error("urlStr={},respCode={}", jsonObject.getString("url"), connection.getResponseCode());
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("[HttpUtil][ReqRtnStr]Error!,ip={},port={},msg={},url={}", ip, port, e.getMessage(), jsonObject.getString("url"));
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!", e);
            }
        }

        return null;
    }


    public static String ReqRtnStr(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap, boolean useProxy, String proxyAddress) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);

            if (useProxy == true) {
                String ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                Integer port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            if (params != null) {
                paramParseUtf8 = params.getBytes(charset);// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), charset);

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            } else {
                logger.error("urlStr={},respCode={}", urlStr, connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!", e);
            }
        }
        return resultData.toString();
    }


    public static String ReqRtnCookie(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap, boolean useProxy) {

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cdqlln", "cdqlln111".toCharArray());
            }
        };

        Authenticator.setDefault(authenticator);
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);

            if (useProxy == true) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                String ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                Integer port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(10000);// 设置连接超时
            connection.setReadTimeout(15000);// 设置读取超时

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            if (params != null) {
                paramParseUtf8 = params.getBytes(charset);// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), charset);

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                StringBuilder stringBuilder = new StringBuilder();
                List<String> list = connection.getHeaderFields().get("Set-Cookie");
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";")));
                    } else {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";"))).append(";");
                    }
                }

                return stringBuilder.toString();
            } else {
                logger.error("urlStr={},respCode={}", urlStr, connection.getResponseCode());
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!,errmsg={},url={}", e.getMessage(), urlStr);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!,errmsg={}", e.getMessage());
            }
        }
        return resultData.toString();

    }


    /**
     * 发送HTTP请求POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 发送到远程主机的正文数据
     * @return String
     */
    public static String ReqRtnStr(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap, int ConnectTimeout, int ReadTimeout) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接

            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConnectTimeout == 0 ? 3000 : ConnectTimeout);// 设置连接超时
            connection.setReadTimeout(ReadTimeout == 0 ? 5000 : ReadTimeout);// 设置读取超时

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            if (params != null) {
                paramParseUtf8 = params.getBytes(charset);// 参数utf-8化
                out = new DataOutputStream(connection.getOutputStream());
                out.write(paramParseUtf8);
                out.flush();
            }

            inputStream = new InputStreamReader(connection.getInputStream(), charset);

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!", e);
            }
        }
        return resultData.toString();
    }

    public static String ReqRtnStrGzip(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] byteFromCharset = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            byteFromCharset = params.getBytes(charset);// 参数utf-8化
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setUseCaches(false);// Post 请求不能使用缓存

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            out.write(byteFromCharset);
            out.flush();

            InputStream Stream = new GZIPInputStream(connection.getInputStream());
            bufferReader = new BufferedReader(new InputStreamReader(Stream, charset));
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!", e);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }

                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                logger.error("[HttpUtil][ReqRtnStr]Error!", e);
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP请求,包括GET,返回String对象,请求参数为URL的一部分
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr    请求地址
     * @param reqMethod 请求方法【GET/POST】
     * @return String
     */

    public static String ReqRtnStr(String urlStr, String charset, String reqMethod, Map<String, String> headMap) {

        URL url;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
//            connection.setRequestProperty("Content-Type", "text/plain");//设置Content-Type
//            connection.setRequestProperty("Charset", "utf-8");
            if (headMap != null) {
                //设置请求头
                Iterator<String> heardIterator = headMap.keySet().iterator();
                while (heardIterator.hasNext()) {
                    connection.setRequestProperty(heardIterator.next(), headMap.get(heardIterator.next()));
                }
            }
            connection.connect();

            inputStream = new InputStreamReader(connection.getInputStream(), charset);

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultData.toString();
    }


    /**
     * 发送HTTP请求,包括GET和POST,返回String对象,请求参数为URL的一部分
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr    请求地址
     * @param reqMethod 请求方法【GET/POST】
     * @return String
     */

    public static String ReqRtnStr(String urlStr, String reqMethod) {

        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置Content-Type
            connection.setRequestProperty("Charset", "utf-8");//设置Charset
            connection.connect();

            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP的GET请求
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr    请求地址
     * @param reqMethod 请求方法【GET/POST】
     * @return String
     */

    public static byte[] ReqRtnBytesGet(String urlStr, String reqMethod, Map<String, String> headMap, boolean useProxy) {

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Authenticator authenticator = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("cdqlln", "cdqlln111".toCharArray());
            }
        };
        Authenticator.setDefault(authenticator);

        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        try {
            url = new URL(urlStr);

            if (useProxy == true) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                String ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                Integer port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时

            //设置请求头
            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            return IOUtil.readInputStream(connection.getInputStream());

        } catch (Exception e) {
            logger.error("[HttpUtil][ReqRtnStr]Error!,errmsg={},url={}", e.getMessage(), urlStr);
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发送HTTP请求,包括GET和POST,返回String对象,请求参数为URL的一部分
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr    请求地址
     * @param reqMethod 请求方法【GET/POST】
     * @return String
     */

    public static byte[] ReqRtnBytes(String urlStr, String reqMethod) {

        URL url = null;
        HttpURLConnection connection = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置Content-Type
            connection.setRequestProperty("Charset", "utf-8");//设置Charset
            connection.connect();

            return IOUtil.readInputStream(connection.getInputStream());

        } catch (Exception e) {
            logger.error("请求失败，错误消息为{}", e.getMessage());
//            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 发送HTTP请求,包括GET和POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 发送到远程主机的正文数据
     * @return String
     */
    public static String ReqRtnStr(String urlStr, String params, String reqMethod) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            paramParseUtf8 = params.getBytes("utf-8");// 参数utf-8化
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置Content-Type
            connection.setRequestProperty("Charset", "utf-8");//设置Charset
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            out.write(paramParseUtf8);
            out.flush();
            out.close();

            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultData.toString();
    }

    /**
     * 发送HTTP请求,包括GET和POST,返回String对象
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 发送到远程主机的正文数据
     * @return String
     */
    public static String ReqRtnStrGBK(String urlStr, String params, String reqMethod) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            paramParseUtf8 = params.getBytes("gbk");// 参数utf-8化
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置Content-Type
            connection.setRequestProperty("Charset", "gbk");//设置Charset
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            out.write(paramParseUtf8);
            out.flush();
            out.close();

            inputStream = new InputStreamReader(connection.getInputStream(), "gbk");

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultData.toString();
    }


    /**
     * 发送HTTP请求,包括GET和POST,返回String对象,参数内容作为content区发送，参数为utf-8字节形式，params.getBytes("utf-8")
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 发送到远程主机的正文数据
     * @return String
     */
    public static String ReqRtnStrByByte(String urlStr, byte[] params, String reqMethod, Map<String, String> headMap, boolean useProxy) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            url = new URL(urlStr);

            if (useProxy == true) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }
                String ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                Integer port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));

                connection = (HttpURLConnection) url.openConnection(proxy);// 打开连接

            } else {
                connection = (HttpURLConnection) url.openConnection();// 打开连接

            }

            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod(reqMethod);// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时


            if (headMap != null) {
                Object[] listKey = headMap.keySet().toArray();
                for (Object key : listKey) {
                    connection.setRequestProperty(String.valueOf(key), headMap.get(key));
                }
            }
            connection.connect();

            out = new DataOutputStream(connection.getOutputStream());
            out.write(params);
            out.flush();
            out.close();

            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");

            bufferReader = new BufferedReader(inputStream);
            if (connection.getResponseCode() == 200) {
                while ((inputLine = bufferReader.readLine()) != null) {
                    resultData.append(inputLine);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferReader != null) {
                    bufferReader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultData.toString();
    }


    /**
     * 发送HTTP_POST请求，返回Document对象
     * 默认的连接和读取超时均为30秒
     *
     * @param urlStr 请求地址
     * @param params 请求参数，如：A=1&B=2&C=3
     * @return Document
     */
    public static Document postReqRtnDoc(String urlStr, String params) {

        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        Document document = null;

        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 打开连接
            paramParseUtf8 = params.getBytes("utf-8");// 参数utf-8化
            connection.setDoInput(true);// 设置输入流采用字节流
            connection.setDoOutput(true);// 设置输出流采用字节流
            connection.setInstanceFollowRedirects(true);// 是否自动执行http重定向
            connection.setRequestMethod("POST");// 请求方式
            connection.setConnectTimeout(ConstantsUtil.CONNECTION_TIMEOUT);// 设置连接超时
            connection.setReadTimeout(ConstantsUtil.READ_TIMEOUT);// 设置读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//设置Content-Type
            connection.setRequestProperty("Charset", "utf-8");//设置Charset
            connection.connect();
            out = new DataOutputStream(connection.getOutputStream());
            out.write(paramParseUtf8);
            out.flush();
            out.close();

            inputStream = new InputStreamReader(connection.getInputStream(), "UTF-8");

            SAXReader saxReader = new SAXReader();
            document = saxReader.read(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;

    }

    public static JSONObject FromRequestGetParameter(HttpServletRequest request) {
        JSONObject json = new JSONObject();
        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String name = en.nextElement();
            String value = request.getParameter(name);
            json.put(name, value);
        }
        return json;
    }

    public static JSONObject FromRequestGetBody(HttpServletRequest request) {

        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();

        try {
            inputStream = new InputStreamReader(request.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        bufferReader = new BufferedReader(inputStream);
        try {
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return JSONObject.parseObject(resultData.toString());

    }


    public static String FromRequestGetBodyRtnString(HttpServletRequest request) {

        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();

        try {
            inputStream = new InputStreamReader(request.getInputStream(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        bufferReader = new BufferedReader(inputStream);
        try {
            while ((inputLine = bufferReader.readLine()) != null) {
                resultData.append(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return resultData.toString();

    }


    public static JSONObject ProcessRequest(HttpServletRequest request) {
        JSONObject paramJson = FromRequestGetParameter(request);
        JSONObject bodyJson = FromRequestGetBody(request);
        if (bodyJson != null) {
            paramJson.putAll(bodyJson);
        }
        //添加RequestURL
        paramJson.put("RequestURL", request.getRequestURL());
        //添加RequestURI
        paramJson.put("RequestURI", request.getRequestURI());
        return paramJson;

    }


    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("\\\\u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }

    //将json转成params字符串,字符串形式为key1=value1&key2=value2
    public static String jsonToParms(JSONObject params) {

        StringBuilder stringBuilder = new StringBuilder();
        Object[] listKey = params.keySet().toArray();
        for (int j = 0; j < listKey.length; j++) {
            if (j == listKey.length - 1) {
                stringBuilder.append(listKey[j]).append("=").append(params.get(listKey[j]));
            } else {
                stringBuilder.append(listKey[j]).append("=").append(params.get(listKey[j])).append("&");

            }
        }

        return stringBuilder.toString();

    }

    //将URL后面的参数转成json,字符串形式为key1=value1&key2=value2
    public static JSONObject parmsToJson(String urlStr) {

        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNoneBlank(url.getQuery())) {
            JSONObject jsonObject = new JSONObject();

            try {
                String[] strings = url.getQuery().split("&");
                String[] keyValue;
                for (int i = 0; i < strings.length; i++) {
                    keyValue = strings[i].split("=");
                    if (keyValue.length == 1) {
                        jsonObject.put(keyValue[0], "");

                    } else {
                        jsonObject.put(keyValue[0], keyValue[1]);

                    }
                }
            } catch (Exception E) {
                E.printStackTrace();
            }

            return jsonObject;

        } else {
            return null;
        }

    }

    //将params字符串,字符串形式为key1=value1&key2=value2，转换成json
    public static JSONObject parmStrToJson(String parmStr) {
        String[] strings = parmStr.split("&");
        String[] keyValue;
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < strings.length; i++) {
            if (StringUtils.isNoneBlank(strings[i])) {
                keyValue = strings[i].split("=");
                if (keyValue.length == 1) {
                    jsonObject.put(keyValue[0], "");

                } else {
                    jsonObject.put(keyValue[0], keyValue[1]);

                }
            }

        }
        return jsonObject;


    }


    public static Map<String, String> convertStringToMap(String headStr) {
        Map<String, String> map = new HashMap<>();
        if (!StringUtils.isBlank(headStr)) {

            String[] strings = headStr.split("\n");
            String[] strings1;
            for (int i = 0; i < strings.length; i++) {
                strings1 = strings[i].split(":");
                map.put(strings1[0], strings[i].substring(strings[i].indexOf(strings1[0]) + strings1[0].length() + 1).trim());
            }
        }

        return map;

    }


    public static JSONObject convertStringToJson(String headStr) {
        JSONObject map = new JSONObject();
        String[] strings = headStr.split("\n");
        String[] strings1;
        for (int i = 0; i < strings.length; i++) {
            strings1 = strings[i].split(":");
            map.put(strings1[0], strings[i].substring(strings[i].indexOf(strings1[0]) + strings1[0].length() + 1).trim());
        }

        return map;

    }

    public static Map<String, Cookie> getCookie(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;

    }
    
    public static String getClientIp(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");


         logger.debug("x-forwarded-for = {}", ip);
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP"); 
            logger.debug("Proxy-Client-IP = {}", ip); 
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
            logger.debug("WL-Proxy-Client-IP = {}", ip);
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            logger.debug("RemoteAddr-IP = {}", ip); 
        }
        if(StringUtils.isNotBlank(ip)) {
            ip = ip.split(",")[0];
        }
        return ip;

    }

}
