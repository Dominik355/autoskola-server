
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.errorApi.customExceptions.UpdatePasswordException;
import com.example.AutoskolaDemoWithSecurity.events.OnResetPasswordEvent;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.otherModels.MyUserDetails;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdateEmailRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UpdatePasswordRequest;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.UserDTO;
import com.example.AutoskolaDemoWithSecurity.repositories.UserRepository;
import com.example.AutoskolaDemoWithSecurity.utils.RoleUtil;
import java.util.Locale;
import java.util.Optional;
import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MyUserDetailsService implements UserDetailsService {
    
    @Autowired
    private RoleUtil roleUtil;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder bcryptEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private TemporaryPasswordService temporaryPasswordService;
    
    @Autowired
    private NotificationMessageService notificationService;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private MessageSource messageSource;
    

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      Optional<User> user = this.userRepository.findByEmail(email);
      user.orElseThrow(() -> new EntityNotFoundException("User Not Found: " + email));
      return user.map(MyUserDetails::new).get();
    }

    public User loadUserWithUsername(String email) {
        Optional<User>  user = this.userRepository.findByEmail(email);
        user.orElseThrow(() -> new EntityNotFoundException("User Not Found: " + email));
        return user.get();
    }

    public ResponseEntity addNewUser(UserDTO userDTO) throws MessagingException {
        if (this.userRepository.existsByEmail(userDTO.getEmail())) {
          throw new EntityExistsException(messageSource.getMessage("email.inUse", null, Locale.ROOT));
        }
        User userToBeSaved = new User(userDTO);
        userToBeSaved.setActive(true); // toto dat potom preč a dat naspet odosielanie emailu
        userToBeSaved.setPassword(this.bcryptEncoder.encode(userDTO.getPassword()));
        userToBeSaved.setRoles(roleUtil.rolesCorrection(userToBeSaved.getRoles()));
        
        //mailService.sendWelcomeEmail(userToBeSaved); // odskusa, ci dany email je skutocny
        this.userRepository.save(userToBeSaved);
        //this.applicationEventPublisher.publishEvent(
                //new OnRegistrationCompleteEvent(userToBeSaved));
        return ResponseEntity.ok(messageSource.getMessage("user.registered", null, Locale.ROOT));
    }

    
    public ResponseEntity updatePassword(UpdatePasswordRequest changePasswordRequest) {
        MyUserDetails principals = ((MyUserDetails) SecurityContextHolder
                  .getContext().getAuthentication().getPrincipal());
        int userID = (int) principals.getId();
        String userEmail = principals.getUsername();

        if (this.authenticationManager != null)
        { this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, changePasswordRequest.getOldPassword())); }
        else {
            throw new UpdatePasswordException(messageSource.getMessage("password.original.wrong", null, Locale.ROOT));
        }

        if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword())) {
            throw new UpdatePasswordException(messageSource.getMessage("password.match", null, Locale.ROOT));
        }

        User user = userRepository.getOne(userID);
        user.setPassword(this.bcryptEncoder.encode(changePasswordRequest.getNewPassword()));
        System.out.println(messageSource.getMessage("password.changed", null, Locale.ROOT));
        return ResponseEntity.ok(this.userRepository.save(user));
    }

    
    public ResponseEntity updateEmail(UpdateEmailRequest changeEmailRequest) {
        int userID = (int) ((MyUserDetails) SecurityContextHolder
                  .getContext().getAuthentication().getPrincipal()).getId();
        User user = userRepository.getOne(userID);
        if(user.getEmail().equals(changeEmailRequest.getNewEmail())) {
            return new ResponseEntity(messageSource.getMessage("email.alreadyUse", null, Locale.ROOT), HttpStatus.BAD_REQUEST);
        }
        user.setEmail(changeEmailRequest.getNewEmail());
        this.userRepository.save(user);
        return ResponseEntity.ok(messageSource.getMessage("email.changed", null, Locale.ROOT));
    }

    
    public void activateUser(User user) {
        User changedUser = userRepository.getOne(user.getId());
        user.setActive(true);
        this.userRepository.save(changedUser);
    }
    
    
    public ResponseEntity resetPassword(String email) throws InterruptedException {
        String temporaryPassword;
        temporaryPassword = this.temporaryPasswordService.createRandomPassword();
        User user = loadUserWithUsername(email);

        this.applicationEventPublisher.publishEvent(new OnResetPasswordEvent(user, temporaryPassword, user));
        temporaryPassword = this.bcryptEncoder.encode(temporaryPassword);
        user.setPassword(temporaryPassword);
        this.userRepository.save(user);
        return ResponseEntity.ok(messageSource.getMessage("password.send", new Object[] {email}, Locale.ROOT));
    }
      
    
    public String logOut() {
       return "Logout succesfull, "+notificationService.logOutEmitter( SecurityContextHolder
                    .getContext().getAuthentication().getName());
    }
    
    @PostConstruct
    private void createAdmin() {
        if(!userRepository.existsByEmail("bima.autoskola@gmail.com")) {
            User userToBeSaved = new User(new UserDTO("Dominik Admin", "Dominik21", "bima.autoskola@gmail.com", "0111222333", "admin"));
            userToBeSaved.setActive(true);
            userToBeSaved.setPassword(this.bcryptEncoder.encode(userToBeSaved.getPassword()));
            userToBeSaved.setRoles(roleUtil.rolesCorrection(userToBeSaved.getRoles()));
            this.userRepository.save(userToBeSaved);
        } 
    }
    
}