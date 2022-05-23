package com.customer_alliance.sdk.interfaces;

import com.customer_alliance.sdk.model.CAResponse;
import com.customer_alliance.sdk.model.SubmitResponse;

public interface IParsar {
    public CAResponse parseFeedbackData(String json);

    public SubmitResponse parseSubmitFeedbackData(String json);

}
