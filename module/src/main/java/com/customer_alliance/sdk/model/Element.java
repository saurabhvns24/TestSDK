package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.List;

public class Element implements Serializable {

    private String type;
    private Question question;
    private List<SubElementCondition> sub_element_conditions;

    public Element(String type, Question question, List<SubElementCondition> sub_element_conditions) {
        this.type = type;
        this.question = question;
        this.sub_element_conditions = sub_element_conditions;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<SubElementCondition> getSub_element_conditions() {
        return sub_element_conditions;
    }

    public void setSub_element_conditions(List<SubElementCondition> sub_element_conditions) {
        this.sub_element_conditions = sub_element_conditions;
    }
}
