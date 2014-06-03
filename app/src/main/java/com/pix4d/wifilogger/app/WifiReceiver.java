package com.pix4d.wifilogger.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WifiReceiver extends BroadcastReceiver
{
    public static final String TAG = "WifiReceiver";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String actionName = intent.getAction();
        Log.d(TAG, "Action: " + intent.getAction());

        // This happens when the Wifi is disabled
        if (actionName.equals("android.net.wifi.supplicant.CONNECTION_CHANGE"))
        {
            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            if (connected)
            {
                Log.d(TAG, "Connected");
            }
            else
            {
                Log.d(TAG, "Disconnected");
                logToFile("D", getFile());
            }
        }
        else if (actionName.equals("android.net.wifi.STATE_CHANGE"))
        {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info != null)
            {
                if (info.isConnected())
                {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();

                    Log.d(TAG, "Connected to " + ssid);
                    logToFile("C" + ", " + ssid, getFile());
                }
            }
        }
        // This happens when the Wifi connection is lost
        else if (actionName.equals("android.net.conn.CONNECTIVITY_CHANGE"))
        {
            boolean disconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            if (disconnected)
            {
                logToFile("D", getFile());
            }
        }
    }

    public static File getFile()
    {
        String filename = "WifiLogs.txt";

        // Get the directory for the user's public DCIM directory
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), filename);

        return file;
    }

    public static void logToFile(String data, File file)
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String dateString = dateFormat.format(date);

            try
            {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                writer.newLine();
                writer.write(dateString + ", " + data);
                writer.close();
            }
            catch (IOException e)
            {
                Log.w(TAG, "IOException while appending to file \"" + file.getAbsolutePath() + "\"");
            }
        }
    }
}
