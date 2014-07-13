package com.pix4d.wifilogger.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, WifiLoggerService.class);
        context.startService(serviceIntent);
    }

}
