package com.customer_alliance.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import android.widget.Button;

import com.customer_alliance.sdk.controller.FeedbackSDK;
import com.customer_alliance.sdk.interfaces.IFeedbackInitialization;

public class launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Button launch = findViewById(R.id.launch_btn);
        launch.setOnClickListener(view -> {
            String primaryColor = "#FF4500";
            String HeaderColor = "#ffffff";
            String paragraphColor = "#FF0000";
            int Frequency = 1;
            boolean showFooter = true;
            FeedbackSDK feedBack = new FeedbackSDK(launcher.this, "akjnajjbap6rfjzy6kyvuutn6l2lbsc7dqw4ivama1vvxirq");
            feedBack.Initialize(new IFeedbackInitialization() {
                @Override
                public void onSuccess() {
                    feedBack.SetConfiguration(primaryColor, HeaderColor, paragraphColor, Frequency, showFooter);
                    feedBack.StartFeedback();
                }

                @Override
                public void onFailure() {
                    feedBack.showNetworkError();
                }
            });

        });
    }
}


