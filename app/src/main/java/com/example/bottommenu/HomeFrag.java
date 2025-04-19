package com.example.bottommenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HomeFrag extends Fragment implements Service_Adapter.ItemSelected{
    public static ArrayList<Service> services;
    private FloatingActionButton btnLogout;
    private TextView date;
    public HomeFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        date=view.findViewById(R.id.date);
        String CurrentDate;
        Calendar calender=Calendar.getInstance();
        CurrentDate= DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());
        date.setText(CurrentDate);
        btnLogout=view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogOut();
            }
        });
        initArray();
        return view;
    }
    private void LogOut(){
        FirebaseAuth.getInstance().signOut();
        MainActivity.loginFrame.setVisibility(View.VISIBLE);
        MainActivity.homeFrame.setVisibility(View.INVISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.haircutFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
        MainActivity.isLogin=false;
    }

    public static void initArray (){
        services = new ArrayList<>();
    }

    @Override
    public void onItemSelected(int index) {

    }

    @Override
    public void onItemClicked(int Index) {

    }
}