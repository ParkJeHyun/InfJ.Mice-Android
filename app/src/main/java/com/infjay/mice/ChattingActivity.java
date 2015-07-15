package com.infjay.mice;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class ChattingActivity extends CustomActionBarActivity implements View.OnClickListener{

    private String name;
    private String message;

    private Button btSend;
    private EditText etSendMessage;

    private LinearLayout llChatting;
    private ScrollView svChatting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();
        name = (String)intent.getSerializableExtra("name");
        setTitle(name);

        btSend = (Button)findViewById(R.id.btMessageSend);
        etSendMessage = (EditText)findViewById(R.id.etMessageText);
        svChatting = (ScrollView)findViewById(R.id.svChatting);
        btSend.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.itDeleteMessage) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btMessageSend){
            message = etSendMessage.getText().toString();
            makeMessageView("right", message);
            etSendMessage.setText("");
            svChatting.fullScroll(ScrollView.FOCUS_DOWN);
            //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeMessageView(String direction, String message)
    {
        LinearLayout LL = new LinearLayout(this);
        ViewGroup.LayoutParams LLParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LL.setLayoutParams(LLParams);

        LinearLayout LLLeft = new LinearLayout(this);
        LinearLayout.LayoutParams LLParamsLeft = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        LinearLayout LLRight = new LinearLayout(this);
        LinearLayout.LayoutParams LLParamsRight = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        TextView tvMessage = new TextView(this);
        ViewGroup.LayoutParams textviewParams =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tvMessage.setLayoutParams(textviewParams);
        tvMessage.setText(message);
        tvMessage.setTextSize(16f);

        if(direction.equals("left"))
        {
            LLParamsLeft.weight = 1f;
            LLParamsRight.weight = 3f;

            LLLeft.addView(tvMessage);
            tvMessage.setBackgroundColor(Color.WHITE);
        }
        else if(direction.equals("right"))
        {
            LLParamsLeft.weight = 3f;
            LLParamsRight.weight = 1f;

            LLRight.setGravity(Gravity.END);
            LLRight.addView(tvMessage);
            LLRight.setPadding(5, 5, 5, 5);
            tvMessage.setBackgroundColor(Color.YELLOW);
        }
        else
        {
            return;
        }


        LLLeft.setLayoutParams(LLParamsLeft);
        LLRight.setLayoutParams(LLParamsRight);

        LL.addView(LLLeft);
        LL.addView(LLRight);

        llChatting = (LinearLayout)findViewById(R.id.llChatting);
        llChatting.addView(LL);
    }
}
