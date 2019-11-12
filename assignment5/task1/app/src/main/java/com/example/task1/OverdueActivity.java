package com.example.task1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class OverdueActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StatusRecyclerViewAdapter statusRecyclerViewAdapter;
    ArrayList<Task> taskArrayList;
    DatabaseManager databaseManager;
    BottomNavigationView bottomNavigationView;
    Date currentDate= Calendar.getInstance().getTime();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overdue);
        initMain();
    }
    private void initMain(){
        initDatabase();
        initRecyclerView();
        initNav();
    }

    private void initNav() {
        bottomNavigationView = findViewById(R.id.overdue_bottom_nav);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.main_view:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.completed_view:
                        startActivity(new Intent(getApplicationContext(),CompletedActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.overdue_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statusRecyclerViewAdapter=new StatusRecyclerViewAdapter(taskArrayList,this);
        recyclerView.setAdapter(statusRecyclerViewAdapter);
    }

    private void initDatabase(){
        databaseManager = new DatabaseManager(this);
        Cursor cursor = databaseManager.loadTasks();
        taskArrayList=new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if(cursor.moveToFirst()){
            do {
                try {
                    Date selectedDate = sdf.parse(cursor.getString(2));
                    long curdate=currentDate.getTime();
                    long seldate=selectedDate.getTime();
                    float result = (float)(seldate-curdate)/(24*60*60*1000)+1;
                    if(result<0){
                        taskArrayList.add(new Task(
                                cursor.getInt(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3),
                                cursor.getString(4),
                                cursor.getString(5)

                        ));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());
        }
    }
}
