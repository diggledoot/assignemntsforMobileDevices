package com.example.task1;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<Link> links;
    private Context context;

    public RecyclerViewAdapter(Context context, ArrayList<Link> links) {
        this.context=context;
        this.links = links;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout,
                parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Link link=links.get(position);
        int resID = holder.image.getResources().getIdentifier(link.getImageName(),
                "drawable",holder.image.getContext().getPackageName());
        holder.image.setImageResource(resID);
        holder.title.setText(link.getTitle());

    }

    @Override
    public int getItemCount() {
        return links.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView image;
        public TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.image);
            title=itemView.findViewById(R.id.title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(v.getContext(),title.getText().toString(),Toast.LENGTH_SHORT).show();
            int pos= getAdapterPosition(); //should pass the URL into webview in fragment three
            if(links.get(pos).getCategory().contains("cybersecurity")){
                FragmentThree fragmentThree =  new FragmentThree(links.get(pos).getURL());
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_one,fragmentThree)
                        .addToBackStack(null)
                        .commit();
            }else{
                FragmentThree fragmentThree =  new FragmentThree(links.get(pos).getURL());
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_two,fragmentThree)
                        .addToBackStack(null)
                        .commit();
            }

                /*FragmentThree fragmentThree =  new FragmentThree(links.get(pos).getURL());
                ((FragmentActivity)context).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main,fragmentThree)
                        .addToBackStack(null)
                        .commit();*/



        }
    }
}
