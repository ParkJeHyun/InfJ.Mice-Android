package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.MemoInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class MemoAdapter extends ArrayAdapter<MemoInfo>{
    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<MemoInfo> memoList = null;
    private Context mContext = null;

    public MemoAdapter(Context c, int textViewResourceId, ArrayList<MemoInfo> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
        memoList = arrays;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public MemoInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_memo, null);
            viewHolder.tvMemoContents = (TextView) v.findViewById(R.id.tvMemoListRow);
            viewHolder.tvMemoModDate = (TextView) v.findViewById(R.id.tvMemoModDate);
            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

<<<<<<< HEAD
        //viewHolder.tvMemoTitle.setText(getItem(position).memoTitle);
=======
        viewHolder.tvMemoContents.setText(getItem(position).contents);
        viewHolder.tvMemoModDate.setText(getItem(position).modDate.substring(0, 10));
        viewHolder.memoSeq = getItem(position).memoSeq;
>>>>>>> 5af52599da4b256364fb2e4404afe31e25a88259

        return v;
    }
}
