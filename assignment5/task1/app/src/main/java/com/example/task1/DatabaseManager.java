package com.example.task1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.PriorityQueue;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="taskDB";
    private static final int DATABASE_VERSION =1;
    private static final String TABLE_NAME = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DUE_DATE = "due_date";
    private static final String COLUMN_DETAILS = "details";
    private static final String COLUMN_PRIORITY = "priority";
    private static final String COLUMN_COMPLETED = "completed";


    public DatabaseManager(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (\n" +
                "    "+COLUMN_ID+" integer NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "    "+COLUMN_TITLE+" varchar(250) NOT NULL,\n" +
                "    "+COLUMN_DUE_DATE+" DATETIME NOT NULL,\n" +
                "    "+COLUMN_DETAILS+" varchar(250) ,\n" +
                "    "+COLUMN_PRIORITY+" varchar(1) NOT NULL,\n" +
                "    "+COLUMN_COMPLETED+" varchar(1) NOT NULL\n" +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    boolean addTask(String title,String due_date,String details,String priority,String completed){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DUE_DATE,due_date);
        cv.put(COLUMN_DETAILS,details);
        cv.put(COLUMN_PRIORITY,priority);
        cv.put(COLUMN_COMPLETED,completed);
        sqLiteDatabase.insert(TABLE_NAME,null,cv);
        return true;
    }

    Cursor loadTasks(){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" ORDER BY "+COLUMN_DUE_DATE+" ASC",null);
    }

    boolean updateTask(String title,String due_date,String details, String priority,String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE,title);
        cv.put(COLUMN_DUE_DATE,due_date);
        cv.put(COLUMN_DETAILS,details);
        cv.put(COLUMN_PRIORITY,priority);
        sqLiteDatabase.update(TABLE_NAME,cv,COLUMN_ID+"=?",new String[]{id});
        return true;
    }
    boolean updateTask(String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_COMPLETED,"1");
        sqLiteDatabase.update(TABLE_NAME,cv,COLUMN_ID+"=?",new String[]{id});
        return true;
    }

    boolean deleteTask(String id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID+"=?",new String[]{id});
        return true;
    }
}
