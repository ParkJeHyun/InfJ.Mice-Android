package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.SponsorInfo;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-05-24.
 */

public class SponsorAdapter extends ArrayAdapter<SponsorInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<SponsorInfo> infoList = null;
    private Context mContext = null;

    public SponsorAdapter(Context c, int textViewResourceId, ArrayList<SponsorInfo> arrays) {
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
    public SponsorInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_sponser, null);
            viewHolder.tvSponsorName = (TextView) v.findViewById(R.id.tvListRowSponser);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSponsorName.setText(getItem(position).sponsorName);
        viewHolder.tvSponsorName.setTextColor(0xFFFFFFFF);

        viewHolder.sponsorInfo= infoList.get(position);
        viewHolder.sponsorSeq = infoList.get(position).sponsorSeq;

        return v;
    }


}
