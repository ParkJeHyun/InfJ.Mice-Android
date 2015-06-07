package com.infjay.mice;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by HJHOME on 2015-06-07.
 * 내 명함 말고 다른 사용자의 명함을 볼 때 이용하는 액티비티
 */
public class ViewBusinessCardActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_business_card);
    }
}
