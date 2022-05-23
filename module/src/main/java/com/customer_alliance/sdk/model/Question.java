package com.customer_alliance.sdk.model;

import java.io.Serializable;
import java.util.List;

public class Question implements Serializable {
    private Options options;
    private int id;
    private String type;
    private String label;
    private Validation validation;
    private List<Choice> choices;
    private RatingScale rating_scale;

    public Question(Options options, int id, String type, String label, Validation validation, List<Choice> choices, RatingScale rating_scale) {
        this.options = options;
        this.id = id;
        this.type = type;
        this.label = label;
        this.validation = validation;
        this.choices = choices;
        this.rating_scale = rating_scale;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public RatingScale getRatingScale() {
        return rating_scale;
    }

    public void setRatingScale(RatingScale rating_scale) {
        this.rating_scale = rating_scale;
    }

    @Override
    public String toString() {
        return "Question{" +
                "options=" + options +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", label='" + label + '\'' +
                ", validation=" + validation +
                ", choices=" + choices +
                ", ratingScale=" + rating_scale +
                '}';
    }
}
