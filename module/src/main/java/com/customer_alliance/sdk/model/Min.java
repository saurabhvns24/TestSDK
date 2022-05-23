package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class Min implements Serializable {
    private String value;
    private String label;

    public Min(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
