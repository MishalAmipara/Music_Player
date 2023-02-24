package com.example.vplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vplayer.Models.MediaFiles;
import com.example.vplayer.R;
import com.example.vplayer.VideoActivity;

import java.io.File;
import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    Context context;
    List<MediaFiles> videosList;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public VideosAdapter(Context context, List<MediaFiles> videosList) {
        this.context = context;
        this.videosList = videosList;

        preferences = context.getSharedPreferences("myPref", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    @NonNull
    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.videos_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideosAdapter.ViewHolder holder, int position) {

        holder.videoTitle.setText(videosList.get(position).getDisplayname());
        String size = videosList.get(position).getSize();
        holder.videoSize.setText(android.text.format.Formatter.formatFileSize(context, Long.parseLong(size)));
        double milliSeconds = Double.parseDouble(videosList.get(position).getDuration());
        holder.videoDuration.setText(timeConvert((long) milliSeconds));
        Glide.with(context).load(new File(videosList.get(position).getPath())).into(holder.videoThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("recentVideo", videosList.get(holder.getAdapterPosition()).getPath());
                editor.commit();

                Intent intent = new Intent(context, VideoActivity.class);
                intent.putExtra("path", videosList.get(holder.getAdapterPosition()).getPath());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView videoThumbnail;
        TextView videoDuration, videoTitle, videoSize;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            videoDuration = itemView.findViewById(R.id.videoDuration);
            videoTitle = itemView.findViewById(R.id.videoTitle);
            videoSize = itemView.findViewById(R.id.videoSize);
        }
    }

    public String timeConvert(long value){
        String videoTime;
        int duration = (int) value;
        int h = (duration/3600000);
        int m = (duration/60000) % 60000;
        int s = duration % 60000 / 1000;
        if (h > 0){
            videoTime = String.format("%2d:%2d:%2d", h, m, s);
        } else {
            videoTime = String.format("%2d:%2d", m, s);
        }

        return videoTime;
    }
}
