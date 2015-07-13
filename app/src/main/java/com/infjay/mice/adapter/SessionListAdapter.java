package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.AgendaSessionInfo;

import java.util.ArrayList;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class SessionListAdapter extends ArrayAdapter<AgendaSessionInfo>{

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<AgendaSessionInfo> infoList = null;
    private Context mContext = null;

    public SessionListAdapter(Context c, int textViewResourceId, ArrayList<AgendaSessionInfo> arrays) {
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
    public AgendaSessionInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_session, null);
            viewHolder.tvSessionName = (TextView) v.findViewById(R.id.tvListRowSessionTitle);
            viewHolder.tvSessionWriter = (TextView) v.findViewById(R.id.tvListRowSessionWriter);
            viewHolder.tvSessionPresenter = (TextView) v.findViewById(R.id.tvListRowSessionPresenter);
            viewHolder.tvSessionStartTime = (TextView) v.findViewById(R.id.tvSessionStartTime);
            viewHolder.tvSessionEndTime = (TextView) v.findViewById(R.id.tvSessionEndTime);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        viewHolder.tvSessionName.setText(getItem(position).sessionTitle);
        viewHolder.tvSessionWriter.setText("Writer : " + getItem(position).sessionWriterUserSeq);
        viewHolder.tvSessionPresenter.setText("Presenter : " + getItem(position).sessionPresenterUserSeq);
        viewHolder.tvSessionStartTime.setText(getItem(position).sessionStartTime);
        viewHolder.tvSessionEndTime.setText("~ " + getItem(position).sessionEndTime);

        viewHolder.tvSessionName.setTextColor(0xFFFFFFFF);
        viewHolder.tvSessionWriter.setTextColor(0xFFFFFFFF);
        viewHolder.tvSessionPresenter.setTextColor(0xFFFFFFFF);
        viewHolder.tvSessionStartTime.setTextColor(0xFFFFFFFF);
        viewHolder.tvSessionEndTime.setTextColor(0xFFFFFFFF);

        viewHolder.agendaSessionInfo = infoList.get(position);
        viewHolder.sessionSeq = infoList.get(position).agendaSessionSeq;
        return v;
    }


}
