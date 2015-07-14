package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.infjay.mice.adapter.SponsorAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.SponsorInfo;

import java.util.ArrayList;

public class SponsorActivity extends CustomActionBarActivity {

    private ListView lvSponsorList;
    private SponsorInfo sInfo;
    private ArrayList<SponsorInfo> sponsorArrayList;

    private String TAG = "SponsorActivity";

    private ListView lvSponser;
    private SponsorAdapter adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spon);

        lvSponsorList = (ListView)findViewById(R.id.lvSponsorList);

        sponsorArrayList = new ArrayList<SponsorInfo>();
        sInfo = new SponsorInfo();
        sInfo.sponsorName = "Samgsung";
        sponsorArrayList.add(sInfo);
        sInfo = new SponsorInfo();
        sInfo.sponsorName = "LG";
        sponsorArrayList.add(sInfo);
        sInfo = new SponsorInfo();
        sInfo.sponsorName = "Naver";
        sponsorArrayList.add(sInfo);

        adapter = new SponsorAdapter(this, R.layout.list_row, sponsorArrayList);
        lvSponsorList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvSponsorList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String rowName = vh.tvSponsorName.getText().toString();

                //start Activity about sponser clicked
                Intent intent = new Intent(getApplicationContext(), SponsorInfoActivity.class);
                intent.putExtra("clicked", rowName);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }
}