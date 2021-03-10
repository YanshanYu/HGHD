package com.yu.zehnit;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.yu.zehnit.tools.IndicatorSeekBar;


public class ParamSettingActivity extends BaseActivity {

    private IndicatorSeekBar sinFrequencySeekBar;
    private IndicatorSeekBar sinAmplitudeSeekBar;
    private IndicatorSeekBar fangFrequencySeekBar;
    private IndicatorSeekBar fangAmplitudeSeekBar;

    private TextView sinFrequencyIndicator;

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

        initData();

    }

    private void initData() {


        final ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) sinFrequencyIndicator.getLayoutParams();
        sinFrequencySeekBar.setOnSeekBarChangeListener(new IndicatorSeekBar.OnIndicatorSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, float indicatorOffset) {
                String indicatorText = Integer.toString(progress);
                sinFrequencyIndicator.setText(indicatorText);
//                params.leftMargin = (int) indicatorOffset + 340;
                params.leftMargin = (int) indicatorOffset;
                Log.d(TestApp, String.valueOf(indicatorOffset));
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

}