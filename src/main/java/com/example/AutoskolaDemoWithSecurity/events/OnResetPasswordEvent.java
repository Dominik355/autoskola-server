
package com.example.AutoskolaDemoWithSecurity.events;

import com.example.AutoskolaDemoWithSecurity.models.databaseModels.User;
import org.springframework.context.ApplicationEvent;


public class OnResetPasswordEvent extends ApplicationEvent {

    private User user;
    private String newPassword;

    public OnResetPasswordEvent(User user, String newPassword, Object source) {
      super(source);
      this.user = user;
      this.newPassword = newPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
}
