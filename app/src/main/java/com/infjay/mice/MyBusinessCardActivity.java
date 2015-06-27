package com.infjay.mice;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


public class MyBusinessCardActivity extends ActionBarActivity {

    private Button btShareBusinessCard;
    private Button btModifyBusinessCard;
    private boolean shareFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card);

        btModifyBusinessCard = (Button)findViewById(R.id.btModifyBusinessCard);
        btModifyBusinessCard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(getApplicationContext(), MyBusinessCardModifyActivity.class);
                startActivity(intent);
            }
        });

        btShareBusinessCard = (Button)findViewById(R.id.btShareBusinessCard);
        btShareBusinessCard.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                shareFlag = !shareFlag;
                if(shareFlag == true)
                {
                    btShareBusinessCard.setText("UnShare");
                }
                else
                {
                    btShareBusinessCard.setText("Share");
                }
            }
        });

        //shareFlag = false;
        //setImage();
        ///setText();
        //shareBtn = (Button)findViewById(R.id.shareBtn);
        //shareBtn.setOnClickListener(new myBriefBtnListenner());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_brief, menu);
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
