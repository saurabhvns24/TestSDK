package com.customer_alliance.sdk.network;

import android.content.Context;
import android.os.AsyncTask;

import com.customer_alliance.sdk.controller.FeedBackManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIManager {
    private final Context context;
    private String API_REQUEST_METHOD;
    private String requestData;
    private String networkURL;

    public APIManager(String getDataURL, String API_REQUEST_METHOD) {
        this.context = FeedBackManager.getContext();
        networkURL = getDataURL;
        this.API_REQUEST_METHOD = API_REQUEST_METHOD;

    }

    public void getFeedbackLookUpData() {
        FeedbackDataAsyncTask myAsyncTasks = new FeedbackDataAsyncTask();
        myAsyncTasks.execute();
    }

    public void submitFeedback(String requestData) {
        SubmitDataAsyncTask myAsyncTasks = new SubmitDataAsyncTask();
        myAsyncTasks.execute(requestData);
    }

    private class FeedbackDataAsyncTask extends AsyncTask<Void, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(networkURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(API_REQUEST_METHOD);
                if (httpURLConnection.getResponseCode() == 200 || httpURLConnection.getResponseCode() == 201) {
                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        result.append((char) data);
                        data = isw.read();
                    }
                    return result.toString();
                } else {
                    return "Exception";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("Exception")) {
                FeedBackManager.getInstance().getFeedBackController().onFeedBackDataFailureResponse();
            } else {
                FeedBackManager.getInstance().getFeedBackController().onFeedBackDataSuccessResponse(result);
            }

        }
    }

    private class SubmitDataAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... request) {
            StringBuilder result = new StringBuilder();
            URL url;
            HttpURLConnection httpURLConnection = null;
            try {
                url = new URL(networkURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod(API_REQUEST_METHOD);
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setChunkedStreamingMode(0);
                OutputStream outputStreamWriter = httpURLConnection.getOutputStream();
                byte[] input = request[0].getBytes("utf-8");
                outputStreamWriter.write(input, 0, input.length);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                if (httpURLConnection.getResponseCode() == 200 || httpURLConnection.getResponseCode() == 201) {
                    InputStream in = httpURLConnection.getInputStream();
                    InputStreamReader isw = new InputStreamReader(in);
                    int data = isw.read();
                    while (data != -1) {
                        result.append((char) data);
                        data = isw.read();
                    }
                    return result.toString();
                } else {
                    return "Exception";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception";
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            FeedBackManager.hideLoader();
            if (result.equals("Exception")) {
                FeedBackManager.getInstance().getFeedBackController().onSubmitDataFailureResponse();
            }else {
                FeedBackManager.getInstance().getFeedBackController().onSubmitDataSuccessResponse(result);
            }
        }
    }
}


