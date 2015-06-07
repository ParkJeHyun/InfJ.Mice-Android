package com.infjay.mice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

<<<<<<< HEAD
import com.infjay.mice.artifacts.SponsorInfo;

=======
>>>>>>> cde1fa69334139b647fb9e5e7288369271fcdda4
import java.util.ArrayList;

/**
 * Created by KimJS on 2015-05-24.
 */


<<<<<<< HEAD
public class SponsorAdapter extends ArrayAdapter<SponsorInfo> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<SponsorInfo> infoList = null;
    private Context mContext = null;

    public SponsorAdapter(Context c, int textViewResourceId, ArrayList<SponsorInfo> arrays) {
=======
public class SponsorAdapter extends ArrayAdapter<String> {

    private ViewHolder viewHolder = null;
    private LayoutInflater inflater = null;
    private ArrayList<String> infoList = null;
    private Context mContext = null;

    public SponsorAdapter(Context c, int textViewResourceId,
                          ArrayList<String> arrays) {
>>>>>>> cde1fa69334139b647fb9e5e7288369271fcdda4
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
<<<<<<< HEAD
    public SponsorInfo getItem(int position) {
=======
    public String getItem(int position) {
>>>>>>> cde1fa69334139b647fb9e5e7288369271fcdda4
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
<<<<<<< HEAD
            v = inflater.inflate(R.layout.list_row, null);
            viewHolder.tvSponsorName = (TextView) v.findViewById(R.id.tvListRow);
=======
            v = inflater.inflate(R.layout.list_row_sponser, null);
            viewHolder.tvSponserName = (TextView) v.findViewById(R.id.tvListRowSponser);
>>>>>>> cde1fa69334139b647fb9e5e7288369271fcdda4

            v.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

<<<<<<< HEAD
        viewHolder.tvSponsorName.setText(getItem(position).sponsorName);
        viewHolder.sponsorInfo= infoList.get(position);
=======
        viewHolder.tvSponserName.setText(getItem(position));
>>>>>>> cde1fa69334139b647fb9e5e7288369271fcdda4

        return v;
    }


}
