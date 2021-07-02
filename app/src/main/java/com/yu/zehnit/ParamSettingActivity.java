package com.yu.zehnit;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.Param;
import com.yu.zehnit.tools.ParamAdapter;
import com.yu.zehnit.tools.SharedPreferencesUtils;
import com.yu.zehnit.tools.VideoAdapter;

import java.util.ArrayList;
import java.util.List;

public class ParamSettingActivity extends BaseActivity {

    private SeekBar sinFrequencySeekBar;
    private SeekBar sinAmplitudeSeekBar;
    private SeekBar fangFrequencySeekBar;
    private SeekBar fangAmplitudeSeekBar;


    private TextView sinFrequencyIndicator;
    private TextView sinAmplitudeIndicator;
    private TextView fangFrequencyIndicator;
    private TextView fangAmplitudeIndicator;

    private Toolbar toolbar;

    private List<Param> paramList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_setting);

        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

        initParams();
        RecyclerView recyclerView = findViewById(R.id.recycle_view_param);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ParamSettingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ParamAdapter adapter = new ParamAdapter(paramList);
        recyclerView.setAdapter(adapter);


        // 监听返回按钮
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initParams() {
        SharedPreferencesUtils.setFileName("data");

        float calibrationValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "calibration", 0.0f);
        Param calibration = new Param("校准", 0, "校准值", calibrationValue);
        paramList.add(calibration);

        float gainValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "gain", 0.0f);
        Param gain = new Param("凝视增益", 0, "增益值", gainValue);
        paramList.add(gain);

        float pursuitFrequencyValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "pursuit_frequency", 0.0f);
        float pursuitAmplitudeValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "pursuit_amplitude", 0.0f);
        Param smoothPursuit = new Param("视追踪实验", 1, "频率", pursuitFrequencyValue, "幅度",pursuitAmplitudeValue);
        paramList.add(smoothPursuit);

        float saccadeFrequencyValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "saccade_frequency", 0.0f);
        Param saccade = new Param("扫视实验", 0, "频率", saccadeFrequencyValue);
        paramList.add(saccade);
    }


}