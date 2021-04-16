package com.yu.zehnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.Setting;
import com.yu.zehnit.tools.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.wandersnail.ble.EasyBLE;
import cn.wandersnail.widget.dialog.DefaultAlertDialog;

public class SettingActivity extends BaseActivity {

    private static final String TAG = "dxx";
    private List<Setting> settingList = new ArrayList<>();
    private Toolbar toolbar;
    private int eqpNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

        toolbar = findViewById(R.id.toolbar_setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btnLogout = findViewById(R.id.button_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipDialog("您确定要退出登录吗？");
            }
        });

        initSetting();
        RecyclerView recyclerView = findViewById(R.id.recycle_view_setting);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        SettingAdapter adapter = new SettingAdapter(settingList);
        adapter.setListener(new OnRecycleViewItemClickListener() {
            @Override
            public void onClick(int pos) {
                Intent intent;
                switch (pos) {
                    case 0:
                        intent = new Intent(SettingActivity.this, ParamSettingActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
                        eqpNum = pref.getInt("num", 0);
                        if (eqpNum != 0 ) {
                            // 解绑最新的设备
                            tipDialog("是否确定解绑设备？");
                        }
                        break;
                    case 2:
                        intent = new Intent(SettingActivity.this, AboutActivity.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
        recyclerView.setAdapter(adapter);

    }

    private void initSetting() {
        Setting param = new Setting("控制设置", R.drawable.param_setting);
        settingList.add(param);
        Setting unbind = new Setting("解绑设备", R.drawable.unbind);
        settingList.add(unbind);
        Setting aboutUs = new Setting("关于我们", R.drawable.us);
        settingList.add(aboutUs);
    }

    /**
     * 提示对话框
     */
    private void tipDialog(String msg) {
        DefaultAlertDialog dialog = new DefaultAlertDialog(SettingActivity.this);
        dialog.setTitle("提示");
        dialog.setMessage(msg);
        dialog.setCancelable(false);         //点击对话框以外的区域是否让对话框消失
        dialog.setTitleBackgroundColor(-1);

        if (msg.equals("您确定要退出登录吗？")) {
            //设置正面按钮
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityController.finishAll();
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    // 将登录页面置于栈顶，并清除其他所有Activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit();
                    // 更新登录状态
                    editor.putBoolean("isLogin", false);
                    editor.apply();
                    // 释放所有连接
                    EasyBLE.getInstance().releaseAllConnections();
                    // 回到登录页面
                    startActivity(intent);
                }
            });
        } else {
            dialog.setPositiveButton("确定", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 释放所有连接
                    EasyBLE.getInstance().releaseAllConnections();
                    // 更新设备总数
                    SharedPreferences.Editor editor = getSharedPreferences("eqpNum", MODE_PRIVATE).edit();
                    eqpNum--;
                    editor.putInt("num", eqpNum);
                    editor.apply();
                    // 清空设备列表
                    MyApplication.getInstance().setEqpList(null);


                    Toast.makeText(SettingActivity.this, "设备已解绑", Toast.LENGTH_SHORT).show();
                }
            });
        }

        //设置反面按钮
        dialog.setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        dialog.show();                              //显示对话框
    }
}