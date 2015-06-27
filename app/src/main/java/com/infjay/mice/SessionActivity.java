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

import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.artifacts.AgendaSessionInfo;


public class SessionActivity extends FragmentActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

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
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "06.23";
                case 1:
                    return "06.24";
                case 2:
                    return "06.25";
            }
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

        //JS
        private ListView lvSessionList;
        private SessionListAdapter adapter;
        private AgendaSessionInfo asInfo;
        private ArrayList<AgendaSessionInfo> sessionArrayList;

        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.session_fragment, container, false);
            TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
            curPageNum = getArguments().getInt(ARG_SECTION_NUMBER);
            dummyTextView.setText(curPageNum + "");

            lvSessionList = (ListView)rootView.findViewById(R.id.lvSessionList);

            //temp data
            sessionArrayList = new ArrayList<>();
            for(int i=1; i<=3; i++) {
                asInfo = new AgendaSessionInfo();
                asInfo.sessionTitle = "session no."+ ((curPageNum-1)*3 + i);
                asInfo.sessionStartTime = "13:30";
                asInfo.sessionEndTime = "15:30";
                sessionArrayList.add(asInfo);
            }
            adapter = new SessionListAdapter(getActivity().getApplicationContext(), R.layout.list_row, sessionArrayList);
            lvSessionList.setAdapter(adapter);


            lvSessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity().getApplicationContext(), SessionInfoActivity.class);
                    startActivity(intent);
                }
            });
            adapter.notifyDataSetChanged();
            //lvSessionList.setAdapter(tmpListAdapter);

            return rootView;
        }
    }

}
