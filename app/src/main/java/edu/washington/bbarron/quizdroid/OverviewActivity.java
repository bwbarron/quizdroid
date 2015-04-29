package edu.washington.bbarron.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class OverviewActivity extends ActionBarActivity {

    private String quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ViewGroup title = (ViewGroup) findViewById(R.id.title);
        TextView titleText = (TextView) title.getChildAt(0);
        ViewGroup desc = (ViewGroup) findViewById(R.id.description);
        TextView descText = (TextView) desc.getChildAt(0);

        Intent intent = getIntent();
        quiz = intent.getStringExtra("quiz");
        String quizDesc = intent.getStringExtra("description");
        titleText.setText(quiz);
        descText.setText(quizDesc);

        Button begin = (Button) findViewById(R.id.begin);
        begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewQ1;

                if (quiz.equals("Math")) {
                    viewQ1 = new Intent(OverviewActivity.this, MathQ1Activity.class);
                } else if (quiz.equals("Physics")) {
                    viewQ1 = new Intent(OverviewActivity.this, PhysQ1Activity.class);
                } else { // Marvel
                    viewQ1 = new Intent(OverviewActivity.this, MarvelQ1Activity.class);
                }
                startActivity(viewQ1);
                finish();
            }
        });
    }

}
