package com.auxing.znhy.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by GY on 2015/10/14.
 */
public class JsonHelperUtil {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    public static HttpHeaders getJSONHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Content-Type", "application/json;charset=UTF-8");
        headers.set("Access-Control-Allow-Origin", "http://localhost:3000");
        headers.set("Access-Control-Allow-Methods", "POST");
        headers.set("Access-Control-Allow-Headers", "Content-Type");
        headers.set("Access-Control-Allow-Credentials", "true");
        return headers;
    }

    public static HttpHeaders getEntityHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static HttpHeaders getEntityHeaders(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Origin", "*");
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("jwt", jwt);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return headers;
    }

    public static HttpEntity getHeaderEntity(String jwt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("jwt", jwt);
        return new HttpEntity<String>("parameters", headers);
    }





}
