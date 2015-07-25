package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.SettingListInfo;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-07-26.
 */
public class SettingListAdapter extends ArrayAdapter<SettingListInfo> {
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<SettingListInfo> imList = null;
    private Context mContext = null;

    public SettingListAdapter(Context c, int textViewResourceId, ArrayList<SettingListInfo> arrays) {
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
    public SettingListInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_setting, null);
            viewHolder.tvSettingTitle = (TextView) v.findViewById(R.id.tvSettingTitle);
            viewHolder.tvSettingSubtitle = (TextView) v.findViewById(R.id.tvSettingSubtitle);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSettingTitle.setText(getItem(position).settingTitle);
        viewHolder.tvSettingSubtitle.setText(getItem(position).settingSubtitle);

        return v;
    }
}
