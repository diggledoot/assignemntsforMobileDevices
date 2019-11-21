package com.example.assignment3task2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements  FragmentOne.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentOne fragmentOne= new FragmentOne();
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.layout,fragmentOne).addToBackStack("true").commit();

    }


    //this here is the interface
    @Override
    public void onFragmentInteraction(String meat, ArrayList<String> fiber) {
        FragmentTwo fragment=FragmentTwo.newInstance(meat,fiber);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout,fragment)
                .addToBackStack(null)
                .commit();
    }
}
