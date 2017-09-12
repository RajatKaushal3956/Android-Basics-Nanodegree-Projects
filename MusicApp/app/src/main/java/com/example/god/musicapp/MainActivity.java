package com.example.god.musicapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button album = (Button) findViewById(R.id.album_button);
        album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent albumActivity = new Intent(MainActivity.this, Albums.class);
                startActivity(albumActivity);
            }
        });
        Button artists = (Button) findViewById(R.id.artist_button);
        artists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent artistActivity = new Intent(MainActivity.this, Artists.class);
                startActivity(artistActivity);
            }
        });
        Button song = (Button) findViewById(R.id.songs_button);
        song.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songActivity = new Intent(MainActivity.this, Songs.class);
                startActivity(songActivity);
            }
        });
        Button genre = (Button) findViewById(R.id.genres_button);
        genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent genreActivity = new Intent(MainActivity.this, genre.class);
                startActivity(genreActivity);
            }
        });
    }

}

