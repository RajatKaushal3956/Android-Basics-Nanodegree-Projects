package com.example.god.habitapp;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.god.habitapp.data.HabitContract;
import com.example.god.habitapp.data.HabitDbHelper;
public class EditActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mNumberOfTimes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mName = (EditText) findViewById(R.id.habitName);
        mNumberOfTimes = (EditText) findViewById(R.id.NumberTimes);
    }
    private void insertHabit(){
        String nameString = mName.getText().toString().trim();
        String numberOfTimesString = mNumberOfTimes.getText().toString().trim();
        int numberOfTimes = Integer.parseInt(numberOfTimesString);
        HabitDbHelper mHabitDbHelper = new HabitDbHelper(this);
        SQLiteDatabase db = mHabitDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(HabitContract.HabitEntry.COLUMN_NAME, nameString);
        values.put(HabitContract.HabitEntry.COLUMN_NUMBER_OF_TIMES, numberOfTimes);
        long newRowId = db.insert(HabitContract.HabitEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, "Error with saving activity_edit", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }
    public void onSave(View view) {
        if("".equals(mName.getText().toString())) {
            Toast.makeText(this, "Habit Name must be not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        insertHabit();
        finish();
    }
    public void onCancel(View view) {
        finish();
    }
}
