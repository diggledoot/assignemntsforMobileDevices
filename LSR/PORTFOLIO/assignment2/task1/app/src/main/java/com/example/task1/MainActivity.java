package com.example.task1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.Configuration;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btn_sneeze,btn_blow,btn_meds;
    private View inner,outer;
    static int state = 2;
    static int health=10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }
    private void sneeze(){
        final MediaPlayer mp=MediaPlayer.create(this,R.raw.sneeze2);
        mp.start();
    }
    private void blow(){
        final MediaPlayer mp=MediaPlayer.create(this,R.raw.blow_nose);
        mp.start();
    }
    private void meds(){
        final MediaPlayer mp=MediaPlayer.create(this,R.raw.slurp);
        mp.start();
    }
    private void initUI(){
        btn_sneeze=findViewById(R.id.button_sneeze);
        btn_blow=findViewById(R.id.button_blow);
        btn_meds=findViewById(R.id.button_take_medication);
        outer=findViewById(R.id.vertical_outer);
        inner=findViewById(R.id.viewLinearLayout);



        Log.d("health",String.valueOf(health));
        btn_sneeze.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                sneeze();
                state=0;
                health--;
                health();
            }
        });


        btn_blow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                blow();
                state=1;

            }
        });

        btn_meds.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(health>=10){
                    Log.d("message","Health is at >=10");
                }
                else{
                    meds();
                    if(health+2>10){
                        health++;
                        health();
                    }else {
                        health += 2;
                        health();
                    }
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(state==0){
            blow();
            state=1;
        }else{
            sneeze();
            state=0;
            health--;
            health();
        }

    }
    private void checkHealth(){
        if(health>5 && health<=7){
            outer.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
            inner.setBackgroundColor(ContextCompat.getColor(this,R.color.light_blue));
        }else if(health<=5 && health>0){
            outer.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            inner.setBackgroundColor(ContextCompat.getColor(this,R.color.red));
            btn_sneeze.setEnabled(true);
        }else if(health==0){
            Log.d("message","Health is empty, please take medication");
            health=0;
            btn_sneeze.setEnabled(false);
        }else{
            outer.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            inner.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        }
    }
    private void printHealth(){
        Log.d("health",String.valueOf(health));
    }
    private void health(){
        printHealth();
        checkHealth();
    }
}
