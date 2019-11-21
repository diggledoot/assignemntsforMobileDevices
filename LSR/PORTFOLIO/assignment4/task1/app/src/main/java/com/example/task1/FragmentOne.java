package com.example.task1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;


public class FragmentOne extends Fragment{

    private ArrayList<Link> linkArrayList;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Context context;
    private OnFragmentInteractionListener mListener;

    public FragmentOne(Context context) {
        // Required empty public constructor
        this.context=context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        initMain(view);
        return view;
    }

    private void initMain(View view) {
        initRecycler(view);
    }

    private void initRecycler(View view) {
        linkArrayList=Link.createLinkList(view);
        ArrayList<Link> cyber = new ArrayList<>();
        for(int i=0;i<linkArrayList.size();i++){
            if(linkArrayList.get(i).getCategory().contains("cybersecurity")){
                cyber.add(linkArrayList.get(i));
            }
        }
        recyclerView = view.findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager staggeredGridLayoutManager =  new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerViewAdapter =  new RecyclerViewAdapter(context,cyber);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String fragment) {
        if (mListener != null) {
            mListener.onFragmentInteraction(fragment);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String fragment);
    }
}
