
package com.example.AutoskolaDemoWithSecurity.interceptors;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.CustomLoginException;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.repositories.RelationshipRepository;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

//ak pouzijem sendError() - tak sa interceptor zavola este raz - neviem preco 
@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RelationshipRepository repository;
    
    @Autowired
    private UserRepository userRepository;
    
    private static Logger log = LoggerFactory.getLogger(AuthInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("AuthInterceptor - preHandle()");
        String relationId = request.getHeader("Relation");
        if(relationId == null) {
            
            throw new IOException("Exception hodena v interceptori");
            
        } else if (relationId != null && relationId != "") {
            
            System.out.println("RelationID is : "+relationId);
            User user = userRepository.findByEmail(
                    SecurityContextHolder.getContext().getAuthentication().getName()).get();
            int relationID = Integer.parseInt(relationId);
            
            if(repository.existsByUserAndId(user, relationID)) {
                return true;
            } else {
                throw new CustomLoginException("Incorrect RelationID in header. ID does not exist or is not yours");
            }
            
        } else throw new IOException("Exception v interceptori, hlavicka existuje, ale nieje zadana hodnota");

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("AuthInterceptor - postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("AuthInterceptor - afterCompletion()");
    }  
    
}
