package com.yu.zehnit;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yu.zehnit.tools.IndicatorSeekBar;


public class ParamSettingActivity extends BaseActivity {

    private IndicatorSeekBar sinFrequencySeekBar;
    private IndicatorSeekBar sinAmplitudeSeekBar;
    private IndicatorSeekBar fangFrequencySeekBar;
    private IndicatorSeekBar fangAmplitudeSeekBar;


    private TextView sinFrequencyIndicator;
    private TextView sinAmplitudeIndicator;
    private TextView fangFrequencyIndicator;
    private TextView fangAmplitudeIndicator;

    private Toolbar toolbar;

    private static final String TestApp="TestApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_setting);

        sinFrequencySeekBar = findViewById(R.id.sin_frequency);
        sinAmplitudeSeekBar = findViewById(R.id.sin_amplitude);
        fangFrequencySeekBar = findViewById(R.id.fang_frequency);
        fangAmplitudeSeekBar = findViewById(R.id.fang_amplitude);

        sinFrequencyIndicator = findViewById(R.id.sin_frequency_indicator);
        sinAmplitudeIndicator = findViewById(R.id.sin_amplitude_indicator);
        fangFrequencyIndicator = findViewById(R.id.fang_frequency_indicator);
        fangAmplitudeIndicator = findViewById(R.id.fang_amplitude_indicator);

        initSinFrequencyData();
        initSinAmplitudeData();
        initFangFrequencyData();
        initFangAmplitudeData();

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int sinFrequencyValue = pref.getInt("sin_frequency", 0);
        int sinAmplitudeValue = pref.getInt("sin_amplitude", 0);
        int fangFrequencyValue = pref.getInt("fang_frequency", 0);
        int fangAmplitudeValue = pref.getInt("fang_amplitude", 0);
        sinFrequencySeekBar.setProgress(sinFrequencyValue);
        sinAmplitudeSeekBar.setProgress(sinAmplitudeValue);
        fangFrequencySeekBar.setProgress(fangFrequencyValue);
        fangAmplitudeSeekBar.setProgress(fangAmplitudeValue);

        // 监听返回按钮
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putInt("sin_frequency", sinFrequencySeekBar.getProgress());
        editor.putInt("sin_amplitude", sinAmplitudeSeekBar.getProgress());
        editor.putInt("fang_frequency", fangFrequencySeekBar.getProgress());
        editor.putInt("fang_amplitude", fangAmplitudeSeekBar.getProgress());
        editor.apply();
        super.onDestroy();

    }

    private void initSinFrequencyData() {


        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) sinFrequencyIndicator.getLayoutParams();
        sinFrequencySeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = Integer.toString(progress);
                sinFrequencyIndicator.setText(indicatorText);
                params.leftMargin = (int) indicatorOffset;

                sinFrequencyIndicator.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sinFrequencyIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sinFrequencyIndicator.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void initSinAmplitudeData() {
        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) sinAmplitudeIndicator.getLayoutParams();
        sinAmplitudeSeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = Integer.toString(progress);
                sinAmplitudeIndicator.setText(indicatorText);
                params.leftMargin = (int) indicatorOffset;
                Log.d(TestApp, String.valueOf(indicatorOffset));
                sinAmplitudeIndicator.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                sinAmplitudeIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sinAmplitudeIndicator.setVisibility(View.INVISIBLE);
            }
        });


    }
    private void initFangFrequencyData() {

        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) fangFrequencyIndicator.getLayoutParams();
        fangFrequencySeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = Integer.toString(progress);
                fangFrequencyIndicator.setText(indicatorText);
                params.leftMargin = (int) indicatorOffset;
                Log.d(TestApp, String.valueOf(indicatorOffset));
                fangFrequencyIndicator.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                fangFrequencyIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fangFrequencyIndicator.setVisibility(View.INVISIBLE);
            }
        });

    }
    private void initFangAmplitudeData() {

        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) fangAmplitudeIndicator.getLayoutParams();
        fangAmplitudeSeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = Integer.toString(progress);
                fangAmplitudeIndicator.setText(indicatorText);
                params.leftMargin = (int) indicatorOffset;
                Log.d(TestApp, String.valueOf(indicatorOffset));
                fangAmplitudeIndicator.setLayoutParams(params);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                fangAmplitudeIndicator.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                fangAmplitudeIndicator.setVisibility(View.INVISIBLE);
            }
        });

    }


}