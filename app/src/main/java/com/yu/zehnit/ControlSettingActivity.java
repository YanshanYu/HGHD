package com.yu.zehnit;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yu.zehnit.tools.IndicatorSeekBar;


public class ControlSettingActivity extends AppCompatActivity {

    private IndicatorSeekBar sinFrequencySeekBar;
    private IndicatorSeekBar sinAmplitudeSeekBar;

    private IndicatorSeekBar fangFrequencySeekBar;
    private IndicatorSeekBar fangAmplitudeSeekBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_setting);
        sinFrequencySeekBar = findViewById(R.id.sin_frequency);

    }

}