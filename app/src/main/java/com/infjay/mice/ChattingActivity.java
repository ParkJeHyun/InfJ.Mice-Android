package com.infjay.mice;


import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.infjay.mice.artifacts.MessageInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.database.DBManager;
import com.infjay.mice.global.GlobalVariable;
import com.infjay.mice.network.AsyncHttpsTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ChattingActivity extends CustomActionBarActivity implements View.OnClickListener{

    private String targetName;
    private String targetSeq;
    private String message;
    private String mySeq;

    private Button btSend;
    private EditText etSendMessage;

    private LinearLayout llChatting;
    private ScrollView svChatting;

    private ArrayList<MessageInfo> messageInfoArrayList;

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what == 1)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("send_message"))
                    {
                        if(jobj.get("result").equals("SEND_MESSAGE_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "SEND_MESSAGE_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEND_MESSAGE_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "SEND_MESSAGE_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("SEND_MESSAGE_SUCCESS"))
                        {
                            JSONObject jobjMessage = new JSONObject(jobj.get("attach")+"");
                            MessageInfo mInfo = new MessageInfo();
                            mInfo.messageSeq = jobjMessage.get("message_seq")+"";
                            mInfo.senderUserSeq = jobjMessage.get("sender_user_seq")+"";
                            mInfo.receiverUserSeq = jobjMessage.get("receiver_user_seq")+"";
                            mInfo.messageText = jobjMessage.get("message")+"";
                            mInfo.sendTime = jobjMessage.get("send_time")+"";
                            mInfo.senderName = jobjMessage.get("sender_user_name")+"";
                            mInfo.receiverName = jobjMessage.get("receiver_user_name")+"";

                            SimpleDateFormat getSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            SimpleDateFormat setSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date d = new Date();
                            try
                            {
                                d = getSdf.parse(mInfo.sendTime);
                            }
                            catch(ParseException e)
                            {
                                e.printStackTrace();
                            }
                            long dd = d.getTime() + (1000 * 60 * 60 * 9);
                            d = new Date(dd);
                            mInfo.sendTime = setSdf.format(d);
                            String result = DBManager.getManager(getApplicationContext()).insertMessageInfo(mInfo);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MASSAGE_TYPE", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if(msg.what == 2)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_message_by_users"))
                    {
                        if(jobj.get("result").equals("GET_MESSAGE_BY_USERS_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_MESSAGE_BY_USERS_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_MESSAGE_BY_USERS_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_MESSAGE_BY_USERS_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_MESSAGE_BY_USERS_SUCCESS"))
                        {
                            MessageInfo messageInfo;
                            JSONArray messageJsonArray = new JSONArray(jobj.get("attach").toString());

                            ArrayList<MessageInfo> messageList = new ArrayList<MessageInfo>();

                            for (int i = 0; i < messageJsonArray.length(); i++) {
                                JSONObject messageJobj = new JSONObject(messageJsonArray.get(i).toString());
                                messageInfo = new MessageInfo();

                                messageInfo.messageSeq = messageJobj.get("message_seq").toString();
                                messageInfo.senderUserSeq = messageJobj.get("sender_user_seq").toString();
                                messageInfo.receiverUserSeq = messageJobj.get("receiver_user_seq").toString();
                                messageInfo.messageText = messageJobj.get("message").toString();
                                messageInfo.sendTime = messageJobj.get("send_time").toString();
                                messageInfo.senderName = messageJobj.get("sender_user_name")+"";
                                messageInfo.receiverName = messageJobj.get("receiver_user_name")+"";

                                messageList.add(messageInfo);

                                SimpleDateFormat getSdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                SimpleDateFormat setSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date d = new Date();
                                try
                                {
                                    d = getSdf.parse(messageInfo.sendTime);
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                long dd = d.getTime() + (1000 * 60 * 60 * 9);
                                d = new Date(dd);
                                messageInfo.sendTime = setSdf.format(d);

                                String result = DBManager.getManager(getApplicationContext()).insertMessageInfo(messageInfo);
                                if(!result.equals("ALREADY_IN"))
                                {
                                    if(messageInfo.senderUserSeq.equals(mySeq))
                                    {
                                        makeMessageView("right", messageInfo.messageText);
                                    }
                                    else if(messageInfo.senderUserSeq.equals(targetSeq))
                                    {
                                        makeMessageView("left", messageInfo.messageText);
                                    }
                                }

                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MASSAGE_TYPE", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();
        targetName = intent.getStringExtra("userName");
        targetSeq = intent.getStringExtra("userSeq");
        setTitle(targetName);

        btSend = (Button)findViewById(R.id.btMessageSend);
        etSendMessage = (EditText)findViewById(R.id.etMessageText);
        svChatting = (ScrollView)findViewById(R.id.svChatting);
        btSend.setOnClickListener(this);

        mySeq = DBManager.getManager(getApplicationContext()).getUserInfo().userSeq;
    }

    @Override
    protected void onResume() {
        super.onResume();

        messageInfoArrayList = DBManager.getManager(getApplicationContext()).getMessageByTwoUser(targetSeq, mySeq);
        if(messageInfoArrayList.size() != 0)
        {
            for(MessageInfo messageInfo : messageInfoArrayList)
            {
                if(messageInfo.senderUserSeq.equals(mySeq))
                {
                    makeMessageView("right", messageInfo.messageText);
                }
                else if(messageInfo.senderUserSeq.equals(targetSeq))
                {
                    makeMessageView("left", messageInfo.messageText);
                }
            }
        }

        JSONObject jobj = new JSONObject();
        String recentTime = DBManager.getManager(getApplicationContext()).getRecentTime(mySeq, targetSeq);
        Log.d("ChattingActivity", recentTime);
        if(recentTime.equals("NO_MESSAGE"))
        {
            recentTime = "2015-01-01 00:00:00";
        }
        try
        {
            jobj.put("messagetype", "get_message_by_users");
            jobj.put("sender_user_seq", mySeq);
            jobj.put("receiver_user_seq", targetSeq);
            jobj.put("send_time", recentTime);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 2, 0);
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

            JSONObject jobj = new JSONObject();
            try
            {
                jobj.put("messagetype", "send_message");
                jobj.put("sender_user_seq", mySeq);
                jobj.put("receiver_user_seq", targetSeq);
                jobj.put("message", message);
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobj, 1, 0);

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
            LLLeft.setPadding(5, 5, 5, 5);
            tvMessage.setBackgroundColor(Color.WHITE);
            tvMessage.setTextColor(Color.BLACK);
        }
        else if(direction.equals("right"))
        {
            LLParamsLeft.weight = 3f;
            LLParamsRight.weight = 1f;

            LLRight.setGravity(Gravity.END);
            LLRight.addView(tvMessage);
            LLRight.setPadding(5, 5, 5, 5);
            tvMessage.setBackgroundColor(Color.YELLOW);
            tvMessage.setTextColor(Color.BLACK);
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
