package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class AnswerValue implements Serializable {
    private Map<String, AnswerValue> sub_question_answers;
    private String text;
    private String[] choices;
    private String Rating;

    public AnswerValue(Map<String, AnswerValue> sub_question_answers, String text, String[] choices, String rating) {
        this.sub_question_answers = sub_question_answers;
        this.text = text;
        this.choices = choices;
        Rating = rating;
    }

    public Map<String, AnswerValue> getSub_question_answers() {
        return sub_question_answers;
    }

    public void setSub_question_answers(Map<String, AnswerValue> sub_question_answers) {
        this.sub_question_answers = sub_question_answers;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String[] getChoices() {
        return choices;
    }

    public void setChoices(String[] choices) {
        this.choices = choices;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String rating) {
        Rating = rating;
    }
}
