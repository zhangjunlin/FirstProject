package com.auxing.znhy.util;

import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by 31690 on 2017/10/31.
 */
public class HttpClientNewUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientNewUtil.class);

    private static Map<String, JSONObject> httpClientHashMap = new HashMap<>();
    private final static Object syncLock = new Object();

    public static JSONObject req(JSONObject jsonObject) {

        if ("POST".equals(jsonObject.getString("reqMethod").toUpperCase())) {

            return post(jsonObject.getString("url"), jsonObject.getString("params"), jsonObject.getString("charset"),
                    jsonObject.getJSONObject("headMap"), jsonObject.getBooleanValue("useProxy"), jsonObject.getString("proxyIp")
                    , jsonObject.getIntValue("connectTimeout"), jsonObject.getIntValue("readTimeout"),
                    jsonObject.getBooleanValue("isRedirect"));
        } else {
            return get(jsonObject.getString("url"), jsonObject.getString("charset"), jsonObject.getJSONObject("headMap"),
                    jsonObject.getBooleanValue("useProxy"), jsonObject.getString("proxyIp"), jsonObject.getIntValue("connectTimeout"),
                    jsonObject.getIntValue("readTimeout"), jsonObject.getBooleanValue("isRedirect"));
        }

    }

    /**
     * GET请求URL获取内容
     */
    public static JSONObject get(String urlStr, String charset, Map<String, Object> headMap, boolean useProxy, String proxyIp,
                                 int connectTimeOut, int readTimeOut, boolean isRedirect) {

        JSONObject respJson = new JSONObject();
        CloseableHttpClient httpClient;
        HttpGet httpget = new HttpGet(urlStr);
        config(httpget, headMap, useProxy, proxyIp, connectTimeOut, readTimeOut, isRedirect);
        CloseableHttpResponse response = null;
        try {
            JSONObject clientJson = getHttpClient(urlStr);
            httpClient = (CloseableHttpClient) clientJson.get("httpClient");
            response = httpClient.execute(httpget, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);

            respJson.put("Status", response.getStatusLine().getStatusCode());
            respJson.put("Content", result);

            List<String> list = new ArrayList<>();

            JSONObject respHeadJson = new JSONObject();
            Header[] headers = response.getAllHeaders();
            if (headers.length > 0) {
                for (int i = 0; i < headers.length; i++) {
                    respHeadJson.put(headers[i].getName(), headers[i].getValue());
                    if ("Set-Cookie".equals(headers[i].getName())) {
                        list.add(headers[i].getValue());
                    }
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";")));
                    } else {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";"))).append(";");
                    }
                }
            }

//            respJson.put("Cookie", getCookie(((InternalHttpClient) httpClient).cookieStore));
            respJson.put("Cookie", getCookie((CookieStore) clientJson.get("CookieStore")));

//            respJson.put("Cookie", stringBuilder.toString());
            respJson.put("RespHead", respHeadJson);
            return respJson;
        } catch (IOException e) {
//            logger.error("[HttpUtil][ReqRtnStr]Error!,msg={},url={}", e.getMessage(), urlStr);
//            e.printStackTrace();
            logger.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * POST请求URL获取内容
     */
    public static JSONObject post(String urlStr, String params, String charset, Map<String, Object> headMap,
                                  boolean useProxy, String proxyIp, int connectTimeout, int readTimeout, boolean isRedirect) {
        JSONObject respJson = new JSONObject();
//        CookieStore cookieStore = new BasicCookieStore();
        HttpPost httppost = new HttpPost(urlStr);
        config(httppost, headMap, useProxy, proxyIp, connectTimeout, readTimeout, isRedirect);
        setPostParams(httppost, HttpUtil.parmStrToJson(params), charset);
        CloseableHttpResponse response = null;
        CloseableHttpClient httpClient;
        try {
            JSONObject clientJson = getHttpClient(urlStr);
            httpClient = (CloseableHttpClient) clientJson.get("httpClient");
            response = httpClient.execute(httppost, HttpClientContext.create());
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, charset);
            EntityUtils.consume(entity);

            respJson.put("Status", response.getStatusLine().getStatusCode());
            respJson.put("Content", result);

            JSONObject respHeadJson = new JSONObject();
            List<String> list = new ArrayList<>();
            Header[] headers = response.getAllHeaders();
            if (headers.length > 0) {
                for (int i = 0; i < headers.length; i++) {
                    respHeadJson.put(headers[i].getName(), headers[i].getValue());
                    if ("Set-Cookie".equals(headers[i].getName())) {
                        list.add(headers[i].getValue());
                    }
                }
            }

            StringBuilder stringBuilder = new StringBuilder();
            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == list.size() - 1) {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";")));
                    } else {
                        stringBuilder.append(list.get(i).substring(0, list.get(i).indexOf(";"))).append(";");
                    }
                }
            }

            respJson.put("Cookie", getCookie((CookieStore) clientJson.get("CookieStore")));

