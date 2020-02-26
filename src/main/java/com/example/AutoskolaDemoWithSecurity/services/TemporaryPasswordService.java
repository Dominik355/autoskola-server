
package com.example.AutoskolaDemoWithSecurity.services;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TemporaryPasswordService {
    
    private static final int LENGTH = 12;
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String PASSWORD_ALLOW = shuffleString(CHAR_LOWER + CHAR_UPPER + NUMBER);

    private static SecureRandom random = new SecureRandom();

     public String createRandomPassword() {
        boolean capitalFlag = false;
        boolean loweCaseFlag = false;
        boolean numberFlag = false;
        StringBuilder sb = new StringBuilder(LENGTH);
        
        for (int i = 0; i < LENGTH; i++) {

            int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
            char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);

            if(Character.isDigit(rndChar)){
                numberFlag = true;
            } else if (Character.isLowerCase(rndChar)) {
                loweCaseFlag = true;
            } else if (Character.isUpperCase(rndChar)) {
                capitalFlag = true;
            }

            sb.append(rndChar);
        }
        if(!capitalFlag) {
            sb.append(CHAR_UPPER.charAt(random.nextInt(CHAR_UPPER.length())));
        }
        if(!loweCaseFlag) {
            sb.append(CHAR_LOWER.charAt(random.nextInt(CHAR_LOWER.length())));
        }
        if(!numberFlag) {
            sb.append(NUMBER.charAt(random.nextInt(NUMBER.length())));
        }
        
        return sb.toString();
     }
     
     public static String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }

}
