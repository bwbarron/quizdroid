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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance = null;
    private List<Topic> topics;
    private DownloadManager dm;
    private long downloadID;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "QuizApp onCreate firing");
        populateTopicsList();
        // start automatic downloading
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
    public List<Topic> getAllTopics() { return topics; }

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
    public void populateTopicsList() {
        topics = new ArrayList<>();
        File file = new File(getFilesDir().getAbsolutePath(), "/questions.json");
        String json;

        try {
            InputStream inputStream;
            if (file.exists()) {
                inputStream = openFileInput("questions.json");
                json = readJSON(inputStream);
            } else { // use backup questions file
                inputStream = getAssets().open("data.json");
                json = readJSON(inputStream);
            }

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) { // parse all topics
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                Topic topic = new Topic();
                topic.title = jsonObj.getString("title");
                topic.desc = jsonObj.getString("desc");
                topic.questions = new ArrayList<>();

                JSONArray questions = jsonObj.getJSONArray("questions");
                for (int j = 0; j < questions.length(); j++) { // parse questions in this topic
                    JSONObject question = (JSONObject) questions.get(j);
                    Quiz quiz = new Quiz();
                    quiz.text = question.getString("text");
                    quiz.correct = question.getInt("answer") - 1;
                    quiz.answers = new ArrayList<>();

                    JSONArray answers = question.getJSONArray("answers");
                    for (int k = 0; k < answers.length(); k++) { // parse answers for this question
                        quiz.answers.add(answers.getString(k));
                    }

                    topic.questions.add(quiz);
                }
                topics.add(topic);
            }

        } catch (IOException | JSONException e) {
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
}