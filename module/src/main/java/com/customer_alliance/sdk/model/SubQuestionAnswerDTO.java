package com.customer_alliance.sdk.model;

import java.util.Map;

public class SubQuestionAnswerDTO {
    public Map<String, SubQuestionAnswerDTO> sub_question_answers;
    public String text;
    public String[] choices;
    public String rating;
}
