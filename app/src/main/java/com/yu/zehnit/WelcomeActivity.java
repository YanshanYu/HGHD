package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends BaseActivity {

    private Intent intentToLogin;
    private Intent intentToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        intentToMain = new Intent(WelcomeActivity.this, MainActivity.class);
        intentToLogin = new Intent(WelcomeActivity.this, LoginActivity.class);

        // 若已登录，直接进入主页
        SharedPreferences pref = getSharedPreferences("loginStatus", MODE_PRIVATE);
        Boolean isLogin = pref.getBoolean("isLogin", false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin) {
                    startActivity(intentToMain);
                } else {
                    startActivity(intentToLogin);
                }
            }
        }, 1500);



    }
}