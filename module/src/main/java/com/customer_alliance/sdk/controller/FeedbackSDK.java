package com.customer_alliance.sdk.controller;

import android.content.Context;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.interfaces.IFeedbackInitialization;

public class FeedbackSDK {

    private final Context mContext;
    private final String hashValue;

    public FeedbackSDK(Context context, String hash) {
        CommonConstants.HASHCODE = hash;
        hashValue = hash;
        mContext = context;
    }

    public void Initialize(IFeedbackInitialization iFeedbackInitialization){
        FeedBackManager.getInstance().Initialize(mContext, hashValue, iFeedbackInitialization);
    }

    public void SetConfiguration(String primaryColor, String headerColor, String paragraphColor, int frequency, boolean showFooter){
        FeedBackManager.getInstance().setConfiguration(primaryColor, headerColor, paragraphColor, frequency, showFooter);
    }

    public void StartFeedback(){
        FeedBackManager.getInstance().StartFeedback();
    }

    public void showNetworkError(){
        FeedBackManager.getAlertDialog().showInternetErrorDialog();
    }
}