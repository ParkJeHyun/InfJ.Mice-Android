package com.infjay.mice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.infjay.mice.artifacts.AgendaSessionInfo;
import com.infjay.mice.artifacts.ConferenceInfo;
import com.infjay.mice.artifacts.CouponInfo;
import com.infjay.mice.artifacts.IndoorMapInfo;
import com.infjay.mice.artifacts.SponsorInfo;
import com.infjay.mice.artifacts.UserInfo;
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

/**
 * Created by KimJS on 2015-07-21.
 */
public class LoadingActivity extends Activity {

    private int GET_CONFERENCE_INFO = 1;
    private int GET_SPONSOR_INFO = 2;
    private int GET_INDOORMAP_INFO = 3;
    private int GET_COUPON_INFO = 4;
    private int GET_SESSION_INFO = 5;

    protected Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            // IF Sucessfull no timeout
            System.out.println("in handler");
            if (msg.what == -1) {
                //   BreakTimeout();
                //ConnectionError();
                System.out.println("handler error");
            }

            if (msg.what == GET_CONFERENCE_INFO) {
                //핸들러 1번일 때
                System.out.println("response : "+msg.obj);

                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_conference_info"))
                    {
                        if(jobj.get("result").equals("GET_CONFERENCE_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_CONFERENCE_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_CONFERENCE_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_CONFERENCE_INFO_SUCCESS"))
                        {
                            JSONObject conf_jobj = new JSONObject(jobj.get("attach")+"");
                            ConferenceInfo conferenceInfo = new ConferenceInfo();
                            String conference_start_date = conf_jobj.get("conference_start_date").toString().split("Z")[0];
                            String conference_end_date = conf_jobj.get("conference_end_date").toString().split("Z")[0];
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                            Date d = new Date();
                            Date d2 = new Date();
                            try
                            {
                                d = sdf.parse(conference_start_date);
                                d2 = sdf.parse(conference_end_date);
                            }
                            catch(ParseException e)
                            {
                                e.printStackTrace();
                            }
                            long dd = d.getTime() + (1000 * 60 * 60 * 9);
                            long dd2 = d2.getTime() + (1000 * 60 * 60 * 9);
                            d = new Date(dd);
                            d2 = new Date(dd2);
                            conference_start_date = sdf.format(d);
                            conference_end_date = sdf.format(d2);

                            conferenceInfo.conferenceName = conf_jobj.get("conference_name").toString();
                            conferenceInfo.conferenceStartDate = conference_start_date.split("T")[0];
                            conferenceInfo.conferenceEndDate = conference_end_date.split("T")[0];
                            conferenceInfo.conferenceSummary = conf_jobj.get("summary").toString();

                            //Insert DB
                            int count = DBManager.getManager(getApplicationContext()).getCountConference();
                            if(count == 0)
                            {
                                DBManager.getManager(getApplicationContext()).insertConferenceInfo(conferenceInfo);
                            }
                            else
                            {
                                DBManager.getManager(getApplicationContext()).updateConferenceInfo(conferenceInfo);
                            }

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "RESULT_WRONG", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if (msg.what == GET_SPONSOR_INFO) {
                //핸들러 1번일 때
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("get_all_sponsor")) {
                        if (jobj.get("result").equals("GET_ALL_SPONSOR_ERROR")) {
                            Toast.makeText(getApplicationContext(), "GET_ALL_SPONSOR_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_ALL_SPONSOR_SUCCESS")) {
                            SponsorInfo sponsorInfo;
                            JSONArray sponsorJsonArray = new JSONArray(jobj.get("attach").toString());

                            ArrayList<SponsorInfo> sponsorList = new ArrayList<SponsorInfo>();

                            for (int i = 0; i < sponsorJsonArray.length(); i++) {
                                JSONObject couponJobj = new JSONObject(sponsorJsonArray.get(i).toString());
                                sponsorInfo = new SponsorInfo();

                                sponsorInfo.sponsorSeq = couponJobj.get("sponsor_seq").toString();
                                sponsorInfo.sponsorName = couponJobj.get("sponsor_name").toString();
                                sponsorInfo.sponsorExplanation = couponJobj.get("explanation").toString();

                                sponsorList.add(sponsorInfo);
                            }
                            DBManager.getManager(getApplicationContext()).deleteAllSponsor();
                            DBManager.getManager(getApplicationContext()).insertSponsor(sponsorList);

                        }
                        else if (jobj.get("result").equals("GET_ALL_SPONSOR_FAIL")) {
                            Toast.makeText(getApplicationContext(), "GET_ALL_SPONSOR_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "RESULT_WRONG", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "MESSAGE_TYPE_WRONG", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
                //response 받은거 파싱해서

            }

            if(msg.what == GET_INDOORMAP_INFO)
            {
                ArrayList<IndoorMapInfo> indoorMapArrayList;
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_indoor_map"))
                    {
                        if(jobj.get("result").equals("GET_INDOOR_MAP_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "Error in getting indoor map information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_INDOOR_MAP_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "Fail in getting indoor map information", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_INDOOR_MAP_SUCCESS"))
                        {
                            JSONArray indoorMapJsonArray = new JSONArray(jobj.get("attach").toString());
                            IndoorMapInfo indoorMapInfo;
                            indoorMapArrayList = new ArrayList<IndoorMapInfo>();
                            for(int i = 0; i < indoorMapJsonArray.length(); i++)
                            {
                                JSONObject indoorMapJsonObj = new JSONObject(indoorMapJsonArray.get(i).toString());
                                indoorMapInfo = new IndoorMapInfo();

                                indoorMapInfo.indoorMapSeq = indoorMapJsonObj.get("indoor_map_seq").toString();
                                indoorMapInfo.order = indoorMapJsonObj.get("map_order").toString();
                                indoorMapInfo.title = indoorMapJsonObj.get("title").toString();
                                indoorMapInfo.imagePath = "null";

                                indoorMapArrayList.add(indoorMapInfo);
                            }

                            DBManager.getManager(getApplicationContext()).deleteIndoorMapInfo();
                            DBManager.getManager(getApplicationContext()).insertIndoorMapInfo(indoorMapArrayList);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Message result", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Wrong MessageType", Toast.LENGTH_SHORT).show();
                    }

                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
            }

            if (msg.what == GET_COUPON_INFO)
            {
                //핸들러 1번일 때
                System.out.println("response : "+msg.obj);

                try {
                    JSONObject jobj = new JSONObject(msg.obj+"");

                    if(jobj.get("messagetype").equals("get_all_coupon_by_user_seq")) {
                        if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_ERROR")) {
                            Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_SUCCESS")) {
                            CouponInfo couponInfo;
                            JSONArray couponJsonArray = new JSONArray(jobj.get("attach").toString());
                            ArrayList<CouponInfo> couponList = new ArrayList<CouponInfo>();

                            for (int i = 0; i < couponJsonArray.length(); i++) {
                                JSONObject couponJobj = new JSONObject(couponJsonArray.get(i).toString());
                                couponInfo = new CouponInfo();

                                couponInfo.couponSeq = couponJobj.get("coupon_seq").toString();
                                couponInfo.couponName = couponJobj.get("coupon_name").toString();
                                couponInfo.couponSerial = couponJobj.get("coupon_serial").toString();
                                couponInfo.couponExplanation = couponJobj.get("coupon_explanation").toString();
                                couponInfo.couponImg = "null";

                                couponList.add(couponInfo);
                            }
                            DBManager.getManager(getApplicationContext()).deleteAllCoupon();
                            DBManager.getManager(getApplicationContext()).insertCoupon(couponList);
                        }
                        //If User has no Coupon
                        else if (jobj.get("result").equals("GET_ALL_COUPON_BY_USER_SEQ_FAIL")) {
                            DBManager.getManager(getApplicationContext()).deleteAllCoupon();
                            //Toast.makeText(getApplicationContext(), "GET_ALL_COUPON_BY_USER_SEQ_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "result wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else {
                        Toast.makeText(getApplicationContext(), "messagetype wrong not add_coupon", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(JSONException e) {
                    e.printStackTrace();
                }
            }

            if(msg.what == GET_SESSION_INFO)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("get_all_session_info"))
                    {
                        if(jobj.get("result").equals("GET_ALL_SESSION_INFO_ERROR"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_ALL_SESSION_INFO_ERROR", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_ALL_SESSION_INFO_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "GET_ALL_SESSION_INFO_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("GET_ALL_SESSION_INFO_SUCCESS"))
                        {
                            JSONArray sessionJsonArray = new JSONArray(jobj.get("attach").toString());
                            AgendaSessionInfo agendaSessionInfo;
                            ArrayList<AgendaSessionInfo> sessionArrayList = new ArrayList<AgendaSessionInfo>();
                            for(int i = 0; i < sessionJsonArray.length(); i++)
                            {
                                JSONObject sessionJsonObj = new JSONObject(sessionJsonArray.get(i).toString());
                                agendaSessionInfo = new AgendaSessionInfo();
                                String session_start_time = sessionJsonObj.get("session_start_time").toString().split("Z")[0];
                                String session_end_time = sessionJsonObj.get("session_end_time").toString().split("Z")[0];
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
                                Date d = new Date();
                                Date d2 = new Date();
                                try
                                {
                                    d = sdf.parse(session_start_time);
                                    d2 = sdf.parse(session_end_time);
                                }
                                catch(ParseException e)
                                {
                                    e.printStackTrace();
                                }
                                long dd = d.getTime() + (1000 * 60 * 60 * 9);
                                long dd2 = d2.getTime() + (1000 * 60 * 60 * 9);
                                d = new Date(dd);
                                d2 = new Date(dd2);
                                session_start_time = sdf.format(d);
                                session_end_time = sdf.format(d2);

                                agendaSessionInfo.sessionTitle = sessionJsonObj.get("title").toString();
                                agendaSessionInfo.sessionContents = sessionJsonObj.get("contents").toString();
                                agendaSessionInfo.sessionWriterUserSeq = sessionJsonObj.get("writer_user_seq").toString();
                                agendaSessionInfo.sessionWriterName = sessionJsonObj.get("writer_user_name").toString();
                                agendaSessionInfo.sessionPresenterUserSeq = sessionJsonObj.get("presenter_user_seq").toString();
                                agendaSessionInfo.sessionPresenterName = sessionJsonObj.get("presenter_user_name").toString();
                                agendaSessionInfo.sessionStartTime = session_start_time.split("T")[1].split("\\.")[0].substring(0, 5);
                                agendaSessionInfo.sessionEndTime = session_end_time.split("T")[1].split("\\.")[0].substring(0, 5);
                                agendaSessionInfo.sessionAttached = sessionJsonObj.get("attached").toString();
                                agendaSessionInfo.agendaSessionSeq = sessionJsonObj.get("agenda_session_seq").toString();

                                agendaSessionInfo.sessionDate = sessionJsonObj.get("session_start_time").toString().split("T")[0];
                                sessionArrayList.add(agendaSessionInfo);
                            }

                            DBManager.getManager(getApplicationContext()).deleteAgendaSession();
                            DBManager.getManager(getApplicationContext()).insertSessionToAgenda(sessionArrayList);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "WRONG_RESULT", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "WRONG_MESSAGE_TYPE", Toast.LENGTH_SHORT).show();
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
        setContentView(R.layout.activity_loading);

        UserInfo userInfo = DBManager.getManager(getApplicationContext()).getUserInfo();

        if(userInfo == null)
        {
            Toast.makeText(getApplicationContext(), "Inappropriate User", Toast.LENGTH_SHORT).show();
        }

        JSONObject jobjConferenceInfo = new JSONObject();
        JSONObject jobjSponsorInfo = new JSONObject();
        JSONObject jobjIndoorMapInfo = new JSONObject();
        JSONObject jobjCouponInfo = new JSONObject();
        JSONObject jobjSessionInfo = new JSONObject();

        try {
            jobjConferenceInfo.put("messagetype", "get_conference_info");
            jobjSponsorInfo.put("messagetype", "get_all_sponsor");
            jobjIndoorMapInfo.put("messagetype", "get_indoor_map");
            jobjCouponInfo.put("messagetype", "get_all_coupon_by_user_seq");
            jobjCouponInfo.put("user_seq", userInfo.userSeq.toString());
            jobjSessionInfo.put("messagetype", "get_all_session_info");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjConferenceInfo, GET_CONFERENCE_INFO, 0);
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjSponsorInfo, GET_SPONSOR_INFO, 0);
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjIndoorMapInfo, GET_INDOORMAP_INFO, 0);
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjCouponInfo, GET_COUPON_INFO, 0);
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjSessionInfo, GET_SESSION_INFO, 0);
        //TODO
        //delete previous DB : CardHolder, Schedule, Memo, Binder, Coupon, Message, Survey, Setting

    }
}
