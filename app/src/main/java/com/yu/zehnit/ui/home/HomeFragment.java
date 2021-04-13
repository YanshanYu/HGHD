package com.yu.zehnit.ui.home;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import cn.wandersnail.ble.Connection;
import cn.wandersnail.ble.ConnectionConfiguration;
import cn.wandersnail.ble.Device;
import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.ble.EventObserver;

import static cn.wandersnail.ble.ConnectionState.SERVICE_DISCOVERED;

public class HomeFragment extends Fragment implements EventObserver {

    private static final String TAG = "dxx";
    private BluetoothAdapter bluetoothAdapter;
    private HomeViewModel homeViewModel;
    private Button addEquipBtn;
    private CardView cardView;

    private FrameLayout homeWithEqp;
    private ConstraintLayout homeWithoutEqp;
    private View root;

    private int eqpNum;
    private String address;

    private List<Equipment> eqpList = new ArrayList<>();
    private EqpAdapter adapter;

    private ProgressDialog progressDialog;
    private AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        root = inflater.inflate(R.layout.fragment_home, container, false);

        homeWithEqp = root.findViewById(R.id.home_with_eqp);
        homeWithoutEqp = root.findViewById(R.id.home_without_eqp);


        // 注册观察者
        EasyBLE.getInstance().registerObserver(this);

        final BluetoothManager bluetoothManager =
                (BluetoothManager) getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        return root;
    }

    @Override
    public void onResume() {

        super.onResume();

        SharedPreferences pref = getActivity().getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
        eqpNum = pref.getInt("num", 0);

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
        if (bluetoothAdapter.isEnabled()) {
            loadEqp();
            RecyclerView recyclerView = root.findViewById(R.id.recycle_view_eqp);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            adapter = new EqpAdapter(eqpList);
            recyclerView.setAdapter(adapter);
        }

    }

    private void showProgressDialog(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("正在连接设备...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void tipDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("提示");
        builder.setMessage(msg);
        // 点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);

        if (msg.equals("连接成功")) {
            Message message = new Message();
            message.what = 1;
            handler.sendMessageDelayed(message, 1000);
        } else {
            builder.setPositiveButton("重试", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!address.equals("N/A")) {
                        progressDialog.show();
                        connectEqp();
                    }
                }
            });
        }
        dialog = builder.create();      //创建AlertDialog对象
        dialog.show();
    }


    private void loadEqp() {

        eqpList.clear();

        SharedPreferences pref;
        String fileName;

        for (int i = 1; i <= eqpNum; i++) {
            fileName = "eqp" + i;
            pref = getActivity().getSharedPreferences(fileName, Context.MODE_PRIVATE);
            String name = pref.getString("name", "N/A");
            address = pref.getString("address", "N/A");
            Connection connection = EasyBLE.getInstance().getConnection(address);
            Equipment eqp;
            if (connection == null) {
                eqp = new Equipment(0, name, R.mipmap.ic_launcher_round, "离线");
            } else {
                eqp = new Equipment(0, name, R.mipmap.ic_launcher_round, "在线");
            }

            eqpList.add(0, eqp);

            Log.d(TAG, "loadEqp: " + address);
            Log.d(TAG, "loadEqp: " + EasyBLE.getInstance().getConnection(address));
            if (connection == null) {
                showProgressDialog();
                connectEqp();
            }

        }
        Equipment addEqp = new Equipment(1);
        eqpList.add(addEqp);
    }

    private void connectEqp() {
        ConnectionConfiguration config = new ConnectionConfiguration();
        config.setConnectTimeoutMillis(10000);
        config.setRequestTimeoutMillis(1000);
        config.setAutoReconnect(false);
        if (!address.equals("N/A")) {
            EasyBLE.getInstance().connect(address, config);
        }
    }

    @Override
    public void onConnectionStateChanged(@NonNull Device device) {
        Log.d(TAG, "onConnectionStateChanged: " +device.getConnectionState());
        Equipment equipment;
        switch (device.getConnectionState()) {
            case DISCONNECTED:
                progressDialog.dismiss();
                tipDialog("连接失败，请确定设备已打开");
                equipment = eqpList.get(0);
                equipment.setText("离线");
                adapter.notifyDataSetChanged();
                break;
            case SERVICE_DISCOVERED:
                progressDialog.dismiss();
                tipDialog("连接成功");
                equipment = eqpList.get(0);
                equipment.setText("在线");
                adapter.notifyDataSetChanged();
                break;
        }
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1){
                dialog.dismiss();
            }
        }
    };
}