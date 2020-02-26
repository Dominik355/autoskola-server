
package com.example.AutoskolaDemoWithSecurity.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoleUtil {
    
    public boolean isRole(String role) {
        System.out.println("com.example.AutoskolaDemoWithSecurity.utils.RoleUtil.isRole()");
        boolean flag = false;
        role = rolesCorrection(role);
        for(String k : role.split(",")) {
            if(k.equals(Roles.STUDENT.toString()) 
                    || k.equals(Roles.INSTRUCTOR.toString()) 
                    || k.equals(Roles.OWNER.toString())) {
                flag = true;
            } else {
                flag = false;
            }
        }
        return flag;
    }
    
    public String rolesCorrection(String roles) {
        System.out.println("com.example.AutoskolaDemoWithSecurity.utils.RoleUtil.rolesCorrection()");
        List<String> list = Arrays.stream(roles.split(",")).collect(Collectors.toList());
        roles = "";
        for(String s : list) {
            s = s.replaceAll("\\s+", "");
            roles += "ROLE_"+s.toUpperCase()+",";
        }
        return roles;
    }
    
    
    private enum Roles {
        STUDENT("ROLE_STUDENT"),
        INSTRUCTOR("ROLE_INSTRUCTOR"),
        OWNER("ROLE_OWNER");   
        
        public final String text;

        private Roles(String name) {
            this.text = name;
        }

        public String getName() {
            return text;
        }    

        @Override
        public String toString() {
            return text;
        }
    }
    
}
