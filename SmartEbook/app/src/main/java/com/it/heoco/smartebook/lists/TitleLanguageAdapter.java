package com.it.heoco.smartebook.lists;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it.heoco.smartebook.R;

import java.util.ArrayList;

/**
 * Created by heoco on 11/04/2017.
 */

public class TitleLanguageAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<SpinnerLangItem> spinnerLangItems;

    public TitleLanguageAdapter(Context context, ArrayList<SpinnerLangItem> spinnerLangItems) {
        this.context = context;
        this.spinnerLangItems = spinnerLangItems;
    }

    @Override
    public int getCount() {
        return spinnerLangItems.size();
    }

    @Override
    public Object getItem(int index) {
        return spinnerLangItems.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_language, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(spinnerLangItems.get(position).getIcon());
        txtTitle.setText(spinnerLangItems.get(position).getTitle());
        return convertView;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_language, null);
        }

        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        imgIcon.setImageResource(spinnerLangItems.get(position).getIcon());
        txtTitle.setText(spinnerLangItems.get(position).getTitle());
        return convertView;
    }
}