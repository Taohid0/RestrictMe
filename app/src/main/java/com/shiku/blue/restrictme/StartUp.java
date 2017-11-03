package com.shiku.blue.restrictme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BLUE on 4/29/2016.
 */
public class StartUp extends BroadcastReceiver {

    public StartUp() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // start your service here
        context.startService(new Intent(context, LockService.class));
    }

}
