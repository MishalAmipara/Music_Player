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

import com.example.vplayer.Adapters.FoldersAdapter;
import com.example.vplayer.Models.MediaFiles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView foldersRecycler;
    FoldersAdapter adapter;
    SwipeRefreshLayout swipeRefreshFolders;
    FloatingActionButton main_recent;
    SharedPreferences preferences;

    List<String> foldersList = new ArrayList<>();
    List<MediaFiles> mediaFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        foldersRecycler = findViewById(R.id.foldersRecycler);
        swipeRefreshFolders = findViewById(R.id.swipeRefreshFolders);
        main_recent = findViewById(R.id.main_recent);
        showFolders();

        preferences = getSharedPreferences("myPref", MODE_PRIVATE);

        swipeRefreshFolders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showFolders();
                swipeRefreshFolders.setRefreshing(false);
            }
        });

        main_recent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recentpath = preferences.getString("recentVideo", "no");
                if (!recentpath.equals("no")) {
                    Intent intent = new Intent(MainActivity.this, VideoActivity.class);
                    intent.putExtra("path", recentpath);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Play any video first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showFolders(){
        mediaFiles = getMedia();
        adapter = new FoldersAdapter(MainActivity.this, mediaFiles, foldersList);
        foldersRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        foldersRecycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @SuppressLint("Range")
    private List<MediaFiles> getMedia() {
        List<MediaFiles> list = new ArrayList<>();
        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

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

                int index = path.lastIndexOf("/");
                String subString = path.substring(0, index);
                if (!foldersList.contains(subString)){
                    foldersList.add(subString);
                }
                list.add(mediaFiles);
            } while (cursor.moveToNext());
        }

        return list;
    }
}