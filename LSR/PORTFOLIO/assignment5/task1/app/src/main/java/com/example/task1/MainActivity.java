package com.example.task1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements  DeleteDialog.OnFragmentInteractionListener{
    //Database
    public static final String DATABASE_NAME="taskDB";
    DatabaseManager databaseManager;

    //RecyclerView
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Task> taskArrayList;
    ArrayList<Task> overdueArrayList;
    Date currentDate = Calendar.getInstance().getTime();

    //Nav Menu
    BottomNavigationView bottomNavigationView;

    //Alarm
    static AlarmManager alarmManager;
    static PendingIntent alarmIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Recycler
        initRecycler();
        //SQL
        loadTable();
        //Floating Button
        initFloatingButton();
        //Navigation
        initNav();
        //Alarm

    }


    private void initNav() {
        bottomNavigationView = findViewById(R.id.main_bottom_nav);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                //make swtich case link to other activityes
                switch (menuItem.getItemId()){
                    case R.id.completed_view:
                        startActivity(new Intent(getApplicationContext(),CompletedActivity.class));
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

    private void initRecycler(){
        recyclerView=findViewById(R.id.recyclerView);
        taskArrayList=new ArrayList<>();
        overdueArrayList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTable(){
        databaseManager = new DatabaseManager(this);
        Cursor cursor = databaseManager.loadTasks();
        if(cursor.moveToFirst()){
            do{
                sortDate(cursor);

            }while(cursor.moveToNext());
            for(int i=0;i<overdueArrayList.size();i++){
                taskArrayList.add(overdueArrayList.get(i));
            }
            recyclerViewAdapter= new RecyclerViewAdapter(taskArrayList,this);
            recyclerView.setAdapter(recyclerViewAdapter);
        }
    }
    private void initFloatingButton(){
        FloatingActionButton fb = findViewById(R.id.fb);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddTask.class));
                finish();
            }
        });
    }

    //Support functions
    private void insertMain(Cursor cursor){
        taskArrayList.add(new Task(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)

        ));
    }
    private void insertOverdue(Cursor cursor){
        overdueArrayList.add(new Task(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)

        ));
    }
    private void sortDate(Cursor cursor){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            Date selectedDate = sdf.parse(cursor.getString(2));

            long curdate=currentDate.getTime();
            long seldate=selectedDate.getTime();
            float result = (float)(seldate-curdate)/(24*60*60*1000)+1;


            if(result<0){
                if(cursor.getString(5).equals("0")){
                    insertOverdue(cursor);
                }

            }else{
                if(cursor.getString(5).equals("0")){
                    insertMain(cursor);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragmentInteraction(int position) {
        taskArrayList.remove(position);
        recyclerViewAdapter.notifyItemRemoved(position);
        recyclerViewAdapter.notifyItemRangeChanged(position,taskArrayList.size());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
