package com.infjay.mice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.infjay.mice.R;
import com.infjay.mice.artifacts.MessageInfo;
import com.infjay.mice.artifacts.MessengerInfo;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;

/**
 * Created by KimJS on 2015-05-24.
 */


public class MessengerAdapter extends ArrayAdapter<MessageInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<MessageInfo> infoList = null;
    private Context mContext = null;

    private String mySeq;

    public MessengerAdapter(Context c, int textViewResourceId,
                            ArrayList<MessageInfo> arrays) {
        super(c, textViewResourceId, arrays);
        this.inflater = LayoutInflater.from(c);
        this.mContext = c;
        infoList = arrays;

        mySeq = DBManager.getManager(getContext()).getUserInfo().userSeq;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public MessageInfo getItem(int position) {
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
            v = inflater.inflate(R.layout.list_row_messenger, null);

            viewHolder.tvMessengerName = (TextView) v.findViewById(R.id.tvListRowMessengerName);
            viewHolder.tvMessengerDate = (TextView)v.findViewById(R.id.tvListRowMessengerDate);
            viewHolder.tvMessengerMessage = (TextView)v.findViewById(R.id.tvListRowMessengerMessage);

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        if(mySeq == null)
        {
            return v;
        }

        if(mySeq.equals(getItem(position).senderUserSeq))
        {
            viewHolder.tvMessengerName.setText(getItem(position).receiverName);
            viewHolder.tvMessengerDate.setText(getItem(position).sendTime);
            viewHolder.tvMessengerMessage.setText(getItem(position).messageText);
            viewHolder.targetSeq = getItem(position).receiverUserSeq;
        }
        else if(mySeq.equals(getItem(position).receiverUserSeq))
        {
            viewHolder.tvMessengerName.setText(getItem(position).senderName);
            viewHolder.tvMessengerDate.setText(getItem(position).sendTime);
            viewHolder.tvMessengerMessage.setText(getItem(position).messageText);
            viewHolder.targetSeq = getItem(position).senderUserSeq;
        }
        else
        {

        }

        return v;
    }


}
