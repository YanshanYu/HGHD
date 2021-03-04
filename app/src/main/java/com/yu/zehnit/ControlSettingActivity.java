package com.yu.zehnit;
import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;


public class ControlSettingActivity extends AppCompatActivity {

    private SeekBar sinFrequencySeekBar;
    private SeekBar sinAmplitudeSeekBar;

    private SeekBar fangFrequencySeekBar;
    private SeekBar fangAmplitudeSeekBar;

    private static final String TestApp="TestApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_setting);
        sinFrequencySeekBar = findViewById(R.id.sinfrequency);
        sinAmplitudeSeekBar = findViewById(R.id.sinamplitude);
        fangFrequencySeekBar = findViewById(R.id.fangfrequency);
        fangAmplitudeSeekBar = findViewById(R.id.fangamplitude);

        //sinFrequencySeekBar.setMin(20);
        sinFrequencySeekBar.setMax(100);
       // sinAmplitudeSeekBar.setMin(20);
        sinAmplitudeSeekBar.setMax(100);
        //fangFrequencySeekBar.setMin(20);
        fangFrequencySeekBar.setMax(100);
        //fangAmplitudeSeekBar.setMin(20);
        fangAmplitudeSeekBar.setMax(100);

        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        String sinFrequencyValue = pref.getString("sin_frequency", "");
        String sinAmplitudeValue = pref.getString("sin_amplitude", "");
        String fangFrequencyValue = pref.getString("fang_frequency", "");
        String fangAmplitudeValue = pref.getString("fang_amplitude", "");
        if (sinFrequencyValue != "") {
            sinFrequencySeekBar.setProgress(Integer.parseInt(sinFrequencyValue));
            sinAmplitudeSeekBar.setProgress(Integer.parseInt(sinAmplitudeValue));
            fangFrequencySeekBar.setProgress(Integer.parseInt(fangFrequencyValue));
            fangAmplitudeSeekBar.setProgress(Integer.parseInt(fangAmplitudeValue));
        }




        sinFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TestApp, String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        sinAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TestApp, String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fangFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TestApp, String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        fangAmplitudeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d(TestApp, String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }


    @Override
    protected void onDestroy() {

        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
        editor.putString("sin_frequency", String.valueOf(sinFrequencySeekBar.getProgress()));
        editor.putString("sin_amplitude", String.valueOf(sinAmplitudeSeekBar.getProgress()));
        editor.putString("fang_frequency", String.valueOf(fangFrequencySeekBar.getProgress()));
        editor.putString("fang_amplitude", String.valueOf(fangAmplitudeSeekBar.getProgress()));
        editor.apply();
        super.onDestroy();
    }
}