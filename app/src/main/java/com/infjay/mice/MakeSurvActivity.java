package com.infjay.mice;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.infjay.mice.adapter.SessionListAdapter;
import com.infjay.mice.adapter.ViewHolder;
import com.infjay.mice.database.DBManager;

import java.util.Locale;


public class MakeSurvActivity extends CustomActionBarActivity {

    private String title;
    private static int numberOfQuestions;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_surv);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        numberOfQuestions = Integer.parseInt(intent.getStringExtra("numberOfQuestions"));
        setTitle(title);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.make_survey_pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }

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
            // Show total pages.
            return numberOfQuestions + 1;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            if(position==0)
            {
                return "Help";
            }
            return "Question " + position;
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
        private int curPageNum;

        private EditText questionTitle, item1, item2, item3, item4, item5;
        private RadioButton rbCheckBox, rbRadioButton, rbSubjective;
        public DummySectionFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            curPageNum = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView;
            if(curPageNum == 1)
            {
                rootView = inflater.inflate(R.layout.make_survey_help_fragment, container, false);
            }
            else
            {
                rootView = inflater.inflate(R.layout.make_survey_fragment, container, false);
                questionTitle = (EditText)rootView.findViewById(R.id.etQuestionTitle);

                rbCheckBox = (RadioButton)rootView.findViewById(R.id.rbCheckBox);
                rbRadioButton = (RadioButton)rootView.findViewById(R.id.rbRadioButton);
                rbSubjective = (RadioButton)rootView.findViewById(R.id.rbSubjective);

                item1 = (EditText)rootView.findViewById(R.id.etSurveyItem1);
                item2 = (EditText)rootView.findViewById(R.id.etSurveyItem2);
                item3 = (EditText)rootView.findViewById(R.id.etSurveyItem3);
                item4 = (EditText)rootView.findViewById(R.id.etSurveyItem4);
                item5 = (EditText)rootView.findViewById(R.id.etSurveyItem5);

                rbCheckBox.setOnClickListener(new RadioButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item1.setEnabled(true);
                        item2.setEnabled(true);
                        item3.setEnabled(true);
                        item4.setEnabled(true);
                        item5.setEnabled(true);
                    }
                });

                rbRadioButton.setOnClickListener(new RadioButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item1.setEnabled(true);
                        item2.setEnabled(true);
                        item3.setEnabled(true);
                        item4.setEnabled(true);
                        item5.setEnabled(true);
                    }
                });

                rbSubjective.setOnClickListener(new RadioButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        item1.setEnabled(false);
                        item2.setEnabled(false);
                        item3.setEnabled(false);
                        item4.setEnabled(false);
                        item5.setEnabled(false);
                        Toast.makeText(getActivity().getApplicationContext(), "Items will not be applied", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            return rootView;
        }

        @Override
        public void onPause() {
            super.onPause();
            Log.d("MakeSurvey", "onPaused");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_surv, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itMakeSurveyDone) {
            //TODO
            // SAVE
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
