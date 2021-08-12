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
import android.widget.LinearLayout;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;
import com.yu.zehnit.tools.MyVideo;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.VideoAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RehabilitationFragment extends Fragment {

    private RehabilitationViewModel rehabilitationViewModel;

    final String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private List<MyVideo> videoList = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rehabilitationViewModel =
                new ViewModelProvider(this).get(RehabilitationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rehabilitation, container, false);

        initVideos();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_video);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        VideoAdapter adapter = new VideoAdapter(videoList);
        adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent = new Intent(getContext(), VideoPlayerActivity.class);
                switch (pos) {
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
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void initVideos(){
        MyVideo video1 = new MyVideo("凝视训练1", R.drawable.p1);
        videoList.add(video1);
        MyVideo video2 = new MyVideo("凝视训练2", R.drawable.p2);
        videoList.add(video2);
        MyVideo video3 = new MyVideo("凝视训练3", R.drawable.p3);
        videoList.add(video3);
        MyVideo video4 = new MyVideo("视追踪", R.drawable.p4);
        videoList.add(video4);
        MyVideo video5 = new MyVideo("扫视", R.drawable.p5);
        videoList.add(video5);
    }

}