package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.infjay.mice.adapter.ScheduleAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.artifacts.MyScheduleInfo;

import java.util.ArrayList;


public class ScheduleActivity extends CustomActionBarActivity {

    private ListView lvScheduleList;

    private ScheduleAdapter adapter;
    private MyScheduleInfo sInfo;
    private ArrayList<MyScheduleInfo> scheduleArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        lvScheduleList = (ListView)findViewById(R.id.lvScheduleList);


        scheduleArrayList = new ArrayList<MyScheduleInfo>();
        sInfo = new MyScheduleInfo();
        sInfo.scheduleTitle = "Schedule 1";
        scheduleArrayList.add(sInfo);
        sInfo = new MyScheduleInfo();
        sInfo.scheduleTitle = "Go To Sinra";
        scheduleArrayList.add(sInfo);
        sInfo = new MyScheduleInfo();
        sInfo.scheduleTitle = "Back to Seoul";
        scheduleArrayList.add(sInfo);

        adapter = new ScheduleAdapter(this, R.layout.list_row_schedule, scheduleArrayList);
        lvScheduleList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        lvScheduleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ViewHolder vh = (ViewHolder)view.getTag();
                String rowName = vh.tvScheduleTitle.getText().toString();

                Intent intent = new Intent(getApplicationContext(), ScheduleInfoActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_schedule, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itAddSchedule) {
            Intent intent = new Intent(getApplicationContext(),
                    FindReceiverActivity.class);
            intent.putExtra("from","Schedule");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
