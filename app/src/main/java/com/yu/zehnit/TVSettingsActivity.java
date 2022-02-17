package com.yu.zehnit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.SharedPreferencesUtils;

public class TVSettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText txWidth;
    private EditText txHeight;
    private EditText txDistance;
    private Button btnSave;
    private static String TV_SETTING="tv_setting";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvsettings);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();
        ActionBar actionBar=this.getSupportActionBar();
        actionBar.hide();
        SharedPreferencesUtils.setFileName(TV_SETTING);
        txWidth=findViewById(R.id.text_tv_width);
        txHeight=findViewById(R.id.text_tv_height);
        txDistance=findViewById(R.id.text_distance);
        btnSave=findViewById(R.id.button_save);
        txWidth.setText(String.valueOf(SharedPreferencesUtils.getParam(TVSettingsActivity.this,"tv_width",110)));
        txHeight.setText(String.valueOf(SharedPreferencesUtils.getParam(TVSettingsActivity.this,"tv_height",63)));
        txDistance.setText(String.valueOf(SharedPreferencesUtils.getParam(TVSettingsActivity.this,"tv_distance",95)));

        txWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int w=Integer.parseInt(txWidth.getText().toString());
                int d=(int) (w/(2*Math.tan(Math.PI*30/180)));
                txDistance.setText(d);
            }
        });

        // 监听返回按钮
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.setFileName(TV_SETTING);
                SharedPreferencesUtils.setParam(TVSettingsActivity.this,"tv_width",Integer.parseInt(txWidth.getText().toString()));
                SharedPreferencesUtils.setParam(TVSettingsActivity.this,"tv_height",Integer.parseInt(txHeight.getText().toString()));
                SharedPreferencesUtils.setParam(TVSettingsActivity.this,"tv_distance",Integer.parseInt(txDistance.getText().toString()));
                Toast.makeText(TVSettingsActivity.this,getString(R.string.toast_text),Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}