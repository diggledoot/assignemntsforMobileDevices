package com.example.task1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatusRecyclerViewAdapter  extends RecyclerView.Adapter<StatusRecyclerViewAdapter.ViewHolder>{
    //Required variables
    ArrayList<Task> taskArrayList;
    Context context;

    //Date
    Date selectedDate;

    public StatusRecyclerViewAdapter(ArrayList<Task> taskArrayList, Context context) {
        this.taskArrayList = taskArrayList;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.completed_task_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Task task = taskArrayList.get(position);
        holder.title.setText(task.getTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat sdf_format=new SimpleDateFormat("d-MMM-yy");
        try {
            String temp = task.getDue_date();
            selectedDate = sdf.parse(temp);
            Date currentDate = Calendar.getInstance().getTime();
            long curdate=currentDate.getTime();
            long seldate=selectedDate.getTime();
            float result = (float)(seldate-curdate)/(24*60*60*1000)+1;
            holder.date.setText(sdf_format.format(selectedDate));
            if(result<0){
                holder.aSwitch.setText("Not Completed");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(task.getCompleted().equals("0")){
            holder.aSwitch.setChecked(true);
        }

    }

    @Override
    public int getItemCount() {
        return taskArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,date;
        private Switch aSwitch;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title_completed);
            date = itemView.findViewById(R.id.datetitle_completed);
            aSwitch=itemView.findViewById(R.id.switch_completed);
        }
    }
}
