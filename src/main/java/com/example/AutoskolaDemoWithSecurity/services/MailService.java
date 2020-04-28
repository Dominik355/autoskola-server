
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.events.OnRegistrationCompleteEvent;
import com.example.AutoskolaDemoWithSecurity.events.OnResetPasswordEvent;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;



@Service
public class MailService {
    
    @Autowired
    private MessageSource messageSource;

    @Value("${spring.mail.username}")
    private static final String EMAIL = "bima.autoskola@gmail.com";
    
    @Value("${get.fullURL}")
    private String serverUrl;
    
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;
    
    
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) throws MessagingException {
        
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(EMAIL);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            log.warn("Email could not be sent to user '{}'", to, e);
            throw new MessagingException(messageSource.getMessage("email.notSend", new Object[] {to}, Locale.ROOT)+to);
            }
        }
    
    
    public void sendActivationEmail(OnRegistrationCompleteEvent event, String token) throws MessagingException {
        sendEmail(event.getUser().getEmail()
                , "Registration Confirmation"
                , "Your verification link: " + this.serverUrl + "/authenticate/registrationConfirm?token=" + token
                , false
                , false);
    }
    
    public void sendResetPasswordEmail(OnResetPasswordEvent event) throws MessagingException {
        sendEmail(event.getUser().getEmail()
                , "Password reset"
                , "Your new generated password: " + event.getNewPassword()
                , false
                , false);
    }
    
    public void sendWelcomeEmail(User user) throws MessagingException {
        sendEmail(user.getEmail()
                , "Welcome to Autoskola App"
                , "Heart welcome at our bachelor project for driving schools"
                , false
                , false);
    }

}


