package com.pix4d.wifilogger.app;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class WifiLoggerService extends Service
{
    private static final String CONNECTION_CHANGE = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    private static final String STATE_CHANGE = "android.net.wifi.STATE_CHANGE";
    private static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private BroadcastReceiver mWifiReceiver;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTION_CHANGE);
        intentFilter.addAction(STATE_CHANGE);
        intentFilter.addAction(CONNECTIVITY_CHANGE);
        mWifiReceiver = new WifiReceiver();

        Log.d("WifiLoggerService", "onCreate");

        this.registerReceiver(mWifiReceiver, intentFilter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("WifiLoggerService", "onStartCommand");

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        this.unregisterReceiver(mWifiReceiver);
    }
}