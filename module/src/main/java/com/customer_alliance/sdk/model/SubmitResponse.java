package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class SubmitResponse implements Serializable {
    private String success_title;
    private String success_description;

    public SubmitResponse(String success_title, String success_description) {
        this.success_title = success_title;
        this.success_description = success_description;
    }

    public String getSuccess_title() {
        return success_title;
    }

    public void setSuccess_title(String success_title) {
        this.success_title = success_title;
    }

    public String getSuccess_description() {
        return success_description;
    }

    public void setSuccess_description(String success_description) {
        this.success_description = success_description;
    }
}
