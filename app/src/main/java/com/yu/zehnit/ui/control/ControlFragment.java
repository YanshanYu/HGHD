package com.yu.zehnit.ui.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.R;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.MyCtrl;

import java.util.ArrayList;
import java.util.List;

public class ControlFragment extends Fragment {

    private ControlViewModel controlViewModel;

    private List<MyCtrl> ctrlList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        controlViewModel =
                new ViewModelProvider(this).get(ControlViewModel.class);
        View root = inflater.inflate(R.layout.fragment_control, container, false);

        initCtrl();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_ctrl);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        CtrlAdapter adapter = new CtrlAdapter(ctrlList);
        recyclerView.setAdapter(adapter);


        return root;
    }

    private void initCtrl(){
        MyCtrl ctrlGaze1 = new MyCtrl("跟随头动1", R.drawable.gaze1, R.drawable.gaze1_2, R.drawable.switch_off);
        ctrlList.add(ctrlGaze1);
        MyCtrl ctrlGaze2 = new MyCtrl("跟随头动2", R.drawable.gaze2, R.drawable.gaze2_2, R.drawable.switch_off);
        ctrlList.add(ctrlGaze2);
        MyCtrl ctrlGaze3 = new MyCtrl("跟随头动3", R.drawable.gaze3, R.drawable.gaze3_2, R.drawable.switch_off);
        ctrlList.add(ctrlGaze3);
        MyCtrl ctrlTrack = new MyCtrl("视追踪实验", R.drawable.pursuit, R.drawable.pursuit_2, R.drawable.switch_off);
        ctrlList.add(ctrlTrack);
        MyCtrl ctrlSaccade = new MyCtrl("扫视实验", R.drawable.saccade, R.drawable.saccade_2, R.drawable.switch_off);
        ctrlList.add(ctrlSaccade);
    }
}