package com.example.movieticketreservation;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList;
    private RecyclerViewAdapter adapter;


    //summary
    private CardView cardView;
    private TextView ticket_adult_num,ticket_kid_num,title,date,time;
    private ImageView imageView;
    private Movie movie;
    private Reservation reservation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList=Movie.createMovieList(this);
        initRecycler();
        initCardView();
    }

    private void initRecycler(){
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration divide=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divide);
        adapter=new RecyclerViewAdapter(movieList);
        recyclerView.setAdapter(adapter);
    }
    private void initCardView(){
        cardView=findViewById(R.id.cardView);
        ticket_adult_num=findViewById(R.id.textView_adult_tickets_num);
        ticket_kid_num=findViewById(R.id.textView_child_tickets_num);
        title=findViewById(R.id.title);
        date=findViewById(R.id.textView_date);
        time=findViewById(R.id.textView_time);
        imageView=findViewById(R.id.imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999 && resultCode==RESULT_OK){
            cardView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //movie=data.getParcelableExtra("reservation");
            reservation=data.getParcelableExtra("reservation");
            int resID=getResources().getIdentifier(reservation.getImageName(),"drawable",getPackageName());
            imageView.setImageResource(resID);
            title.setText(reservation.getTitle());
            date.setText("Date: "+reservation.getSelected_date());
            time.setText("Time: "+reservation.getSelected_time());
            ticket_adult_num.setText(reservation.getAdults());
            ticket_kid_num.setText(reservation.getChild());
        }
    }
}
