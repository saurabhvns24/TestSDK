package com.customer_alliance.sdk.controller;

import android.content.Context;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.DEBUGURLConstant;
import com.customer_alliance.sdk.interfaces.IFeedbackNetworkService;
import com.customer_alliance.sdk.network.APIManager;

public class FeedbackNetworkService implements IFeedbackNetworkService {

    @Override
    public void getFeedBackLookUpData() {
       APIManager apiManager = new APIManager(DEBUGURLConstant.BASE_URL+ CommonConstants.HASHCODE +DEBUGURLConstant.feedbackLookUpRelativePath
                , "GET");
        apiManager.getFeedbackLookUpData();
    }

    @Override
    public void submitFeedback(String request) {
        APIManager apiManager = new APIManager(DEBUGURLConstant.BASE_URL+ CommonConstants.HASHCODE +DEBUGURLConstant.feedbackSubmitRelativePath
                , "POST");
        apiManager.submitFeedback(request);
    }
}
