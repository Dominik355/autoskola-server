
package com.example.AutoskolaDemoWithSecurity.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//Neberie ohlad na async requesty ... ak tent orequest vyvola druhe vlakno, na ktoreho dokoncenie nemusi cakat
// a trva aj po vrateni responsu, tak nie je tento cas zahrnuty...si myslim
// a samozrejme ani cas vo filtri, ale, neviem ako naraz zapocitat aj cas vo filtri aj v programe samotnom, ked spolu nesuvisia
// dalo by sa cez AOP, ale to bude nepresnejsie este, lebo nezarata cas vo filtri, ani v interceptore na kontrolu hlavicky
@Component
public class LogInterceptor extends HandlerInterceptorAdapter {

    Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long endTime = System.currentTimeMillis();
        long startTime=Long.parseLong(request.getAttribute("startTime")+"");
        log.info("Total time taken to process request: "+(endTime-startTime)*0.001+" seconds");
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        return true;
    }
    
}
