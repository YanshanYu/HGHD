package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        Intent intent=getIntent();
        String path=intent.getStringExtra("videopath");
        VideoView videoView=(VideoView)findViewById(R.id.videoView);
        videoView.setVideoPath(path);
        MediaController mediaController=new MediaController(this);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
    }
}