package com.customer_alliance.sdk.view;

import static com.customer_alliance.sdk.constant.CommonConstants.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.customer_alliance.sdk.adapter.DropDownListAdapter;
import com.customer_alliance.sdk.adapter.MultipleChoiceAdapter;
import com.customer_alliance.sdk.constant.ConfigurationConstant;
import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.RequestConstant;
import com.customer_alliance.sdk.constant.TextConstants;
import com.customer_alliance.sdk.controller.FeedBackManager;
import com.customer_alliance.sdk.listener.onItemClickListener;
import com.customer_alliance.sdk.R;
import com.customer_alliance.sdk.constant.BitMapImageConstant;
import com.customer_alliance.sdk.model.AnswerValue;
import com.customer_alliance.sdk.model.CheckedAndUncheckedOption;
import com.customer_alliance.sdk.model.Choice;
import com.customer_alliance.sdk.model.Element;
import com.customer_alliance.sdk.model.QuestionAnswerDTO;
import com.customer_alliance.sdk.model.SubElementCondition;
import com.customer_alliance.sdk.model.SubQuestionAnswerDTO;
import com.customer_alliance.sdk.model.SubmitResponse;
import com.google.gson.Gson;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FeedBackDialog implements onItemClickListener {
    Activity activity;
    AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private static final String TAG = "ShowAlterDialog";
    private String ratingType;
    private List<CheckedAndUncheckedOption> optionLookUpList;
    private AutoCompleteTextView multipleChoiceAutoCompleteTextView;
    private Map<String, AnswerValue> sub_question_answers = new HashMap<>();
    private PopupWindow pw;
    public static boolean[] checkSelected;
    private List<CheckedAndUncheckedOption> userSelectedOptionList;
    private AutoCompleteTextView autoCompleteTextView;
    private int primaryColor;
    private QuestionAnswerDTO globalQuestionAnswerDTO = null;
    private SubQuestionAnswerDTO globalSubQuestionAnswerDTO;
    private int firstQuestionId = -1;
    private boolean isFromBackPress;
    private List<Element> backstackElementList = new ArrayList<>();
    private ProgressBar progressBar;
    private View view;
    private TextView autoTextHelperTextview;
    private Spinner spinner;

    public FeedBackDialog(Activity activity) {
        this.activity = activity;
    }

    private void setUpAlertDialogView() {
        builder = new AlertDialog.Builder(activity);
        builder.setView(view);
    }

    public void setUpView(int layoutId) {
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(layoutId, null);
    }


    public void showRatingTypeAlert(int questionId, int numberOfButtons, String question, String zerothPlace, String lastPlace, boolean isRequired, String type, List<SubElementCondition> subElementConditions, boolean isFromSubElement, Element element) {
        hideErrorMessage(view);
        if (!isFromBackPress) {
            backstackElementList.add(element);
        }
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        ((TextView) view.findViewById(R.id.rating_worst)).setText(zerothPlace);
        ((TextView) view.findViewById(R.id.rating_best)).setText(lastPlace);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        removeTextQuestionView(view);
        removeMultipleChoiceTypeQuestion(view);
        showRatingView(view);
        LinearLayout dynamicLinearLayout = view.findViewById(R.id.dynamic_rating_linearlayout);
        dynamicLinearLayout.setWeightSum(numberOfButtons);
        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
        if (ratingType != null && ratingType.equals(STAR_RATING_TYPE)) {
            starRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        } else if (ratingType != null && ratingType.equals(CIRCLE_RATING_TYPE)) {
            circleRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        } else {
            numberRatingType(questionId, paramsButton, dynamicLinearLayout, numberOfButtons, type, subElementConditions);
        }
        dynamicLinearLayout.setVisibility(View.VISIBLE);
        view.findViewById(R.id.customer_allience_text_btn).setOnClickListener(v -> onClickCall());
        view.findViewById(R.id.close_button).setOnClickListener(v -> {
                    isFromBackPress = false;
                    onClickCall();
                    backstackElementList.clear();
                }
        );
        createAndShowAlertDialog();
    }


    private void circleRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String questionType, List<SubElementCondition> subElementConditions) {
        ImageView[] dynamicImageButtons = new ImageView[numberOfButtons];

        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            int clickedNumber = buttonNumber;
            dynamicImageButtons[buttonNumber] = new ImageView(activity);
            dynamicImageButtons[buttonNumber].setImageBitmap(BitMapImageConstant.iconCircle);
            dynamicImageButtons[buttonNumber].setLayoutParams(paramsButton);
            dynamicImageButtons[buttonNumber].setOnClickListener(v -> clickOnCircleRatingButton(questionId, String.valueOf(clickedNumber), questionType, subElementConditions));
            dynamicLinearLayout.addView(dynamicImageButtons[buttonNumber]);
        }
    }


    private void clickOnCircleRatingButton(int questionId, String answerNumber, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(questionId, answerNumber, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answerNumber)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0), false);
                    createAnswerRequest(questionId, answerNumber, questionType, null);
                    break;
                }
            }
        }
    }


    private void starRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String type, List<SubElementCondition> subElementConditions) {
        ImageView[] dynamicImageButtons = new ImageView[numberOfButtons];
        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            int clickedNumber = buttonNumber;
            dynamicImageButtons[buttonNumber] = new ImageView(activity);
            dynamicImageButtons[buttonNumber].setImageBitmap(BitMapImageConstant.iconStar);
            dynamicImageButtons[buttonNumber].setLayoutParams(paramsButton);
            dynamicImageButtons[buttonNumber].setOnClickListener(v -> clickOnStarRatingButton(questionId, String.valueOf(clickedNumber), type, subElementConditions));
            dynamicLinearLayout.addView(dynamicImageButtons[buttonNumber]);
        }
    }


    private void clickOnStarRatingButton(int questionId, String answerNumber, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(questionId, answerNumber, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answerNumber)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0), false);
                    createAnswerRequest(questionId, answerNumber, questionType, null);
                }
            }
        }
    }


    private void removeOnBackPress() {
        if (globalSubQuestionAnswerDTO.sub_question_answers.entrySet().isEmpty()) {
            globalSubQuestionAnswerDTO = null;
        } else {
            String key = null;
            for (Map.Entry<String, SubQuestionAnswerDTO> entry : globalSubQuestionAnswerDTO.sub_question_answers.entrySet()) {
                key = entry.getKey();
            }
            globalSubQuestionAnswerDTO.sub_question_answers.remove(key);
        }
    }

    private void createAnswerRequest(int questionId, String answer, String questionType, String[] choiceList) {
        if (firstQuestionId == -1) {
            firstQuestionId = questionId;
        }
        SubQuestionAnswerDTO subQuestionAnswerDTO = null;
        Map<String, SubQuestionAnswerDTO> hashMap = new HashMap<>();
        switch (questionType) {
            case CommonConstants.TEXT_TYPE_QUESTION:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = null;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = "";
                subQuestionAnswerDTO.text = answer;
                break;
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
            case CommonConstants.STAR_RATING_TYPE_QUESTION:
            case CommonConstants.CIRCLE_RATING_TYPE:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = null;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = answer;
                subQuestionAnswerDTO.text = null;
                break;
            case CommonConstants.CHOICE_QUESTION:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = choiceList;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = null;
                subQuestionAnswerDTO.text = null;
                break;
        }
        if (globalSubQuestionAnswerDTO == null) {
            globalSubQuestionAnswerDTO = subQuestionAnswerDTO;
        } else {
            SubQuestionAnswerDTO subQuestionAnswerDTO1 = new SubQuestionAnswerDTO();
            subQuestionAnswerDTO1.sub_question_answers = new HashMap<>();
            for (Map.Entry<String, SubQuestionAnswerDTO> entry : globalSubQuestionAnswerDTO.sub_question_answers.entrySet()) {
                String key = entry.getKey();
                subQuestionAnswerDTO1 = entry.getValue();
            }
            if (globalSubQuestionAnswerDTO.sub_question_answers.isEmpty()) {
                globalSubQuestionAnswerDTO.sub_question_answers.put(String.valueOf(questionId), subQuestionAnswerDTO);
            } else {
                subQuestionAnswerDTO1.sub_question_answers.put(String.valueOf(questionId), subQuestionAnswerDTO);
            }
        }
    }


    private void numberRatingType(int questionId, LinearLayout.LayoutParams paramsButton, LinearLayout dynamicLinearLayout, int numberOfButtons, String type, List<SubElementCondition> subElementConditions) {
        Button[] dynamicButtons = new Button[numberOfButtons];
        for (int buttonNumber = 0; buttonNumber < numberOfButtons; buttonNumber++) {
            LinearLayout.LayoutParams paramsButton1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f);
            dynamicButtons[buttonNumber] = new Button(activity);
            String buttonText = String.valueOf(buttonNumber + 1);
            dynamicButtons[buttonNumber].setText(buttonText);
            dynamicButtons[buttonNumber].setTextColor(activity.getColor(R.color.text_color));
            dynamicButtons[buttonNumber].setTextSize(13);
            dynamicButtons[buttonNumber].setPadding(2, 2, 2, 2);
            dynamicButtons[buttonNumber].setLayoutParams(paramsButton1);
            dynamicButtons[buttonNumber].setOnClickListener(v -> clickOnNumberRatingButton(questionId, buttonText, type, subElementConditions));
            dynamicLinearLayout.addView(dynamicButtons[buttonNumber]);
        }
    }


    private void clickOnNumberRatingButton(int questionId, String answer, String questionType, List<SubElementCondition> subElementConditions) {
        if (subElementConditions == null || subElementConditions.size() == 0) {
            submitAnswerJSon(questionId, answer, questionType, null);
        } else {
            for (int i = 0; i < subElementConditions.size(); i++) {
                if (subElementConditions.get(i).getValues().contains(answer)) {
                    setDataAndShowPopUp(subElementConditions.get(i).getElements().get(0), false);
                    createAnswerRequest(questionId, answer, questionType, null);
                    break;
                }
            }
        }
    }


    private void setDataAndShowPopUp(Element element, boolean fromBackPressed) {
        String ratingType = element.getQuestion().getType();
        switch (ratingType) {
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
                showRatingTypeAlert(element.getQuestion().getId(),
                        Integer.parseInt(element.getQuestion().getRatingScale().getMax().getValue()),
                        element.getQuestion().getLabel(),
                        element.getQuestion().getRatingScale().getMin().getLabel(),
                        element.getQuestion().getRatingScale().getMax().getLabel(),
                        element.getQuestion().getValidation().isRequired(),
                        element.getQuestion().getType(),
                        element.getSub_element_conditions(), true, element
                );
                break;
            case CommonConstants.TEXT_TYPE_QUESTION:
                showFeedbackMessageTypeAlert(element.getQuestion().getValidation().getMin_characters(), element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true, element);
                break;
            case CommonConstants.CHOICE_QUESTION:
                if (element.getQuestion().getOptions().isMultiple()) {
                    startMultipleChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true, element);
                } else {
                    startChoiceFragment(element.getQuestion().getId(), element.getQuestion().getLabel(), element.getQuestion().getChoices(), element.getQuestion().getValidation().isRequired(), element.getSub_element_conditions(), true, element);
                }
                break;
        }
    }

    private void submitAnswerJSon(int questionId, String answer, String questionType, String[] choiceList) {
        SubQuestionAnswerDTO subQuestionAnswerDTO = null;
        Map<String, SubQuestionAnswerDTO> hashMap = new HashMap<>();
        switch (questionType) {
            case CommonConstants.TEXT_TYPE_QUESTION:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = null;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = "";
                subQuestionAnswerDTO.text = answer;
                break;
            case CommonConstants.NUMBER_RATING_TYPE_QUESTION:
            case CommonConstants.STAR_RATING_TYPE_QUESTION:
            case CommonConstants.CIRCLE_RATING_TYPE:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = null;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = answer;
                subQuestionAnswerDTO.text = null;
                break;
            case CommonConstants.CHOICE_QUESTION:
                subQuestionAnswerDTO = new SubQuestionAnswerDTO();
                subQuestionAnswerDTO.choices = choiceList;
                subQuestionAnswerDTO.sub_question_answers = hashMap;
                subQuestionAnswerDTO.rating = null;
                subQuestionAnswerDTO.text = null;
                break;
        }
        if (globalSubQuestionAnswerDTO == null) {
            globalSubQuestionAnswerDTO = subQuestionAnswerDTO;
        } else {
            SubQuestionAnswerDTO subQuestionAnswerDTO1 = new SubQuestionAnswerDTO();
            subQuestionAnswerDTO1.sub_question_answers = new HashMap<>();
            for (Map.Entry<String, SubQuestionAnswerDTO> entry : globalSubQuestionAnswerDTO.sub_question_answers.entrySet()) {
                String key = entry.getKey();
                subQuestionAnswerDTO1 = entry.getValue();
            }
            if (globalSubQuestionAnswerDTO.sub_question_answers.isEmpty()) {
                globalSubQuestionAnswerDTO.sub_question_answers.put(String.valueOf(questionId), subQuestionAnswerDTO);
            } else {
                subQuestionAnswerDTO1.sub_question_answers.put(String.valueOf(questionId), subQuestionAnswerDTO);
            }
        }
        QuestionAnswerDTO questionAnswerDTO = new QuestionAnswerDTO();
        questionAnswerDTO.hash = RequestConstant.hash;
        questionAnswerDTO.token = RequestConstant.token;
        questionAnswerDTO.version = RequestConstant.version;
        questionAnswerDTO.question_answers = new HashMap<>();
        if (firstQuestionId != -1)
            questionAnswerDTO.question_answers.put(String.valueOf(firstQuestionId), globalSubQuestionAnswerDTO);

        Log.d(TAG, "submitAnswerJSon: " + questionAnswerDTO);
        String requestJSON = new Gson().toJson(questionAnswerDTO);
        Log.d(TAG, "submitAnswerJSon json: " + requestJSON);
        sub_question_answers.clear();
        FeedBackManager.getInstance().getFeedBackController().submitFeedBack(requestJSON);
    }

    private void onClickCall() {
        closeDialog();
    }


    private void removeTextQuestionView(View view) {
        view.findViewById(R.id.feedback_message).setVisibility(View.GONE);
        view.findViewById(R.id.back_button).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setVisibility(View.GONE);
    }


    public void showFeedbackMessageTypeAlert(String min_characters, int questionId, String question, boolean isRequired, List<SubElementCondition> sub_element_conditions, boolean isFromSubElement, Element element) {
        hideErrorMessage(view);
        backstackElementList.add(element);
        removeRatingView(view);
        removeMultipleChoiceTypeQuestion(view);
        showTextQuestionView(view);
        if (element.getSub_element_conditions().isEmpty()) {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.submit);
        } else {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.next);
        }
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((EditText) view.findViewById(R.id.feedback_message)).setText("");
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        ((EditText) view.findViewById(R.id.feedback_message)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideValidationError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            String feedbackAnswer = "";
            feedbackAnswer = ((EditText) view.findViewById(R.id.feedback_message)).getText().toString().trim();

            if (min_characters != null && feedbackAnswer.length() < Integer.parseInt(min_characters)) {
                showValidationError(TextConstants.text_too_short);
                return;
            }
            if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                submitAnswerJSon(questionId, feedbackAnswer, CommonConstants.TEXT_TYPE_QUESTION, null);
            } else {
                for (int i = 0; i < sub_element_conditions.size(); i++) {
                    if (sub_element_conditions.get(i).getValues().contains(feedbackAnswer)) {
                        setDataAndShowPopUp(sub_element_conditions.get(i).getElements().get(0), false);
                        createAnswerRequest(questionId, feedbackAnswer, CommonConstants.TEXT_TYPE_QUESTION, null);
                        break;
                    }
                }
            }
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            isFromBackPress = true;
            backstackElementList.remove(element);
            if (backstackElementList.size() != 0) {
                setDataAndShowPopUp(backstackElementList.get(backstackElementList.size() - 1), true);
                removeOnBackPress();
            } else {
                isFromBackPress = false;
                onClickCall();
                backstackElementList.clear();
            }
        });
        if (!isFromSubElement || !isFromBackPress) {
            createAndShowAlertDialog();
        }
    }

    private void createAndShowAlertDialog() {
        if (alertDialog == null) {
            alertDialog = builder.create();
        }
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void showTextQuestionView(View view) {
        view.findViewById(R.id.feedback_message).setVisibility(View.VISIBLE);
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
    }

    private void showRatingView(View view) {
        view.findViewById(R.id.dynamic_rating_linearlayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.rating_worst).setVisibility(View.VISIBLE);
        view.findViewById(R.id.rating_best).setVisibility(View.VISIBLE);
        view.findViewById(R.id.close_button).setVisibility(View.VISIBLE);
    }

    private void removeRatingView(View view) {
        view.findViewById(R.id.dynamic_rating_linearlayout).setVisibility(View.GONE);
        view.findViewById(R.id.rating_worst).setVisibility(View.GONE);
        view.findViewById(R.id.rating_best).setVisibility(View.GONE);
        view.findViewById(R.id.close_button).setVisibility(View.GONE);
    }

    public void closeDialog() {
        alertDialog.dismiss();
        alertDialog = null;
        view = null;
    }

    public void loadImageLogo() {
        setUpView(R.layout.initial_layout);
        setUpAlertDialogView();
        ImageView imageView = view.findViewById(R.id.logo_powered_by);
        imageView.setImageBitmap(BitMapImageConstant.iconCustomerAlliance);
    }

    public void setRatingType(String starRatingType) {
        ratingType = starRatingType;
    }

    public void setCircleType(String circleRatingType) {
        ratingType = circleRatingType;
    }


    @SuppressLint("ClickableViewAccessibility")
    public void startChoiceFragment(int questionId, String question, List<Choice> choices, boolean isRequired, List<SubElementCondition> sub_element_conditions, boolean isFromSubElement, Element element) {
        hideErrorMessage(view);
        backstackElementList.add(element);
        removeRatingView(view);
        removeTextQuestionView(view);
        showMultipleChoiceTypeQuestion(view);
        if (element.getSub_element_conditions().isEmpty()) {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.submit);
        } else {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.next);
        }
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            isFromBackPress = true;
            backstackElementList.remove(element);
            if (backstackElementList.size() != 0) {
                setDataAndShowPopUp(backstackElementList.get(backstackElementList.size() - 1), true);
                removeOnBackPress();
                isFromBackPress = false;
            } else {
                isFromBackPress = false;
                onClickCall();
                backstackElementList.clear();
            }
        });
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        ((ImageView) view.findViewById(R.id.dropdown_icon_imageview)).setImageBitmap(BitMapImageConstant.iconCaretDown);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        String[] list = getStringListOfDropdown(choices);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(activity, R.layout.dropdown_item, list);
        spinner = view.findViewById(R.id.dropdown_auto_complete_spinner);
        spinner.setAdapter(arrayAdapter);
