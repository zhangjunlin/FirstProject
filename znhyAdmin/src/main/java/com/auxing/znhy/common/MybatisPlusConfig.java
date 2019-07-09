package com.auxing.znhy.common;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;

//分页插件
@EnableTransactionManagement
@Configuration
@MapperScan("com.auxing.znhy.service.*.mapper*")
public class MybatisPlusConfig {
   
	/**
     *	 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }
}
