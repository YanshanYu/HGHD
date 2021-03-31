package com.yu.zehnit.ui.home;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.AddEquipmentActivity;
import com.yu.zehnit.R;
import com.yu.zehnit.tools.CardUtils;
import com.yu.zehnit.tools.CtrlAdapter;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button addEquipBtn;
    private CardView cardView;

    private List<Equipment> eqpList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home_with_eqp, container, false);

//        cardView = root.findViewById(R.id.card_view_home);
//        CardUtils.setCardShadowColor(cardView, getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.white));


//        addEquipBtn = root.findViewById(R.id.button_add_experiment);
//        addEquipBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 切换到添加设备页面
//                Intent intent = new Intent(getActivity(), AddEquipmentActivity.class);
//                startActivity(intent);
//
//            }
//        });

        initEqp();
        RecyclerView recyclerView = root.findViewById(R.id.recycle_view_eqp);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        EqpAdapter adapter = new EqpAdapter(eqpList);
        recyclerView.setAdapter(adapter);

        return root;
    }

    private void initEqp() {
        Equipment eqp1 = new Equipment(0,"设备1",R.mipmap.ic_launcher_round, "在线");
        eqpList.add(eqp1);
        Equipment addEqp = new Equipment(1);
        eqpList.add(addEqp);
    }
}