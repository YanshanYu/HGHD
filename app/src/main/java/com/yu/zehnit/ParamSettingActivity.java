package com.yu.zehnit;
import androidx.appcompat.widget.Toolbar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;


import com.gyf.immersionbar.ImmersionBar;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_param_setting);

        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();

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
        float sinFrequencyValue = pref.getFloat("sin_frequency", (float) 0);
        int sinAmplitudeValue = pref.getInt("sin_amplitude", 0);
        float fangFrequencyValue = pref.getFloat("fang_frequency", (float) 0);
        int fangAmplitudeValue = pref.getInt("fang_amplitude", 0);
        sinFrequencySeekBar.setProgress((int) sinFrequencyValue * 100);
        sinAmplitudeSeekBar.setProgress(sinAmplitudeValue);
        fangFrequencySeekBar.setProgress((int) fangFrequencyValue * 100);
        fangAmplitudeSeekBar.setProgress(fangAmplitudeValue);

        sinFrequencyIndicator.setText(sinFrequencyValue + "Hz");
        sinAmplitudeIndicator.setText(Integer.toString(sinAmplitudeValue));
        fangFrequencyIndicator.setText(fangFrequencyValue + "Hz");
        fangAmplitudeIndicator.setText(Integer.toString(fangAmplitudeValue));
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

        super.onDestroy();

    }

    private void initSinFrequencyData() {


        sinFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String indicatorText = Integer.toString(i);
                sinFrequencyIndicator.setText((float) i/100 + "Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putFloat("sin_frequency", (float) sinFrequencySeekBar.getProgress()/100);
                editor.apply();

            }
        });

    }

    private void initSinAmplitudeData() {
        sinAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String indicatorText = Integer.toString(i);
                sinAmplitudeIndicator.setText(indicatorText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt("sin_amplitude", sinAmplitudeSeekBar.getProgress());
                editor.apply();

            }
        });


    }
    private void initFangFrequencyData() {
        fangFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String indicatorText = Integer.toString(i);
                fangFrequencyIndicator.setText((float) i/100 + "Hz");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putFloat("fang_frequency", (float) fangFrequencySeekBar.getProgress()/100);
                editor.apply();

            }
        });



    }
    private void initFangAmplitudeData() {
        fangAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                String indicatorText = Integer.toString(i);
                fangAmplitudeIndicator.setText(indicatorText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                editor.putInt("fang_amplitude", fangAmplitudeSeekBar.getProgress());
                editor.apply();

            }
        });


    }


}