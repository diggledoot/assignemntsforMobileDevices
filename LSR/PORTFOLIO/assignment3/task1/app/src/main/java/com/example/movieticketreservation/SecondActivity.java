package com.example.movieticketreservation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.DatabaseErrorHandler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SecondActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList; //add selected Movie into this ArrayList after extraction from bundle
    private RecyclerViewAdapter adapter;
    private Movie movie;

    //calender
    private TextView textView_calendar;
    private Date currentDate;
    private Date selectedDate;//to be packaged

    //spinner
    private Spinner spinner;
    private ArrayAdapter<String> timeAdapter;
    private String selectedTime="";//to be packaged

    //tickets
    private TextView adult,kid;
    private ImageView adult_up,adult_down,kid_up,kid_down;
    private int ticket_adult=0;//to be packaged
    private int ticket_kid=0;//to be packaged

    //book
    private Button book;

    //reservation
    Reservation reservation; //to be packaged

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        movieList=new ArrayList<>();
        if(getIntent().hasExtra("selected_movie")){
            movie=getIntent().getParcelableExtra("selected_movie");
            movieList.add(movie);
        }
        reservation=new Reservation(movie);
        initRecycler();
        initCalendar();
        initSpinner();
        initButtons();

    }
    private int calcDays(){
        long curDate=currentDate.getTime();
        long selDate=selectedDate.getTime();
        int result=(int)((selDate-curDate)/(24*60*60*1000))+1;
        return result;
    }
    private void initButtons(){
        adult=findViewById(R.id.textView_numAdult);
        kid=findViewById(R.id.textView_numKid);
        adult.setText(String.valueOf(ticket_adult));
        kid.setText(String.valueOf(ticket_kid));

        adult_up=findViewById(R.id.imageView_adult_up);
        adult_down=findViewById(R.id.imageView_adult_down);
        adult_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ticket_adult++;
                adult.setText(String.valueOf(ticket_adult));
            }
        });
        adult_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ticket_adult<=0){
                    ticket_adult=0;
                    adult.setText(String.valueOf(ticket_adult));
                }else{
                    ticket_adult--;
                    adult.setText(String.valueOf(ticket_adult));
                }
            }
        });
        kid_up=findViewById(R.id.imageView_kid_up);
        kid_down=findViewById(R.id.imageView_kid_down);
        kid_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ticket_kid++;
                kid.setText(String.valueOf(ticket_kid));
            }
        });
        kid_down.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ticket_kid<=0){
                    ticket_kid=0;
                    kid.setText(String.valueOf(ticket_kid));
                }else {
                    ticket_kid--;
                    kid.setText(String.valueOf(ticket_kid));
                }
            }
        });

        book=findViewById(R.id.button_book);
        book.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int days=calcDays();
                if(days>=4 || days<=0){
                    Toast.makeText(getApplicationContext(),
                            "Invalid selected data, please reselect"
                            ,Toast.LENGTH_SHORT).show();
                }else if(ticket_kid==0 && ticket_adult==0){
                    Toast.makeText(getApplicationContext(),"You must purchase a ticket to book"
                            ,Toast.LENGTH_SHORT).show();
                }
                else {
                    SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy");
                    String temp = sdf.format(selectedDate);
                    reservation.setSelected_date(temp);
                    reservation.setSelected_time(selectedTime);
                    reservation.setAdults(String.valueOf(ticket_adult));
                    reservation.setChild(String.valueOf(ticket_kid));
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("reservation", reservation);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    private void initSpinner(){
        spinner=findViewById(R.id.spinner);
        String[] time=movieList.get(0).getShowtime();
        timeAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item
                ,time);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(timeAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTime = spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTime = movieList.get(0).getShowtime()[0];
            }
        });
    }
    private void initCalendar() {
        Calendar calendar=Calendar.getInstance();
        currentDate=calendar.getTime();
        textView_calendar=findViewById(R.id.editText_date);

        textView_calendar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog=new DatePickerDialog(this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String temp=dayOfMonth+" "+(month+1)+" "+year;
        SimpleDateFormat sdf=new SimpleDateFormat("d M yyyy");
        try {
            selectedDate=sdf.parse(temp);
            sdf=new SimpleDateFormat("d MMMM yyyy");
            sdf.format(selectedDate);
            textView_calendar.setText(sdf.format(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void initRecycler() {
        recyclerView=findViewById(R.id.second_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new RecyclerViewAdapter(movieList);
        recyclerView.setAdapter(adapter);
    }




}
