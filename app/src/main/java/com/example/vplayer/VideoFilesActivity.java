package com.example.vplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.vplayer.Adapters.VideosAdapter;
import com.example.vplayer.Models.MediaFiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VideoFilesActivity extends AppCompatActivity {

    RecyclerView videosRecycler;
    VideosAdapter adapter;
    SwipeRefreshLayout swipeRefreshVideos;
    SharedPreferences preferences;
    FloatingActionButton video_recent;

    List<MediaFiles> videosArr = new ArrayList<>();
    String nameOfFolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_files);

        videosRecycler = findViewById(R.id.videosRecycler);
        swipeRefreshVideos = findViewById(R.id.swipeRefreshVideos);
        video_recent = findViewById(R.id.video_recent);
        nameOfFolder = getIntent().getStringExtra("folderName");
        getSupportActionBar().setTitle(nameOfFolder);
        showVideo();

        preferences = getSharedPreferences("myPref", MODE_PRIVATE);

        swipeRefreshVideos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showVideo();
                swipeRefreshVideos.setRefreshing(false);
            }
        });

        video_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recentpath = preferences.getString("recentVideo", "no");
                if (!recentpath.equals("no")) {
                    Intent intent = new Intent(VideoFilesActivity.this, VideoActivity.class);
                    intent.putExtra("path", recentpath);
                    startActivity(intent);
                } else {
                    Toast.makeText(VideoFilesActivity.this, "Play any video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showVideo() {
        videosArr = getVideo(nameOfFolder);
        videosRecycler.setLayoutManager(new LinearLayoutManager(VideoFilesActivity.this));
        adapter = new VideosAdapter(VideoFilesActivity.this, videosArr);
        videosRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    private List<MediaFiles> getVideo(String folderName) {
        List<MediaFiles> list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Video.Media.DATA + " like?";
        String[] selectionArg = new String[]{"%" + folderName + "%"};
        Cursor cursor = getContentResolver().query(uri, null, selection, selectionArg, null);

        if (cursor != null && cursor.moveToNext()){
            do {
                String id = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.TITLE));
                String displayname = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME));
                String size = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.SIZE));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
                String dateadded = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED));
                MediaFiles mediaFiles = new MediaFiles(id, title, displayname, size, duration, path, dateadded);

                list.add(mediaFiles);
            } while (cursor.moveToNext());
        }

        return list;
    }
}