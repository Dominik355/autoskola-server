
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@Entity
@EnableTransactionManagement
@Table(name = "verification_tokens")
public class VerificationToken implements Serializable {
    
  private static final int EXPIRATION_DAYS = 7;
  
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(updatable = false, nullable = false, insertable = false, unique = true)
  private Long id;
  
  @NotEmpty
  private String token;
  
  @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private User user;
  
  private Timestamp expiryDate;
  
  
  public VerificationToken() {
  
  }
  
  public VerificationToken(String token, User user) {
    this.token = token;
    this.user = user;
    this.expiryDate = new Timestamp(calculateExpiryDate(EXPIRATION_DAYS).getTime());
  } 

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Timestamp expiryDate) {
        this.expiryDate = expiryDate;
    }
  
    private Date calculateExpiryDate(int expiryTimeInDays) { 
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.DAY_OF_MONTH, expiryTimeInDays);
        return new Date(cal.getTime().getTime());
  }

    @Override
    public String toString() {
        return "id: "+this.getId()+", expiration date: "+this.getExpiryDate();
    }
    
}