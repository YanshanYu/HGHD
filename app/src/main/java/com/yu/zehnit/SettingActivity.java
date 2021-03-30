package com.yu.zehnit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gyf.immersionbar.ImmersionBar;

import com.yu.zehnit.tools.Setting;
import com.yu.zehnit.tools.SettingAdapter;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends BaseActivity {

    private static final String TAG = "dxx";
    private List<Setting> settingList = new ArrayList<>();
    private Toolbar toolbar;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("提示");
        builder.setMessage(msg);
//        builder.setIcon(R.mipmap.ic_launcher);
        builder.setCancelable(false);            //点击对话框以外的区域是否让对话框消失

        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(SettingActivity.this, "你点击了确定", Toast.LENGTH_SHORT).show();
                ActivityController.finishAll();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit();
                editor.putBoolean("isLogin", false);
                editor.apply();
                startActivity(intent);
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();      //创建AlertDialog对象
        dialog.show();                              //显示对话框
    }
}