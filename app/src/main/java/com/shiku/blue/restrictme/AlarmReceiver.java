package com.shiku.blue.restrictme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BLUE on 4/9/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "on receive", Toast.LENGTH_SHORT).show();
        //while (true)
        Intent myIntent=new Intent(context,LockService.class);
        myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startService(myIntent);

    }


}
