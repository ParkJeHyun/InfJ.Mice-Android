package com.infjay.mice;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONException;
import org.json.JSONObject;


public class ConfInfoActivity extends ActionBarActivity {

    private String TAG = "ConfInfoActivity";

    private ConferenceInfo conferenceInfo;
    private TextView tvConferenceName;
    private TextView tvConferencePeriod;
    private TextView tvConferenceSummary;

    private boolean isRefreshCompleted = false;
    protected ProgressDialog dialog;
    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_conference_info"))
                    {
                        if(jobj.get("result").equals("GET_CONFERENCE_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "Error in getting conference info", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "Fail in getting congerence info", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_SUCCESS"))
                        {
                            JSONObject conf_jobj = new JSONObject(jobj.get("attach")+"");
                            ConferenceInfo conferenceInfo = new ConferenceInfo();

                            conferenceInfo.conferenceName = conf_jobj.get("conference_name").toString();
                            conferenceInfo.conferenceStartDate = conf_jobj.get("conference_start_date").toString().split("T")[0];
                            conferenceInfo.conferenceEndDate = conf_jobj.get("conference_end_date").toString().split("T")[0];
                            conferenceInfo.conferenceSummary = conf_jobj.get("summary").toString();

                            //Insert DB
                            int count = DBManager.getManager(getApplicationContext()).getCountConference();
                            if(count == 0)
                            {
                                DBManager.getManager(getApplicationContext()).insertConferenceInfo(conferenceInfo);
                            }
                            else
                            {
                                DBManager.getManager(getApplicationContext()).updateConferenceInfo(conferenceInfo);
                            }
                            isRefreshCompleted = true;
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Message result", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong MessageType", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_info);
        setTitle("Agenda > Conference Info");

        tvConferenceName = (TextView)findViewById(R.id.tvConferenceName);
        tvConferencePeriod = (TextView)findViewById(R.id.tvConferencePeriod);
        tvConferenceSummary = (TextView)findViewById(R.id.tvConferenceSummary);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume Start");

        //Get data from DB
        conferenceInfo = DBManager.getManager(getApplicationContext()).getConferenceInfo();

        if(conferenceInfo.conferenceName != null)
        {
            setConferenceActivity();
        }
    }

    private void setConferenceActivity()
    {
        tvConferenceName.setText(conferenceInfo.conferenceName);
        tvConferencePeriod.setText(conferenceInfo.conferenceStartDate + " ~ " + conferenceInfo.conferenceEndDate);
        tvConferenceSummary.setText(conferenceInfo.conferenceSummary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conf_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.itRefreshConferenceInfo) {
            refresh();

            conferenceInfo = DBManager.getManager(getApplicationContext()).getConferenceInfo();
            setConferenceActivity();
            isRefreshCompleted = false;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refresh()
    {
        //request to server
        JSONObject jobj = new JSONObject();
        try
        {
            jobj.put("messagetype", "get_conference_info");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }
}
