package edu.washington.bbarron.quizdroid;

import android.app.Application;
import android.app.DownloadManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance = null;
    private List<Topic> topics;
    private DownloadManager dm;
    private long enqueue;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "QuizApp onCreate firing");

        File file = new File(getFilesDir().getAbsolutePath(), "/data.json");
        String json = null;

        if (file.exists()) {
            try {
                FileInputStream fis = openFileInput("data.json");
                json = readJSON(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // if file not found, fetch from assets
            try {
                InputStream inputStream = getAssets().open("data.json");
                json = readJSON(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        populateTopicsList(json);
        DownloadService.startOrStopAlarm(this, true);
    }

    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Can only have one instance of QuizApp");
        }
    }

    public static QuizApp getInstance() { return instance; }

    // returns a list with all available quiz topics
    public List<Topic> getAllTopics() {
        return topics;
    }

    // returns a single topic object based on keyword parameter
    public Topic getTopicByKeyword(String keyword) {
        for (int i = 0; i < topics.size(); i++) {
            Topic topic = topics.get(i);
            if (topic.title.contains(keyword)) {
                return topic;
            }
        }
        return null;
    }

    // populates a list with all available topics
    private void populateTopicsList(String json) {
        topics = new ArrayList<Topic>();

        try {

            //InputStream input = getAssets().open("data.json");
            //json = readJSON(input);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) { // parse all topics
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                Topic topic = new Topic();
                topic.title = jsonObj.getString("title");
                topic.desc = jsonObj.getString("desc");
                topic.questions = new ArrayList<Quiz>();

                JSONArray questions = jsonObj.getJSONArray("questions");
                for (int j = 0; j < questions.length(); j++) { // parse questions in this topic
                    JSONObject question = (JSONObject) questions.get(j);
                    Quiz quiz = new Quiz();
                    quiz.text = question.getString("text");
                    quiz.correct = question.getInt("answer") - 1;
                    quiz.answers = new ArrayList<String>();

                    JSONArray answers = question.getJSONArray("answers");
                    for (int k = 0; k < answers.length(); k++) { // parse answers for this question
                        quiz.answers.add(answers.getString(k));
                    }

                    topic.questions.add(quiz);
                }
                topics.add(topic);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // reads JSON file
    private String readJSON(InputStream input) throws IOException {
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();

        return new String(buffer, "UTF-8");
    }

    // writes data into file data.json
    public void writeToFile(String data) {
        try {
            Log.i("MyApp", "writing to file");

            File file = new File(getFilesDir().getAbsolutePath(), "data.json");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.getBytes());
            fos.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}