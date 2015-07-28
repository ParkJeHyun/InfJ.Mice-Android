package com.infjay.mice.backgroundservice;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.infjay.mice.R;

/**
 * Created by KimJS on 2015-07-29.
 */
public class GCMIntentService extends IntentService {

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;

    public GCMIntentService() {

        super("GCMIntentService");
        Log.d(TAG, "GCMIntentService Start");
    }

    public static final String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that
			 * GCM will be extended in the future with new message types, just
			 * ignore any message types you're not interested in, or that you
			 * don't recognize.
			 */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                    .equals(messageType)) {
                sendNotification("Send error: " + extras.toString(), "");
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                    .equals(messageType)) {
                sendNotification("Deleted messages on server: "
                        + extras.toString(), "");
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                // This loop represents the service doing some work.
                /*
                for (int i = 0; i < 1; i++) {
                    Log.i(TAG,
                            "Working... " + (i + 1) + "/5 @ "
                                    + SystemClock.elapsedRealtime());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                }*/

                Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());
                // Post notification of received message.

                String key1 = extras.getString("key1");
                String key2 = extras.getString("key2");
                sendNotification(key1, key2);

                /*
                String strKey1="", strKey2="";
                if ("is_in_family".equals(extras.getString("key1"))) {
                    if ("refresh_in_out_status".equals(extras.getString("key2"))) {
                        //가족으로 확인된 사람이 들어오기 or 나가기 했으니 가족정보 refresh 시켜야해
                        strKey1 = "가족 구성원의 출입정보가 변경되었습니다.";
                        strKey2 = extras.getString("key3")+"님 께서";
                        if("1".equals(extras.getString("key4"))){
                            strKey2 += " 들어오셨습니다.";
                        }
                        else if("0".equals(extras.getString("key4"))){
                            strKey2 += " 집을 나섰습니다.";
                        }

                    } else if ("no_permission_person".equals(extras.getString("key2"))) {
                        //가족 아닌사람이 얼쩡거리고 있어. notification 띄워야돼
                        strKey1 = "비상 상황";
                        strKey2 = "인증되지 않은 사람이 출입하였습니다.";
                    }
                    sendNotification(strKey1, strKey2);
                }*/

                //sendNotification("Received: " + extras.toString());
                Log.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String key1, String key2) {

        NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.icon_app_mc, key1, System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE ;
        notification.number = 13;

        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setLatestEventInfo(this, key1, key2, pendingIntent);
        nm.notify(1234, notification);
    }
}