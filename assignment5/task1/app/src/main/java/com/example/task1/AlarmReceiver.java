package com.example.task1;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    //Required variables to check if it is one day before due_date, if true, notification
    Date currentDate;
    DatabaseManager databaseManager;
    NotificationManagerCompat notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("message","Accessed Alarm Receiver");
        notificationManager = NotificationManagerCompat.from(context);
        currentDate= Calendar.getInstance().getTime();
        databaseManager = new DatabaseManager(context);
        Cursor cursor = databaseManager.loadTasks();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if(cursor.moveToFirst()){
            do{
                try {
                    Date due_date = sdf.parse(cursor.getString(2));
                    long curDate=currentDate.getTime();
                    long selDate=due_date.getTime();
                    int result=(int)((selDate-curDate)/(24*60*60*1000))+1;
                    if(result==1 && cursor.getString(5).equals("0")){
                        Log.d("message",cursor.getString(1));
                        Notification notification = new NotificationCompat.Builder(context, App.CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.home)
                                .setContentTitle("Due Soon!")
                                .setContentText(cursor.getString(1))
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                .build();

                        notificationManager.notify(cursor.getInt(0), notification);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }while(cursor.moveToNext());

        }
    }

}
