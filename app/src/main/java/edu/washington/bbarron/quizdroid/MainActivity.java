package edu.washington.bbarron.quizdroid;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private DownloadManager dm;
    private long enqueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizApp app = (QuizApp) getApplication();
        List<Topic> topics = app.getAllTopics();

        // label quiz topics
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

        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(receiver, filter);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            dm = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);

            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Log.i("MyApp BroadcastReceiver", "download complete!");
                long downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

                // if the downloadID exists
                if (downloadID != 0) {

                    // Check status
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadID);
                    Cursor c = dm.query(query);
                    if(c.moveToFirst()) {
                        int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                        Log.d("DM Sample","Status Check: " + status);
                        switch(status) {
                            case DownloadManager.STATUS_PAUSED:
                            case DownloadManager.STATUS_PENDING:
                            case DownloadManager.STATUS_RUNNING:
                                break;
                            case DownloadManager.STATUS_SUCCESSFUL:
                                ParcelFileDescriptor file;
                                StringBuffer strContent = new StringBuffer("");

                                try {
                                    // Get file from Download Manager (which is a system service as explained in the onCreate)
                                    file = dm.openDownloadedFile(downloadID);
                                    FileInputStream fis = new FileInputStream(file.getFileDescriptor());

                                    // handle writing the downloaded file to data.json here

                                    // use writeToFile method in QuizApp class





                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case DownloadManager.STATUS_FAILED:
                                // handle the case where the download failed here
                                // retry download?


                                break;
                        }
                    }
                }
            }
        }
    };

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
