package com.yu.zehnit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.gyf.immersionbar.ImmersionBar;

public class WelcomeActivity extends BaseActivity {

    private static final int REQUEST_FINE_LOCATION = 2;
    private Intent intentToLogin;
    private Intent intentToMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // 沉浸状态栏设置
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

        // 请求位置权限
        requestLocationPermission();

        intentToMain = new Intent(WelcomeActivity.this, MainActivity.class);
        intentToLogin = new Intent(WelcomeActivity.this, LoginActivity.class);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                    finish();
                }
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    chooseActivity();
                }
            }
            break;
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    private void chooseActivity() {
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