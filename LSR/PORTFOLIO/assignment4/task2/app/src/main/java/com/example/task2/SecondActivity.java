package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

public class SecondActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        imageView=findViewById(R.id.imageView2);
        Bundle bundle = getIntent().getExtras();
        String link = bundle.getString(MainActivity.KEY);
        ImageLoader imageLoader = MySingleton.getInstance(getApplicationContext()).getImageLoader();
        imageLoader.get(link, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                Bitmap bitmap = response.getBitmap();
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
