package edu.washington.bbarron.quizdroid;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
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
            topic = ((QuizApp) getApplication()).getTopicByKeyword(title); // get current topic
        }

        OverviewFragment overview = new OverviewFragment();

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, overview);
        ft.commit();
    }

    // sets necessary data and passes it to question fragment
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

    // sets necessary data and passes it to answer fragment
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

    // ends quiz and brings user back to main menu
    public void finishQuiz() {
        Intent intent = new Intent(TriviaActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
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
            Intent startPrefs = new Intent(TriviaActivity.this, PreferencesActivity.class);
            startActivity(startPrefs);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
