package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        Button btnParamSetting = findViewById(R.id.button_controlsetting);
        Button btnLogout = findViewById(R.id.button_logout);
//        Button btnAbout = findViewById(R.id.button_aboutus);

//        btnParamSetting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, ParamSettingActivity.class);
//                startActivity(intent);
//            }
//        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.finishAll();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences("loginStatus", MODE_PRIVATE).edit();
                editor.putBoolean("isLogin", false);
                editor.apply();
                startActivity(intent);
            }
        });

//        btnAbout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}