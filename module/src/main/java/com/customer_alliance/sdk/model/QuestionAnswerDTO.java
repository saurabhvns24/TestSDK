package com.customer_alliance.sdk.model;

import java.util.Map;

public class QuestionAnswerDTO {
    public String hash;
    public String token;
    public String version;
    public Map<String, SubQuestionAnswerDTO> question_answers;

}
