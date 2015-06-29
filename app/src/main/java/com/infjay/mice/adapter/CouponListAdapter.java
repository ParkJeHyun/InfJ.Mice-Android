package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.BusinessCardInfo;
import com.infjay.mice.artifacts.CouponInfo;

import java.util.ArrayList;

/**
 * Created by admin on 2015-06-29.
 */
public class CouponListAdapter extends ArrayAdapter<CouponInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<CouponInfo> infoList = null;
    private Context mContext = null;

    public CouponListAdapter(Context c, int textViewResourceId,
                             ArrayList<CouponInfo> arrays) {
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
    public CouponInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_coupon, null);
            viewHolder.tvCouponName = (TextView) v.findViewById(R.id.tvListRowCouponName);
            viewHolder.tvCouponSerial = (TextView)v.findViewById(R.id.tvListRowCouponSerial);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvCouponName.setText(getItem(position).couponName);
        viewHolder.tvCouponSerial.setText(getItem(position).couponSerial);

        viewHolder.couponSeq = infoList.get(position).couponSeq;
        return v;
    }


}