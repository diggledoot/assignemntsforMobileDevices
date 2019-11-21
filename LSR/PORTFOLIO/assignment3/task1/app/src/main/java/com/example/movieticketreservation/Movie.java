package com.example.movieticketreservation;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String title;
    private String genre;
    private String[] showtime;
    private String duration;
    private String imageName;




    public Movie(String title, String genre, String[] showtime, String duration,String imageName) {
        this.title = title;
        this.genre = genre;
        this.showtime = showtime;
        this.duration = duration;
        this.imageName=imageName;

    }

    protected Movie(Parcel in) {
        title = in.readString();
        genre = in.readString();
        showtime = in.createStringArray();
        duration = in.readString();
        imageName=in.readString();

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String[] getShowtime() {
        return showtime;
    }

    public void setShowtime(String[] showtime) {
        this.showtime = showtime;
    }
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }


    public static ArrayList<Movie> createMovieList(Context context){
        ArrayList<Movie> movieList=new ArrayList<>();

        movieList.add(new Movie("Avengers : Endgame","Action,Adventure,Sci-fi",context.getResources().getStringArray(R.array.avengers),"3 hours","avengers"));
        movieList.add(new Movie("47 Meters Down","Adventure,Drama,Horror",context.getResources().getStringArray(R.array.meters),"2 hours 30 minutes","meters"));
        movieList.add(new Movie("Spiderman : Far From Home","Action,Adventure,Sci-fi",context.getResources().getStringArray(R.array.spiderman),"2 hours 15 minutes","spiderman"));
        movieList.add(new Movie("Lion King","Animation,Adventure,Drama",context.getResources().getStringArray(R.array.lionking),"1 hour 45 minutes","lion_king"));
        movieList.add(new Movie("Fast and Furious : Hobbs and Shaw","Action,Adventure",context.getResources().getStringArray(R.array.hobbs),"2 hours","hobbs"));
        return movieList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(genre);
        dest.writeStringArray(showtime);
        dest.writeString(duration);
        dest.writeString(imageName);
    }
}
