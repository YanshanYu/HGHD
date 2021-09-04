package com.yu.zehnit;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.Toolbar;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.gyf.immersionbar.ImmersionBar;
import com.yu.zehnit.tools.ValueFormatter;
import com.yu.zehnit.ui.sessions.Session;
import com.yu.zehnit.ui.sessions.SessionDataManager;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends BaseActivity {
    private Toolbar toolbar;
    private CombinedChart dataChart;
    private CombinedData data;
    public static final int[] TASK_COLORS = { rgb("#EE5050"), rgb("#EC905D"), rgb("#2DC5C8"), rgb("#4375FB"), rgb("#AC82F1"), rgb("#7240e4")};
    private String[] mColCcaptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int amount_columns= SessionDataManager.getSize();
        setContentView(R.layout.activity_chart);
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true)
                .fitsSystemWindows(true).init();
        // 监听返回按钮
        toolbar = findViewById(R.id.toolbar_chart);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataChart= findViewById(R.id.data_chart);
        dataChart.getDescription().setEnabled(false);
        dataChart.setBackgroundColor(Color.WHITE);
        dataChart.setDrawGridBackground(false);
        dataChart.setDrawBarShadow(false);
        dataChart.setHighlightFullBarEnabled(false);
        dataChart.setPinchZoom(false);
        dataChart.setScaleXEnabled(false);
        dataChart.setScaleYEnabled(false);

       // dataChart.setTouchEnabled(false);
        dataChart.setDrawValueAboveBar(false);
        dataChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,CombinedChart.DrawOrder.LINE
        });

        Legend legend=dataChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        showDataOnChart();
        Matrix m=new Matrix();
        m.postScale(2.5f,1f);
        dataChart.moveViewToX(amount_columns-1);
        dataChart.getViewPortHandler().refresh(m,dataChart,false);
        dataChart.animateX(500);

    }
    private void showDataOnChart(){
        data=new CombinedData();
        data.setData(getLineData());
        data.setData(getBarData());
        //set X axis
        int amount_columns= SessionDataManager.getSize();
        mColCcaptions = new String[amount_columns];
        DateFormat df = android.text.format.DateFormat.getDateFormat(this);
        for(int i=0;i<amount_columns;i++){
            Session session= SessionDataManager.getSession(i);
            assert session != null;
            mColCcaptions[i]=df.format(session.getDate());
        }
        XAxis xAxis=dataChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(amount_columns+1);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IAxisValueFormatter(){
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if(value<0||value>amount_columns-1) return "";
                return mColCcaptions[(int) value];
            }
        });
        // set Y axis
        YAxis yAxisLeft=dataChart.getAxisLeft();
        yAxisLeft.setAxisMinimum(0f);
        yAxisLeft.enableGridDashedLine(10f,10f,0f);
        yAxisLeft.setDrawGridLines(true);

        YAxis yAxisRight=dataChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawLabels(false);
        dataChart.setData(data);
        xAxis.setAxisMinimum(dataChart.getCombinedData().getXMin()-0.5f);
        xAxis.setAxisMaximum(dataChart.getCombinedData().getXMax()+0.5f);

        dataChart.invalidate();
    }
    public LineData getLineData(){
        LineData lineData=new LineData();
        List<Entry> customCounts=new ArrayList<>();
        int amount_columns= SessionDataManager.getSize();

        for(int i=0;i<amount_columns;i++){
            Session session= SessionDataManager.getSession(i);
            assert session != null;
            customCounts.add(new Entry(i,session.getTotalScore()));
        }
        LineDataSet lineDataSet=new LineDataSet(customCounts,"Total Score");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(getResources().getColor(R.color.colorLineChart));
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorLineChart));
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setCubicIntensity(0.5f);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setLineWidth(3);
        lineDataSet.setValueTextColor(getResources().getColor(R.color.colorLineChart));
        lineDataSet.setValueTextSize(16f);
        lineData.addDataSet(lineDataSet);
        return lineData;
    }


    public BarData getBarData(){
        ArrayList<BarEntry> values = new ArrayList<>();
        int amount_columns= SessionDataManager.getSize();
        DateFormat df = android.text.format.DateFormat.getDateFormat(this);
        for (int s = 0; s < amount_columns; s++) {
            float[] taskscores= new float[Session.AMOUNT_TASKS];
            Session session= SessionDataManager.getSession(s);
            assert session != null;
            for(int t=0;t<taskscores.length;t++) {
                if(session.getTaskScore(t)==0)continue;
                taskscores[t] = session.getTaskScore(t);
            }
            values.add(new BarEntry(s,taskscores));
        }
        BarDataSet bardataset = new BarDataSet(values, "Scores");

        int[] colors= new int[Session.AMOUNT_TASKS];
        if (colors.length >= 0) System.arraycopy(TASK_COLORS, 0, colors, 0, colors.length);
        bardataset.setColors(colors);
        bardataset.setStackLabels(MainActivity.TASKCAPTIONS);
        BarData barData=new BarData(bardataset);
        barData.setValueTextSize(16f);
        barData.setBarWidth(0.5f);

        barData.setValueFormatter(new ValueFormatter());
        return barData;


      /*  ArrayList<BarEntry> entries1 = new ArrayList<>();
        ArrayList<BarEntry> entries2 = new ArrayList<>();
        int amount_columns= SessionDataManager.getSize();
        for (int index = 0; index < amount_columns; index++) {
            entries1.add(new BarEntry(0, getRandom(25, 25)));

            // stacked
            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
        }

        BarDataSet set1 = new BarDataSet(entries1, "Bar 1");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        BarDataSet set2 = new BarDataSet(entries2, "");
        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255));
        set2.setValueTextColor(Color.rgb(61, 165, 255));
        set2.setValueTextSize(10f);
        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1, set2);
        d.setBarWidth(barWidth);

        // make this BarData object grouped
        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;

       */
    }

    private static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = (color >> 0) & 0xFF;
        return Color.rgb(r, g, b);
    }
    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }
}