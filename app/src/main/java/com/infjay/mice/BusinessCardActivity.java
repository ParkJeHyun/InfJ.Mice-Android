package com.infjay.mice;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infjay.mice.artifacts.BusinessCardInfo;


public class BusinessCardActivity extends ActionBarActivity {
    LinearLayout imageLayout;

    String name;
    String company;
    String title="CTO";
    String phone="01000000000";
    String email="cto@gmail.com";
    String addr="Seuol,Korea";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_card);

        Intent intent = getIntent();
        name = (String)intent.getSerializableExtra("name");
        company = (String)intent.getSerializableExtra("company");

        //setImage();
        setText();
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
                    tmpTextView = (TextView)findViewById(R.id.tvCardName);
                    tmpTextView.setText(this.name);
                    break;
                case 1:
                    tmpTextView = (TextView)findViewById(R.id.tvCompany);
                    tmpTextView.setText(this.company);
                    break;
                case 2:
                    tmpTextView = (TextView)findViewById(R.id.tvTitle);
                    tmpTextView.setText(this.title);
                    break;
                case 3:
                    tmpTextView = (TextView)findViewById(R.id.tvPhonenumber);
                    tmpTextView.setText(this.phone);
                    break;
                case 4:
                    tmpTextView = (TextView)findViewById(R.id.tvEmail);
                    tmpTextView.setText(this.email);
                    break;
                case 5:
                    tmpTextView = (TextView)findViewById(R.id.tvAddr);
                    tmpTextView.setText(this.addr);
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_business_card, menu);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
