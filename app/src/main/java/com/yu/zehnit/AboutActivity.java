package com.yu.zehnit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.gyf.immersionbar.ImmersionBar;

public class AboutActivity extends BaseActivity {

    private Toolbar toolbar;
    private Button btnCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        toolbar = findViewById(R.id.toolbar_about_us);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCall = findViewById(R.id.button_call);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:1008611"));
                startActivity(intent);
            }
        });
    }
}