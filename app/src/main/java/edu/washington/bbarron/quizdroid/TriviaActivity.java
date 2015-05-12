package edu.washington.bbarron.quizdroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.Toast;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.List;


public class TriviaActivity extends ActionBarActivity {

    Topic topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("topic");
            //topic = ((QuizApp) getApplication()).getTopicByKeyword(title);
            topic = ((QuizApp) getApplication()).getAllTopics().get(0); // testing purposes
        }

        OverviewFragment overview = new OverviewFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, overview);
        ft.commit();
    }

    public void createQuestion(Bundle bundle) {
        Bundle questionBundle = new Bundle();
        questionBundle.putInt("qNum", bundle.getInt("qNum"));
        questionBundle.putInt("nCorrect", bundle.getInt("nCorrect"));

        QuestionFragment questionFrag = new QuestionFragment();
        questionFrag.setArguments(questionBundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, questionFrag);
        ft.commit();
    }

    public void createAnswer(Bundle bundle) {
        Bundle ansBundle = new Bundle();
        ansBundle.putInt("nextQNum", bundle.getInt("qNum") + 1);
        ansBundle.putInt("nCorrect", bundle.getInt("nCorrect"));
        ansBundle.putString("yourAns", bundle.getString("yourAns"));
        ansBundle.putString("correctAns", bundle.getString("correctAns"));

        AnswerFragment ansFrag = new AnswerFragment();
        ansFrag.setArguments(ansBundle);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, ansFrag);
        ft.commit();
    }

    public void finishQuiz() {
        Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
