package com.auxing.znhy.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * <p>Title: ProxyUtil</p>
 * <p>Description: 获取高级代理的util</p>
 * <p>Company: 苏州奥科星</p>
 *
 * @author zhangjl
 * @date 2018年10月8日 下午9:04:04
 */
public class ProxyUtil {

	private static Logger logger = LoggerFactory.getLogger(ProxyUtil.class);

	public static LinkedBlockingQueue<String> proxyAddresss = new LinkedBlockingQueue<>();
	public static String currentProxyAddress = null;

	public synchronized static String getProxyAddress() {
		try {
			while (true) {
				String proxyAddress = proxyAddresss.poll(2, TimeUnit.SECONDS);
				if (StringUtils.isNoneBlank(proxyAddress)) {
					return proxyAddress;
				} else {
					refreshProxyPool();
				}
			}

		} catch (Exception e) {
			return null;
		}
	}

	public synchronized static void resetProxyAddress() {
		try {
			while (true) {
				String proxyAddress = proxyAddresss.poll(500, TimeUnit.MILLISECONDS);
				if (StringUtils.isNoneBlank(proxyAddress)) {
					currentProxyAddress = proxyAddress;
					break;
				} else {
					refreshProxyPool();
					logger.error("重置代理完成");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			currentProxyAddress = null;
		}
	}

	public static String loadUrlContent(final String url) {
		final StringBuilder content = new StringBuilder();
		try {
			final URL oracle = new URL(url);
			final URLConnection conn = oracle.openConnection();
			final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String inputLine = null;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine + "  ");
			}
			in.close();
		} catch (final MalformedURLException e) {
			throw new IllegalStateException(e);
		} catch (final IOException e) {
			throw new IllegalStateException(e);
		}
		return content.toString();
	}

	public static void refreshProxyPool() {
		String url = "http://10.0.0.252:8899/api/Values?type=VPS&count=10";
		String result = ProxyUtil.loadUrlContent(url);
		if (StringUtils.isNoneBlank(result)) {
			String[] proxyAddresses = result.split("\\s+");
			for (String poxyAddress : proxyAddresses) {
				//遍历所有的ip地址，访问，https://www.baidu.com,看是否能访问
				//获取代理端口
				if (!ProxyUtil.proxyAddresss.contains(poxyAddress)) {
					try {
						ProxyUtil.proxyAddresss.put(poxyAddress);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}


}
