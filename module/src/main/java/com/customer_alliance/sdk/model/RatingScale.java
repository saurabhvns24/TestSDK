package com.customer_alliance.sdk.model;

import java.io.Serializable;

public class RatingScale implements Serializable {
    private Min min;
    private Max max;

    public RatingScale(Min min, Max max) {
        this.min = min;
        this.max = max;
    }

    public Min getMin() {
        return min;
    }

    public void setMin(Min min) {
        this.min = min;
    }

    public Max getMax() {
        return max;
    }

    public void setMax(Max max) {
        this.max = max;
    }
}
