package com.customer_alliance.sdk.controller;

import android.content.Context;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.RequestConstant;
import com.customer_alliance.sdk.constant.TextConstants;
import com.customer_alliance.sdk.interfaces.IParsar;
import com.customer_alliance.sdk.model.CAResponse;

import com.customer_alliance.sdk.model.SubmitResponse;
import com.customer_alliance.sdk.network.DownloadImageFromInternet;

public class FeedBackController {

    private Context mContext;

    public FeedBackController(Context context) {
        mContext = context;
    }

    public void getFeedBack() {
        FeedBackManager.showLoader();
        FeedBackManager.getInstance().getFeedbackService().getFeedBackLookUpData();
    }

    public void onFeedBackDataSuccessResponse(String result) {
        IParsar iParsar = FeedBackManager.getInstance().getParser();
        CAResponse caResponse = iParsar.parseFeedbackData(result);
        FeedBackManager.getAlertDialog().hideProgressBar();
        new DownloadImageFromInternet(FeedBackManager.getContext()
                , caResponse).execute(caResponse.getAssets().getIcon_caret_down()
                , caResponse.getAssets().getIcon_circle()
                , caResponse.getAssets().getIcon_error()
                , caResponse.getAssets().getIcon_loading()
                , caResponse.getAssets().getIcon_customer_alliance()
                , caResponse.getAssets().getIcon_star());
        setTextConstant(caResponse);
        setRequestConstant(caResponse);
    }

    public void submitFeedBack(String request) {
        FeedBackManager.getAlertDialog().showProgressBar();
        FeedBackManager.getInstance().getFeedbackService().submitFeedback(request);
    }

    public void onSubmitDataSuccessResponse(String result) {
        IParsar iParsar = FeedBackManager.getInstance().getParser();
        SubmitResponse submitResponse = iParsar.parseSubmitFeedbackData(result);
        FeedBackManager.getAlertDialog().ShowSuccessAnswerSubmission(submitResponse);
        FeedBackManager.hideLoader();
    }

    public void onSubmitDataFailureResponse() {
        FeedBackManager.getAlertDialog().showInternetErrorDialog();
        FeedBackManager.hideLoader();
    }

    public void onFeedBackDataFailureResponse() {
        FeedBackManager.getAlertDialog().showInternetErrorDialog();
        FeedBackManager.hideLoader();
    }

    private void setTextConstant(CAResponse caResponse) {
        TextConstants.choice_required = caResponse.getTranslations().getValidationChoiceRequired();
        TextConstants.select = caResponse.getTranslations().getSelect();
        TextConstants.multiple_selected = caResponse.getTranslations().getMultiple_selected();
        TextConstants.required = caResponse.getTranslations().getRequired();
        TextConstants.text_too_short = caResponse.getTranslations().getValidationTextTooShort();
        TextConstants.text_required = caResponse.getTranslations().getValidationTextRequired();
        TextConstants.powered_by = caResponse.getTranslations().getPoweredBy();
        TextConstants.submissionError = caResponse.getTranslations().getSubmission_error();
        TextConstants.back = caResponse.getTranslations().getBack();
        TextConstants.close = caResponse.getTranslations().getClose();
        TextConstants.submit = caResponse.getTranslations().getSubmit();
        TextConstants.next = caResponse.getTranslations().getNext();
    }

    private void setRequestConstant(CAResponse caResponse) {
        RequestConstant.hash = caResponse.getHash();
        RequestConstant.version = caResponse.getVersion();
        RequestConstant.token = caResponse.getToken();
        CommonConstants.CUSTOMER_ALLIANCE_URL = caResponse.getTranslations().getWebsite_url();
    }

}