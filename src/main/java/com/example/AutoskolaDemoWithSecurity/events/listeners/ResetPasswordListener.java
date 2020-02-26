
package com.example.AutoskolaDemoWithSecurity.events.listeners;


import com.example.AutoskolaDemoWithSecurity.events.OnResetPasswordEvent;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


@Component
public class ResetPasswordListener implements ApplicationListener<OnResetPasswordEvent> {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(OnResetPasswordEvent event) {
      System.out.println("com.example.AutoskolaDemoWithSecurity.events.RegistrationListener.onApplicationEvent()");
      sendNewPassword(event);
    }

    private void sendNewPassword(OnResetPasswordEvent event) {
      User user = event.getUser();
      String newPassword = event.getNewPassword();
      SimpleMailMessage email = new SimpleMailMessage();
      email.setFrom("bima.autoskola@gmail.com");
      email.setTo(user.getEmail());
      email.setSubject("Password reset");
      email.setText("Your new generated password: " + newPassword);
      try {
          this.mailSender.send(email); 
      } catch (MailException e) {
          System.out.println("Problem with sending passwordReset email to: " + user.getEmail()); 
      }

    } 
  
}