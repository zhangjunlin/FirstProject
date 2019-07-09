package com.auxing.znhy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auxing.znhy.util.RedisUtil;

@RestController
@RequestMapping(value = "/redis")
public class RedisTestController {

    @Autowired
    private RedisUtil redisUtil;
    
    @RequestMapping("/setValue")
    public String setValue(){
        if(!redisUtil.hasKey("RedisTest_Key")){
        	redisUtil.set("RedisTest_Key", "RedisTest_我是值");
            return "使用redis缓存保存数据成功";
        }else{
        	redisUtil.del("RedisTest_Key");
            return "key已存在";
        }
    }

    @RequestMapping("/getValue")
    public String getValue(){

        if(!redisUtil.hasKey("RedisTest_Key")){
            return "key不存在，请先保存数据";
        }else{
            System.out.println("进入方法开始读取数据");
            String value = (String) redisUtil.get("RedisTest_Key");;//根据key获取缓存中的val
            return "获取到缓存中的数据：RedisTest_Key="+value;
        }
    }
}
