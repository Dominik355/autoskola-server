
package com.example.AutoskolaDemoWithSecurity.tests;


public class QuestionDTO {
    
    private int id;
    
    private Answers answers;
    
    private String question;
    
    private String type;
    
    private int points;
    
    private int questionOrder;
    
    private int correctAnswer;
    
    private boolean hasPhoto;

    public QuestionDTO() {
        
    }
    
    public QuestionDTO(Question question) {
        this.id = question.getId();
        this.answers = new Answers(question.getAnswer_1(), question.getAnswer_2(), question.getAnswer_3());
        this.question = question.getQuestion();
        this.type = question.getType();
        this.points = question.getPoints();
        this.correctAnswer = question.getCorrectAnswer();
        this.hasPhoto = question.getHasPhoto();
    }

    public QuestionDTO(int id, String answer_1, String answer_2, String answer_3, String question, String type, int points, int questionOrder, int correctAnswer, String photo) {
        this.id = id;
        this.answers = new Answers(answer_1, answer_2, answer_3);
        this.question = question;
        this.type = type;
        this.points = points;
        this.questionOrder = questionOrder;
        this.correctAnswer = correctAnswer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAnswers(Answers answers) {
        this.answers = answers;
    }

    public Answers getAnswers() {
        return answers;
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

    public int getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean getHasPhoto() {
        return this.hasPhoto;
    }

    public void setHasPhoto(boolean hasPhoto) {
        this.hasPhoto = hasPhoto;
    }
    
}
