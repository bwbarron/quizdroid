package edu.washington.bbarron.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizApp app = (QuizApp) getApplication();
        List<Topic> topics = app.getAllTopics();

        ViewGroup layout = (ViewGroup) findViewById(R.id.layout);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View view = layout.getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setText(topics.get(i).title);
            }
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewOverview = new Intent(MainActivity.this, TriviaActivity.class);

                TextView topic = (TextView) findViewById(v.getId());
                viewOverview.putExtra("topic", topic.getText());

                startActivity(viewOverview);
            }
        };

        View mathQuiz = findViewById(R.id.quiz1);
        mathQuiz.setOnClickListener(clickListener);
        View physQuiz = findViewById(R.id.quiz2);
        physQuiz.setOnClickListener(clickListener);
        View marvelQuiz = findViewById(R.id.quiz3);
        marvelQuiz.setOnClickListener(clickListener);
    }

}
