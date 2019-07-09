package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class DemoApplicationTests {

	@Autowired
	private Environment env;
	
	@Test
	public void contextLoads() {
		System.out.println(env.getProperty("pwd"));
		//map.put("title", new String(env.getProperty("com.zyd.title2").getBytes("ISO-8859-1"), "UTF-8"));
	}

}
