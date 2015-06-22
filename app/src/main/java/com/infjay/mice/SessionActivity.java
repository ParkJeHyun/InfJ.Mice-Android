package com.infjay.mice;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.artifacts.AgendaSessionInfo;

import java.util.ArrayList;

public class SessionActivity extends FragmentActivity {

    private ViewPager viewPager;
    private static int NUM_AWESOME_VIEWS = 5;
    private Context cxt;
    private sessionPagerAdapter sessionAdapter;
    private int position = 0;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvSessionDate;

    private ListView lvSessionList;

    //임시용
    private SessionListAdapter adapter;
    private AgendaSessionInfo asInfo;
    private ArrayList<AgendaSessionInfo> sessionArrayList;

    private String TAG = "SessionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
        setTitle("Agenda > Session Schedule");

        cxt = this;


        sessionAdapter = new sessionPagerAdapter(cxt);
        //viewPager = (ViewPager) findViewById(R.id.viewpager);
        //viewPager.setAdapter(sessionAdapter);


        ivLeft = (ImageView)findViewById(R.id.ivSessionViewLeft);
        ivRight = (ImageView)findViewById(R.id.ivSessionViewRight);
        tvSessionDate = (TextView)findViewById(R.id.tvSessionDate);

        lvSessionList = (ListView)findViewById(R.id.lvSessionList);
        lvSessionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //아이템 클릭 시 행동

                Intent intent = new Intent(getApplicationContext(), SessionInfoActivity.class);
                startActivity(intent);
            }
        });

        ivLeft.setOnClickListener(new OnClickDateChangeListener());
        ivRight.setOnClickListener(new OnClickDateChangeListener());

        setSessionList();

    }


    private void setSessionList(){
        sessionArrayList = new ArrayList<>();
        for(int i=1; i<=3; i++) {
            asInfo = new AgendaSessionInfo();
            asInfo.sessionTitle = "session no."+i;
            sessionArrayList.add(asInfo);
        }
        adapter = new SessionListAdapter(getApplication(), R.layout.list_row, sessionArrayList);
        lvSessionList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //lvSessionList.setAdapter(tmpListAdapter);
    }




    class OnClickDateChangeListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.ivSessionViewLeft){
                if(position != 0)
                {
                    position--;
                }

                tvSessionDate.setText("Page " + position);
                //viewPager.setCurrentItem(position);
            }
            else if(view.getId() == R.id.ivSessionViewRight){
                if(position != NUM_AWESOME_VIEWS-1)
                {
                    position++;
                }

                tvSessionDate.setText("Page " + position);
                //viewPager.setCurrentItem(position);
            }
            setSessionList();
        }
    }

    private class sessionPagerAdapter extends PagerAdapter{
        private LayoutInflater mInflater;

        public sessionPagerAdapter(Context c)
        {
            super();
            mInflater = LayoutInflater.from(c);
        }

        @Override
        public int getCount() {
            return NUM_AWESOME_VIEWS;
        }

        /**
         * Create the page for the given position.  The adapter is responsible
         * for adding the view to the container given here, although it only
         * must ensure this is done by the time it returns from
         * {@link #finishUpdate(android.view.ViewGroup)}.
         *
         * @param collection The containing View in which the page will be shown.
         * @param position The page position to be instantiated.
         * @return Returns an Object representing the new page.  This does not
         * need to be a View, but can be some other container of the page.
         */


        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            tvSessionDate.setText("Page " + position);

            TextView tv = new TextView(cxt);
            tv.setText("Page " + position);
            tv.setTextColor(Color.BLACK);
            tv.setTextSize(20);

            collection.addView(tv,0);

            return tv;
        }


        /**
         * Remove a page for the given position.  The adapter is responsible
         * for removing the view from its container, although it only must ensure
         * this is done by the time it returns from {@link #finishUpdate(android.view.ViewGroup)}.
         *
         * @param collection The containing View from which the page will be removed.
         * @param position The page position to be removed.
         * @param view The same object that was returned by
         * {@link #instantiateItem(android.view.View, int)}.
         */
        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((TextView) view);
        }


        /**
         * Determines whether a page View is associated with a specific key object
         * as returned by instantiateItem(ViewGroup, int). This method is required
         * for a PagerAdapter to function properly.
         * @param view Page View to check for association with object
         * @param object Object to check for association with view
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view==object);
        }


        /**
         * Called when the a change in the shown pages has been completed.  At this
         * point you must ensure that all of the pages have actually been added or
         * removed from the container as appropriate.
         * @param arg0 The containing View which is displaying this adapter's
         * page views.
         */
        @Override
        public void finishUpdate(ViewGroup arg0) {}


        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {}

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(ViewGroup arg0) {}

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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
