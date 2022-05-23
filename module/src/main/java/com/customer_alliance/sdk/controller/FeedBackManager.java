package com.customer_alliance.sdk.controller;

import android.app.Activity;
import android.content.Context;

import com.customer_alliance.sdk.constant.ConfigurationConstant;
import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.interfaces.IFeedbackInitialization;
import com.customer_alliance.sdk.interfaces.IFeedbackNetworkService;
import com.customer_alliance.sdk.interfaces.IParsar;
import com.customer_alliance.sdk.view.FeedBackDialog;

public class FeedBackManager {

    private static FeedBackManager single_instance = null;
    private boolean mIsFeedbackInitialized = false;

    private static Context mContext;
    private String mHash;
    private static IFeedbackNetworkService iFeedbackNetworkService;
    private static FeedBackController feedBackController;
    private static IParsar parser;
    private static FeedBackDialog showAlterDialog;


    public static FeedBackManager getInstance(){
        if (single_instance == null)
            single_instance = new FeedBackManager();
        return single_instance;
    }

    private FeedBackManager(){}

    public static Context getContext() {
        return mContext;
    }

    public IFeedbackNetworkService getFeedbackService() {
        if (iFeedbackNetworkService == null) {
            iFeedbackNetworkService = new FeedbackNetworkService();
        }
        return iFeedbackNetworkService;
    }

    public FeedBackController getFeedBackController() {
        return feedBackController;
    }

    public void SetController(Context context){
        feedBackController = new FeedBackController(context);
    }

    public IParsar getParser() {
        if (parser == null) {
            parser = new Parser();
        }
        return parser;
    }


    public static FeedBackDialog getAlertDialog() {
        if (showAlterDialog == null) {
            showAlterDialog = new FeedBackDialog((Activity) getContext());
        }
        return showAlterDialog;
    }

    public void Initialize(Context context, String hash, IFeedbackInitialization iFeedbackInitialization){
        mContext = context;
        mHash = hash;
        getParser();
        getFeedbackService();
        SetController(context);
        iFeedbackInitialization.onSuccess();
    }

    public static void showLoader() {
        getAlertDialog().showProgressBar();
    }

    public static void hideLoader() {
        getAlertDialog().hideProgressBar();
    }

    public void StartFeedback() {
        getFeedBackController().getFeedBack();
    }

    public void setConfiguration(String primaryColor, String headerColor, String paragraphColor, int frequency, boolean showFooter) {
        ConfigurationConstant.primaryColor = paragraphColor;
        getAlertDialog().loadImageLogo();
        getAlertDialog().setPrimaryColorToAllView(primaryColor);
        getAlertDialog().setParagraphColorComeFromInternet(paragraphColor);
        getAlertDialog().setTextColor(headerColor);
        CommonConstants.frequency = frequency;
        CommonConstants.showFooterContent = showFooter;
    }
}