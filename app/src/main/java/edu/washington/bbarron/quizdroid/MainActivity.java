package edu.washington.bbarron.quizdroid;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener viewListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.quiz1) { // Math
                    Intent viewMathOverview = new Intent(MainActivity.this, OverviewActivity.class);
                    viewMathOverview.putExtra("quiz", getString(R.string.quiz1));
                    viewMathOverview.putExtra("description", getString(R.string.quiz1desc));
                    startActivity(viewMathOverview);
                    finish();
                } else if (id == R.id.quiz2) { // Physics
                    Intent viewPhysOverview = new Intent(MainActivity.this, OverviewActivity.class);
                    viewPhysOverview.putExtra("quiz", getString(R.string.quiz2));
                    viewPhysOverview.putExtra("description", getString(R.string.quiz2desc));
                    startActivity(viewPhysOverview);
                    finish();
                } else if (id == R.id.quiz3) { // Marvel
                    Intent viewMarvelOverview = new Intent(MainActivity.this, OverviewActivity.class);
                    viewMarvelOverview.putExtra("quiz", getString(R.string.quiz3));
                    viewMarvelOverview.putExtra("description", getString(R.string.quiz3desc));
                    startActivity(viewMarvelOverview);
                    finish();
                }
            }
        };

        View q1 = findViewById(R.id.quiz1);
        q1.setOnClickListener(viewListener);
        View q2 = findViewById(R.id.quiz2);
        q2.setOnClickListener(viewListener);
        View q3 = findViewById(R.id.quiz3);
        q3.setOnClickListener(viewListener);
    }

}
