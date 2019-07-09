package com.auxing.znhy.util;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
	public static String pwdEncrypt(String str){
		return DigestUtils.md5Hex(str);
	}
}
