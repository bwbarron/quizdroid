package edu.washington.bbarron.quizdroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private DownloadManager dm;
    private long downloadID;
    DownloadReceiver downloadReceiver = new DownloadReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // label quiz topics
        setTopicsUI();

        // check if user has internet connection
        checkConnection();

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewOverview = new Intent(MainActivity.this, TriviaActivity.class);

                ViewGroup topic = (ViewGroup) findViewById(v.getId());
                TextView text = (TextView) topic.getChildAt(0);
                viewOverview.putExtra("topic", text.getText().toString());

                startActivity(viewOverview);
                finish();
            }
        };

        View mathQuiz = findViewById(R.id.quiz1);
        mathQuiz.setOnClickListener(clickListener);
        View physQuiz = findViewById(R.id.quiz2);
        physQuiz.setOnClickListener(clickListener);
        View marvelQuiz = findViewById(R.id.quiz3);
        marvelQuiz.setOnClickListener(clickListener);

        // listen for completed download
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        setTopicsUI();
    }

    // stops automatic background downloads
    @Override
    protected void onDestroy() {
        super.onDestroy();

        DownloadService.startOrStopAlarm(getApplicationContext(), false);
        unregisterReceiver(downloadReceiver);
    }

    // TODO: programmatically set xml with ListView!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    private void setTopicsUI() {
        QuizApp app = (QuizApp) getApplication();
        List<Topic> topics = app.getAllTopics();
        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        int topicIndex = 0;
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view.getClass() == RelativeLayout.class) {
                View childView = ((RelativeLayout) view).getChildAt(0);
                if (childView.getClass() == TextView.class) {
                    ((TextView) childView).setText(topics.get(topicIndex).title);
                    topicIndex++;
                }
            }
        }
    }

    // checks user's network connection and airplane mode status
    private void checkConnection() {
        if (!hasNetworkConnection()) { // user has no internet connection
            if (isAirplaneModeOn()) { // user has airplane mode on
                Log.i("MainActivity", "airplane mode is on");

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getApplicationContext());
                alertBuilder
                        .setTitle("No internet connection")
                        .setMessage("Airplane mode is on. Would you like to turn it off?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsIntent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(settingsIntent);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DownloadService.startOrStopAlarm(getApplicationContext(), false);
                                dialog.cancel();
                            }
                        });
                alertBuilder.create().show();
            } else { // no connection available
                Log.i("MainActivity", "no network connection available");

                Toast.makeText(getApplicationContext(), "No internet connection",
                        Toast.LENGTH_SHORT).show();
                DownloadService.startOrStopAlarm(getApplicationContext(), false);
            }
        }
    }

    // returns true if airplane mode is on
    private boolean isAirplaneModeOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getApplicationContext().getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.System.getInt(getApplicationContext().getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    // returns true if user's phone currently has network connection
    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.preferences) {
            Intent startPrefs = new Intent(MainActivity.this, PreferencesActivity.class);
            startActivity(startPrefs);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}