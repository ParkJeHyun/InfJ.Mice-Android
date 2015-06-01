package com.infjay.mice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import com.infjay.mice.artifacts.*;

/**
 * Created by KimJS on 2015-05-24.
 */


public class CardholderAdapter extends ArrayAdapter<BusinessCardInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<BusinessCardInfo> infoList = null;
    private Context mContext = null;

    public CardholderAdapter(Context c, int textViewResourceId,
                          ArrayList<BusinessCardInfo> arrays) {
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
    public BusinessCardInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_cardholder, null);
            viewHolder.tvCardName = (TextView) v.findViewById(R.id.tvListRowCardName);
            viewHolder.tvCardCompany = (TextView)v.findViewById(R.id.tvListRowCompany);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvCardName.setText(getItem(position).getName());
        viewHolder.tvCardCompany.setText(getItem(position).getCompany());

        return v;
    }


}
