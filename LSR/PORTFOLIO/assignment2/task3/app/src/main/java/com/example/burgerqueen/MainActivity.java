package com.example.burgerqueen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity{
    //Spinner fields and related variables
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    String[] size;
    static String sizeDefault="Small";

    //Button UI variables
    private Button order,reset;
    private RadioGroup radio_group;
    private RadioButton radiobutton;

    //Total price variables
    private TextView price;
    static double initialPrice=0.00;

    //Meat section variables
    static Boolean isMeatPicked=false;
    static double meat_price; //used to change back total price if meat is unpicked
    // and apply new price for new meat selected
    static Boolean isProteinPicked=false; //check if meat section is selection, it is required

    //Vegetable section variables
    //static String[] checkBox_ID={"lettuce","tomato","pickle","onion","cheese","mayo","mustard"};
    static String[] checkBox_fiber_ID;
    static String[] checkBox_junk_ID;
    //static CheckBox[] checkBoxes=new CheckBox[checkBox_ID.length];
    static CheckBox[] checkBoxes_fiber;
    static CheckBox[] checkBoxes_junk;
    static int selection=3;
    static int numOfVeg=0;
    static boolean vegPromo=false;/*vegPromo is used to check if promo is hit*/

    //Hashmaps
    private HashMap<String,Double> meat;
    private HashMap<String,Double> fiber;
    private HashMap<String,Double> junk;
    private HashMap<String,Double> sizeHash;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Call initMain to initialize all UI elements
        initMain();
    }
    private void initMain(){
        //Initialization individual UI elements and systems of the program.
        initPrice();
        initSpinner();
        initRadioGroup();
        initCheckBox();
        initButton();
    }
    private void initPrice(){
        price=findViewById(R.id.total);
        price.setText(String.format("RM%.2f",initialPrice));

        //HashMap Meat
        meat=new HashMap<>();
        meat.put("Beef",4.50);
        meat.put("Fish",4.00);
        meat.put("Chicken",3.00);
        meat.put("Egg",2.00);

        //HashMap Fiber
        fiber=new HashMap<>();
        fiber.put("Veg",0.50);

        //HashMap Junk
        junk=new HashMap<>();
        junk.put("Cheese",1.00);
        junk.put("Mayonaise",0.50);
        junk.put("Mustard",0.70);

        //HashMap Size
        sizeHash=new HashMap<>();
        sizeHash.put("Regular",1.2);
        sizeHash.put("Small",1.0);
        sizeHash.put("Large",1.3);
        sizeHash.put("Gigantic",1.5);



    }
    private void initButton(){
        order=findViewById(R.id.order);
        reset=findViewById(R.id.reset);

        //ClickListener
        order.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!isProteinPicked){
                    Toast.makeText(MainActivity.this,"Please select a meat",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Your total is "+String.format("RM%.2f",initialPrice), Toast.LENGTH_SHORT).show();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Reset radio_group
                radio_group.setOnCheckedChangeListener(null);
                radio_group.clearCheck();
                isMeatPicked=false;
                meat_price=0.00;
                isProteinPicked=false;

                //Reset price
                initialPrice=0.00;
                updatePrice();

                //Reset vegetable section
                selection=3;
                vegPromo=false;
                clearCheckBox();

                //Reset spinner
                sizeDefault="Small";
                spinner.setSelection(0);
                spinner.setClickable(true);
                spinner.setEnabled(true);
                numOfVeg=0;

                //Reinitialize
                initMain();
            }
        });
    }



    private void initCheckBox(){
        //initialize fiber and junk checkbox arrays(testing)
        checkBox_fiber_ID=getResources().getStringArray(R.array.fiber);
        checkBox_junk_ID=getResources().getStringArray(R.array.junk);
        checkBoxes_fiber=new CheckBox[checkBox_fiber_ID.length];
        checkBoxes_junk=new CheckBox[checkBox_junk_ID.length];
        for(int i=0;i<checkBox_fiber_ID.length;i++){
            int temp=getResources().getIdentifier(checkBox_fiber_ID[i],"id",getPackageName());
            checkBoxes_fiber[i]=findViewById(temp);
            checkBoxes_fiber[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(((CheckBox) v).isChecked()){
                        vegChecked();
                    }else{
                        vegUnchecked();
                    }
                }
            });
        }
        for(int i=0;i<checkBox_junk_ID.length;i++){
            int temp=getResources().getIdentifier(checkBox_junk_ID[i],"id",getPackageName());
            checkBoxes_junk[i]=findViewById(temp);
            checkBoxes_junk[i].setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if(((CheckBox)v).isChecked()){
                        Double temp=junk.get(((CheckBox) v).getText().toString());
                        if(temp!=null){
                            initialPrice+=temp;
                            updatePrice();
                        }

                    }else{
                        Double temp=junk.get(((CheckBox) v).getText().toString());
                        if(temp!=null){
                            initialPrice-=temp;
                            updatePrice();
                        }
                    }
                }
            });
        }
    }
    private void initRadioGroup(){
        radio_group=findViewById(R.id.meat_rg);
        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                isProteinPicked=true;
                int id=radio_group.getCheckedRadioButtonId();
                radiobutton=findViewById(id);
                String result=radiobutton.getText().toString();
                //check() is used to check for the meat selected and apply the price
                // or detract the price accordingly
                check(result);
                //isMeatPicked is to confirm that selection has been done before, this is to allow reselection
                isMeatPicked=true;
            }
        });

    }
    private void initSpinner(){
        size=getResources().getStringArray(R.array.sizes);
        spinner=findViewById(R.id.spinner);
        adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,size);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sizeDefault=spinner.getSelectedItem().toString();
                //checkSize() is used to check the size selected and apply
                // the appropriate percentage of increase to the total price
                checkSize();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //if nothing is selected, this is too ensure that the default size is "Small"
                sizeDefault="Small";
            }
        });

    }


    private void check(String result){
        //if true, it will subtract meat_price from the initialPrice
        // to revert the totalPrice to 0.00 and apply the new price based on the meat selected.
        if(isMeatPicked){
            initialPrice-=meat_price;
            initialPrice+=meat.get(result);
            meat_price=meat.get(result);
            updatePrice();
        }else {
            //meat_price is used to subtract later on if isMeatPicked is true.
            initialPrice+=meat.get(result);
            meat_price=meat.get(result);
            updatePrice();
        }
    }
    private void updatePrice(){
        price.setText(String.valueOf(String.format("RM%.2f",initialPrice)));
    }

    private void checkSize(){
        //checks for the size selected, and apply the correct percentage for price increase.
        for(int i=0;i<size.length;i++){
            if(sizeDefault.equals("Small")){
                continue;
            }else{
                initialPrice*=sizeHash.get(sizeDefault);
                spinner.setClickable(false);
                spinner.setEnabled(false);
                updatePrice();
                break;
            }
        }

    }
    private void clearCheckBox(){
        for(int i=0;i<checkBoxes_fiber.length;i++){
            checkBoxes_fiber[i].setChecked(false);
            checkBoxes_fiber[i].setClickable(true);
            checkBoxes_fiber[i].setEnabled(true);
        }
        for(int i=0;i<checkBoxes_junk.length;i++){
            checkBoxes_junk[i].setChecked(false);
            checkBoxes_junk[i].setClickable(true);
            checkBoxes_junk[i].setEnabled(true);
        }
    }

    private void checkSelection(){
        for(int i=0;i<checkBoxes_fiber.length;i++){
            if(selection==0){
                if(!checkBoxes_fiber[i].isChecked()){
                    checkBoxes_fiber[i].setClickable(false);
                    checkBoxes_fiber[i].setEnabled(false);
                }
            }else{
                checkBoxes_fiber[i].setEnabled(true);
                checkBoxes_fiber[i].setClickable(true);
            }
        }
    }

    private void checkVeg(){
        if(numOfVeg==3){
            vegPromo=true;
            initialPrice-=0.50;
            updatePrice();
        }
        else{
            if(vegPromo){
                initialPrice+=0.50;
                vegPromo=false;
                updatePrice();
            }
        }
    }
    private void vegChecked(){
        initialPrice+=fiber.get("Veg");
        updatePrice();
        selection--;
        numOfVeg++;
        checkSelection();
        checkVeg();
    }
    private void vegUnchecked(){
        initialPrice-=fiber.get("Veg");
        updatePrice();
        selection++;
        numOfVeg--;
        checkSelection();
        checkVeg();
    }
}