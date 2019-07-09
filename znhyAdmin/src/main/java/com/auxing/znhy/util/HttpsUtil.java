package com.auxing.znhy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

/**
 * Created by 31690 on 2017/6/28.
 */
public class HttpsUtil {

    private static final Logger logger = LoggerFactory.getLogger(HttpsUtil.class);

    public static String ReqRtnStr(String urlStr, String params, String charset, String reqMethod, Map<String, String> headMap) {

        URL url = null;
        HttpsURLConnection connection = null;
        DataOutputStream out = null;
        byte[] paramParseUtf8 = null;
        InputStreamReader inputStream = null;
        BufferedReader bufferReader = null;
        String inputLine = null;
        StringBuilder resultData = new StringBuilder();
        try {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            url = new URL(urlStr);
            connection = (HttpsURLConnection) url.openConnection();// 打开连接
            connection.setSSLSocketFactory(ssf);
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

}
