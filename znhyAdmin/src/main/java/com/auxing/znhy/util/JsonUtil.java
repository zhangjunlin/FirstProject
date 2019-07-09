package com.auxing.znhy.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by GY on 2016/1/19.
 */
public class JsonUtil {

    //JSONObject转成JSON字符串
    public static String jsonObjToJsonStr(JSONObject jsonObject) {

        return JSONObject.toJSONString(jsonObject);
    }

    //JSONObject转成Java对象
    public static <T> T jsonObjToJavaObj(JSONObject jsonObject, Class<T> clazz) {

        return JSONObject.toJavaObject(jsonObject, clazz);

    }

    //Json字符串转为JSONObject
    public static JSONObject jsonStrToJsonObj(String str) {

        return JSONObject.parseObject(str);
    }

    //Json字符串转为Java对象
    public static <T> T jsonStrToJavaObj(String str, Class<T> clazz) {

        return JSONObject.toJavaObject(JSONObject.parseObject(str), clazz);
    }

    //Java对象转为Json字符串
    public static <T> String javaObjToJsonStr(Class<T> clazz) {

        return JSONObject.toJSONString(clazz);
    }

    //Java对象转为JSONObject
    public static <T> JSONObject javaObjToJsonObj(Class<T> clazz) {

        return JSONObject.parseObject(JSONObject.toJSONString(clazz));
    }

    //listMap对象转成JsonArray
    public static JSONArray listToJsonArray(List<Map<String, Object>> list) {

        return JSONObject.parseArray(JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue));
    }

    //listMap对象转成JsonArray
    public static JSONArray listToJsonArrayWithDateFormat(List<Map<String, Object>> list) {

        return JSONObject.parseArray(JSONObject.toJSONStringWithDateFormat(list,"yyyy-MM-dd",SerializerFeature.WriteMapNullValue));
    }

    //Map对象转成JsonObj
    public static JSONObject mapToJsonObj(Map<String, Object> mapObj) {

        return mapObj != null && mapObj.size() != 0 ? JSONObject.parseObject(JSONObject.toJSONString(mapObj, SerializerFeature.WriteMapNullValue)) : null;
    }


    //将json当中所有为""的数据转成null
    public static JSONObject transformToNull(JSONObject jsonObject) {

        Object[] listKey = jsonObject.keySet().toArray();
        for (Object key : listKey) {
            if (StringUtils.isBlank(jsonObject.getString(key.toString()))) {
                jsonObject.put(key.toString(), null);
            }
        }
        return jsonObject;
    }

    public static void main(String[] args) {

        //System.out.println(jsonStrToJsonObj("{'a':123}").get("a"));

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("a", 123);

        System.out.println(jsonObjToJsonStr(jsonObject));
        //jsonObjToJsonStr


    }
}
