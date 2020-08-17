package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class DesignerHomeActivity extends AppCompatActivity {

    float downloads[] = {20,32,13,43,12,32,43,23,32,12,32,12};
    String months[] = {"Jan","Feb","march","april","may","June","July","Aug","Sept","SAM","Nov","Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_home);
        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("My Home");
        
        setUpPieChart();
    }

    private void setUpPieChart() {
        List<PieEntry> pieEntries = new ArrayList<>();
        for(int a = 0 ; a < downloads.length ; a++ ){
            pieEntries.add(new PieEntry(downloads[a],months[a]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Downloads per month");

        PieData pieData = new PieData(dataSet);

        PieChart chart = (PieChart) findViewById(R.id.pir_chart);
        chart.setData(pieData);
        chart.invalidate();
    }
}