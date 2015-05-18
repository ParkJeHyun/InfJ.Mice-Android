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

/**
 * Created by Administrator on 2015-05-02.
 */
public class MyBriefCaseActivity extends ActionBarActivity {

    Button mybriefBtn;
    Button CardHolderBtn;
    Button memoBtn;
    Button schedBtn;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_briefcase);

        setBtn();
    }

    public void setBtn(){
        mybriefBtn = (Button)findViewById(R.id.btnMyBusinessCard);
        CardHolderBtn = (Button)findViewById(R.id.btnMyCardHolder);
        schedBtn = (Button)findViewById(R.id.btnMySchedule);
        memoBtn = (Button)findViewById(R.id.btnMyMemo);


        mybriefBtn.setOnClickListener(new briefBtnListenner());
        CardHolderBtn.setOnClickListener(new briefBtnListenner());
        schedBtn.setOnClickListener(new briefBtnListenner());
        memoBtn.setOnClickListener(new briefBtnListenner());
    }

    public class briefBtnListenner implements View.OnClickListener{
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btnMyBusinessCard:
                    intent = new Intent(getApplicationContext(),
                            MyBusinessCardActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnMyCardHolder:
                    intent = new Intent(getApplicationContext(),
                            MyCardHolderActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnMySchedule:
                    intent = new Intent(getApplicationContext(),
                            ScheduleActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btnMyMemo:
                    intent = new Intent(getApplicationContext(),
                            MemoActivity.class);
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