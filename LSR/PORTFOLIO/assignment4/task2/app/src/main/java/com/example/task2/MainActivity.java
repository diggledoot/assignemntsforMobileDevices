package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

public class MainActivity extends AppCompatActivity {
    GridView gridView;
    public static String KEY="IMAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] links = getResources().getStringArray(R.array.links);
        gridView=findViewById(R.id.gridView);
        GridAdapter gridAdapter = new GridAdapter(this,links); //replace imgID with URLs
        gridView.setAdapter(gridAdapter);
    }
}
