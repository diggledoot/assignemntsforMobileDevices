package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddTask extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener{
    //Edit Text Fields
    private EditText editText,editDetails;
    private String title,details;

    //Date Selection
    private TextView calendarText;
    private ImageView calendar;
    private Date selectedDate;

    //Buttons
    private CheckBox priorityButton;
    private String priority="0";

    //Submission
    private ImageButton submit;
    private Boolean isValid=false;
    private DatabaseManager databaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        databaseManager = new DatabaseManager(this);
        initMain();
    }
    private void initMain(){
        initEditableFields();
        initCalendar();
        initButtons();
    }
    private void initEditableFields(){
        editText=findViewById(R.id.editText);
        editDetails=findViewById(R.id.multiline);
    }
    private void initCalendar(){
        calendarText=findViewById(R.id.editDate);
        calendar=findViewById(R.id.imageView);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog();
            }
        });
    }
    private void initButtons(){
        priorityButton=findViewById(R.id.important);

        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.clearFocus();
                Validation();
                if(isValid){
                    //SQLite
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    databaseManager.addTask(title,sdf.format(selectedDate),details,priority,"0");
                    initAlarm();
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                    finish();
                }

            }
        });
    }
    private void Validation(){
        //extract data
        title=editText.getText().toString().trim();
        details=editDetails.getText().toString().trim();
        if(title.matches("") || selectedDate==null){
            Toast.makeText(getApplicationContext()
                    , "Fill in the required fields!"
                    ,Toast.LENGTH_SHORT).show();
            isValid=false;
        }else{
            isValid=true;
        }
        if(priorityButton.isChecked()){
            priority="1";
        }
    }
    private void DatePickerDialog(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String temp=dayOfMonth+" "+(month+1)+" "+year;
        SimpleDateFormat sdf=new SimpleDateFormat("d M yyyy");
        try {
            selectedDate=sdf.parse(temp);
            sdf=new SimpleDateFormat("d-MMM-yy");
            sdf.format(selectedDate);
            calendarText.setText(sdf.format(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initAlarm() {
        AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        if(alarmManager!=null){
            alarmManager.cancel(alarmIntent);
        }
        //Set time
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,8);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        Log.d("message",String.valueOf(calendar.getTime()));
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,alarmIntent);


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
