package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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
        setContentView(R.layout.activity_edit_task);
        final Task task = getIntent().getExtras().getParcelable("edit_task");
        databaseManager=new DatabaseManager(this);
        initMain(task);
    }
    private void initMain(Task task){
        initEditableFields(task);
        initCalendar(task);
        initButtons(task);
    }
    private void initEditableFields(Task task){
        editText=findViewById(R.id.edit_editText);
        editDetails=findViewById(R.id.edit_multiline);
        editText.setText(task.getTitle());
        editDetails.setText(task.getDetails());
    }

    private void initCalendar(Task task){
        calendarText=findViewById(R.id.edit_editDate);
        //date not yet
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            selectedDate=sdf.parse(task.getDue_date());
            sdf= new SimpleDateFormat("d-MMM-yy");
            calendarText.setText(sdf.format(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar=findViewById(R.id.edit_imageView);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog();
            }
        });
    }
    private void initButtons(final Task task){
        priorityButton=findViewById(R.id.edit_important);
        if(task.getPriority().equals("1")){
            priority=task.getPriority();
            priorityButton.setChecked(true);
        }
        submit = findViewById(R.id.edit_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.clearFocus();
                Validation();
                if(isValid){
                    //SQLite
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                    databaseManager.updateTask(title,sdf.format(selectedDate),details,priority,String.valueOf(task.getId()));
                    startActivity(new Intent(v.getContext(),MainActivity.class));
                    finish();
                }
            }
        });
    }
    private void Validation(){
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
        }else{
            priority="0";
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
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }


}
