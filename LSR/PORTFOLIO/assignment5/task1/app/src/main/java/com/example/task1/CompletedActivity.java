package com.example.task1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CompletedActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StatusRecyclerViewAdapter statusRecyclerViewAdapter;
    ArrayList<Task> taskArrayList;
    DatabaseManager databaseManager;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed);
        initMain();
    }
    private void initMain(){
        initDatabase();
        initRecyclerView();
        initNav();
    }

    private void initNav() {
        bottomNavigationView = findViewById(R.id.completed_bottom_nav);
        bottomNavigationView.getMenu().getItem(0).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.main_view:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.overdue_view:
                        startActivity(new Intent(getApplicationContext(),OverdueActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private void initRecyclerView() {
        recyclerView=findViewById(R.id.completed_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        statusRecyclerViewAdapter=new StatusRecyclerViewAdapter(taskArrayList,this);
        recyclerView.setAdapter(statusRecyclerViewAdapter);
    }

    private void initDatabase(){
        databaseManager = new DatabaseManager(this);
        Cursor cursor = databaseManager.loadTasks();
        taskArrayList=new ArrayList<>();
        if(cursor.moveToFirst()){
            do {
                if(cursor.getString(5).equals("1")){
                    taskArrayList.add(new Task(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5)

                    ));
                }

            }while(cursor.moveToNext());
        }
    }
}
