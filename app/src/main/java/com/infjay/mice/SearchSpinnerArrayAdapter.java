package com.infjay.mice;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.artifacts.BusinessCardInfo;

import static android.graphics.Color.*;

/**
 * Created by Administrator on 2015-06-08.
 */
public class SearchSpinnerArrayAdapter extends ArrayAdapter<String> {
    String[] object = new String[] {};
    Context context;

    Typeface typeface = null;

    public SearchSpinnerArrayAdapter(Context context,int resource,String[] object) {
        super(context, resource,object);
        this.context = context;
        this.object = object;
    }

    public View getDropDownView(int position, View convertView,ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);

        tv.setText(object[position]);

        tv.setTextColor(BLACK);
        tv.setTypeface(Typeface.MONOSPACE);
        tv.setBackgroundColor(WHITE);
        //tv.setTextSize(15);
        //tv.setHeight(50);
        return convertView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(
                    android.R.layout.simple_spinner_item, parent, false);
        }

        TextView tv = (TextView) convertView
                .findViewById(android.R.id.text1);

        tv.setText(object[position]);

        tv.setTypeface(Typeface.MONOSPACE);
        tv.setTextColor(Color.BLACK);
//            tv.setTextSize(15);
        //tv.setHeight(50);

//            tv.setTextSize(12);

        return convertView;
    }

}
