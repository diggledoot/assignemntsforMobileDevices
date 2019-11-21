package com.example.assignment3task2;

import android.content.Context;
import android.gesture.GestureUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


public class FragmentOne extends Fragment implements GestureDetector.OnGestureListener{

    //RadioGroup
    private RadioGroup radioGroup;
    private boolean isProtein=false; //validate that a protein is picked
    private RadioButton radioButton;
    private String selected_meat="";//to be bundled

    //Fiber
    private String[] fiberID;
    private CheckBox[] checkBoxes_fiber;
    private ArrayList<String> selected_fiber=new ArrayList<>();//to be bundled
    private int selection=3;
    private int numofVeg=0;
    private boolean vegPromo=false;



    private OnFragmentInteractionListener mListener;

    private GestureDetector gestureDetector;

    public FragmentOne() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initMain(View view){
        initRadioGroup(view);
        initFiber(view);
    }

    private void initRadioGroup(final View view) {
        radioGroup=view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                isProtein=true;
                int id=radioGroup.getCheckedRadioButtonId();
                radioButton=((View)view).findViewById(id);
                String result=radioButton.getText().toString();
                selected_meat=result;
            }
        });
    }


    private void initFiber(View view){
        fiberID=view.getResources().getStringArray(R.array.fiber);
        checkBoxes_fiber=new CheckBox[fiberID.length];
        for(int i=0;i<fiberID.length;i++){
            int id=getResources().getIdentifier(fiberID[i],"id",view.getContext().getPackageName());
            checkBoxes_fiber[i]=view.findViewById(id);
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
    }
    private void vegChecked(){
        selection--;
        numofVeg++;
        checkSelection();
    }
    private void vegUnchecked(){
        selection++;
        numofVeg--;
        checkSelection();
    }
    private void checkSelection(){
        for(int i=0;i<checkBoxes_fiber.length;i++){
            if(selection==0){
                if(!checkBoxes_fiber[i].isChecked()){
                    checkBoxes_fiber[i].setClickable(false);
                    checkBoxes_fiber[i].setEnabled(false);
                }
            }else{
                checkBoxes_fiber[i].setClickable(true);
                checkBoxes_fiber[i].setEnabled(true);
            }
        }
    }


    private void reset(View view){

        //reset meat section
        radioGroup.setOnCheckedChangeListener(null);
        radioGroup.clearCheck();
        selected_meat="";

        //reset vegetables section
        selected_fiber=new ArrayList<>();
        selection=3;
        numofVeg=0;
        vegPromo=false;
        clearCheckBox();

        //re-init
        initRadioGroup(view);
        initFiber(view);
    }
    private void clearCheckBox(){
        for(int i=0;i<checkBoxes_fiber.length;i++){
            checkBoxes_fiber[i].setChecked(false);
            checkBoxes_fiber[i].setClickable(true);
            checkBoxes_fiber[i].setEnabled(true);
        }
    }
    private void getVeg(){
        for(int i=0;i<checkBoxes_fiber.length;i++){
            if(checkBoxes_fiber[i].isChecked()){
                selected_fiber.add(checkBoxes_fiber[i].getText().toString());
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view=inflater.inflate(R.layout.fragment_one,container,false);
        //GestureDetector
        gestureDetector=new GestureDetector(this);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(gestureDetector.onTouchEvent(event)){
                    if(isProtein){
                        //function to get array of selected veggies
                        getVeg();
                        onButtonPressed(selected_meat,selected_fiber);
                        reset(view);
                    }else{
                        Toast.makeText(getActivity(),"Pick a Protein to proceed",Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }
        });
        initMain(view);
        return view;
    }

    public void onButtonPressed(String meat,ArrayList<String> fiber) {
        if (mListener != null) {
            mListener.onFragmentInteraction(meat,fiber);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        boolean result=false;
        float diffX=e2.getX()-e1.getX();
        //diffX<0 means swipe right to left.
        if(diffX<0){
            result=true;
        }
        return result;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String meat,ArrayList<String> fiber);
    }
}
