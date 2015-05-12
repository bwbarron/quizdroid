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

    public List<Topic> getAllTopics() {
        /*
        // testing shit
        List list = new ArrayList<Topic>();
        Topic topic = new Topic();
        topic.title = "math test";
        topic.desc = "math test description";
        List<Quiz> questions = new ArrayList<Quiz>();

        Quiz q = new Quiz();
        q.text = "test question text";
        q.answers = new ArrayList<String>();
        q.answers.add("1");
        q.answers.add("2");
        q.answers.add("3");
        q.answers.add("4");
        q.correct = 1;
        questions.add(q);

        Quiz q2 = new Quiz();
        q2.text = "test question 22222222 text";
        q2.answers = new ArrayList<String>();
        q2.answers.add("2");
        q2.answers.add("4");
        q2.answers.add("6");
        q2.answers.add("8");
        q2.correct = 0;
        questions.add(q2);

        topic.questions = questions;
        list.add(topic);
        return list;
        // end testing shit
        */

        List<Topic> topics = new ArrayList<Topic>();
        String json = null;

        try {

            InputStream input = getAssets().open("data.json");
            json = readJSON(input);
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                Topic topic = new Topic();
                topic.title = jsonObj.getString("title");
                topic.desc = jsonObj.getString("desc");
                topic.questions = new ArrayList<Quiz>();

                JSONArray questions = jsonObj.getJSONArray("questions");
                for (int j = 0; j < questions.length(); j++) {
                    JSONObject question = (JSONObject) questions.get(i);
                    Quiz quiz = new Quiz();
                    quiz.text = question.getString("text");
                    quiz.correct = question.getInt("answer");
                    quiz.answers = new ArrayList<String>();

                    JSONArray answers = question.getJSONArray("answers");
                    for (int k = 0; k < answers.length(); k++) {
                        quiz.answers.add(answers.getString(i));
                    }

                    topic.questions.add(quiz);
                }
                topics.add(topic);
            }

        } catch (IOException | JSONException e) {
            Toast.makeText(getApplicationContext(), "fuck", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        return topics;
    }

    public Topic getTopicByKeyword(String keyword) {
        return null;
    }

    private String readJSON(InputStream input) throws IOException {
        int size = input.available();
        byte[] buffer = new byte[size];
        input.read(buffer);
        input.close();

        return new String(buffer, "UTF-8");
    }
}