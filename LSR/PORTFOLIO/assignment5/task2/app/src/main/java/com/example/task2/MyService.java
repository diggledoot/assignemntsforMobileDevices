package com.example.task2;

import android.Manifest;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MyService extends Service {
    private Date currentTime;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String input;
    private ArrayList<String> stringArrayList;
    private String url = "http://10.0.2.2/assignment5/insert.php";
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("message","onStartCommand is called");
        input = (String)intent.getExtras().get("minute");
        Log.d("message",input);
        stringArrayList= new ArrayList<>();
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //Do write to database here
                if(MainActivity.count!=0){
                    currentTime = Calendar.getInstance().getTime();
                    final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-d HH:mm:ss");
                    Log.d("message",sdf.format(currentTime)+" "+"Long: "+location.getLongitude()+"  Lat: "+location.getLatitude());
                    //stringArrayList.add(sdf.format(currentTime)+" "+"Long: "+location.getLongitude()+"  Lat: "+location.getLatitude());
                    insertDB(sdf.format(currentTime)+" "+"Long: "+location.getLongitude()+"  Lat: "+location.getLatitude());
                    MainActivity.count--;
                }else{
                    Log.d("message","Nothing anymore :D");

                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return super.onStartCommand(intent, flags, startId);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000/*1000*60*Integer.parseInt(input)*/, 0, locationListener);

        return super.onStartCommand(intent, flags, startId);
    }
    private void insertDB(final String string){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                , new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("location",string);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("message","Service started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("message","Service stopped");
        if(locationManager!=null){
            stringArrayList.clear();
            locationManager.removeUpdates(locationListener);
            MainActivity.count=2;
        }
    }
}
