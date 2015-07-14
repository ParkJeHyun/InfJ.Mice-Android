package com.infjay.mice;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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

import java.io.FileNotFoundException;
import java.io.IOException;


public class MyBusinessCardActivity extends ActionBarActivity {

    private Button btShareBusinessCard;
    private Button btModifyBusinessCard;
    private boolean shareFlag = false;
    private ImageView ivMyPhoto;

    private final int REQ_CODE_SELECT_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_card);
        setTitle("");


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

        ivMyPhoto = (ImageView)findViewById(R.id.ivMyPhoto);
        ivMyPhoto.setOnClickListener(new ImageView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
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
        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    //Get Image name from Uri
                    //String name_Str = getImageNameToUri(data.getData());

                    //Get Bitmap from Image file
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    ImageView image = (ImageView)findViewById(R.id.ivMyPhoto);

                    //set to image view
                    image.setImageBitmap(image_bitmap);

                    //set Image size to fit in
                    image.setScaleType(ImageView.ScaleType.FIT_XY);

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
/*
    public String getImageNameToUri(Uri data)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/")+1);

        return imgName;
    }*/
}
