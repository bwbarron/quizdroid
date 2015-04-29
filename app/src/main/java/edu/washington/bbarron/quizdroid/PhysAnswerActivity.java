package edu.washington.bbarron.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class PhysAnswerActivity extends ActionBarActivity {

    private int next;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Intent intent = getIntent();

        TextView response = (TextView) findViewById(R.id.response);
        response.setText(intent.getStringExtra("response"));
        TextView yourAnswer = (TextView) findViewById(R.id.your_answer);
        yourAnswer.setText("Your answer: " + intent.getStringExtra("yourAnswer"));
        TextView correctAnswer = (TextView) findViewById(R.id.correct_answer);
        correctAnswer.setText("Correct answer: " + intent.getStringExtra("correctAnswer"));

        Button button = (Button) findViewById(R.id.submit);
        next = intent.getIntExtra("nextQ", 0);
        TextView totals = (TextView) findViewById(R.id.totals);
        score = intent.getIntExtra("totalCorrect", 0);
        totals.setText("You have " + score + " out of " + (next - 1) + " correct");

        if (next != 6) {
            button.setText("Next");
        } else { // done with quiz
            button.setText("Finish");
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewNextActivity;
                if (next == 2) {
                    viewNextActivity = new Intent(PhysAnswerActivity.this, PhysQ2Activity.class);
                } else if (next == 3) {
                    viewNextActivity = new Intent(PhysAnswerActivity.this, PhysQ3Activity.class);
                } else if (next == 4) {
                    viewNextActivity = new Intent(PhysAnswerActivity.this, PhysQ4Activity.class);
                } else if (next == 5) {
                    viewNextActivity = new Intent(PhysAnswerActivity.this, PhysQ5Activity.class);
                } else {
                    viewNextActivity = new Intent(PhysAnswerActivity.this, MainActivity.class);
                    score = 0;
                }
                viewNextActivity.putExtra("totalCorrect", score);
                startActivity(viewNextActivity);
                finish();
            }
        });
    }

}
