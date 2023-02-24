package com.example.vplayer;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView = findViewById(R.id.videoView);
        String path = getIntent().getStringExtra("path");
        videoView.setVideoPath(path);
        videoView.setMediaController(new MediaController(VideoActivity.this));
        videoView.requestFocus();
        videoView.start();
    }
}