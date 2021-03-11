package com.yu.zehnit.ui.rehabilitation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.yu.zehnit.R;
import com.yu.zehnit.VideoPlayerActivity;

public class RehabilitationFragment extends Fragment {

    private RehabilitationViewModel rehabilitationViewModel;
    private Button gazeHolding1;
    private Button gazeHolding2;
    private Button gazeHolding3;
    private Button saccades;
    private Button smoothPursuit;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rehabilitationViewModel =
                new ViewModelProvider(this).get(RehabilitationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_rehabilitation, container, false);
        String path = Environment.getExternalStorageDirectory().getPath();
        gazeHolding1=root.findViewById(R.id.gaze_holding1);
        gazeHolding1.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), VideoPlayerActivity.class);
                String gazeHolding1Path=path+"/video/gaze_holding1.mp4";
                intent.putExtra("videopath",gazeHolding1Path);
                startActivity(intent);
            }

        });


        return root;
    }


}