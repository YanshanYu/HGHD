package com.yu.zehnit;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;


import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

public class VideoPlayerActivity extends Activity {

    StandardGSYVideoPlayer videoPlayer;

    OrientationUtils orientationUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
    }


    private void init() {
        videoPlayer = (StandardGSYVideoPlayer)findViewById(R.id.video_player);

        Intent intent = getIntent();
        int res = intent.getIntExtra("ID", 0);

        // 播放raw视频
        GSYVideoManager.instance().enableRawPlay(getApplicationContext());

        //增加封面
       // ImageView imageView = new ImageView(this);
      //  imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String url="android.resource://" + getPackageName() + "/" + MainActivity.TASKVIDEOS[res];
        videoPlayer.setUp(url,false,MainActivity.TASKCAPTIONS[res]);

      /*  String url;
        switch (video) {
            case 1:
                url = "android.resource://" + getPackageName() + "/" + R.raw.gaze_holding1;
                videoPlayer.setUp(url, false, "凝视稳定性训练1");
                imageView.setImageResource(R.drawable.gaze_holding_1);
                break;
            case 2:
                url = "android.resource://" + getPackageName() + "/" + R.raw.gaze_holding2;
                videoPlayer.setUp(url, false, "凝视稳定性训练2");
                imageView.setImageResource(R.drawable.gaze_holding_2);
                break;
            case 3:
                url = "android.resource://" + getPackageName() + "/" + R.raw.gaze_holding3;
                videoPlayer.setUp(url, false, "凝视稳定性训练3");
                imageView.setImageResource(R.drawable.gaze_holding_3);
                break;
            case 4:
                url = "android.resource://" + getPackageName() + "/" + R.raw.smooth_pursuit;
                videoPlayer.setUp(url, false, "视追踪实验");
                imageView.setImageResource(R.drawable.smooth_pursuit);
                break;
            case 5:
                url = "android.resource://" + getPackageName() + "/" + R.raw.saccades;
                videoPlayer.setUp(url, false, "扫视实验");
                imageView.setImageResource(R.drawable.saccades);
                break;
            default:
                break;
        }

        videoPlayer.setThumbImageView(imageView);
*/

        //增加title
        videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
        //设置返回键
        videoPlayer.getBackButton().setVisibility(View.VISIBLE);
        //设置旋转
        orientationUtils = new OrientationUtils(this, videoPlayer);


        //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
        videoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orientationUtils.resolveByClick();
            }
        });
        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true);
        //设置返回按键功能
        videoPlayer.getBackButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        videoPlayer.startPlayLogic();
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoPlayer.onVideoPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoPlayer.onVideoResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onBackPressed() {
        //先返回正常状态
        if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            videoPlayer.getFullscreenButton().performClick();
            return;
        }
        //释放所有
        videoPlayer.setVideoAllCallBack(null);
        super.onBackPressed();
    }
}