package com.customer_alliance.sdk.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.customer_alliance.sdk.constant.BitMapImageConstant;
import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.model.CAResponse;
import com.customer_alliance.sdk.model.Element;
import com.customer_alliance.sdk.controller.FeedBackManager;

import java.io.InputStream;

public class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
    private CAResponse caResponse;

    public DownloadImageFromInternet(Context context, CAResponse caResponse) {
        this.caResponse = caResponse;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected Bitmap doInBackground(String... urls) {
        try {
            InputStream inCaretDown = new java.net.URL(urls[0]).openStream();
            BitMapImageConstant.iconCaretDown = BitmapFactory.decodeStream(inCaretDown);
            InputStream inCircle = new java.net.URL(urls[1]).openStream();
            BitMapImageConstant.iconCircle = BitmapFactory.decodeStream(inCircle);
            InputStream inCustomerAlliance = new java.net.URL(urls[2]).openStream();
            BitMapImageConstant.iconCustomerAlliance = BitmapFactory.decodeStream(inCustomerAlliance);
            InputStream inError = new java.net.URL(urls[3]).openStream();
            BitMapImageConstant.iconError = BitmapFactory.decodeStream(inError);
            InputStream inLoading = new java.net.URL(urls[4]).openStream();
            BitMapImageConstant.iconLoading = BitmapFactory.decodeStream(inLoading);
            InputStream inStar = new java.net.URL(urls[5]).openStream();
            BitMapImageConstant.iconStar = BitmapFactory.decodeStream(inStar);
        } catch (Exception e) {
            Log.d("Error Message", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        FeedBackManager.hideLoader();
        showFeedbackPopup(caResponse);
    }

    private void showFeedbackPopup(CAResponse caResponse) {
        setDataAndShowPopUp(caResponse.getElements().get(0));
    }

    private void setDataAndShowPopUp(Element element) {
        String ratingType = element.getQuestion().getType();
        switch (ratingType) {
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
                showRatingPopup(element);
                break;
            case CommonConstants.TEXT_TYPE_QUESTION:
                showFeedbackQuestionPopup(element);
                break;
            case CommonConstants.CHOICE_QUESTION:
                showChoiceQuestionType(element);
                break;
        }
    }

    private void showRatingPopup(Element element) {
        FeedBackManager.getInstance().getAlertDialog().showRatingTypeAlert(element.getQuestion().getId(),
                Integer.parseInt(element.getQuestion().getRatingScale().getMax().getValue()),
                element.getQuestion().getLabel(),
                element.getQuestion().getRatingScale().getMin().getLabel(),
                element.getQuestion().getRatingScale().getMax().getLabel(),
                element.getQuestion().getValidation().isRequired(),
                element.getQuestion().getType(),
                element.getSub_element_conditions(), false,
                element);
    }

    private void showFeedbackQuestionPopup(Element element) {
        FeedBackManager.getInstance().getAlertDialog().showFeedbackMessageTypeAlert(element.getQuestion().getValidation().getMin_characters(), element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), false, element);
    }

    private void showChoiceQuestionType(Element element) {
        if (element.getQuestion().getOptions() != null && element.getQuestion().getOptions().isMultiple()) {
            FeedBackManager.getAlertDialog().startMultipleChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), false, element);
        } else {
            FeedBackManager.getAlertDialog().startChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), false, element);
        }
    }
}
