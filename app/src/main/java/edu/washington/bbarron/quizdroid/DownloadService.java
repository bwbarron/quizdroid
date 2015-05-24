package edu.washington.bbarron.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;


public class DownloadService extends IntentService {

    private DownloadManager dm;
    private long downloadID;
    private static final String DEFAULT_URL = "http://tednewardsandbox.site44.com/questions.json";


    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i("DownloadService", "entered onHandleIntent()");

        // get download URL from preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String url = prefs.getString("pref_url", DEFAULT_URL);

        // Start the download
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        downloadID = dm.enqueue(request);
    }

    public static void startOrStopAlarm(Context context, boolean on) {
        Log.i("DownloadService", "startOrStopAlarm on = " + on);

        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 42, alarmReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (on) {
            // get interval from preferences
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            long refreshInterval = Integer.parseInt(sharedPref.getString("pref_freq", "5")) * 60 * 1000;

            Log.i("DownloadService", "setting alarm interval to " + (refreshInterval / 60000) + " minute(s)");

            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                    refreshInterval, pendingIntent);
        }
        else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();

            Log.i("DownloadService", "Stopping alarm");
        }
    }
}