
package com.example.AutoskolaDemoWithSecurity.events.listeners;

import com.example.AutoskolaDemoWithSecurity.events.OnRegistrationCompleteEvent;
import com.example.AutoskolaDemoWithSecurity.services.MailService;
import com.example.AutoskolaDemoWithSecurity.services.VerificationTokenService;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;



@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private MailService mailService;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            sendVerificationemail(event);
        } catch (MessagingException ex) {
            Logger.getLogger(RegistrationListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sendVerificationemail(OnRegistrationCompleteEvent event) throws MessagingException {
        String token = UUID.randomUUID().toString();
        this.verificationTokenService.createVerificationToken(event.getUser(), token);
        mailService.sendActivationEmail(event, token);
    }
}