
package com.example.AutoskolaDemoWithSecurity.tests;

import java.util.ArrayList;
import java.util.List;


public class TestDTO {
    
    private int id;
    
    private int number;
    
    private String type;
    
    private List<QuestionDTO> questions;

    public TestDTO() {
    
    }
    
    public TestDTO(Test test) {
        this.id = test.getId();
        this.number = test.getNumber();
        this.type = test.getType();
        this.questions = new ArrayList<>();
    }

    public TestDTO(int id, int number, String type, List<QuestionDTO> questions) {
        this.id = id;
        this.number = number;
        this.type = type;
        this.questions = questions;
    }

    public int getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
    
    public void addQuestion(QuestionDTO question) {
        if(questions == null) {
            questions = new ArrayList<>();
        }
        this.questions.add(question);
    }
    
}
