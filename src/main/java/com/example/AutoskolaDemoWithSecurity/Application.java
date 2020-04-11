 
package com.example.AutoskolaDemoWithSecurity;

import com.example.AutoskolaDemoWithSecurity.tests.QuestionRepository;
import com.example.AutoskolaDemoWithSecurity.tests.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.DispatcherServlet;


@SpringBootApplication
@EnableScheduling
@EnableEurekaClient
@EnableAsync
public class Application {
    
    @Autowired
    private DispatcherServlet servlet;
    
    @Autowired
    TestRepository testRepository;
    
    @Autowired
    QuestionRepository questionRepository;
    
    @Bean
    @LoadBalanced 
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
    
    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder() {
        return WebClient.builder();
    }

    
    public static void main(String[] args) {
        
        SpringApplication.run(Application.class, args); 
        
    }

    
    @Bean
    public CommandLineRunner noHandlerFoundRunner(ApplicationContext context) {
        servlet.setThrowExceptionIfNoHandlerFound(true);
        return args -> {};
        
    }
    /*
    @Bean
    public ServletWebServerFactory servletContainer() {
        //Enable SSL traffic
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory(){
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint constraint = new SecurityConstraint();
                //ak pouzijeme Confidential alebo Integral ako security constraint, vseobecne to znamena ze je pozadovane SSL
                /*
                Specify CONFIDENTIAL when the application requires that data be transmitted so
                as to prevent other entities from observing the contents of the transmission.
                Specify INTEGRAL when the application requires that the data be sent between client
                and server in such a way that it cannot be changed in transit.
                Specify NONE to indicate that the container must accept the constrained
                requests on any connection, including an unprotected one.
                
                constraint.setUserConstraint("CONFIDENTIAL"); 
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                constraint.addCollection(collection);
                context.addConstraint(constraint);
            }             
        };
        
        tomcat.addAdditionalTomcatConnectors(hhtpToHttpsRedirectConncetor());
        return tomcat;
    }
   
    private Connector hhtpToHttpsRedirectConncetor() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setPort(8080);
        connector.setSecure(false);
        connector.setRedirectPort(8443);
        return connector;
    }
     */
}