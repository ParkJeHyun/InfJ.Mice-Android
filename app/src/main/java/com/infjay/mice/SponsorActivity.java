package com.infjay.mice;

/**
 * Created by Administrator on 2015-05-02.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.SponsorAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.SponsorInfo;

import java.util.ArrayList;

public class SponsorActivity extends ActionBarActivity {

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
        setTitle("Agenda > Sponsor");

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
                Intent intent = new Intent(getApplicationContext(), SponsorDetailActivity.class);
                intent.putExtra("clicked", rowName);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_basic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}