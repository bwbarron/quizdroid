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
    private String topic;
    private int nextQNum;
    private int nCorrect;
    private String yourAnswer;
    private String correctAnswer;
    private boolean correct;

    public AnswerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topic = getArguments().getString("topic");
            nextQNum = getArguments().getInt("qNum") + 1;
            nCorrect = getArguments().getInt("nCorrect");
            yourAnswer = getArguments().getString("yourAnswer");
            correctAnswer = getArguments().getString("correctAnswer");
            correct = getArguments().getBoolean("correct");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_answer, container, false);

        TextView response = (TextView) v.findViewById(R.id.response);
        if (correct) {
            response.setText("Correct!");
        } else {
            response.setText("Incorrect");
        }

        TextView your = (TextView) v.findViewById(R.id.your_answer);
        your.setText("Your answer: " + yourAnswer);
        TextView ans = (TextView) v.findViewById(R.id.correct_answer);
        ans.setText("Correct answer: " + correctAnswer);
        TextView totals = (TextView) v.findViewById(R.id.totals);
        totals.setText("You have " + nCorrect + " out 5 correct");

        Button submit = (Button) v.findViewById(R.id.submit);
        if (nextQNum > 5) {
            submit.setText("Next");
        } else {
            submit.setText("Finish");
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextQNum > 5) { // done with quiz
                    ((TriviaActivity) activity).finishQuiz();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("topic", topic);
                    bundle.putInt("qNum", nextQNum);

                    try {
                        ((TriviaActivity) activity).createQuestion(bundle);
                    } catch (XmlPullParserException | IOException e) {
                        // handle
                    }
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
