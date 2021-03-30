package com.yu.zehnit.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.MainActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.SettingActivity;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.Info;
import com.yu.zehnit.tools.InfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class InfoFragment extends Fragment {

    private InfoViewModel infoViewModel;
    private List<Info> infoList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoViewModel =
                new ViewModelProvider(this).get(InfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_info, container, false);

        initInfo();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        InfoAdapter adapter = new InfoAdapter(infoList);
        recyclerView.setAdapter(adapter);

//        Button btnSetting  = root.findViewById(R.id.button_setting);
//        btnSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), SettingActivity.class);
//                startActivity(intent);
//            }
//        });

        return root;
    }

    private void initInfo() {
        Info goal = new Info("训练目标", R.drawable.goal);
        infoList.add(goal);
        Info times = new Info("训练次数", R.drawable.times);
        infoList.add(times);
        Info mall = new Info("商城链接", R.drawable.mall);
        infoList.add(mall);
        Info setting = new Info("设置", R.drawable.setting);
        infoList.add(setting);
    }
}