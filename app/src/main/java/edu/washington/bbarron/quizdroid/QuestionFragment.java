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
    private String topic;
    private String question;
    private String[] answers;
    private int correct;
    private Button submit;
    private RadioGroup options;
    private int qNum;
    private int nCorrect;

    public QuestionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        answers = new String[4];
        if (getArguments() != null) {
            topic = getArguments().getString("topic");
            question = getArguments().getString("question");
            correct = getArguments().getInt("aNum");
            qNum = getArguments().getInt("qNum");
            nCorrect = getArguments().getInt("nCorrect", 0);
            for (int i = 0; i < 4; i++) {
                answers[i] = getArguments().getString("a" + i);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_question, container, false);

        ViewGroup q = (ViewGroup) v.findViewById(R.id.question);
        TextView tv = (TextView) q.getChildAt(0);
        tv.setText(question);

        options = (RadioGroup) v.findViewById(R.id.options);
        options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                submit.setVisibility(View.VISIBLE);
            }
        });

        for (int i = 0; i < options.getChildCount(); i++) {
            RadioButton option = (RadioButton) options.getChildAt(i);
            option.setText(answers[i]);
        }

        submit = (Button) v.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("topic", topic);
                bundle.putInt("qNum", qNum);

                int selectedId = options.getCheckedRadioButtonId();
                RadioButton selected = (RadioButton) v.findViewById(selectedId);
                if (((String)selected.getText()).equalsIgnoreCase(answers[correct])) { // correct
                    bundle.putBoolean("correct", true);
                    bundle.putInt("nCorrect", nCorrect + 1);
                } else {
                    bundle.putBoolean("correct", false);
                    bundle.putInt("nCorrect", nCorrect);
                }
                bundle.putString("yourAnswer", (String)selected.getText());
                bundle.putString("correctAnswer", answers[correct]);
                try {
                    ((TriviaActivity) activity).createAnswer(bundle);
                } catch (XmlPullParserException | IOException e) {
                    // HANDLE IT!!
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
