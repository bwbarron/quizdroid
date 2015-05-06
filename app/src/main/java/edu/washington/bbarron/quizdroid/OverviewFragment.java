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

    private String topic;
    private String desc;
    private Activity activity;

    public OverviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            topic = getArguments().getString("topic");
            desc = getArguments().getString("desc");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_overview, container, false);

        ViewGroup view = (ViewGroup) v.findViewById(R.id.title);
        TextView tv = (TextView) view.getChildAt(0);
        tv.setText(topic);

        ViewGroup description = (ViewGroup) v.findViewById(R.id.description);
        TextView descText = (TextView) description.getChildAt(0);
        descText.setText(desc);

        Button b = (Button) v.findViewById(R.id.begin);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if (activity instanceof TriviaActivity) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("qNum", 1);
                    bundle.putString("topic", topic);
                    try {
                        ((TriviaActivity) activity).createQuestion(bundle);
                    } catch (XmlPullParserException | IOException e) {
                        // handle
                    }
                //}
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
