package com.example.god.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class genre extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        Button nowplaying = (Button) findViewById(R.id.nowPlaying_Button);
        nowplaying.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(genre.this,NowPlaying.class);
                startActivity(i);
            }
        });
    }
}
