package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class GenerateData extends AppCompatActivity {
    private static final String TestApp="TestApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_data);
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        int sinFrequencyValue = pref.getInt("sin_frequency", 0);
        int sinAmplitudeValue = pref.getInt("sin_amplitude", 0);
        int fangFrequencyValue = pref.getInt("fang_frequency", 0);
        int fangAmplitudeValue = pref.getInt("fang_amplitude", 0);

        ArrayList<double[]> data = new ArrayList<double[]>();
        Log.d(TestApp, String.valueOf(sinFrequencyValue));
        for(int i=0; i<=100; i++) {
            double x = Math.PI / 50  * i  * sinFrequencyValue / 60;
            double y = sinAmplitudeValue * Math.sin(x*60/sinFrequencyValue);
            double[] value = {x, y};
            data.add(value);
            Log.d(TestApp, String.valueOf(data.get(i)[0]) +  "    " +  String.valueOf(data.get(i)[1]) + "\n");
        }
        

    }
}