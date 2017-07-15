package com.example.mahendran.teacherspet.DiscussionRoom;

import java.io.Serializable;

/**
 * Created by Mahendran on 09-06-2017.
 */

public class DiscussionboardValues implements Serializable {
    private String question;
    private String answer;
    private String id;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
