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
import android.widget.Button;

public class SearchActivity extends ActionBarActivity {
    Button findPeopleBtn;
    Button searchSessionBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findPeopleBtn = (Button)findViewById(R.id.btnFindPeople);
        searchSessionBtn = (Button)findViewById(R.id.btnFindSession);

        findPeopleBtn.setOnClickListener(new SearchBtnListener());
        searchSessionBtn.setOnClickListener(new SearchBtnListener());
    }

    class SearchBtnListener implements View.OnClickListener{
        Intent intent;
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnFindPeople :
                    intent = new Intent(getApplicationContext(),
                            FindPeopleActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btnFindSession :
                    intent = new Intent(getApplicationContext(),
                            SearchSessionActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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