//        spinner.setOnClickListener(v -> clickedOnAutoCompleteTextView(autoCompleteTextView));
//        spinner.setOnItemClickListener((parent, view, position, id) -> onItemSelectedBackground(autoCompleteTextView));
        spinner.setOnFocusChangeListener((v, hasFocus) -> clickedOnAutoCompleteTextView(autoCompleteTextView));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(position)).setBackgroundColor(Color.parseColor(ConfigurationConstant.primaryColor));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            if (autoCompleteTextView.getText().toString().equals("")) {
                showValidationError(TextConstants.choice_required);
                return;
            }
            for (int i = 0; i < choices.size(); i++) {
                if (choices.get(i).getLabel().equals(autoCompleteTextView.getText().toString())) {
                    String[] resultChoiceList = {choices.get(i).getValue()};
                    if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                        submitAnswerJSon(questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
                    } else {
                        for (int j = 0; j < sub_element_conditions.size(); j++) {
                            if (sub_element_conditions.get(j).getValues().contains(resultChoiceList[0])) {
                                setDataAndShowPopUp(sub_element_conditions.get(j).getElements().get(0), false);
                                createAnswerRequest(questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        });
        if (!isFromSubElement || !isFromBackPress) {
            createAndShowAlertDialog();
        }

    }

    private void onClickCustomerAllience() {
        Uri uri = Uri.parse(CommonConstants.CUSTOMER_ALLIANCE_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }


    public void startMultipleChoiceFragment(int questionId, String question, List<Choice> choices, boolean isRequired, List<
            SubElementCondition> sub_element_conditions, boolean isFromSubElement, Element element) {
        hideErrorMessage(view);
        if (!isFromBackPress) {
            backstackElementList.add(element);
        }
        removeRatingView(view);
        removeTextQuestionView(view);
        showMultipleChoiceTypeQuestion(view);
        if (element.getSub_element_conditions().isEmpty()) {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.submit);
        } else {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.next);
        }
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            isFromBackPress = true;
            backstackElementList.remove(element);
            if (backstackElementList.size() != 0) {
                setDataAndShowPopUp(backstackElementList.get(backstackElementList.size() - 1), true);
                removeOnBackPress();
                isFromBackPress = false;
            } else {
                isFromBackPress = false;
                onClickCall();
                backstackElementList.clear();
            }
        });
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        requiredText(isRequired, view.findViewById(R.id.layout_header_required_textview));
        if (element.getSub_element_conditions().isEmpty()) {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.submit);
        } else {
            ((Button) view.findViewById(R.id.submit_button)).setText(TextConstants.next);
        }
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(question);
        (view.findViewById(R.id.customer_allience_text_btn)).setOnClickListener(v -> onClickCustomerAllience());
        ((ImageView) view.findViewById(R.id.dropdown_icon_imageview)).setImageBitmap(BitMapImageConstant.iconCaretDown);
        ArrayList<String> str = new ArrayList<String>();
        for (int i = 0; i < choices.size(); i++) {
            str.add(choices.get(i).getLabel());
        }
        optionLookUpList = getChoiceListOfDropdown(choices);
        userSelectedOptionList = new ArrayList<>();
        MultipleChoiceAdapter multipleChoiceAdapter = new MultipleChoiceAdapter(activity, optionLookUpList, this);
        multipleChoiceAutoCompleteTextView = view.findViewById(R.id.dropdown_auto_complete_textview);
        multipleChoiceAutoCompleteTextView.setAdapter(multipleChoiceAdapter);
        multipleChoiceAutoCompleteTextView.setHint(TextConstants.select);
        multipleChoiceAutoCompleteTextView.setOnClickListener(v -> clickedOnMultiAutoCompleteTextView(multipleChoiceAutoCompleteTextView));
        multipleChoiceAutoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> onItemSelectedBackground(multipleChoiceAutoCompleteTextView));
        multipleChoiceAutoCompleteTextView.setOnFocusChangeListener((v, hasFocus) -> {
            clickedOnMultiAutoCompleteTextView(multipleChoiceAutoCompleteTextView);
        });
        multipleChoiceAutoCompleteTextView.setOnDismissListener(() -> {
            onDismissDropdown(multipleChoiceAutoCompleteTextView);
            multipleChoiceAutoCompleteTextView.setAdapter(new MultipleChoiceAdapter(activity, optionLookUpList, this));
        });
      /*  checkSelected = new boolean[choices.size()];
        fill(checkSelected, false);
        view.findViewById(R.id.SelectBox).setOnClickListener(v -> {
            initiatePopUp(str, view.findViewById(R.id.SelectBox));
        });*/
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            if (multipleChoiceAutoCompleteTextView.getText().toString().equals("")) {
                showValidationError(TextConstants.choice_required);
                return;
            }
            String[] resultChoiceList = new String[userSelectedOptionList.size()];
            if (sub_element_conditions == null || sub_element_conditions.size() == 0) {
                for (int index = 0; index < userSelectedOptionList.size(); index++) {
                    resultChoiceList[index] = userSelectedOptionList.get(index).getValue();
                }
                submitAnswerJSon(questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
            } else {
                for (int i = 0; i < sub_element_conditions.size(); i++) {
                    if (sub_element_conditions.get(i).getValues().contains(resultChoiceList[0])) {
                        setDataAndShowPopUp(sub_element_conditions.get(i).getElements().get(0), false);
                        createAnswerRequest(questionId, null, CommonConstants.CHOICE_QUESTION, resultChoiceList);
                    }
                }
            }
        });
        if (!isFromSubElement || !isFromBackPress) {
            createAndShowAlertDialog();
        }
        autoTextHelperTextview = view.findViewById(R.id.autoTextHelperTextview);
        multipleChoiceAutoCompleteTextView.setHint(TextConstants.select);
    }

    private void onDismissDropdown(AutoCompleteTextView autoCompleteTextView) {
        autoTextHelperTextview.setVisibility(View.GONE);
        if (userSelectedOptionList.size() == 1) {
            autoCompleteTextView.setText(userSelectedOptionList.get(0).getTitle());
        } else if (userSelectedOptionList.size() > 1) {
            autoCompleteTextView.setText(TextConstants.multiple_selected);
        } else {
            autoCompleteTextView.setText("");
            autoCompleteTextView.setHint(TextConstants.select);
            autoCompleteTextView.setBackgroundResource(0);
            autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
        }
    }

    @Override
    public void onItemClicked(int position, boolean selected) {
        if (selected) {
            userSelectedOptionList.add(optionLookUpList.get(position));
        } else {
            userSelectedOptionList.remove(optionLookUpList.get(position));
        }
        setValueOnTexView(multipleChoiceAutoCompleteTextView);
    }

    private void setValueOnTexView(AutoCompleteTextView autoCompleteTextView) {
        if (userSelectedOptionList.size() == 1) {
            autoCompleteTextView.setHint("");
            autoTextHelperTextview.setText(userSelectedOptionList.get(0).getTitle());
        } else if (userSelectedOptionList.size() > 1) {
            autoCompleteTextView.setHint("");
            autoTextHelperTextview.setText(TextConstants.multiple_selected);
        } else {
            autoTextHelperTextview.setText(TextConstants.select);
            autoCompleteTextView.setHint(TextConstants.select);
            autoCompleteTextView.setBackgroundResource(0);
            autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
        }
    }

    private void clickedOnMultiAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        hideValidationError();
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.rectangle_edit_text);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#F6F7F9"));
        gd.setCornerRadius(4);
        gd.setStroke(2, primaryColor);
        autoCompleteTextView.setBackground(gd);
        autoCompleteTextView.showDropDown();
        autoCompleteTextView.setHint(TextConstants.select);
        autoTextHelperTextview.setVisibility(View.VISIBLE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void clickedOnAutoCompleteTextView(AutoCompleteTextView autoCompleteTextView) {
        hideValidationError();
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.rectangle_edit_text);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#F6F7F9"));
        gd.setCornerRadius(4);
        gd.setStroke(2, primaryColor);
        autoCompleteTextView.setBackground(gd);
        autoCompleteTextView.showDropDown();
    }

    private void onItemSelectedBackground(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setBackgroundResource(0);
        autoCompleteTextView.setBackgroundColor(activity.getColor(R.color.default_color_for_auto_complete));
    }

    private String[] getStringListOfDropdown(List<Choice> choiceList) {
        String[] list = new String[choiceList.size()];
        for (int index = 0; index < choiceList.size(); index++) {
            list[index] = choiceList.get(index).getLabel();
        }
        return list;
    }

    private List<CheckedAndUncheckedOption> getChoiceListOfDropdown(List<Choice> choices) {
        List<CheckedAndUncheckedOption> checkedAndUncheckedOptions = new ArrayList<>(choices.size());
        for (int index = 0; index < choices.size(); index++) {
            CheckedAndUncheckedOption checkedAndUncheckedOption = new CheckedAndUncheckedOption();
            checkedAndUncheckedOption.setTitle(choices.get(index).getLabel());
            checkedAndUncheckedOption.setSelected(false);
            checkedAndUncheckedOption.setValue(choices.get(index).getValue());
            checkedAndUncheckedOptions.add(checkedAndUncheckedOption);
        }
        return checkedAndUncheckedOptions;
    }

    private void requiredText(boolean required, TextView textView) {
        if (required) {
            textView.setText(TextConstants.required);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    private void removeMultipleChoiceTypeQuestion(View view) {
        view.findViewById(R.id.dropdown_auto_complete_textview).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setVisibility(View.GONE);
        view.findViewById(R.id.back_button).setVisibility(View.GONE);
    }

    private void showMultipleChoiceTypeQuestion(View view) {
        view.findViewById(R.id.dropdown_auto_complete_textview).setVisibility(View.VISIBLE);
        view.findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
    }


    public void showInternetErrorDialog() {
        removeRatingView(view);
        removeMultipleChoiceTypeQuestion(view);
        removeTextQuestionView(view);
        showErrorMessage(view);
    }

    private void hideErrorMessage(View view) {
        view.findViewById(R.id.alert_info).setVisibility(View.GONE);
        view.findViewById(R.id.error_red).setVisibility(View.GONE);
        view.findViewById(R.id.back_button).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setVisibility(View.GONE);
    }


    private void showErrorMessage(View view) {
        ((TextView) view.findViewById(R.id.alert_info)).setText(TextConstants.submissionError);
        ((ImageView) view.findViewById(R.id.error_red)).setImageBitmap(BitMapImageConstant.iconError);
        view.findViewById(R.id.layout_header_textview).setVisibility(View.VISIBLE);
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText("");
        view.findViewById(R.id.alert_info).setVisibility(View.VISIBLE);
        view.findViewById(R.id.error_red).setVisibility(View.VISIBLE);
        view.findViewById(R.id.back_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.submit_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.layout_header_required_textview).setVisibility(View.GONE);
        view.findViewById(R.id.submit_button).setOnClickListener(v -> {
            isFromBackPress = false;
            onClickCall();
            backstackElementList.clear();
        });
        view.findViewById(R.id.back_button).setOnClickListener(v -> {
            isFromBackPress = true;
            setDataAndShowPopUp(backstackElementList.get(backstackElementList.size() - 1), true);
            removeOnBackPress();
            isFromBackPress = false;
        });
    }

    public void ShowSuccessAnswerSubmission(SubmitResponse submitResponse) {
        removeTextQuestionView(view);
        removeRatingView(view);
        removeMultipleChoiceTypeQuestion(view);
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText(submitResponse.getSuccess_title());
        ((TextView) view.findViewById(R.id.thank_you_dialog_textview)).setText(submitResponse.getSuccess_description());
        view.findViewById(R.id.close_button).setVisibility(View.VISIBLE);
        view.findViewById(R.id.thank_you_dialog_textview).setVisibility(View.VISIBLE);
        createAndShowAlertDialog();
    }

    private void showValidationError(String message) {
        ((TextView) view.findViewById(R.id.error_message_drop_down)).setText(message);
        view.findViewById(R.id.linearLayout_error).setVisibility(View.VISIBLE);
    }

    private void hideValidationError() {
        view.findViewById(R.id.linearLayout_error).setVisibility(View.GONE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setPrimaryColorToAllView(String color) {
        primaryColor = Color.parseColor(color);
        view.findViewById(R.id.layout_header).setBackgroundColor(Color.parseColor(color));
        view.findViewById(R.id.layout_header_textview).setBackgroundColor(Color.parseColor(color));
        view.findViewById(R.id.submit_button).setBackgroundColor(Color.parseColor(color));
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(activity, R.drawable.rectangle_edit_text);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.parseColor("#F6F7F9"));
        gd.setCornerRadius(4);
        gd.setStroke(2, Color.parseColor(color));
        view.findViewById(R.id.feedback_message).setBackground(gd);
//        autoCompleteTextView.setBackgroundColor(Color.parseColor(color));
        setCursorColor(view.findViewById(R.id.feedback_message), Color.parseColor(color));
//        multipleChoiceAutoCompleteTextView.setBackgroundColor(Color.parseColor(color));
    }

    public void setTextColor(String textColor) {
        ((Button) view.findViewById(R.id.submit_button)).setTextColor(Color.parseColor(textColor));
        ((TextView) view.findViewById(R.id.layout_header_textview)).setTextColor(Color.parseColor(textColor));
    }

    public void setParagraphColorComeFromInternet(String textColor) {
        ((TextView) view.findViewById(R.id.error_message_drop_down)).setTextColor(Color.parseColor(textColor));
        ((TextView) view.findViewById(R.id.alert_info)).setTextColor(Color.parseColor(textColor));
    }

    public void showFooter() {
        view.findViewById(R.id.logo_powered_by).setVisibility(View.VISIBLE);
        view.findViewById(R.id.powered_by_text).setVisibility(View.VISIBLE);
        view.findViewById(R.id.customer_allience_text_btn).setVisibility(View.VISIBLE);
    }

    public void hideFooter() {
        view.findViewById(R.id.logo_powered_by).setVisibility(View.GONE);
        view.findViewById(R.id.powered_by_text).setVisibility(View.GONE);
        view.findViewById(R.id.customer_allience_text_btn).setVisibility(View.GONE);
    }

    @SuppressLint("DiscouragedPrivateApi")
    public static void setCursorColor(EditText view, @ColorInt int color) {
        try {
            // Get the cursor resource id
            @SuppressLint("SoonBlockedPrivateApi")
            Field field = TextView.class.getDeclaredField("mCursorDrawableRes");
            field.setAccessible(true);
            int drawableResId = field.getInt(view);

            // Get the editor
            field = TextView.class.getDeclaredField("mEditor");
            field.setAccessible(true);
            Object editor = field.get(view);

            // Get the drawable and set a color filter
            Drawable drawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
            Drawable[] drawables = {drawable, drawable};

            // Set the drawables
            field = editor.getClass().getDeclaredField("mCursorDrawable");
            field.setAccessible(true);
            field.set(editor, drawables);
        } catch (Exception ignored) {
        }
    }

    public void showProgressBar() {
        progressBar = view.findViewById(R.id.progress_bar);
        removeRatingView(view);
        removeTextQuestionView(view);
        removeMultipleChoiceTypeQuestion(view);
        ((TextView) view.findViewById(R.id.layout_header_textview)).setText("");
        ((TextView) view.findViewById(R.id.layout_header_required_textview)).setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor(ConfigurationConstant.primaryColor), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);
        createAndShowAlertDialog();
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        createAndShowAlertDialog();
    }

}
