
package com.example.AutoskolaDemoWithSecurity.events.listeners;

import com.example.AutoskolaDemoWithSecurity.events.OnRegistrationCompleteEvent;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.services.VerificationTokenService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;



@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
      sendVerificationemail(event);
    }

    private void sendVerificationemail(OnRegistrationCompleteEvent event) {
      User user = event.getUser();
      String token = UUID.randomUUID().toString();
      this.verificationTokenService.createVerificationToken(user, token);
      String recipientAddress = user.getEmail();
      String subject = "Registration Confirmation";
      String confirmationURL = "Your verification link: " + event.getAppUrl() + "/registrationConfirm?token=" + token;
      SimpleMailMessage email = new SimpleMailMessage();
      email.setFrom("bima.autoskola@gmail.com");
      email.setTo(recipientAddress);
      email.setSubject(subject);
      email.setText(confirmationURL);
      System.out.println("Sending email ");
      try {
          this.mailSender.send(email); 
      } catch (MailException e) {
          throw new MailException("Could not send Email to address: " + recipientAddress) {}; 
      }
    }
}