package edu.washington.bbarron.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class MathQ3Activity extends ActionBarActivity {

    private RadioGroup options;
    private Button submit;
    private int totalCorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_q3);

        Intent intent = getIntent();
        totalCorrect = intent.getIntExtra("totalCorrect", 0);
        options = (RadioGroup) findViewById(R.id.options);
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submit.setVisibility(View.VISIBLE);
            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = options.getCheckedRadioButtonId();
                RadioButton selected = (RadioButton) findViewById(selectedId);
                String yourAnswer = (String) selected.getText();
                RadioButton answer = (RadioButton) findViewById(R.id.a1);
                String correctAnswer = (String) answer.getText();

                Intent viewAnswer = new Intent(MathQ3Activity.this, MathAnswerActivity.class);
                if (selectedId == R.id.a1) { // correct
                    viewAnswer.putExtra("response", "Correct!");
                    totalCorrect += 1;
                } else { // incorrect
                    viewAnswer.putExtra("response", "Incorrect");
                }

                viewAnswer.putExtra("totalCorrect", totalCorrect);
                viewAnswer.putExtra("yourAnswer", yourAnswer);
                viewAnswer.putExtra("correctAnswer", correctAnswer);
                viewAnswer.putExtra("nextQ", 4);
                startActivity(viewAnswer);
                finish();
            }
        });
    }
}
