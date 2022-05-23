package com.customer_alliance.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Translations implements Serializable {
    private String select;
    private String website_url;
    private String poweredBy;
    @SerializedName("validation.text.required")
    private String validationTextRequired;
    @SerializedName("validation.text.too_short")
    private String validationTextTooShort;
    @SerializedName("validation.choice.required")
    private String validationChoiceRequired;
    private String multiple_selected;
    private String required;
    private String submission_error;
    private String back;
    private String next;
    private String close;
    private String submit;

    public Translations(String select, String website_url, String poweredBy, String validationTextRequired, String validationTextTooShort, String validationChoiceRequired, String multiple_selected, String required, String submission_error, String back, String next, String close, String submit) {
        this.select = select;
        this.website_url = website_url;
        this.poweredBy = poweredBy;
        this.validationTextRequired = validationTextRequired;
        this.validationTextTooShort = validationTextTooShort;
        this.validationChoiceRequired = validationChoiceRequired;
        this.multiple_selected = multiple_selected;
        this.required = required;
        this.submission_error = submission_error;
        this.back = back;
        this.next = next;
        this.close = close;
        this.submit = submit;
    }

    public String getSelect() {
        return select;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public String getPoweredBy() {
        return poweredBy;
    }

    public String getValidationTextRequired() {
        return validationTextRequired;
    }

    public String getValidationTextTooShort() {
        return validationTextTooShort;
    }

    public String getValidationChoiceRequired() {
        return validationChoiceRequired;
    }

    public String getMultiple_selected() {
        return multiple_selected;
    }

    public String getRequired() {
        return required;
    }

    public String getSubmission_error() {
        return submission_error;
    }

    public String getBack() {
        return back;
    }

    public String getNext() {
        return next;
    }

    public String getClose() {
        return close;
    }

    public String getSubmit() {
        return submit;
    }
}
