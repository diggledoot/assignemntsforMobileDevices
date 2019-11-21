package com.example.assignment3task2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class FragmentTwo extends Fragment {

    private static final String ARG_PARAM1 = "selected_meat";
    private static final String ARG_PARAM2 = "selected_fiber";


    // TODO: Rename and change types of parameters
    private String selected_meat;
    private ArrayList<String> selected_fiber;
    private String temp;
    private Double initialPrice=0.0;


    //Junk
    private String[] junkID;
    private CheckBox[] checkBoxes_junk;
    private String selected_junk=""; //implement an update method to dynamically change the summary

    //Size
    private boolean isSize=false;
    static private double selected_size;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String sizeDefault="Small";
    private String[] size;

    //HashMap
    private HashMap<String,Double> junk;
    private HashMap<String,Double> sizeHash;
    private HashMap<String,Double> meatHash; //placed in second fragment
    private HashMap<String,Double> fiberHash; //placed in second fragment

    //widgets
    private TextView summary,price;
    private String initialSummary="";

    public FragmentTwo() {
        // Required empty public constructor
    }


    public static FragmentTwo newInstance(String param1, ArrayList<String> param2) {
        FragmentTwo fragment = new FragmentTwo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putStringArrayList(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selected_meat = getArguments().getString(ARG_PARAM1);
            selected_fiber = getArguments().getStringArrayList(ARG_PARAM2);
        }
        initHashMap();
        initPrice();
    }
    private void initMain(View view,ViewGroup container){
        price=view.findViewById(R.id.price);
        initSummary(view);
        updatePrice();
        initJunk(view);
        initSpinner(view,container);
    }
    private void initHashMap(){
        //junk
        junk=new HashMap<>();
        junk.put("Cheese",1.00);
        junk.put("Mayonaise",0.50);
        junk.put("Mustard",0.70);

        //size
        sizeHash=new HashMap<>();
        sizeHash.put("Regular",1.2);
        sizeHash.put("Small",1.0);
        sizeHash.put("Large",1.3);
        sizeHash.put("Gigantic",1.5);

        //meat
        meatHash=new HashMap<>();
        meatHash.put("beef",4.50);
        meatHash.put("chicken",3.00);
        meatHash.put("fish",4.00);
        meatHash.put("egg",2.00);

        //fiber
        fiberHash=new HashMap<>();
        fiberHash.put("veg",0.50);
    }
    private void initPrice(){
        initialPrice+=meatHash.get(selected_meat);
        double temp=fiberHash.get("veg");
        switch(selected_fiber.size()){
            case 0:
                break;
            case 1:
                initialPrice+=temp;
                break;
            case 2:
                initialPrice+=(temp*2);
                break;
            case 3:
                initialPrice+=(temp*2);
                break;
        }
    }
    private void updateSummary(){
        if(selected_fiber.size()!=0){
            initialSummary=selected_meat+" with "+temp+" top up with "+selected_junk;
        }else{
            initialSummary=selected_meat+" top up with "+selected_junk;
        }
        summary.setText(initialSummary);
    }
    private void initSummary(View view){
        summary=view.findViewById(R.id.summary);
        getVeg();
        if(selected_fiber.size()!=0){
            initialSummary=selected_meat+" with "+temp+" top up with ";
        }else{
            initialSummary=selected_meat+" top up with ";
        }
        summary.setText(initialSummary);
    }
    private void initJunk(View view){
        junkID=view.getResources().getStringArray(R.array.junk);
        checkBoxes_junk=new CheckBox[junkID.length];
        for(int i=0;i<junkID.length;i++){
            int id=getResources().getIdentifier(junkID[i],"id",view.getContext().getPackageName());
            checkBoxes_junk[i]=view.findViewById(id);
            checkBoxes_junk[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        checkChanges();
                        getJunk();
                        updateSummary();

                    }else{
                        checkChanges();
                        getJunk();
                        updateSummary();
                    }
                }
            });
        }
    }
    private void getJunk(){
        ArrayList<String> temp=new ArrayList<>();
        selected_junk="";
        for(int i=0;i<checkBoxes_junk.length;i++){
            if(checkBoxes_junk[i].isChecked()){
                temp.add(checkBoxes_junk[i].getText().toString());
            }
        }
        for(int i=0;i<temp.size();i++){
            if(i!=temp.size()-1){
                selected_junk+=temp.get(i)+",";
            }else{
                selected_junk+=temp.get(i);
            }
        }
        selected_junk=selected_junk.toLowerCase();
    }
    private void getVeg(){
        temp="";
        for(int i=0;i<selected_fiber.size();i++){
            if(i!=selected_fiber.size()-1){
                temp+=selected_fiber.get(i)+",";
            }else{
                temp+=selected_fiber.get(i);
            }
        }

    }


    private void initSpinner(View view,ViewGroup container){
        size=view.getResources().getStringArray(R.array.sizes);
        spinner=view.findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(container.getContext(),android.R.layout
                .simple_spinner_item,size);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sizeDefault=spinner.getSelectedItem().toString();
                checkChanges();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sizeDefault="Small";
            }
        });
    }

    private void updatePrice(){
        price.setText(String.format("RM%.2f",initialPrice));
    }

    private void checkChanges(){
        initialPrice=0.0;
        initPrice();
        for(int i=0;i<checkBoxes_junk.length;i++){
            if(checkBoxes_junk[i].isChecked()){
                initialPrice+=junk.get(checkBoxes_junk[i].getText().toString());
            }
        }
        initialPrice*=sizeHash.get(sizeDefault);
        updatePrice();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_two, container, false);
        initMain(view,container);
        return view;
    }
}
