package com.webzino.milkdelightuser.Model;

public class Faq_Model {

    String faq_id,question,answer;

    public Faq_Model(String faq_id, String question, String answer) {
        this.faq_id = faq_id;
        this.question = question;
        this.answer = answer;
    }

    public String getFaq_id() {
        return faq_id;
    }

    public void setFaq_id(String faq_id) {
        this.faq_id = faq_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
