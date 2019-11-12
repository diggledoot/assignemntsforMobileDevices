package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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

            //remember to set local time to malaysia
            /*Date currentDate= Calendar.getInstance().getTime();
            long curdate=currentDate.getTime();
            long seldate=selectedDate.getTime();
            float result = (float)(seldate-curdate)/(24*60*60*1000)+1;
            if(result<0){
                Log.d("message","That is a day before today!");
            }
            else{
                Log.d("message","That's some day in the future or today!");
            }
            String test = String.format("%.2f",result);
            String[] split = test.split("\\.");
            int days = Integer.parseInt(split[0]);
            split[1]="0."+split[1];
            float hours = Float.parseFloat(split[1]);
            hours = hours*24;
            int hr= (int)hours+1;
            if(result<0){
                Log.d("message","Deadline has passed a long time ago!");

            }else if(days==0){
                Log.d("message",days+" days "+hr+" hours until deadline");

            }else{
                Log.d("message",days+" days and "+hr+" hours until deadline");
            }*/

            //Getting individual Month
            /*SimpleDateFormat sdf_month = new SimpleDateFormat("MMM");
            String s_month = sdf_month.format(selectedDate);
            Log.d("Month",s_month);*/
            //Getting individual day
            /*SimpleDateFormat sdf_day = new SimpleDateFormat("d");
            String s_day = sdf_day.format(selectedDate);
            Log.d("Day",s_day);*/
            //Log.d("message","Current Date:"+Calendar.getInstance().getTime());
            //Log.d("message","Selected Date:"+selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}
