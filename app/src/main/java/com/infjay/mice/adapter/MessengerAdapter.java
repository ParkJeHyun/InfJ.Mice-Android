package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.MessengerInfo;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-05-24.
 */


public class MessengerAdapter extends ArrayAdapter<MessengerInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<MessengerInfo> infoList = null;
    private Context mContext = null;

    public MessengerAdapter(Context c, int textViewResourceId,
                            ArrayList<MessengerInfo> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
        infoList = arrays;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public MessengerInfo getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if (v == null) {
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.list_row_messenger, null);

            viewHolder.tvMessengerName = (TextView) v.findViewById(R.id.tvListRowMessengerName);
            viewHolder.tvMessengerDate = (TextView)v.findViewById(R.id.tvListRowMessengerDate);
            viewHolder.tvMessengerMessage = (TextView)v.findViewById(R.id.tvListRowMessengerMessage);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvMessengerName.setText(getItem(position).getName());
        viewHolder.tvMessengerDate.setText(getItem(position).getDate());
        viewHolder.tvMessengerMessage.setText(getItem(position).getMessage());

        return v;
    }


}
