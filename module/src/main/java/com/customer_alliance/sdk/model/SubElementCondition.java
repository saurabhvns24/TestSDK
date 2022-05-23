package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SubElementCondition implements Serializable {
    private List<String> values ;
    private List<Element> elements;

    public SubElementCondition(List<String> values, List<Element> elements) {
        this.values = values;
        this.elements = elements;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
