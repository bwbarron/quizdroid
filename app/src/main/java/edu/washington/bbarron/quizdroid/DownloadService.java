package edu.washington.bbarron.quizdroid;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class DownloadService extends IntentService {

    private DownloadManager dm;
    private long enqueue;
    public static final int MY_ALARM = 420;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    public void onCreate() { super.onCreate(); }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        Log.i("DownloadService", "entered onHandleIntent()");
        // this method is called where the AlarmManager should've started the download service and we just received it here!

        String url = "http://www.EricCheeIsAwesome.com/data.json"; // set this based on preferences


        // Start the download
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        enqueue = dm.enqueue(request);
    }

    public static void startOrStopAlarm(Context context, boolean on) {
        Log.i("DownloadService", "startOrStopAlarm on = " + on);

        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, MY_ALARM, alarmReceiverIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (on) {
            int refreshInterval = 5 * 60000; // set this based on preferences

            Log.i("DownloadService", "setting alarm interval to " + (refreshInterval / 60000) + " minutes");

            // Start the alarm manager to repeat
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), refreshInterval, pendingIntent);
        }
        else {
            manager.cancel(pendingIntent);
            pendingIntent.cancel();

            Log.i("DownloadService", "Stopping alarm");
        }
    }

    // gets download url from preferences
    public String getURL() {

    }
}
