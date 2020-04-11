
package com.example.AutoskolaDemoWithSecurity.events;

import javax.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEvent;


public class StatisticEvent extends ApplicationEvent {
    
    private HttpServletResponse response;
    
    private double responseTime;
    
    public StatisticEvent(Object source, HttpServletResponse response, double responseTime) {
        super(source);
        this.response = response;
        this.responseTime = responseTime;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public double getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(double responseTime) {
        this.responseTime = responseTime;
    }
    
}
