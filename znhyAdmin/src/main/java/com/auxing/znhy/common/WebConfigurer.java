package com.auxing.znhy.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.auxing.znhy.filter.LoginInterceptor;
 
@Configuration
public class WebConfigurer extends WebMvcConfigurationSupport {
	/**
	 * addPathPatterns用来设置拦截路径，
	 * excludePathPatterns用来设置白名单，
	 * 也就是不需要触发这个拦截器的路径。
	 */
	@Autowired
	private LoginInterceptor loginInterceptor;
    // 这个方法是用来配置静态资源的，比如html，js，css，等等
  @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	  registry.addResourceHandler("swagger-ui.html")
      .addResourceLocations("classpath:/META-INF/resources/");
	  registry.addResourceHandler("/webjars/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/");	
	  registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	  super.addResourceHandlers(registry);
	 
  }
 
    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
  @Override
    public void addInterceptors(InterceptorRegistry registry) {
    	System.out.println("注册拦截器");
    	String [] excludes = new String[]{"/login/**","/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**","/static/**"};
    	registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(excludes);
        super.addInterceptors(registry);
   }
   
}
