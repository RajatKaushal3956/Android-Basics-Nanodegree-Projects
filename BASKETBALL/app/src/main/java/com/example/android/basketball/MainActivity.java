package com.example.android.basketball;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    int scoreA=0;
    int scoreB=0;
    public void teamA3(View view)
    {
        scoreA=scoreA+3;
        displayA(scoreA);
    }
    public void teamA2(View view)
    {
        scoreA=scoreA+2;
        displayA(scoreA);
    }
    public void teamA1(View view)
    {
        scoreA=scoreA+1;
        displayA(scoreA);
    }
    public void teamB3(View view)
    {
        scoreB=scoreB+3;
        displayB(scoreB);
    }
    public void teamB2(View view)
    {
        scoreB=scoreB+2;
        displayB(scoreB);
    }
    public void teamB1(View view)
    {
        scoreB=scoreB+1;
        displayB(scoreB);
    }
    public void reset(View view)
    {
        scoreA=0;
        scoreB=0;
        displayB(scoreB);
        displayA(scoreA);
    }
    private void displayA(int number)
    {
        TextView scoreATextView = (TextView) findViewById(R.id.scoreA_text_view);
        scoreATextView.setText(""+number);
    }
    private void displayB(int number)
    {
        TextView scoreBTextView = (TextView) findViewById(R.id.scoreB_text_view);
        scoreBTextView.setText(""+number);
    }
}
