package com.example.god.cricquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    int count=0;
    public void correct(){
        RadioButton question1 = (RadioButton) findViewById(R.id.questionA);
        Boolean q1=question1.isChecked();
        if(q1)
            count++;
        RadioButton question2 = (RadioButton) findViewById(R.id.questionB);
        Boolean q2=question2.isChecked();
        if(q2)
            count++;
        CheckBox question31 = (CheckBox) findViewById(R.id.questionC1);
        CheckBox question32 = (CheckBox) findViewById(R.id.questionC2);
        CheckBox question33 = (CheckBox) findViewById(R.id.q32);
        Boolean q3=question31.isChecked();
        Boolean q31=question32.isChecked();
        Boolean q32=question33.isChecked();
        if(q3&&q31&&!q32)
            count++;
        RadioButton question4 = (RadioButton) findViewById(R.id.questionD);
        Boolean q4=question4.isChecked();
        if(q4)
            count++;
        RadioButton question5 = (RadioButton) findViewById(R.id.questionE);
        Boolean q5=question5.isChecked();
        if(q5)
            count++;
        EditText answer = (EditText) findViewById(R.id.q6);
        String strInput = answer.getText().toString();
        if(strInput.equals("1756"))
            count++;

    }
    public void Submit(View view){
        correct();
        Toast.makeText(this, "You answered "+ count +" correct answers out of 6", Toast.LENGTH_LONG).show();
        if(count>=4)
            Toast.makeText(this,"Well Done",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"still you need more practice to clear this quz",Toast.LENGTH_LONG).show();
        count=0;    }
}
