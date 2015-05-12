package edu.washington.bbarron.quizdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class OverviewFragment extends Fragment {

    private Topic topic;
    private Activity activity;

    public OverviewFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = getActivity();
        topic = ((TriviaActivity) activity).topic;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        ViewGroup view = (ViewGroup) v.findViewById(R.id.title);
        TextView titleText = (TextView) view.getChildAt(0);
        titleText.setText(topic.title);

        ViewGroup description = (ViewGroup) v.findViewById(R.id.description);
        TextView descText = (TextView) description.getChildAt(0);
        descText.setText("Number of questions: " + topic.questions.size() + "\n" + topic.desc);

        Button b = (Button) v.findViewById(R.id.begin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("qNum", 0);
                bundle.putInt("nCorrect", 0);

                ((TriviaActivity) activity).createQuestion(bundle);
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
