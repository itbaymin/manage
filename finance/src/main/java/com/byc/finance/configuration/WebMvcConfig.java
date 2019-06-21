package com.byc.finance.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by baiyc
 * 2019/6/11/011 10:04
 * Description：
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${resource.location}")
    String resourceLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //配置静态资源的访问，addResourceHandler配置资源访问URI，addResourceLocations配置资源存放路径
        registry.addResourceHandler("/**").addResourceLocations(resourceLocation,"classPath:/static/");
    }
}
