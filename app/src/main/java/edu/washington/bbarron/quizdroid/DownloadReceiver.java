package edu.washington.bbarron.quizdroid;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class DownloadReceiver extends BroadcastReceiver {

    private Context context;
    private long downloadID;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String action = intent.getAction();
        DownloadManager dm = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            Log.i("DownloadReceiver", "download complete!");
            downloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);

            // if the downloadID exists
            if (downloadID != 0) {

                // Check status
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadID);
                Cursor c = dm.query(query);
                if(c.moveToFirst()) {
                    int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    Log.d("DownloadReceiver", "Status Check: " + status);
                    switch(status) {
                        case DownloadManager.STATUS_PAUSED:
                        case DownloadManager.STATUS_PENDING:
                        case DownloadManager.STATUS_RUNNING:
                            break;
                        case DownloadManager.STATUS_SUCCESSFUL:
                            ParcelFileDescriptor file;
                            //StringBuffer strContent = new StringBuffer("");

                            try {
                                // get file from Download Manager
                                file = dm.openDownloadedFile(downloadID);

                                writeToFile(file);

                                // update topics list
                                QuizApp app = (QuizApp) context.getApplicationContext();
                                app.populateTopicsList();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case DownloadManager.STATUS_FAILED:
                            onDownloadFail();
                            break;
                    }
                }
            }
        }
    }

    private void writeToFile(ParcelFileDescriptor file) throws IOException {
        Log.i("DownloadReceiver", "writing to file");

        FileInputStream fis = new FileInputStream(file.getFileDescriptor());
        byte[] buffer = new byte[fis.available()];
        fis.read(buffer);
        File questionFile = new File(context.getFilesDir().getAbsolutePath(),
                "/questions.json");
        FileOutputStream outputStream = new FileOutputStream(questionFile);
        outputStream.write(buffer);
    }

    private void onDownloadFail() {
        Log.e("DownloadReceiver", "download failed");

        // build dialog that allows user to retry download or cancel
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder
                .setTitle("Quiz download failed")
                .setMessage("Do you want to retry the download?")
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent downloadIntent = new Intent(context, DownloadReceiver.class);
                        context.startActivity(downloadIntent);
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        // display alert dialog
        alertBuilder.create().show();
    }
}
