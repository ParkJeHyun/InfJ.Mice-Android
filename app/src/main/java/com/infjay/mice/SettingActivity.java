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

import com.infjay.mice.adapter.CouponListAdapter;
import com.infjay.mice.adapter.SettingListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.SettingListInfo;

import java.util.ArrayList;

public class SettingActivity extends CustomActionBarActivity {

    private ListView lvSetting;
    private SettingListAdapter adapter;
    private ArrayList<SettingListInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lvSetting = (ListView)findViewById(R.id.lvSetting);
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayList = new ArrayList<SettingListInfo>();
        SettingListInfo sInfoLanguage = new SettingListInfo();
        sInfoLanguage.settingTitle = "Language";
        sInfoLanguage.settingSubtitle = "";
        arrayList.add(sInfoLanguage);

        SettingListInfo sInfoRefresh = new SettingListInfo();
        sInfoRefresh.settingTitle = "Refresh All";
        sInfoRefresh.settingSubtitle = "";
        arrayList.add(sInfoRefresh);

        adapter = new SettingListAdapter(this, R.layout.list_row_setting, arrayList);
        lvSetting.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String title = vh.tvSettingTitle.getText().toString();
                if(title.equals("Refresh All")){
                    Intent intent = new Intent(getApplicationContext(), LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }

                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }
}