package com.example.movieticketreservation;

import android.os.Parcel;
import android.os.Parcelable;

public class Reservation implements Parcelable {
    private String title;
    private String imageName;
    private String selected_time;
    private String selected_date;
    private String adults;
    private String child;

    protected Reservation(Parcel in) {
        title = in.readString();
        imageName = in.readString();
        selected_time = in.readString();
        selected_date = in.readString();
        adults = in.readString();
        child = in.readString();
    }

    public static final Creator<Reservation> CREATOR = new Creator<Reservation>() {
        @Override
        public Reservation createFromParcel(Parcel in) {
            return new Reservation(in);
        }

        @Override
        public Reservation[] newArray(int size) {
            return new Reservation[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getSelected_time() {
        return selected_time;
    }

    public void setSelected_time(String selected_time) {
        this.selected_time = selected_time;
    }

    public String getSelected_date() {
        return selected_date;
    }

    public void setSelected_date(String selected_date) {
        this.selected_date = selected_date;
    }

    public String getAdults() {
        return adults;
    }

    public void setAdults(String adults) {
        this.adults = adults;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }



    public Reservation(Movie movie) {
        this.title = movie.getTitle();
        this.imageName = movie.getImageName();
        this.selected_time = "";
        this.selected_date = "";
        this.adults = "";
        this.child = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(imageName);
        dest.writeString(selected_time);
        dest.writeString(selected_date);
        dest.writeString(adults);
        dest.writeString(child);
    }
}
