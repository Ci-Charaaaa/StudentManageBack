package com.java.studentmanage.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;
    //配置跨域访问
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*") //任意域名
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") //允许的前端请求的种类
                .allowedHeaders("*") //任意请求头
                .allowCredentials(true);
    }
    //注册拦截器，除了登录/注册
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") //拦截所有
                .excludePathPatterns("/api/login", "/api/register"); //除了登录/注册
    }
}
