package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.infjay.mice.R;

/**
 * Created by KimJS on 2015-06-05.
 */
public class SponsorDetailActivity extends ActionBarActivity {

    private TextView tvSponserName;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_detail);
        //view = (View) getLayoutInflater().inflate(R.layout.activity_sponsor_detail, null);

        Intent intent = getIntent();
        String sponserName = intent.getExtras().getString("clicked");

        tvSponserName = (TextView)findViewById(R.id.tvSponserName);
        tvSponserName.setText(sponserName);

        /*
        if(sponserName.eqauls("Samsung"))
            set ImageView >> samsung
        else if()
            ...
         */


        //setContentView(view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
    }
}
