package com.example.vplayer.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vplayer.Models.MediaFiles;
import com.example.vplayer.R;
import com.example.vplayer.VideoFilesActivity;

import java.util.List;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.ViewHolder> {

    Context context;
    List<MediaFiles> mediaFiles;
    List<String> folderPath;

    public FoldersAdapter(Context context, List<MediaFiles> mediaFiles, List<String> folderPath) {
        this.context = context;
        this.mediaFiles = mediaFiles;
        this.folderPath = folderPath;
    }

    @NonNull
    @Override
    public FoldersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.folders_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoldersAdapter.ViewHolder holder, int position) {

        int indexPath = folderPath.get(position).lastIndexOf("/");
        String nameOfFolder = folderPath.get(position).substring(indexPath+1);
        holder.folderName.setText(nameOfFolder);
        holder.folderPath.setText(folderPath.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoFilesActivity.class);
                intent.putExtra("folderName", nameOfFolder);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return folderPath.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView folderName, folderPath;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = itemView.findViewById(R.id.folderName);
            folderPath = itemView.findViewById(R.id.folderPath);
        }
    }
}
