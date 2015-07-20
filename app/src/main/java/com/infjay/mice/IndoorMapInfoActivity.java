package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.ImageView;

import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.database.DBManager;

/**
 * Created by HJHOME on 2015-06-07.
 */
public class IndoorMapInfoActivity extends CustomActionBarActivity{
    private String mapSeq;
    private IndoorMapInfo indoorMapInfo;
    private ImageView ivMapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indoor_map_info);

        Intent intent = getIntent();
        mapSeq = intent.getStringExtra("mapSeq");

        ivMapImage = (ImageView)findViewById(R.id.ivIndoorMapImage);

        setImage();
    }

    public void setImage(){
        indoorMapInfo = DBManager.getManager(getApplicationContext()).getIndoorMapByMapSeq(mapSeq);

        byte[] resBytes = Base64.decode(indoorMapInfo.imagePath, Base64.DEFAULT);
        Bitmap imageBitmap = BitmapFactory.decodeByteArray(resBytes, 0, resBytes.length);
        ivMapImage.setImageBitmap(imageBitmap);
        ivMapImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home)
        {
            finish();
            overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
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
