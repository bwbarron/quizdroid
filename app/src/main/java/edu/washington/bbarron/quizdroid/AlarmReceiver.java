package edu.washington.bbarron.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class AlarmReceiver extends BroadcastReceiver {

    public AlarmReceiver() {}

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "entered onReceive() from AlarmReceiver");

        Toast.makeText(context, "PUT URL HERE", Toast.LENGTH_SHORT).show();

        // have DownloadService class start the download
        /*
        Intent downloadServiceIntent = new Intent(context, DownloadService.class);
        context.startService(downloadServiceIntent);
        */
    }
}
