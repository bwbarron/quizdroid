package edu.washington.bbarron.quizdroid;

import android.app.Application;
import android.util.Log;

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

    public void accessRepo() {
        // will probably return the topic list from the repo
        // won't need this if we implement TopicRepository interface
    }

    public List<Topic> getAllTopics() {
        List list = new ArrayList<Topic>();
        Topic topic = new Topic();
        topic.title = "math test";
        topic.desc = "math test description";
        List<Quiz> questions = new ArrayList<Quiz>();

        Quiz q = new Quiz();
        q.text = "test question text";
        q.answer1 = "1";
        q.answer2 = "2";
        q.answer3 = "3";
        q.answer4 = "4";
        q.correct = 1;
        questions.add(q);

        topic.questions = questions;
        list.add(topic);
        return list;
    }

    public List<Topic> getTopicsByKeyword(String keyword) {
        return null;
    }
}