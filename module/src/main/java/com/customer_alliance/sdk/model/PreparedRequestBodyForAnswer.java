package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.Map;

public class PreparedRequestBodyForAnswer implements Serializable {
    private String hash;
    private String token;
    private String version;
    private Map<String, AnswerValue> question_answers;

    public PreparedRequestBodyForAnswer(String hash, String token, String version, Map<String, AnswerValue> question_answers) {
        this.hash = hash;
        this.token = token;
        this.version = version;
        this.question_answers = question_answers;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Map<String, AnswerValue> getQuestion_answers() {
        return question_answers;
    }

    public void setQuestion_answers(Map<String, AnswerValue> question_answers) {
        this.question_answers = question_answers;
    }

    @Override
    public String toString() {
        return "PreparedRequestBodyForAnswer{" +
                "hash='" + hash + '\'' +
                ", token='" + token + '\'' +
                ", version='" + version + '\'' +
                ", question_answers=" + question_answers +
                '}';
    }
}
