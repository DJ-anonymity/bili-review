package com.zfg.learn.config;

import com.zfg.learn.component.interceptor.ReviewManageInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * todo  学习bean的注入原理
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    /*拦截器的执行是在spring容器中bean初始化之前的，
    拦截器执行时，spring中我们定义的bean还未初始化，自然也就无法自动注入，无法使用。*/
    // 将拦截器bean化
    @Bean
    public ReviewManageInterceptor reviewManageInterceptor(){
        return new ReviewManageInterceptor();
    }

    // 注入拦截器时，不再是new一个拦截器对象，直接使用jwtInterceptor()方法
    //返回的拦截器对象
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(reviewManageInterceptor()).addPathPatterns("/admin/review/**")
                .excludePathPatterns("");
    }

    /*拦截器的执行是在spring容器中bean初始化之前的，
    拦截器执行时，spring中我们定义的bean还未初始化，自然也就无法自动注入，无法使用。*/
    // 将拦截器bean化

}
