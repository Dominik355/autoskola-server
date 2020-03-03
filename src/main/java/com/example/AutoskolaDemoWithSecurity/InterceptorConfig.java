
package com.example.AutoskolaDemoWithSecurity;

import com.example.AutoskolaDemoWithSecurity.interceptors.AuthInterceptor;
import com.example.AutoskolaDemoWithSecurity.interceptors.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// nevytvarat nove instancie rovno do addInterceptor(new interceptor) -- vyhadzovalo mi to nullpointerException
// v authinterceptore na vsetky autowiring..btw, 2  tyzdne to islo vpohode aj hentak
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    
    @Autowired
    private LogInterceptor logInterceptor;
    
    @Autowired
    private AuthInterceptor authInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor);
        registry.addInterceptor(authInterceptor)
                .excludePathPatterns("/authenticate/**"
                ,"/school/**"
                ,"/relationship/**"
                ,"/info/**"
                ,"/tests/**");
    }
    
}
