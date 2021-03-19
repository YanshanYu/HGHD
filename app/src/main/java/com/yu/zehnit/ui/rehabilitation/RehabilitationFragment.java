package com.yu.zehnit.ui.rehabilitation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;

import java.io.File;

public class RehabilitationFragment extends Fragment implements View.OnClickListener{

    private RehabilitationViewModel rehabilitationViewModel;
    private Button gazeHolding1;
    private Button gazeHolding2;
    private Button gazeHolding3;
    private Button saccades;
    private Button smoothPursuit;

    final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rehabilitationViewModel =
                new ViewModelProvider(this).get(RehabilitationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rehabilitation, container, false);
        String path = Environment.getExternalStorageDirectory().getPath();
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.
//                WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            this.requestPermissions( permissions, 1);
//        }
        gazeHolding1=root.findViewById(R.id.gaze_holding1);
        gazeHolding1.setOnClickListener(this);

        gazeHolding2=root.findViewById(R.id.gaze_holding2);
        gazeHolding2.setOnClickListener(this);

        gazeHolding3=root.findViewById(R.id.gaze_holding3);
        gazeHolding3.setOnClickListener(this);

        saccades=root.findViewById(R.id.scanner_experiment);
        saccades.setOnClickListener(this);

        smoothPursuit=root.findViewById(R.id.eye_tracking_experiment);
        smoothPursuit.setOnClickListener(this);
//        gazeHolding1.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), VideoPlayerActivity.class);
//                String gazeHolding1Path=path+"/video/gaze_holding1.mp4";
//                intent.putExtra("videopath",gazeHolding1Path);
//                startActivity(intent);
//            }
//
//        });


        return root;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        if (id == R.id.gaze_holding1) {
            intent.putExtra("video", 1);
        } else if (id == R.id.gaze_holding2) {
            intent.putExtra("video", 2);
        } else if (id == R.id.gaze_holding3) {
            intent.putExtra("video", 3);
        } else if (id == R.id.eye_tracking_experiment) {
            intent.putExtra("video", 4);
        } else if (id == R.id.scanner_experiment) {
            intent.putExtra("video", 5);
        }
        startActivity(intent);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                Log.d("TAG", "onRequestPermissionsResult: ");
//                if (!(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//                    Toast.makeText(getActivity(), "没获取到sd卡权限，无法播放本地视频", Toast.LENGTH_LONG).show();
//                }
//        }
//    }
}