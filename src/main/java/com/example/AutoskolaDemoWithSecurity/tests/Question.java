
package com.example.AutoskolaDemoWithSecurity.tests;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Question implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(insertable = false, updatable = false, unique = true, nullable = false)
    private int id;
    
    private String answer_1;
    
    private String answer_2;

    private String answer_3;
    
    private String question;
    
    private String type;
    
    private int points;

    @Column(name = "correct_answer")
    private int correctAnswer;
    
    @Column(name = "has_photo")
    private boolean hasPhoto;

    public Question() {
    
    }

    public Question(QuestionDTO questionDTO) {
        this.answer_1 = questionDTO.getAnswers().getAnswer_1();
        this.answer_2 = questionDTO.getAnswers().getAnswer_2();
        this.answer_3 = questionDTO.getAnswers().getAnswer_3();
        this.question = questionDTO.getQuestion();
        this.type = questionDTO.getType();
        this.points = questionDTO.getPoints();
        this.correctAnswer = questionDTO.getCorrectAnswer();
        this.hasPhoto = false;
    }
    
    public Question(String answer_1, String answer_2, String answer_3
            , String question, String type, int points, int correctAnswer) {
        this.answer_1 = answer_1;
        this.answer_2 = answer_2;
        this.answer_3 = answer_3;
        this.question = question;
        this.type = type;
        this.points = points;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public String getAnswer_1() {
        return answer_1;
    }

    public void setAnswer_1(String answer_1) {
        this.answer_1 = answer_1;
    }

    public String getAnswer_2() {
        return answer_2;
    }

    public void setAnswer_2(String answer_2) {
        this.answer_2 = answer_2;
    }

    public String getAnswer_3() {
        return answer_3;
    }

    public void setAnswer_3(String answer_3) {
        this.answer_3 = answer_3;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean getHasPhoto() {
        return hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }

}
