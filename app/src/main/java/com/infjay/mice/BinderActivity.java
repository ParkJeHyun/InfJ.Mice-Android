package com.infjay.mice;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.UserInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalFunction;


public class BinderActivity extends CustomActionBarActivity {

    private ListView lvSessionList;
    private String userSeq;
    private SessionListAdapter adapter;

    private ArrayList<String> conferenceDates;
    private ArrayList<AgendaSessionInfo> sessionInfoArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
        lvSessionList = (ListView)findViewById(R.id.lvSessionList);
        userSeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //set conferences date

        if(userSeq == null)
        {
            Toast.makeText(getApplicationContext(), "User Sequence Error", Toast.LENGTH_SHORT).show();
            return;
        }

        sessionInfoArrayList = DBManager.getManager(getApplicationContext()).getAllSessionFromBinder(userSeq);
        adapter = new SessionListAdapter(getApplicationContext(), R.layout.list_row_session, sessionInfoArrayList);
        lvSessionList.setAdapter(adapter);

        lvSessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String sessionSeq = vh.sessionSeq;
                Intent intent = new Intent(getApplicationContext(), SessionInfoActivity.class);
                intent.putExtra("sessionSeq", sessionSeq);
                intent.putExtra("activityFrom", "BinderActivity");
                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();
    }




}
