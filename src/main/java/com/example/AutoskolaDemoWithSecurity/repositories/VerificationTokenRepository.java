
package com.example.AutoskolaDemoWithSecurity.repositories;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import com.example.AutoskolaDemoWithSecurity.models.transferModels.VerificationToken;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String paramString);

    Optional<VerificationToken> findByUser(User paramUser);

    List<VerificationToken> deleteByUser_id(int paramInt);

    List<VerificationToken> deleteByToken(String paramString);

    List<VerificationToken> deleteByExpiryDate(Date paramDate);
    
}
