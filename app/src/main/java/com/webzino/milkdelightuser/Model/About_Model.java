package com.webzino.milkdelightuser.Model;

public class About_Model {

    String about_id,question,answer;

    public About_Model(String about_id, String question, String answer) {
        this.about_id = about_id;
        this.question = question;
        this.answer = answer;
    }

    public About_Model( String question, String answer) {
        this.question = question;
        this.answer = answer;
    }



    public String getAboutid() {
        return about_id;
    }

    public void setAbout_id(String about_id) {
        this.about_id = about_id;
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
