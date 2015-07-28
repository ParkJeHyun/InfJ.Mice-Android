package com.infjay.mice.backgroundservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by KimJS on 2015-07-29.
 */
public class GCMBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that GcmIntentService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());
        //Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);


//        Intent gcmIntent = new Intent (context, GCMIntentService.class);
//        gcmIntent.putExtras (intent.getExtras());
//        startWakefulService(context, gcmIntent);
//        setResultCode(Activity.RESULT_OK);
//
    }


}
