package com.example.task2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.ThumbnailUtils;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class GridAdapter extends BaseAdapter {
    Context context;
    private String links[];
    LayoutInflater layoutInflater;
    private final int THUMBNAIL_SIZE=250;
    View view;

    public GridAdapter(Context context,String[] links) {
        this.context = context;
        this.links=links;
    }

    @Override
    public int getCount() {
        return links.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.img_layout,null);
            final ImageView imageView = view.findViewById(R.id.imageView);
            ImageLoader imageLoader = MySingleton.getInstance(context.getApplicationContext()).getImageLoader();
            imageLoader.get(links[position], new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    Bitmap image = response.getBitmap();
                    Bitmap thumbnail = ThumbnailUtils.extractThumbnail(image,THUMBNAIL_SIZE,THUMBNAIL_SIZE);
                    imageView.setImageBitmap(thumbnail);
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getMessage());
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context.getApplicationContext(),SecondActivity.class);
                    intent.putExtra(MainActivity.KEY,links[position]);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }

}