//            respJson.put("Cookie", stringBuilder.toString());
            respJson.put("RespHead", respHeadJson);


            //            respJson.put("Cookie", getCookie(cookieStore));

            return respJson;
        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("[HttpUtil][ReqRtnStr]Error!,msg={},url={}", e.getMessage(), urlStr);

        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * HttpClient配置
     */
    private static void config(HttpRequestBase httpRequestBase, Map<String, Object> headMap, boolean useProxy, String proxyIp,
                               int connectTimeOut, int readTimeOut, boolean isRedirect) {

        //设置请求头
        if (headMap != null) {
            Object[] listKey = headMap.keySet().toArray();
            for (Object key : listKey) {
                httpRequestBase.setHeader(String.valueOf(key), String.valueOf(headMap.get(key)));
            }
        }

        HttpHost proxy = null;
        if (useProxy == true) {
            if (StringUtils.isBlank(proxyIp)) {
                //获取队列中的代理ip
                String proxyAddress = ProxyUtil.currentProxyAddress;
                if (StringUtils.isBlank(proxyAddress)) {
                    ProxyUtil.resetProxyAddress();
                    proxyAddress = ProxyUtil.currentProxyAddress;
                }

                String ip = proxyAddress.substring(0, proxyAddress.indexOf(":"));
                int port = Integer.parseInt(proxyAddress.substring(proxyAddress.indexOf(":") + 1));
                proxy = new HttpHost(ip, port);
            } else {
                String ip = proxyIp.substring(0, proxyIp.indexOf(":"));
                int port = Integer.parseInt(proxyIp.substring(proxyIp.indexOf(":") + 1));
                proxy = new HttpHost(ip, port);
            }

        }

        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                //从连接池中获取连接的超时时间
                .setConnectionRequestTimeout(connectTimeOut)
                //与服务器连接超时时间
                .setConnectTimeout(connectTimeOut)
                //socket读数据超时时间
                .setSocketTimeout(readTimeOut)
                .setProxy(proxy)
//                .setCookieSpec(CookieSpecs.IGNORE_COOKIES)
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setRedirectsEnabled(isRedirect)
                .build();

        //设置成下面这句话，就可以使会话保持
        //                .setCookieSpec(CookieSpecs.DEFAULT)

        httpRequestBase.setConfig(requestConfig);
    }

    /**
     * 获取HttpClient对象
     */
    public static JSONObject getHttpClient(String url) throws Exception {
        String hostname = null;
        CloseableHttpClient httpClient = null;
        JSONObject jsonObject = new JSONObject();

        try {
            hostname = url.split("/")[2];
            int port = 80;
            if (hostname.contains(":")) {
                String[] arr = hostname.split(":");
                hostname = arr[0];
                port = Integer.parseInt(arr[1]);
            }
            JSONObject jsonObject1 = httpClientHashMap.get(hostname + port);
            if (jsonObject1 == null) {

                synchronized (syncLock) {
                    if (httpClient == null) {
                        CookieStore cookieStore = new BasicCookieStore();
                        httpClient = createHttpClient(200, 40, 100, hostname, port, cookieStore);
                        jsonObject.put("CookieStore", cookieStore);
                        jsonObject.put("httpClient", httpClient);

                        httpClientHashMap.put(hostname + port, jsonObject);

                    }
                }
            }else {
                jsonObject.put("CookieStore", jsonObject1.get("CookieStore"));
                jsonObject.put("httpClient", jsonObject1.get("httpClient"));
            }

        } catch (Exception E) {
            logger.error("分割url取值失败，url={},错误信息为：{}", url, E.getMessage());
        }

        return jsonObject;
    }


    /**
     * 创建HttpClient对象
     */
    @SuppressWarnings("resource")
	public static CloseableHttpClient createHttpClient(int maxTotal,
                                                       int maxPerRoute, int maxRoute, String hostname, int port, CookieStore cookieStore) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory>create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
        HttpHost httpHost = new HttpHost(hostname, port);
        // 将目标主机的最大连接数增加
        cm.setMaxPerRoute(new HttpRoute(httpHost), maxRoute);

        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = (exception, executionCount, context) -> {
            // 如果已经重试了5次，就放弃
            if (executionCount >= 5) {
                return false;
            }
            // 如果服务器丢掉了连接，那么就重试
            if (exception instanceof NoHttpResponseException) {
                return true;
            }
            // 不要重试SSL握手异常
            if (exception instanceof SSLHandshakeException) {
                return false;
            }
            // 超时
            if (exception instanceof InterruptedIOException) {
                return false;
            }
            // 目标服务器不可达
            if (exception instanceof UnknownHostException) {
                return false;
            }
            // 连接被拒绝
            if (exception instanceof ConnectTimeoutException) {
                return false;
            }
            // SSL握手异常
            if (exception instanceof SSLException) {
                return false;
            }

            HttpClientContext clientContext = HttpClientContext
                    .adapt(context);
            HttpRequest request = clientContext.getRequest();
            // 如果请求是幂等的，就再次尝试
            if (!(request instanceof HttpEntityEnclosingRequest)) {
                return true;
            }
            return false;
        };


        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        Credentials credentials = new UsernamePasswordCredentials("cdqlln",
                "cdqlln111");
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRetryHandler(httpRequestRetryHandler)
                .setDefaultCredentialsProvider(credentialsProvider)
                .setDefaultCookieStore(cookieStore)
                .build();

        return httpClient;
    }

    private static void setPostParams(HttpPost httpost, Map<String, Object> params, String charset) {
        List<NameValuePair> nvps = new ArrayList<>();
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            nvps.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
        try {
            httpost.setEntity(new UrlEncodedFormEntity(nvps, charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static String getCookie(CookieStore cookieStore) {

        List<Cookie> cookies = cookieStore.getCookies();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < cookies.size(); i++) {
            if (i == cookies.size() - 1) {
                stringBuilder.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue());
            } else {
                stringBuilder.append(cookies.get(i).getName()).append("=").append(cookies.get(i).getValue()).append(";");
            }
        }
        return stringBuilder.toString();

    }


    public static void main(String[] args) {

        String headerStr = "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
                "Accept-Encoding:gzip, deflate, sdch\n" +
                "Accept-Language:zh-CN,zh;q=0.8\n" +
                "Cache-Control:no-cache\n" +
                "Connection:keep-alive\n" +
                "Host:www.ccopyright.com.cn\n" +
                "Pragma:no-cache\n" +
                "Upgrade-Insecure-Requests:1\n" +
                "User-Agent:Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.221 Safari/537.36 SE 2.X MetaSr 1.0";

        Map<String, String> headerMap = HttpUtil.convertStringToMap(headerStr);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", "http://www.ccopyright.com.cn/img.php?0.39786659358980736");
        jsonObject.put("charset", "UTF-8");
        jsonObject.put("headMap", headerMap);
        jsonObject.put("useProxy", true);
        jsonObject.put("reqMethod", "GET");
        jsonObject.put("params", null);
        jsonObject.put("connectTimeout", 10000);
        jsonObject.put("readTimeout", 15000);

        System.out.println("1231=" + HttpClientNewUtil.req(jsonObject).getString("Cookie"));

//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("url", "http://blog.csdn.net/catoop/article/details/38849497");
//        jsonObject.put("charset", "UTF-8");
//        jsonObject.put("headMap", null);
//        jsonObject.put("useProxy", true);
//        jsonObject.put("reqMethod", "GET");
//        jsonObject.put("params", null);
//        jsonObject.put("connectTimeout", 15000);
//        jsonObject.put("readTimeout", 30000);
//
////        System.out.println(HttpClientNewUtil.get("http://blog.csdn.net/catoop/article/details/38849497", "UTF-8", null, true));
//        System.out.println(HttpClientNewUtil.req(jsonObject).getString("Content"));
    }

}
