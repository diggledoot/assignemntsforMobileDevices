package com.example.task2;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class MySingleton {
    private static MySingleton mySingleton;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public MySingleton(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String,Bitmap> cache = new LruCache<>(3);

            @Override
            public Bitmap getBitmap(String url) {
                Bitmap bmp = cache.get(url);
                if(bmp==null){
                    System.out.println("Image not in cache");
                }else{
                    System.out.println("Image is in cache");
                }
                return bmp;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                System.out.println("Put image in cache");
                cache.put(url,bitmap);
            }
        });
    }
    public static synchronized MySingleton getInstance(Context context){
        if(mySingleton==null){
            mySingleton = new MySingleton(context);
        }
        return mySingleton;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
