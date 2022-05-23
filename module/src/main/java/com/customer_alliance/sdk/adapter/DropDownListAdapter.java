package com.customer_alliance.sdk.adapter;

import java.util.ArrayList;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.customer_alliance.sdk.R;
import com.customer_alliance.sdk.view.FeedBackDialog;

public class DropDownListAdapter extends BaseAdapter {

    private final ArrayList<String> mListItems;
    private final LayoutInflater mInflater;
    private final TextView mSelectedItems;
    private static int selectedCount = 0;
    private static String firstSelected = "";
    private static String selected = "";    //shortened selected values representation

    public static String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        DropDownListAdapter.selected = selected;
    }

    public DropDownListAdapter(Context context, ArrayList<String> items,
                               TextView tv) {
        mListItems = new ArrayList<>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.multiple_choice_dropdown_item, null);
            holder = new ViewHolder();
            holder.tv = convertView.findViewById(R.id.SelectOption);
            holder.checkBox = convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mListItems.get(position));

        final int position1 = position;
        holder.checkBox.setOnClickListener(v -> setText(position1));
        holder.checkBox.setChecked(FeedBackDialog.checkSelected[position]);
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void setText(int position1) {
        if (!FeedBackDialog.checkSelected[position1]) {
            FeedBackDialog.checkSelected[position1] = true;
            selectedCount++;
        } else {
            FeedBackDialog.checkSelected[position1] = false;
            selectedCount--;
        }

        if (selectedCount == 0) {
            mSelectedItems.setText("");
        } else if (selectedCount == 1) {
            for (int i = 0; i < FeedBackDialog.checkSelected.length; i++) {
                if (FeedBackDialog.checkSelected[i]) {
                    firstSelected = mListItems.get(i);
                    break;
                }
            }
            mSelectedItems.setText(firstSelected);
            setSelected(firstSelected);
        } else if (selectedCount > 1) {
            for (int i = 0; i < FeedBackDialog.checkSelected.length; i++) {
                if (FeedBackDialog.checkSelected[i]) {
                    firstSelected = mListItems.get(i);
                    break;
                }
            }
            mSelectedItems.setText(firstSelected + " & " + (selectedCount - 1) + " more");
            setSelected(firstSelected + " & " + (selectedCount - 1) + " more");
        }
    }

    private static class ViewHolder {
        TextView tv;
        CheckBox checkBox;
    }
}
