
package com.example.AutoskolaDemoWithSecurity;


import com.example.AutoskolaDemoWithSecurity.errorApi.CustomAccessDeniedHandler;
import com.example.AutoskolaDemoWithSecurity.filters.FilterExceptionHandler;
import com.example.AutoskolaDemoWithSecurity.errorApi.JwtAuthenticationEntryPoint;
import com.example.AutoskolaDemoWithSecurity.filters.JwtRequestFilter;
import com.example.AutoskolaDemoWithSecurity.services.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
    
    @Autowired
    private FilterExceptionHandler exceptionFilter;
    
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    
    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() { 
        return new BCryptPasswordEncoder(); 
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean(); 
  }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
          http.csrf().disable()
          .authorizeRequests().antMatchers("/authenticate/**", "/info/**", "/tests/**")
          .permitAll().anyRequest().authenticated().and()
          .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
          .accessDeniedHandler(accessDeniedHandler()).and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

          http.addFilterBefore(exceptionFilter, CorsFilter.class);
          http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.myUserDetailsService); 
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui"
                , "/swagger-resources/**", "/configuration/**"
                , "/swagger-ui.html", "/webjars/**");
    }
    
}
