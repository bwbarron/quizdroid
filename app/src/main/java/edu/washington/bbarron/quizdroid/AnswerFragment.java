package edu.washington.bbarron.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class AnswerFragment extends Fragment {

    private Activity activity;
    private int nextQNum;
    private int nCorrect;
    private String yourAns;
    private String correctAns;

    public AnswerFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            nextQNum = getArguments().getInt("nextQNum");
            nCorrect = getArguments().getInt("nCorrect");
            yourAns = getArguments().getString("yourAns");
            correctAns = getArguments().getString("correctAns");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView response = (TextView) v.findViewById(R.id.response);
        if (yourAns.equalsIgnoreCase(correctAns)) {
            response.setText("Correct!");
            nCorrect += 1;
        } else {
            response.setText("Incorrect");
        }

        TextView your = (TextView) v.findViewById(R.id.your_answer);
        your.setText("Your answer: " + yourAns);
        TextView ans = (TextView) v.findViewById(R.id.correct_answer);
        ans.setText("Correct answer: " + correctAns);
        TextView totals = (TextView) v.findViewById(R.id.totals);
        final int totalQuestions = ((TriviaActivity) activity).topic.questions.size();
        totals.setText("You have " + nCorrect + " out of " + totalQuestions + " correct");

        Button submit = (Button) v.findViewById(R.id.submit);
        if (nextQNum < totalQuestions) {
            submit.setText("Next");
        } else {
            submit.setText("Finish");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nextQNum >= totalQuestions) { // done with quiz
                    ((TriviaActivity) activity).finishQuiz();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt("qNum", nextQNum);
                    bundle.putInt("nCorrect", nCorrect);

                    ((TriviaActivity) activity).createQuestion(bundle);
                }
            }
        });

        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
    }

}
