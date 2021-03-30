package com.yu.zehnit.tools;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    private List<MyVideo> videoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View videoView;
        ImageView videoImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView;
            videoImage = itemView.findViewById(R.id.video_name);
        }
    }

    public VideoAdapter(List<MyVideo> videoList) {
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
        ViewHolder holder = new ViewHolder(view);

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Context context = v.getContext();
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                switch (position) {
                    case 0:
                        intent.putExtra("video", 1);
                        break;
                    case 1:
                        intent.putExtra("video", 2);
                        break;
                    case 2:
                        intent.putExtra("video", 3);
                        break;
                    case 3:
                        intent.putExtra("video", 4);
                        break;
                    case 4:
                        intent.putExtra("video", 5);
                        break;
                    default:
                        break;
                }
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyVideo video = videoList.get(position);
        holder.videoImage.setImageResource(video.getImageId());
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


}
