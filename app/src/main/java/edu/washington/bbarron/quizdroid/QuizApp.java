package edu.washington.bbarron.quizdroid;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class QuizApp extends Application implements TopicRepository {

    private static QuizApp instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "QuizApp onCreate firing");
    }

    public QuizApp() {
        if (instance == null) {
            instance = this;
        } else {
            throw new RuntimeException("Can only have one instance of QuizApp");
        }
    }

    public static QuizApp getInstance() { return instance; }

    // returns a list of all available topics
    public List<Topic> getAllTopics() {
        List<Topic> topics = new ArrayList<Topic>();
        String json = null;

        try {

            InputStream input = getAssets().open("data.json");
            json = readJSON(input);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                topics.add(getTopicByKeyword(jsonObj.getString("title")));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return topics;
    }

    // returns a single topic object based on keyword parameter
    public Topic getTopicByKeyword(String keyword) {
        Topic topic = new Topic();
        String json = null;

        try {

            InputStream input = getAssets().open("data.json");
            json = readJSON(input);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) { // parse topics
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                if (jsonObj.getString("title").contains(keyword)) {
                    topic.title = jsonObj.getString("title");
                    topic.desc = jsonObj.getString("desc");
                    topic.questions = new ArrayList<Quiz>();

                    JSONArray questions = jsonObj.getJSONArray("questions");
                    for (int j = 0; j < questions.length(); j++) { // parse questions in topic
                        JSONObject question = (JSONObject) questions.get(j);
                        Quiz quiz = new Quiz();
                        quiz.text = question.getString("text");
                        quiz.correct = question.getInt("answer") - 1;
                        quiz.answers = new ArrayList<String>();

                        JSONArray answers = question.getJSONArray("answers");
                        for (int k = 0; k < answers.length(); k++) { // parse answers for question
                            quiz.answers.add(answers.getString(k));
                        }

                        topic.questions.add(quiz);
                    }
                }
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return topic;
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