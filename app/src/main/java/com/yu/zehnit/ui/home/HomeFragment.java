package com.yu.zehnit.ui.home;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.AddEquipmentActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private static final String TAG = "dxx";
    private HomeViewModel homeViewModel;
    private Button addEquipBtn;
    private CardView cardView;

    private FrameLayout homeWithEqp;
    private ConstraintLayout homeWithoutEqp;
    private View root;

    private int eqpInitNum;
    private boolean haveAdd = false;

    private List<Equipment> eqpList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        SharedPreferences pref = getActivity().getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
        eqpInitNum = pref.getInt("num", 0);
        root = inflater.inflate(R.layout.fragment_home, container, false);

        homeWithEqp = root.findViewById(R.id.home_with_eqp);
        homeWithoutEqp = root.findViewById(R.id.home_without_eqp);


        if (eqpInitNum == 0) {
            Log.d(TAG, "onCreateView: 无设备");
            loadingHomeWithoutEqp();
        } else {
            Log.d(TAG, "onCreateView: 有设备");
            loadingHomeWithEqp(eqpInitNum);
        }
        return root;
    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences pref = getActivity().getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
        int eqpUpdateNum = pref.getInt("num", 0);

        if (eqpUpdateNum != eqpInitNum) {
            Log.d(TAG, "onResume: 添加设备");
            loadingHomeWithEqp(eqpUpdateNum - eqpInitNum);
            // 更新初始值
            eqpInitNum = eqpUpdateNum;
        }

    }

    private void initEqp(int num) {
        for (int i = 0; i < num; i++) {
            Equipment eqp = new Equipment(0,"设备",R.mipmap.ic_launcher_round, "在线");
            eqpList.add(0, eqp);
        }

        if (!haveAdd) {
            Equipment addEqp = new Equipment(1);
            eqpList.add(addEqp);
            haveAdd = true;
        }

    }

    private void loadingHomeWithoutEqp(){
        homeWithoutEqp.setVisibility(View.VISIBLE);
        addEquipBtn = root.findViewById(R.id.button_add_experiment);
        addEquipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 切换到添加设备页面
                Intent intent = new Intent(getActivity(), AddEquipmentActivity.class);
                startActivity(intent);

            }
        });
    }

    private void loadingHomeWithEqp(int num){
        homeWithEqp.setVisibility(View.VISIBLE);
        initEqp(num);
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_eqp);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EqpAdapter adapter = new EqpAdapter(eqpList);
        recyclerView.setAdapter(adapter);
    }
}