package com.example.task1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    SQLiteDatabase sqLiteDatabase;
    //DatabaseManager databaseManager;
    //Required variables
    ArrayList<Task> taskArrayList;
    Context context;

    //Date
    Date currentDate = Calendar.getInstance().getTime();
    Date selectedDate;


    public RecyclerViewAdapter(ArrayList<Task> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Task task =taskArrayList.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf_day= new SimpleDateFormat("d");
        SimpleDateFormat sdf_month = new SimpleDateFormat("MMM");
        try {
            String temp = task.getDue_date();
            selectedDate = sdf.parse(temp);
            //Log.d("after parse date",String.valueOf(selectedDate));
            holder.day.setText(sdf_day.format(selectedDate));
            holder.month.setText(sdf_month.format(selectedDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //Status icon and duration initialization
        statusCheck(holder);
        holder.task_title.setText(task.getTitle());
        if(task.getPriority().equals("1")){
            int resID=context.getResources().getColor(R.color.colorImportant);
            holder.cardView.setBackgroundColor(resID);
        }
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                //Dialog
                DeleteDialog deleteDialog = new DeleteDialog(context,task.getId(),position);
                deleteDialog.show(((FragmentActivity)context).getSupportFragmentManager()
                        ,"Delete Dialog Fragment");
                return false;
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //to edit task page,send as parcelable
                Intent intent = new Intent(context,EditTask.class);
                intent.putExtra("edit_task",taskArrayList.get(position));
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });
        holder.pending_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(context,"I am completed",Toast.LENGTH_SHORT).show();
                //databaseManager =  new DatabaseManager(context);
                sqLiteDatabase=context.openOrCreateDatabase(MainActivity.DATABASE_NAME,Context.MODE_PRIVATE,null);
                String sql = "UPDATE tasks SET completed='1' WHERE id="+taskArrayList.get(position).getId();
                sqLiteDatabase.execSQL(sql);
                //Log.d("completed?",taskArrayList.get(position).getCompleted());
                taskArrayList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,taskArrayList.size());
            }
        });
    }

    private void statusCheck(ViewHolder holder){
        long curdate=currentDate.getTime();
        long seldate=selectedDate.getTime();
        float result = (float)(seldate-curdate)/(24*60*60*1000)+1;
        String test = String.format("%.2f",result);
        String[] split = test.split("\\.");
        int days = Integer.parseInt(split[0]);
        split[1]="0."+split[1];
        float hours = Float.parseFloat(split[1]);
        hours = hours*24;
        int hr= (int)hours+1;
        if(result<0){
            int resID= context.getResources().getIdentifier("clipboard_red1"
                    ,"drawable"
                    ,context.getPackageName());
            holder.status_icon.setImageResource(resID);
            holder.duration.setText("0 hours");
            holder.day.setTextColor(Color.RED);
            holder.month.setTextColor(Color.RED);
            holder.task_title.setTextColor(Color.RED);

        }else if(days==0){
            int resID= context.getResources().getIdentifier("clipboard_orange1"
                    ,"drawable"
                    ,context.getPackageName());
            holder.status_icon.setImageResource(resID);
            holder.duration.setText(days+" days and "+hr+" hours");

        }else{
            int resID= context.getResources().getIdentifier("clipboard_blue1"
                    ,"drawable"
                    ,context.getPackageName());
            holder.status_icon.setImageResource(resID);
            holder.duration.setText(days+" days and "+hr+" hours");
        }
    }
    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //CardView
        private CardView cardView;
        //TextView
        private TextView day,month,task_title,duration;
        //ImageView
        private ImageView status_icon;
        //Switch
        private Switch pending_switch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView=itemView.findViewById(R.id.cardView);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
            task_title=itemView.findViewById(R.id.task_title);
            duration=itemView.findViewById(R.id.duration);
            status_icon=itemView.findViewById(R.id.status_icon);
            pending_switch=itemView.findViewById(R.id.pending_switch);
        }
    }

}
