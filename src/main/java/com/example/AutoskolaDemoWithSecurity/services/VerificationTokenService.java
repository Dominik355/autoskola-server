
package com.example.AutoskolaDemoWithSecurity.services;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.databaseModels.VerificationToken;
import com.example.AutoskolaDemoWithSecurity.repositories.VerificationTokenRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class VerificationTokenService
{

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public User getUser(String verificationToken) {
        Optional<VerificationToken> token = this.tokenRepository.findByToken(verificationToken);
        token.orElseThrow(() -> new NoSuchElementException("User not found with String Token as parameter: " + verificationToken));
        return (token.get()).getUser();
    }

    public VerificationToken getVerificationToken(String verificationToken) {
        Optional<VerificationToken> token = this.tokenRepository.findByToken(verificationToken);
        token.orElseThrow(() -> new NoSuchElementException("Verification token not found with Token as parameter: " + verificationToken));
        return token.get();
    }

    public VerificationToken getVerificationToken(User user) {
        Optional<VerificationToken> token = this.tokenRepository.findByUser(user);
        token.orElseThrow(() -> new NoSuchElementException("Verification token not found with User as a parameter: " + user));
        return token.get();
    }

    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        try {
            this.tokenRepository.save(myToken); 
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not create verification token for user: " + user);
        }
    }
    
    public void deleteVerificationToken(User user) {
        int userId = user.getId();
        List<VerificationToken> deletedTokens = this.tokenRepository.deleteByUser_id(userId);
        System.out.println("Following tokens related to user \"\" were deleted :");
        Objects.requireNonNull(System.out); deletedTokens.stream().forEach(System.out::println);
    } 
  
}