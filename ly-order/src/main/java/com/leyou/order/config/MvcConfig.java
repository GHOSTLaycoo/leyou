package com.leyou.order.config;

import com.leyou.order.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author GHOSTLaycoo
 * @date 2020/1/14 0014 - 上午 11:50
 */
@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class MvcConfig implements WebMvcConfigurer{

    @Autowired
    private JwtProperties prop;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor(prop)).addPathPatterns("/order/**");
    }
}
