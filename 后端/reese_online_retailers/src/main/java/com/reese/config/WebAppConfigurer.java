package com.reese.config;

import com.reese.interceptor.SysInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/*
* web项目配置类
* */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "HEAD", "POST", "PUT",
                        "DELETE","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /*
        * 路径映射
        * */
        registry.addResourceHandler("/image/swiper/**").addResourceLocations("file:D:\\my_material\\swiperImgs\\");
        registry.addResourceHandler("/image/bigType/**").addResourceLocations("file:D:\\my_material\\bigTypeImgs\\");
        registry.addResourceHandler("/image/product/**").addResourceLocations("file:D:\\my_material\\productImgs\\");
        registry.addResourceHandler("/image/productSwiper/**").addResourceLocations("file:D:\\my_material\\productSwiperImgs\\");
        registry.addResourceHandler("/image/productIntro/**").addResourceLocations("file:D:\\my_material\\productIntroImgs\\");
        registry.addResourceHandler("/image/productPara/**").addResourceLocations("file:D:\\my_material\\productParaImgs\\");
    }

    @Bean
    public SysInterceptor sysInterceptor(){
        return new SysInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] patterns=new String[]{"/adminLogin","/product/**","/bigType/**","/user/wxlogin","/weixinpay/**"};
        registry.addInterceptor(sysInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(patterns);
    }
}
