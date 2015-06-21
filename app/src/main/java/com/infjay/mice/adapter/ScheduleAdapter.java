package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.MyScheduleInfo;

import java.util.ArrayList;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class ScheduleAdapter extends ArrayAdapter<MyScheduleInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<MyScheduleInfo> sInfoList = null;
    private Context mContext = null;

    public ScheduleAdapter(Context c, int textViewResourceId, ArrayList<MyScheduleInfo> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
        sInfoList = arrays;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public MyScheduleInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row, null);
            viewHolder.tvScheduleTitle = (TextView) v.findViewById(R.id.tvListRow);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvScheduleTitle.setText(getItem(position).scheduleTitle);
        viewHolder.myScheduleInfo= sInfoList.get(position);

        return v;
    }
}
