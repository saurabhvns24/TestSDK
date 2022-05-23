package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class Options implements Serializable {
    private boolean multiple;

    public Options(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }
}
