
package com.example.AutoskolaDemoWithSecurity.events.listeners;


import com.example.AutoskolaDemoWithSecurity.events.OnResetPasswordEvent;
import com.example.AutoskolaDemoWithSecurity.services.MailService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
        try {
            sendNewPassword(event);
        } catch (MessagingException ex) {
            Logger.getLogger(ResetPasswordListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendNewPassword(OnResetPasswordEvent event) throws MessagingException {
        mailService.sendResetPasswordEmail(event);
    } 
  
}