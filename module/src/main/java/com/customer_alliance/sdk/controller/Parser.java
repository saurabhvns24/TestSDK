package com.customer_alliance.sdk.controller;

import com.customer_alliance.sdk.interfaces.IParsar;
import com.customer_alliance.sdk.model.CAResponse;
import com.customer_alliance.sdk.model.SubmitResponse;
import com.google.gson.Gson;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Parser implements IParsar {

    @Override
    public CAResponse parseFeedbackData(String Json) {
        Gson gson = new Gson();
        CAResponse caResponse = gson.fromJson(Json, CAResponse.class);
//        try {
//            InputStream inputStream = FeedBackManager.getContext().getAssets().open("ca_question.json");
//            int size = inputStream.available();
//            byte[] buffer = new byte[size];
//            inputStream.read(buffer);
//            inputStream.close();
//            String json = new String(buffer, StandardCharsets.UTF_8);
//            caResponse = gson.fromJson(json, CAResponse.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return caResponse;
    }

    @Override
    public SubmitResponse parseSubmitFeedbackData(String Json) {
        Gson gson = new Gson();
        SubmitResponse submitResponse = gson.fromJson(Json, SubmitResponse.class);
        return submitResponse;
    }

}
