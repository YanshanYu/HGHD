package com.yu.zehnit.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yu.zehnit.AddEquipmentActivity;
import com.yu.zehnit.MyApplication;
import com.yu.zehnit.R;
import com.yu.zehnit.tools.EqpAdapter;
import com.yu.zehnit.tools.Equipment;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.SharedPreferencesUtils;

import java.util.List;

import cn.wandersnail.ble.EasyBLE;

public class HomeFragment extends Fragment {

    private static final String TAG = "dxx";
    private HomeViewModel homeViewModel;
    private Button addEquipBtn;

    private ConstraintLayout homeWithEqp;
    private ConstraintLayout homeWithoutEqp;
    private View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);

        homeWithEqp = root.findViewById(R.id.home_with_eqp);
        homeWithEqp.setVisibility(View.INVISIBLE);
        homeWithoutEqp = root.findViewById(R.id.home_without_eqp);
        homeWithoutEqp.setVisibility(View.INVISIBLE);

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferencesUtils.setFileName("info");
        int eqpNum = (int) SharedPreferencesUtils.getParam(getContext(), "eqpNum", 0);
//        SharedPreferences pref = getActivity().getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
//        int eqpNum = pref.getInt("num", 0);

        if (eqpNum == 0) {
            Log.d(TAG, "HomeFragment onResume: 无设备");
            loadingHomeWithoutEqp();
        } else {
            Log.d(TAG, "HomeFragment onResume: 有设备");
            loadingHomeWithEqp();
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

    private void loadingHomeWithEqp(){
        homeWithEqp.setVisibility(View.VISIBLE);
        if (EasyBLE.getInstance().isBluetoothOn()) {
            // 获取设备适配器
            EqpAdapter adapter = MyApplication.getInstance().getAdapter();
            // 获取设备列表
            List<Equipment> eqpList = MyApplication.getInstance().getEqpList();
            RecyclerView recyclerView = root.findViewById(R.id.recycle_view_eqp);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter.setListener(new OnRecycleViewItemClickListener() {
                @Override
                public void onClick(int pos) {
                    Equipment equipment = eqpList.get(pos);
                    if (equipment.getCategory() == 1) {
                        Intent intent = new Intent(getContext(), AddEquipmentActivity.class);
                        startActivity(intent);
                    } else {
                        // 跳转到fragment_control
                        Navigation.findNavController(getView()).navigate(R.id.action_navigation_home_to_navigation_control);
                    }
                }
            });
            recyclerView.setAdapter(adapter);
        }

    }

}