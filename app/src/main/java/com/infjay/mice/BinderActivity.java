package com.infjay.mice;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;

import com.infjay.mice.adapter.MemoAdapter;
import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.MemoInfo;
import com.infjay.mice.database.DBManager;

import java.util.ArrayList;


public class BinderActivity extends ActionBarActivity {

    private ListView lvBinder;
    private SessionListAdapter binderAdapter;
    private ArrayList<AgendaSessionInfo> sessionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder);
    }
    @Override
    protected void onResume() {
        super.onResume();

        lvBinder = (ListView)findViewById(R.id.lvBinder);

        sessionList = new ArrayList<AgendaSessionInfo>();

        sessionList = DBManager.getManager(getApplicationContext()).getAllSessionFromBinder();
        binderAdapter = new SessionListAdapter(getApplication(), R.layout.list_row_session, sessionList);

        lvBinder.setAdapter(binderAdapter);
        binderAdapter.notifyDataSetChanged();

        lvBinder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder) view.getTag();
                String sessionSeq = vh.sessionSeq;

                Intent intent = new Intent(getApplicationContext(), SessionInfoActivity.class);
                intent.putExtra("sessionSeq", sessionSeq);
                startActivity(intent);
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_binder, menu);
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
