package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.IndoorMapInfo;

import java.util.ArrayList;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class IndoorMapAdapter extends ArrayAdapter<IndoorMapInfo> {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<IndoorMapInfo> imList = null;
    private Context mContext = null;

    public IndoorMapAdapter(Context c, int textViewResourceId, ArrayList<IndoorMapInfo> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
        imList = arrays;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public IndoorMapInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_indoor_map, null);
            viewHolder.tvIndoorMapTitle = (TextView) v.findViewById(R.id.tvListRowFloor);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvIndoorMapTitle.setText(getItem(position).title);
        viewHolder.indoorMapSeq = getItem(position).indoorMapSeq;

        return v;
    }
}
