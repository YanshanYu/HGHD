package com.yu.zehnit;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import com.yu.zehnit.tools.Equipment;
import com.yu.zehnit.tools.OnRecycleViewItemClickListener;
import com.yu.zehnit.tools.Setting;
import com.yu.zehnit.tools.SettingAdapter;
import com.yu.zehnit.tools.SharedPreferencesUtils;

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
                tipDialog(getString(R.string.sure_to_logout));
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
                        SharedPreferencesUtils.setFileName("info");
                        eqpNum = (int) SharedPreferencesUtils.getParam(SettingActivity.this, "eqpNum", 0);
//                        SharedPreferences pref = getSharedPreferences("eqpNum", Context.MODE_PRIVATE);
//                        eqpNum = pref.getInt("num", 0);
                        if (eqpNum != 0 ) {
                            // 解绑最新的设备
                            tipDialog(getString(R.string.sure_to_unbind));
                        }
                        break;
                    case 2:
                        Setting swLang = settingList.get(pos);
                        SharedPreferencesUtils.setFileName("info");
                        if (swLang.getName().equals("切换语言")) {
                            SharedPreferencesUtils.setParam(SettingActivity.this, "language", "en");



                        } else {
                            SharedPreferencesUtils.setParam(SettingActivity.this, "language", "zh");
                        }
                        ActivityController.finishAll();
                        Intent intent1 = new Intent(SettingActivity.this, MainActivity.class);
                        // 将登录页面置于栈顶，并清除其他所有Activity
                        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        // 释放所有连接
                        EasyBLE.getInstance().releaseAllConnections();
                        MyApplication.getInstance().setConnected(false);
                        // 回到登录页面
                        startActivity(intent1);

                        break;
                    case 3:
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
        Setting param = new Setting(getString(R.string.param_setting), R.drawable.param_setting);
        settingList.add(param);
        Setting unbind = new Setting(getString(R.string.unbind_device), R.drawable.unbind);
        settingList.add(unbind);
        Setting switchLang = new Setting(getString(R.string.switch_lang), R.drawable.us);
        settingList.add(switchLang);
        Setting aboutUs = new Setting(getString(R.string.about_us), R.drawable.us);
        settingList.add(aboutUs);
    }

    /**
     * 提示对话框
     */
    private void tipDialog(String msg) {
        DefaultAlertDialog dialog = new DefaultAlertDialog(SettingActivity.this);
        dialog.setTitle(getString(R.string.tip));
        dialog.setMessage(msg);
        dialog.setCancelable(false);         //点击对话框以外的区域是否让对话框消失
        dialog.setTitleBackgroundColor(-1);

        if (msg.equals(getString(R.string.sure_to_logout))) {
            //设置正面按钮
            dialog.setPositiveButton(getString(R.string.sure), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityController.finishAll();
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    // 将登录页面置于栈顶，并清除其他所有Activity
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    // 更新登录状态
                    SharedPreferencesUtils.setFileName("info");
                    SharedPreferencesUtils.setParam(SettingActivity.this, "isLogin", false);
                    // 释放所有连接
                    EasyBLE.getInstance().releaseAllConnections();
                    //更改连接状态
                    MyApplication.getInstance().setConnected(false);
                    // 回到登录页面
                    startActivity(intent);
                }
            });
        } else {
            dialog.setPositiveButton(getString(R.string.sure), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 释放所有连接
                    EasyBLE.getInstance().releaseAllConnections();
                    // 更新设备连接状态
                    MyApplication.getInstance().setConnected(false);
                    // 更新设备总数
                    SharedPreferencesUtils.setFileName("info");
                    eqpNum--;
                    SharedPreferencesUtils.setParam(SettingActivity.this, "eqpNum", eqpNum);
//                    SharedPreferences.Editor editor = getSharedPreferences("eqpNum", MODE_PRIVATE).edit();
//                    eqpNum--;
//                    editor.putInt("num", eqpNum);
//                    editor.apply();
                    // 更新设备列表
                    List<Equipment> equipmentList = MyApplication.getInstance().getEqpList();
                    equipmentList.remove(0);
                    MyApplication.getInstance().setEqpList(equipmentList);


                    Toast.makeText(SettingActivity.this, getString(R.string.unbind_info), Toast.LENGTH_SHORT).show();
                }
            });
        }

        //设置反面按钮
        dialog.setNegativeButton(getString(R.string.cancel), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        //显示对话框
        dialog.show();
    }
}