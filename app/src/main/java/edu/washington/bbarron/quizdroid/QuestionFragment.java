package edu.washington.bbarron.quizdroid;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class QuestionFragment extends Fragment {

    private Activity activity;
    private Quiz quiz;
    private int qNum;
    private int nCorrect; // total correct so far in this quiz
    private View v; // for use in submit button OnClickListener
    private Button submit;
    private RadioGroup options;

    public QuestionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            qNum = getArguments().getInt("qNum");
            nCorrect = getArguments().getInt("nCorrect");
        }
        activity = getActivity();
        quiz = ((TriviaActivity) activity).topic.questions.get(qNum);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_question, container, false);

        // set question text
        ViewGroup q = (ViewGroup) v.findViewById(R.id.question);
        TextView tv = (TextView) q.getChildAt(0);
        tv.setText(quiz.text);

        options = (RadioGroup) v.findViewById(R.id.options);
        submit = (Button) v.findViewById(R.id.submit);

        // set text for answers
        for (int i = 0; i < options.getChildCount(); i++) {
            RadioButton option = (RadioButton) options.getChildAt(i);
            option.setText(quiz.answers.get(i));
        }

        // make submit button visible after user selected an answer
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submit.setVisibility(View.VISIBLE);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("qNum", qNum);

                // get selected and correct answers
                int selectedId = options.getCheckedRadioButtonId();
                RadioButton selected = (RadioButton) v.findViewById(selectedId);
                String selectedAns = selected.getText().toString();
                String correctAns = quiz.answers.get(quiz.correct);

                bundle.putInt("nCorrect", nCorrect);
                bundle.putString("yourAns", selectedAns);
                bundle.putString("correctAns", correctAns);

                // send user to answer page
                ((TriviaActivity) activity).createAnswer(bundle);
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
