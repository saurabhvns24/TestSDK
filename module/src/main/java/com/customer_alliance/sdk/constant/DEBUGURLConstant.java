package com.customer_alliance.sdk.constant;

import com.customer_alliance.sdk.controller.FeedBackManager;

public class DEBUGURLConstant {
    public static final String BASE_URL = "http://questionnaire.master.ra.testing.customer-alliance.com/ca/embedded/";
    public static String feedbackLookUpRelativePath = "/fetch?local=" + FeedBackManager.getContext().getResources().getConfiguration().locale;
    public static String feedbackSubmitRelativePath = "/submit?local=" + FeedBackManager.getContext().getResources().getConfiguration().locale;
}
