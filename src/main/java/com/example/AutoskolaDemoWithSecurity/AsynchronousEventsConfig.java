
package com.example.AutoskolaDemoWithSecurity;

import com.example.AutoskolaDemoWithSecurity.events.listeners.StatisticEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class AsynchronousEventsConfig {
    
    @Autowired
    private StatisticEventListener statisticEventListener;
    
    @Bean(name = "AsyncEventMulticaster")
    public ApplicationEventMulticaster getEventMulticaster() {
        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor("AsyncEvent"));
        eventMulticaster.addApplicationListener(statisticEventListener);
        return eventMulticaster;
    }
    
    @Bean
    public ResourceBundleMessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        source.setUseCodeAsDefaultMessage(true);
        return source;
    }
    
}
