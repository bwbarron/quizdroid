package edu.washington.bbarron.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "onReceive() firing");

        // get URL from preferences and display in toast message
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String url = prefs.getString("pref_url", null);
        Toast.makeText(context, url, Toast.LENGTH_SHORT).show();

        // have DownloadService class start the download
        Intent downloadIntent = new Intent(context, DownloadService.class);
        context.startService(downloadIntent);
    }
}