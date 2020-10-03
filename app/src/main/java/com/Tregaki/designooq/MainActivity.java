package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//abcd
        setContentView(R.layout.activity_main);
        Intent someIntent = new Intent(MainActivity.this,MessageListActivity.class);
        startActivity(someIntent);
    }
}