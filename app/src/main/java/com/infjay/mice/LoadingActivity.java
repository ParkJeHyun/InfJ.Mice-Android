package com.infjay.mice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
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

import java.io.IOException;
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
    private int SEND_GCM_REGID = 6;

    private String TAG = "LoadingActivity";

    private UserInfo userInfo;

    //GCM
    private GoogleCloudMessaging gcm;
    private Context mContext;

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "AIzaSyBkJ7yPTvaC7UNAtmfD05BBL3gUYgJG2WE";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    //GCM project key
    private static final String SENDER_ID = "579395890963";
    //GCM 등록용 키(핸드폰 기준 1개)
    private String regid;


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
            if(msg.what == SEND_GCM_REGID)
            {
                try
                {
                    JSONObject jobj = new JSONObject(msg.obj + "");
                    if(jobj.get("messagetype").equals("update_reg_id"))
                    {
                        if(jobj.get("result").equals("UPDATE_REG_ID_FAIL"))
                        {
                            Toast.makeText(getApplicationContext(), "UPDATE_REG_ID_FAIL", Toast.LENGTH_SHORT).show();
                        }
                        else if(jobj.get("result").equals("UPDATE_REG_ID_SUCCESS"))
                        {
                            Log.d(TAG, "UPDATE_REG_ID_SUCCESS");
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

        mContext = getApplicationContext();
        userInfo = DBManager.getManager(getApplicationContext()).getUserInfo();
        setGCM();

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

    private void setGCM()
    {
        GCMRegistrar.checkDevice(this);
        GCMRegistrar.checkManifest(this);

        regid = GCMRegistrar.getRegistrationId(this);

        if(regid.equals("")){
            GCMRegistrar.register(this,SENDER_ID);
        }

        sendRegistrationIdToBackend();

        //Check device for Play Services APK. If check succeeds, proceed with GCM registration.
//        if (checkPlayServices()) {
//            gcm = GoogleCloudMessaging.getInstance(this);
//
//            //If there is not in sharedPreference
//            regid = getRegistrationId(mContext);
//
//            if (regid.equals("")) {
//                registerInBackground();
//            }
//            //Toast.makeText(this, "등록 id = " + regid, Toast.LENGTH_SHORT).show();
//            Log.d(TAG,regid);
//
//        } else {
//            Log.i(TAG, "No valid Google Play Services APK found.");
//        }
///*
//        gcm = GoogleCloudMessaging.getInstance(this);
//        regid = getRegistrationId(mContext);
//
//        if (regid.equals("")) {
//            registerInBackground();
//        }*/
//        //Toast.makeText(this, "등록 id = " + regid, Toast.LENGTH_SHORT).show();
//        sendRegistrationIdToBackend();
//        Log.d(TAG, "REG ID : " + regid);
    }

    //For GCM
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.equals("")) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        Log.i(TAG, "Registration id : " + registrationId);
        return registrationId;
    }


    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    Log.d(TAG, "sendRegistrationIdToBackend start");
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(mContext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        JSONObject jobjUpdateGCM = new JSONObject();

        if(userInfo == null)
        {
            Toast.makeText(getApplicationContext(), "Inappropriate User", Toast.LENGTH_SHORT).show();
        }

        try {
            jobjUpdateGCM.put("messagetype", "update_reg_id");
            jobjUpdateGCM.put("user_seq", userInfo.userSeq);
            jobjUpdateGCM.put("reg_id", regid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new AsyncHttpsTask(getApplicationContext(), GlobalVariable.WEB_SERVER_IP, mHandler, jobjUpdateGCM, SEND_GCM_REGID, 0);
        Log.d(TAG, "sendRegistrationIdToBackend");
    }
}
