package com.yu.zehnit.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;

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
        adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent;
                switch (pos) {
                    case 0:
//                        intent = new Intent(getContext(), SettingActivity.class);
//                        break;
                    case 1:
//                        intent = new Intent(getContext(), SettingActivity.class);
//                        break;
                    case 2:
                        Toast.makeText(getContext(), "跳转", Toast.LENGTH_SHORT).show();
//                        intent = new Intent(getContext(), SettingActivity.class);
                        break;
                    case 3:
                        intent = new Intent(getContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

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