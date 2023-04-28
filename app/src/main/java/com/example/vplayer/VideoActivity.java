package com.example.vplayer;

import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.ui.StyledPlayerView;

public class VideoActivity extends AppCompatActivity {

    StyledPlayerView playerView;
    ExoPlayer exoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        playerView = findViewById(R.id.exo);
        String path = getIntent().getStringExtra("path");
        exoPlayer = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(path);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.prepare();
        exoPlayer.play();
    }

    @Override
    protected void onPause() {
        exoPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        exoPlayer.pause();
        super.onStop();
    }

    @Override
    protected void onResume() {
        exoPlayer.play();
        super.onResume();
    }
}