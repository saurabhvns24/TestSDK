package com.customer_alliance.sdk.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;

import com.customer_alliance.sdk.constant.CommonConstants;
import com.customer_alliance.sdk.constant.ConfigurationConstant;
import com.customer_alliance.sdk.listener.onItemClickListener;
import com.customer_alliance.sdk.R;
import com.customer_alliance.sdk.model.CheckedAndUncheckedOption;
import com.customer_alliance.sdk.view.FeedBackDialog;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceAdapter extends ArrayAdapter<CheckedAndUncheckedOption> {
    private Context mContext;
    private ArrayList<CheckedAndUncheckedOption> optionLookList;
    private boolean isFromView = false;
    private final onItemClickListener onItemClickListener;

    public MultipleChoiceAdapter(Context context, List<CheckedAndUncheckedOption> objects, FeedBackDialog showAlterDialog) {
        super(context, 0, objects);
        this.mContext = context;
        this.optionLookList = (ArrayList<CheckedAndUncheckedOption>) objects;
        this.onItemClickListener = showAlterDialog;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView);
    }

    @SuppressLint("InflateParams")
    public View getCustomView(final int position, View convertView) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.multiple_choice_dropdown_item, null);
            holder = new ViewHolder();
            holder.mTextView = convertView.findViewById(R.id.SelectOption);
            holder.mCheckBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTextView.setText(optionLookList.get(position).getTitle());
        isFromView = true;
        holder.mCheckBox.setChecked(optionLookList.get(position).isSelected());
        isFromView = false;
        holder.mCheckBox.setTag(position);
        if (Build.VERSION.SDK_INT < 21) {
            CompoundButtonCompat.setButtonTintList(holder.mCheckBox, ColorStateList.valueOf(Color.parseColor(ConfigurationConstant.primaryColor)));//Use android.support.v4.widget.CompoundButtonCompat when necessary else
        } else {
            holder.mCheckBox.setButtonTintList(ColorStateList.valueOf(Color.parseColor(ConfigurationConstant.primaryColor)));//setButtonTintList is accessible directly on API>19
        }
        holder.mCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isFromView) {
                optionLookList.get(position).setSelected(isChecked);
                onItemClickListener.onItemClicked(position, optionLookList.get(position).isSelected());
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private TextView mTextView;
        private CheckBox mCheckBox;
    }
}

