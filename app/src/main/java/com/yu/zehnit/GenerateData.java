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
        String sinFrequencyValue = pref.getString("sin_frequency", "");
        String sinAmplitudeValue = pref.getString("sin_amplitude", "");
        String fangFrequencyValue = pref.getString("fang_frequency", "");
        String fangAmplitudeValue = pref.getString("fang_amplitude", "");

        ArrayList<double[]> data = new ArrayList<double[]>();
        Log.d(TestApp, String.valueOf(sinFrequencyValue));
        for(int i=0; i<=100; i++) {
            double x = Math.PI / 50  * i  * Integer.parseInt(sinFrequencyValue) / 60;
            double y = Integer.parseInt(sinAmplitudeValue) * Math.sin(x*60/Integer.parseInt(sinFrequencyValue));
            double[] value = {x, y};
            data.add(value);
            Log.d(TestApp, String.valueOf(data.get(i)[0]) +  "    " +  String.valueOf(data.get(i)[1]));
        }

    }
}