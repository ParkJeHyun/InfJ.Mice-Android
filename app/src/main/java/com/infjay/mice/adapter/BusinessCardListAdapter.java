package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.BusinessCardInfo;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-08-18.
 */
public class BusinessCardListAdapter extends ArrayAdapter<BusinessCardInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<BusinessCardInfo> infoList = null;
    private Context mContext = null;

    public BusinessCardListAdapter(Context c, int textViewResourceId,
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
            v = inflater.inflate(R.layout.list_row_add_card, null);
            viewHolder.tvAddCardName = (TextView) v.findViewById(R.id.tvAddCardName);
            viewHolder.tvAddCardCompany = (TextView)v.findViewById(R.id.tvAddCardCompany);
            viewHolder.cbAddCard = (CheckBox)v.findViewById(R.id.cbAddCard);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvAddCardName.setText(getItem(position).name);
        viewHolder.tvAddCardCompany.setText(getItem(position).company);
        viewHolder.userSeq = getItem(position).userSeq;
        viewHolder.cbAddCard.setFocusable(false);
        viewHolder.cbAddCard.setClickable(false);

        return v;
    }

}
