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
    String name = "Park Je Hyun";
    String company = "Inf. J";
    String title = "SaJang";
    String phone = "01011111111";
    String email = "lovegjgjgj@naver.com";
    String addr = "서울특별시";
    LinearLayout imageLayout;


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

    public void setImage(){
        LinearLayout.LayoutParams param;

        Display display = getWindowManager().getDefaultDisplay();
        int height = display.getHeight();

        imageLayout = (LinearLayout)findViewById(R.id.briefLinearLayout);

        imageLayout.getLayoutParams().height = height/4;
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.test_picture);
        img.setScaleType(ImageView.ScaleType.FIT_CENTER);

        imageLayout.addView(img);

    }


    public void setText(){
        //명함의 Text를 채우는 함수
        //후에 디비에서 가져온 정보로 변경
        for(int i=0;i<6;i++){
            TextView tmpTextView;
            switch(i){
                case 0:
                    tmpTextView = (TextView)findViewById(R.id.name);
                    tmpTextView.setText(this.name);
                    break;
                case 1:
                    tmpTextView = (TextView)findViewById(R.id.company);
                    tmpTextView.setText(this.company);
                    break;
                case 2:
                    tmpTextView = (TextView)findViewById(R.id.title);
                    tmpTextView.setText(this.title);
                    break;
                case 3:
                    tmpTextView = (TextView)findViewById(R.id.phone);
                    tmpTextView.setText(this.phone);
                    break;
                case 4:
                    tmpTextView = (TextView)findViewById(R.id.email);
                    tmpTextView.setText(this.email);
                    break;
                case 5:
                    tmpTextView = (TextView)findViewById(R.id.addr);
                    tmpTextView.setText(this.addr);
                    break;
            }
        }
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
