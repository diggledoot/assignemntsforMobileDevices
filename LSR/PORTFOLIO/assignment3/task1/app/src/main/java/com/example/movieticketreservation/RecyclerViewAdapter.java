package com.example.movieticketreservation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<Movie> movieList;

    public RecyclerViewAdapter(ArrayList<Movie> movieList) {
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_layout,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie=movieList.get(position);
        int resID=holder.imageView.getResources().getIdentifier(movie.getImageName(),
                "drawable",holder.imageView.getContext().getPackageName());
        holder.imageView.setImageResource(resID);
        holder.title.setText(movie.getTitle());
        holder.genre.setText(movie.getGenre());
        holder.duration.setText(movie.getDuration());
        /*String test = "nigga";
        if(test.equals("nigga")){
            holder.layout.setBackgroundColor(Color.BLUE);
        }*/
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView imageView;
        public TextView title,genre,duration;
        public ConstraintLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            layout=itemView.findViewById(R.id.movie_layout);
            imageView=itemView.findViewById(R.id.imageView);
            title=itemView.findViewById(R.id.textView_title);
            genre=itemView.findViewById(R.id.textView_genre);
            duration=itemView.findViewById(R.id.textView_duration);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos=getAdapterPosition();
            Intent intent=new Intent(v.getContext(),SecondActivity.class);
            intent.putExtra("selected_movie",movieList.get(pos));
            ((Activity)v.getContext()).startActivityForResult(intent,999);
        }
    }
}
