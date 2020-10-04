package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DownloadsActivty extends AppCompatActivity {
    private String user;
    private float downloads[] = {0,0,0,0,0,0,0,0,0,0,0,0};
    private boolean loaded;


    public int colors[] = {
            Color.rgb(193,37,82),            Color.rgb(255,102,0),            Color.rgb(245,199,0),
            Color.rgb(106,150,31),            Color.rgb(179,100,53)
    };

    //float downloads[] = {20,32,13,43,12,32,43,23,32,12,32,12};
    String months[] = {"Jan","Feb","march","april","may","June","July","Aug","Sept","oct","Nov","Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloads_activty);
        user = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference().child("post").orderByChild("user").equalTo(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    //String desc = ds.child("desc").getValue(String.class);
                    //String title = ds.child("title").getValue(String.class);
                    downloads[Integer.parseInt(ds.child("month").getValue().toString()) - 1]++;
                    //Log.d("DOWNLOAD_MONTH_Added_MONTH", String.valueOf(downloads[Integer.parseInt(ds.child("month").getValue().toString())]));

                    //Log.d("TAG", ds.child("month").getValue().toString());
                }
                setUpPieChart();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("Monthly Uploads");
    }



    @Override
    protected void onStart() {
        super.onStart();
    }
    private void setUpPieChart() {
        Log.d("DOWNLOAD_MONTH",Float.toString(downloads.length));
        Log.d("DOWNLOAD_MONTH",Float.toString(downloads[8]));
        Log.d("DOWNLOAD_MONTH",Float.toString(downloads[9]));



        List<PieEntry> pieEntries = new ArrayList<>();
        for(int a = 0 ; a < downloads.length ; a++ ){
            Log.d("DOWNLOAD_MONTH",Float.toString(downloads[a]));
            pieEntries.add(new PieEntry(this.downloads[a] ,months[a]));
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