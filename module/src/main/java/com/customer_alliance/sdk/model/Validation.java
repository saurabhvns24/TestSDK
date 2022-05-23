package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class Validation implements Serializable {
    private boolean required;
    private String min_characters;

    public Validation(boolean required, String min_characters) {
        this.required = required;
        this.min_characters = min_characters;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getMin_characters() {
        return min_characters;
    }

    public void setMin_characters(String min_characters) {
        this.min_characters = min_characters;
    }
}
