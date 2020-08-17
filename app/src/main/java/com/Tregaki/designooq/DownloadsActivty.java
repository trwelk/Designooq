package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DownloadsActivty extends AppCompatActivity {

    public int colors[] = {
            Color.rgb(193,37,82),            Color.rgb(255,102,0),            Color.rgb(245,199,0),
            Color.rgb(106,150,31),            Color.rgb(179,100,53)
    };

    float downloads[] = {20,32,13,43,12,32,43,23,32,12,32,12};
    String months[] = {"Jan","Feb","march","april","may","June","July","Aug","Sept","SAM","Nov","Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads_activty);

        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("Monthly Downloads");
        setUpPieChart();
    }

    private void setUpPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int a = 0 ; a < downloads.length ; a++ ){
            pieEntries.add(new PieEntry(downloads[a],months[a]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Downloads per month");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(20);
        PieData pieData = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.pir_chart);
        chart.setData(pieData);
        chart.setCenterTextSize(20);

        chart.invalidate();
    }
}