
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
import java.util.Optional;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
      System.out.println("Inside loadUserByUsername(): " + email);
      Optional<User> user = this.userRepository.findByEmail(email);
      user.orElseThrow(() -> new EntityNotFoundException("User Not Found: " + email));
      return user.map(MyUserDetails::new).get();
    }

    public User loadUserWithUsername(String email) {
      System.out.println("Inside loadUserWithUsername(): "+email);
      Optional<User>  user = this.userRepository.findByEmail(email);
      user.orElseThrow(() -> new EntityNotFoundException("User Not Found: " + email));
      return user.get();
    }

    public ResponseEntity addNewUser(UserDTO userDTO, HttpServletRequest request) {
      if (this.userRepository.existsByEmail(userDTO.getEmail())) {
        throw new EntityExistsException("This email is already in use!");
      }
      User userToBeSaved = new User(userDTO);
      userToBeSaved.setActive(true); // toto dat potom preč a dat naspet odosielanie emailu
      userToBeSaved.setPassword(this.bcryptEncoder.encode(userDTO.getPassword()));
      userToBeSaved.setRoles(roleUtil.rolesCorrection(userToBeSaved.getRoles()));
      User user = this.userRepository.save(userToBeSaved);
      if (user == null) {
        throw new PersistenceException("Something went wrong, User " + userToBeSaved.getEmail() + " could not be saved");
      }
      
        //String appUrl = "https://" + request.getServerName() + ":" + request.getServerPort();
       // this.applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(
         //       appUrl, request.getLocale(), userToBeSaved));
      return ResponseEntity.ok("User was succesfuly registered, please confirm verification in your email within 24 hours");
    }

    
    public ResponseEntity updatePassword(UpdatePasswordRequest changePasswordRequest) {
      MyUserDetails principals = ((MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal());
      int userID = (int) principals.getId();
      String userEmail = principals.getUsername();

      if (this.authenticationManager != null)
      { this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userEmail, changePasswordRequest.getOldPassword())); }
      else {
          throw new UpdatePasswordException("Wrong original password!");
      }

      if (changePasswordRequest.getNewPassword().equals(changePasswordRequest.getOldPassword())) {
          throw new UpdatePasswordException("Passwords match! Please choose else new password");
      }

      User user = userRepository.getOne(userID);
      user.setPassword(this.bcryptEncoder.encode(changePasswordRequest.getNewPassword()));
      System.out.println("Password succesfully changed");
      return ResponseEntity.ok(this.userRepository.save(user));
    }

    
    public ResponseEntity updateEmail(UpdateEmailRequest changeEmailRequest) {
      int userID = (int) ((MyUserDetails) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).getId();
      User user = userRepository.getOne(userID);
      if(user.getEmail().equals(changeEmailRequest.getNewEmail())) {
          return new ResponseEntity("You already use this email", HttpStatus.BAD_REQUEST);
      }
      user.setEmail(changeEmailRequest.getNewEmail());
      System.out.println("Email succesfully changed");
      return ResponseEntity.ok(this.userRepository.save(user));
    }

    
    public void activateUser(User user) {
        // vrati iba instanciu, takze save je vlastne UPDATE
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
      return ResponseEntity.ok("Your new password has been sent to: " + email);
    }
      
}