 
package com.example.AutoskolaDemoWithSecurity;

import com.example.AutoskolaDemoWithSecurity.tests.QuestionRepository;
import com.example.AutoskolaDemoWithSecurity.tests.TestRepository;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.DispatcherServlet;


@SpringBootApplication
@EnableScheduling
public class Application {
    
    @Autowired
    private DispatcherServlet servlet;
    
    @Autowired
    TestRepository testRepository;
    
    @Autowired
    QuestionRepository questionRepository;
    
    public static void main(String[] args) {
        
        SpringApplication.run(Application.class, args); 
        
    }

    
    @Bean
    public CommandLineRunner noHandlerFoundRunner(ApplicationContext context) {
       /* 
        Question question1 = new Question("programovaci jazyk", "Nadoba na vino", "druh psa", "čo je to java?", "legal", 2, 1);
        Question question2 = new Question("lebo ho tak vidím", "lebo žltá je pekná", "lebo je to slnko", "preco je slnko zlte?", "legal", 2, 2);
        Question question3 = new Question("nič podstatné", "aby si šiel dalej", "že zastav", "Čo hovorí značka STOP ?", "legal", 2, 3);
        Question question4 = new Question("oboje", "ročné obdobie", "Framework pre javu", "Čo je Spring ?", "legal", 2, 1);
        Question question5 = new Question("Karpatské chrbáty", "Šteruská bažina", "Para brutálna zostava", "Aká je najlepšia SVK skupina?", "sign", 2, 3);
        Question question6 = new Question("ako bobinko", "docela fajn", "toto bude správne", "Ako sa dnes cítiš ?", "sign", 2, 3);
        Question question7 = new Question("redbull", "monster", "Hellko najlepsi energi", "Najelpší energy drink?", "crossing", 2, 3);
        Question question8 = new Question("IX", "X", "VII", "najlepšie EVO ?", "crossing", 2, 3);
        Question question9 = new Question("driftiky pičoviny na zadnom", "4x4", "Predný", "Najlepší náhon?", "crossing", 2, 1);
        questionRepository.save(question1);
        questionRepository.save(question2);
        questionRepository.save(question3);
        questionRepository.save(question4);
        questionRepository.save(question5);
        questionRepository.save(question6);
        questionRepository.save(question7);
        questionRepository.save(question8);
        questionRepository.save(question9);
       
        Test test1 = new Test(1, "ab", "152-1,157-2,160-3,155-4");
        Test test2 = new Test(2, "ab", "154-1,155-2,157-3,160-4");
        Test test3 = new Test(3, "ab", "159-1,156-2,152-3,154-4");
        testRepository.save(test3);
        testRepository.save(test2);
        testRepository.save(test1);*/
        
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