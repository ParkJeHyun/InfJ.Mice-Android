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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConfInfoActivity extends CustomActionBarActivity {

    private String TAG = "ConfInfoActivity";

    private ConferenceInfo conferenceInfo;
    private TextView tvConferenceName;
    private TextView tvConferencePeriod;
    private TextView tvConferenceSummary;

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
                            String conference_start_date = conf_jobj.get("conference_start_date").toString().split("Z")[0];
                            String conference_end_date = conf_jobj.get("conference_end_date").toString().split("Z")[0];
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            Date d = new Date();
                            Date d2 = new Date();
                            try
                            {
                                d = sdf.parse(conference_start_date);
                                d2 = sdf.parse(conference_end_date);
                            }
                            catch(ParseException e)
                            {
                                e.printStackTrace();
                            }
                            long dd = d.getTime() + (1000 * 60 * 60 * 9);
                            long dd2 = d2.getTime() + (1000 * 60 * 60 * 9);
                            d = new Date(dd);
                            d2 = new Date(dd2);
                            conference_start_date = sdf.format(d);
                            conference_end_date = sdf.format(d2);

                            conferenceInfo.conferenceName = conf_jobj.get("conference_name").toString();
                            conferenceInfo.conferenceStartDate = conference_start_date.split("T")[0];
                            conferenceInfo.conferenceEndDate = conference_end_date.split("T")[0];
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
                            //test
                            setConferenceActivity();
                            Toast.makeText(getApplicationContext(), "Refresh Done", Toast.LENGTH_SHORT).show();
                            //finish();
                            //startActivity(getIntent());
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

        tvConferenceName = (TextView)findViewById(R.id.tvConferenceName);
        tvConferencePeriod = (TextView)findViewById(R.id.tvConferencePeriod);
        tvConferenceSummary = (TextView)findViewById(R.id.tvConferenceSummary);

        //Get data from DB
        conferenceInfo = DBManager.getManager(getApplicationContext()).getConferenceInfo();
        setConferenceActivity();
    }

    private void setConferenceActivity()
    {
        if(conferenceInfo.conferenceName != null)
        {
            tvConferenceName.setText(conferenceInfo.conferenceName);
            tvConferencePeriod.setText(conferenceInfo.conferenceStartDate + " ~ " + conferenceInfo.conferenceEndDate);
            tvConferenceSummary.setText(conferenceInfo.conferenceSummary);
        }
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
