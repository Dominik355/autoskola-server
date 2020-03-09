
package com.example.AutoskolaDemoWithSecurity;

import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
    
    //return customized, prepared Docket instance
    @Bean
    public Docket swaggerConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/**"))
                .apis(RequestHandlerSelectors.basePackage("com.example.AutoskolaDemoWithSecurity"))
                .build()
                .apiInfo(apiDetails());
    }
    
    private ApiInfo apiDetails() {
        return new ApiInfo(
                "Autoskola server",    //title
                "Bachelor project for reservation system",                         //description
                "1.0",   //version
                "https://www.google.com/search?q=permission+denied&sxsrf=ALeKk000sCMD6nAabrE8L1HOQrEil761bg:1583400402441&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjf85iUgoPoAhUS3aQKHXmnBaoQ_AUoAXoECA0QAw&biw=1682&bih=937",  //termsOfServiceUrl
                new springfox.documentation.service.Contact("Dominik Bílik", "", "bima.autoskola@gmail.com"),   //contact
                "Used only free versions of software",   //license
                "",   //licenseURL
                Collections.emptyList());  //vendorExtensions
    }
    
}
