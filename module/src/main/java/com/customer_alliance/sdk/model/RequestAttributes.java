package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class RequestAttributes implements Serializable {
    private String key;

    public RequestAttributes(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
