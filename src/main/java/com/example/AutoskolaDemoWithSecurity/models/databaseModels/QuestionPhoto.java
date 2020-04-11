
package com.example.AutoskolaDemoWithSecurity.models.databaseModels;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class QuestionPhoto implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true, nullable = false)
    private int id;
    
    @Column(name = "question_id", unique = true)
    @NotNull
    private int question;
    
    private byte[] picture;

    public QuestionPhoto() {
    
    }

    public QuestionPhoto(int question) {
        this.question = question;
    }
    
    public QuestionPhoto(int question, byte[] picture) {
        this.question = question;
        this.picture = picture;
    }

    public int getId() {
        return id;
    }

    public int getQuestion() {
        return question;
    }

    public void setQuestion(int question) {
        this.question = question;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    
}
