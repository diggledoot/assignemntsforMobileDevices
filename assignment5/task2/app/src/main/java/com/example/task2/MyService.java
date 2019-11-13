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

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyService extends Service {
    private Date currentTime;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String input;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        input = (String)intent.getExtras().get("minute");
        Log.d("message",input);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("message","Service started");
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if(MainActivity.count!=0){
                    currentTime = Calendar.getInstance().getTime();
                    final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-d HH:mm:ss");
                    Log.d("message",sdf.format(currentTime)+" "+"Long: "+location.getLongitude()+"  Lat: "+location.getLatitude());
                    MainActivity.count--;
                }else{
                    Log.d("message","Its 0");
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
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000*10, 0, locationListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("message","Service stopped");
        if(locationManager!=null){
            locationManager.removeUpdates(locationListener);
            MainActivity.count=2;
        }
    }
}
