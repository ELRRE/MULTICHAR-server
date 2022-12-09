package com.example.multichar;

import com.example.multichar.interceptors.AuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;

@Configuration
public class Config implements WebMvcConfigurer {
    final AuthenticationInterceptor authenticationInterceptor;

    public Config(AuthenticationInterceptor authenticationInterceptor) {
        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("*")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        ArrayList<String> excludePaths = new ArrayList<>();
        excludePaths.add("/api/user/registration");
        excludePaths.add("/api/user/login");
        excludePaths.add("/api/user/tokens/refresh");
        registry.addInterceptor(authenticationInterceptor).addPathPatterns("/api/**").excludePathPatterns(excludePaths);
    }

}