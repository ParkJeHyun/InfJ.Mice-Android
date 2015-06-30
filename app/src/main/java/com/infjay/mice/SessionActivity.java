package com.infjay.mice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
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
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalFunction;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SessionActivity extends ActionBarActivity {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    //for inner class
    protected static int curPageNum;
    protected static int totalPageCount;
    //JS
    protected static ListView lvSessionList;
    protected static SessionListAdapter adapter;
    protected static AgendaSessionInfo asInfo;
    protected static ArrayList<AgendaSessionInfo> sessionArrayList;

    protected static ArrayList<String> conferenceDates;


    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_all_session_info"))
                    {
                        if(jobj.get("result").equals("GET_ALL_SESSION_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "Error in getting session information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_ALL_SESSION_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "Fail in getting session information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_ALL_SESSION_INFO_SUCCESS"))
                        {
                            JSONArray sessionJsonArray = new JSONArray(jobj.get("attach").toString());
                            AgendaSessionInfo agendaSessionInfo;
                            sessionArrayList = new ArrayList<AgendaSessionInfo>();
                            for(int i = 0; i < sessionJsonArray.length(); i++)
                            {
                                JSONObject sessionJsonObj = new JSONObject(sessionJsonArray.get(i).toString());
                                agendaSessionInfo = new AgendaSessionInfo();
                                String session_start_time = sessionJsonObj.get("session_start_time").toString().split("Z")[0];
                                String session_end_time = sessionJsonObj.get("session_end_time").toString().split("Z")[0];
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date d = new Date();
                                Date d2 = new Date();
                                try
                                {
                                    d = sdf.parse(session_start_time);
                                    d2 = sdf.parse(session_end_time);
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                long dd = d.getTime() + (1000 * 60 * 60 * 9);
                                long dd2 = d2.getTime() + (1000 * 60 * 60 * 9);
                                d = new Date(dd);
                                d2 = new Date(dd2);
                                session_start_time = sdf.format(d);
                                session_end_time = sdf.format(d2);

                                agendaSessionInfo.sessionTitle = sessionJsonObj.get("title").toString();
                                agendaSessionInfo.sessionContents = sessionJsonObj.get("contents").toString();
                                agendaSessionInfo.sessionWriterUserSeq = sessionJsonObj.get("writer_user_seq").toString();
                                agendaSessionInfo.sessionWriterName = sessionJsonObj.get("writer_user_name").toString();
                                agendaSessionInfo.sessionPresenterUserSeq = sessionJsonObj.get("presenter_user_seq").toString();
                                agendaSessionInfo.sessionPresenterName = sessionJsonObj.get("presenter_user_name").toString();
                                agendaSessionInfo.sessionStartTime = session_start_time.split("T")[1].split("\\.")[0];
                                agendaSessionInfo.sessionEndTime = session_end_time.split("T")[1].split("\\.")[0];
                                agendaSessionInfo.sessionAttached = sessionJsonObj.get("attached").toString();
                                agendaSessionInfo.agendaSessionSeq = sessionJsonObj.get("agenda_session_seq").toString();

                                agendaSessionInfo.sessionDate = sessionJsonObj.get("session_start_time").toString().split("T")[0];
                                sessionArrayList.add(agendaSessionInfo);
                            }

                            DBManager.getManager(getApplicationContext()).deleteAgendaSession();
                            DBManager.getManager(getApplicationContext()).insertSessionToAgenda(sessionArrayList);
                            finish();
                            startActivity(getIntent());
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
        setContentView(R.layout.activity_session);

        totalPageCount = 0;

        //set conferences date
        ConferenceInfo conferenceInfo = DBManager.getManager(getApplicationContext()).getConferenceInfo();
        if(conferenceInfo != null)
        {
            conferenceDates = GlobalFunction.getConferenceDates(conferenceInfo);
            totalPageCount = conferenceDates.size();
            sessionArrayList = DBManager.getManager(getApplicationContext()).getAllSessionFromAgenda();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.session_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DummySectionFragment (defined as a static inner class
            // below) with the page number as its lone argument.
            Fragment fragment = new DummySectionFragment();
            Bundle args = new Bundle();
            args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return totalPageCount;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            Locale l = Locale.getDefault();
            if(conferenceDates.size() == totalPageCount)
            {
                return conferenceDates.get(position);
            }
            else
                return null;

        }
    }

    /**
     * A fragment representing a section of the app
     */
    public static class DummySectionFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String ARG_SECTION_NUMBER = "section_number";

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.session_fragment, container, false);
            curPageNum = getArguments().getInt(ARG_SECTION_NUMBER);

            lvSessionList = (ListView)rootView.findViewById(R.id.lvSessionList);

            sessionArrayList = DBManager.getManager(getActivity().getApplicationContext()).getSessionFromAgendaBySessionDate(conferenceDates.get(curPageNum-1));

            adapter = new SessionListAdapter(getActivity().getApplicationContext(), R.layout.list_row_session, sessionArrayList);
            lvSessionList.setAdapter(adapter);

            lvSessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    ViewHolder vh = (ViewHolder) view.getTag();
                    String sesseionSeq = vh.sessionSeq;

                    Intent intent = new Intent(getActivity().getApplicationContext(), SessionInfoActivity.class);
                    intent.putExtra("sessionSeq", sesseionSeq);
                    intent.putExtra("activityFrom", "SessionActivity");
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
            //lvSessionList.setAdapter(tmpListAdapter);

            return rootView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.itRefreshSessionInfo) {
            refresh();
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
            jobj.put("messagetype", "get_all_session_info");
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);
    }

}
