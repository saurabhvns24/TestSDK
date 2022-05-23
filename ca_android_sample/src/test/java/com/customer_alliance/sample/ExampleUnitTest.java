package com.customer_alliance.sample;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.customer_alliance.sdk.controller.FeedBackManager;
import com.customer_alliance.sdk.controller.FeedbackSDK;
import com.customer_alliance.sdk.interfaces.IFeedbackInitialization;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(RobolectricTestRunner.class)
public class ExampleUnitTest {
    @Test
    public void sdkInitializationTest() {
        launcher activity = Robolectric.setupActivity(launcher.class);
        String primaryColor = "#FF4500";
        String HeaderColor = "#ffffff";
        String paragraphColor = "#FF0000";
        int Frequency = 1;
        boolean showFooter = true;
        FeedbackSDK feedbackSDK = new FeedbackSDK(activity, "akjnajjbap6rfjzy6kyvuutn6l2lbsc7dqw4ivama1vvxirq");
        {
            feedbackSDK.Initialize(new IFeedbackInitialization() {
                @Override
                public void onSuccess() {
                    feedbackSDK.SetConfiguration(primaryColor, HeaderColor, paragraphColor, Frequency, showFooter);
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Test
    public void startFeedBackTest() {
        launcher activity = Robolectric.setupActivity(launcher.class);
        String primaryColor = "#FF4500";
        String HeaderColor = "#ffffff";
        String paragraphColor = "#FF0000";
        int Frequency = 1;
        boolean showFooter = true;
        FeedbackSDK feedbackSDK = new FeedbackSDK(activity, "akjnajjbap6rfjzy6kyvuutn6l2lbsc7dqw4ivama1vvxirq");
        {
            feedbackSDK.Initialize(new IFeedbackInitialization() {
                @Override
                public void onSuccess() {
                    feedbackSDK.SetConfiguration(primaryColor, HeaderColor, paragraphColor, Frequency, showFooter);
                    feedbackSDK.StartFeedback();
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    @Test
    public void addition_isCorrect() {
        launcher activity = Robolectric.setupActivity(launcher.class);
        String primaryColor = "#FF4500";
        String HeaderColor = "#ffffff";
        String paragraphColor = "#FF0000";
        int Frequency = 1;
        boolean showFooter = true;
        FeedbackSDK feedbackSDK = new FeedbackSDK(activity, "akjnajjbap6rfjzy6kyvuutn6l2lbsc7dqw4ivama1vvxirq");
        {
            feedbackSDK.Initialize(new IFeedbackInitialization() {
                @Override
                public void onSuccess() {
                    feedbackSDK.SetConfiguration(primaryColor, HeaderColor, paragraphColor, Frequency, showFooter);
                    FeedBackManager.getInstance().getFeedBackController().submitFeedBack("");
                }

                @Override
                public void onFailure() {

                }
            });
        }

    }
}