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
        RecyclerView recyclerView = findViewById(R.id.recycle_view_param1);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ParamSettingActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        ParamAdapter adapter = new ParamAdapter(paramList);
        recyclerView.setAdapter(adapter);

//        sinFrequencySeekBar = findViewById(R.id.sin_frequency);
//        sinAmplitudeSeekBar = findViewById(R.id.sin_amplitude);
//        fangFrequencySeekBar = findViewById(R.id.fang_frequency);
//        fangAmplitudeSeekBar = findViewById(R.id.fang_amplitude);
//
//        sinFrequencyIndicator = findViewById(R.id.sin_frequency_indicator);
//        sinAmplitudeIndicator = findViewById(R.id.sin_amplitude_indicator);
//        fangFrequencyIndicator = findViewById(R.id.fang_frequency_indicator);
//        fangAmplitudeIndicator = findViewById(R.id.fang_amplitude_indicator);
//
//        initSinFrequencyData();
//        initSinAmplitudeData();
//        initFangFrequencyData();
//        initFangAmplitudeData();
//
//        SharedPreferencesUtils.setFileName("data");
//        float sinFrequencyValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "sin_frequency", 0.0f);
//        int sinAmplitudeValue = (int) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "sin_amplitude", 0);
//        float fangFrequencyValue = (float) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "fang_frequency", 0.0f);
//        int fangAmplitudeValue = (int) SharedPreferencesUtils.getParam(ParamSettingActivity.this, "fang_amplitude", 0);
//
//        sinFrequencySeekBar.setProgress((int) (sinFrequencyValue * 100));
//        sinAmplitudeSeekBar.setProgress(sinAmplitudeValue);
//        fangFrequencySeekBar.setProgress((int) (fangFrequencyValue * 100));
//        fangAmplitudeSeekBar.setProgress(fangAmplitudeValue);
//
//        sinFrequencyIndicator.setText(sinFrequencyValue + "Hz");
//        sinAmplitudeIndicator.setText(Integer.toString(sinAmplitudeValue));
//        fangFrequencyIndicator.setText(fangFrequencyValue + "Hz");
//        fangAmplitudeIndicator.setText(Integer.toString(fangAmplitudeValue));
//        // 监听返回按钮
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//
//    }
//
//    private void initSinFrequencyData() {
//
//
//        sinFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                String indicatorText = Integer.toString(i);
//                sinFrequencyIndicator.setText((float) i/100 + "Hz");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SharedPreferencesUtils.setParam(ParamSettingActivity.this, "sin_frequency", (float) sinFrequencySeekBar.getProgress()/100);
//            }
//        });
//
//    }
//
//    private void initSinAmplitudeData() {
//        sinAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                String indicatorText = Integer.toString(i);
//                sinAmplitudeIndicator.setText(indicatorText);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SharedPreferencesUtils.setParam(ParamSettingActivity.this, "sin_amplitude", sinAmplitudeSeekBar.getProgress());
//            }
//        });
//
//
//    }
//    private void initFangFrequencyData() {
//        fangFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                String indicatorText = Integer.toString(i);
//                fangFrequencyIndicator.setText((float) i/100 + "Hz");
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SharedPreferencesUtils.setParam(ParamSettingActivity.this, "fang_frequency", (float) fangFrequencySeekBar.getProgress()/100);
//            }
//        });
//
//
//
//    }
//    private void initFangAmplitudeData() {
//        fangAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                String indicatorText = Integer.toString(i);
//                fangAmplitudeIndicator.setText(indicatorText);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SharedPreferencesUtils.setParam(ParamSettingActivity.this, "fang_amplitude", fangAmplitudeSeekBar.getProgress());
//
//            }
//        });
//
//
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