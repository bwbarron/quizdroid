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


public class TriviaActivity extends ActionBarActivity {

    String topic;
    String desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        Intent intent = getIntent();
        if (intent != null) {
            topic = intent.getStringExtra("topic");

            try {
                XmlResourceParser xrp = getResources().getXml(R.xml.trivia);
                createOverview(xrp);
            } catch (XmlPullParserException | IOException e) {
                // handle exception
            }
        }
    }

    public void createOverview(XmlResourceParser xrp) throws XmlPullParserException, IOException {
        xrp.next();
        int eventType = xrp.getEventType();
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String quiz = xrp.getName();
                if (quiz.equalsIgnoreCase(topic)) {
                    break;
                }
            }
            eventType = xrp.next();
        }
        xrp.next();
        if (xrp.getName().equalsIgnoreCase("description")) {
            xrp.next();
            desc = xrp.getText();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Bundle topicBundle = new Bundle();
        topicBundle.putString("topic", topic);
        topicBundle.putString("desc", desc);

        OverviewFragment overview = new OverviewFragment();
        overview.setArguments(topicBundle);

        ft.add(R.id.container, overview);
        ft.commit();
    }

    public void createQuestion(Bundle bundle) throws XmlPullParserException, IOException {
        int qNum = bundle.getInt("qNum");

        XmlResourceParser xrp = getResources().getXml(R.xml.trivia);
        xrp.next();
        int eventType = xrp.getEventType();
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String quiz = xrp.getName();
                if (quiz.equalsIgnoreCase(topic)) {
                    for (int i = 0; i < 3 + ((qNum - 1) * 10); i++) {
                        xrp.next();
                    }
                }
            }
        }
        xrp.next();
        Bundle questionBundle = new Bundle();
        String question = xrp.getText();
        questionBundle.putString("topic", topic);
        questionBundle.putInt("qNum", qNum);
        questionBundle.putString("question", question);
        for (int i = 0; i < 4; i++) {
            xrp.next();
            if (xrp.getAttributeCount() > 0 && xrp.getAttributeName(0).equals("answer")) {
                bundle.putInt("aNum", i);
            }
            xrp.next();
            String answer = xrp.getText();
            questionBundle.putString("a" + i, answer);
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        QuestionFragment questionFrag = new QuestionFragment();
        questionFrag.setArguments(questionBundle);

        ft.replace(R.id.container, questionFrag);
        ft.commit();
    }

    public void createAnswer(Bundle bundle) throws XmlPullParserException, IOException {
        Bundle ansBundle = new Bundle();
        ansBundle.putString("topic", topic);
        ansBundle.putBoolean("correct", bundle.getBoolean("correct"));
        ansBundle.putInt("qNum", bundle.getInt("qNum"));
        ansBundle.putInt("nCorrect", bundle.getInt("nCorrect"));
        ansBundle.putString("yourAnswer", bundle.getString("yourAnswer"));
        ansBundle.putString("correctAnswer", bundle.getString("correctAnswer"));

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        QuestionFragment ansFrag = new QuestionFragment();
        ansFrag.setArguments(ansBundle);

        ft.replace(R.id.container, ansFrag);
        ft.commit();
    }

    public void finishQuiz() {
        Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
