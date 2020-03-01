
package com.example.AutoskolaDemoWithSecurity;

import com.example.AutoskolaDemoWithSecurity.interceptors.AuthInterceptor;
import com.example.AutoskolaDemoWithSecurity.interceptors.LogInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
        registry.addInterceptor(new AuthInterceptor())
                .excludePathPatterns("/authenticate/**"
                ,"/school/**"
                ,"/relationship/**"
                ,"/info/**");
    }
    
}
