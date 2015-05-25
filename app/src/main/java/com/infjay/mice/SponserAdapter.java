package com.infjay.mice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-05-24.
 */


public class SponserAdapter extends ArrayAdapter<String> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<String> infoList = null;
    private Context mContext = null;

    public SponserAdapter(Context c, int textViewResourceId,
                             ArrayList<String> arrays) {
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
    public String getItem(int position) {
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
            viewHolder.tvSponserName = (TextView) v.findViewById(R.id.tvListRowSponser);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSponserName.setText(getItem(position));

        return v;
    }


}
