
package com.example.AutoskolaDemoWithSecurity.events.listeners;

import com.example.AutoskolaDemoWithSecurity.events.StatisticEvent;
import com.example.AutoskolaDemoWithSecurity.services.ServerStatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class StatisticEventListener implements ApplicationListener<StatisticEvent> {

    @Autowired
    private ServerStatisticService statisticService;
    
    @Override
    public void onApplicationEvent(StatisticEvent event) {
        this.statisticService.addResponse(event.getResponse(), event.getResponseTime());
    }
    
}
