package com.example.bottommenu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public static boolean isLogin = false;
    public static FrameLayout homeFrame,loginFrame,signUpFrame,haircutFrame,appointmentFrame,complaintsFrame;
    private HomeFrag homeFrag;
    private haircut haircutFrag;
    private appointment appointmentFrag;
    private LoginFrag loginFrag;
    private SignUpFrag signUpFrag;
    private complaints complaintsFrag;
    private BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFrame=findViewById(R.id.home_frame);
        loginFrame=findViewById(R.id.log_In_frame);
        signUpFrame=findViewById(R.id.Sign_Up_frame);
        haircutFrame=findViewById(R.id.haircut_frame);
        appointmentFrame=findViewById(R.id.appointment_frame);
        complaintsFrame=findViewById(R.id.complaints_frame);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        begin();
    }
    private void begin(){
        homeFrag=new HomeFrag();
        loginFrag=new LoginFrag();
        signUpFrag=new SignUpFrag();
        haircutFrag=new haircut();
        appointmentFrag=new appointment();
        complaintsFrag=new complaints();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame,homeFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.log_In_frame,loginFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.Sign_Up_frame,signUpFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.haircut_frame,haircutFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.appointment_frame,appointmentFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.complaints_frame,complaintsFrag).commit();


        haircutFrame.setVisibility(View.INVISIBLE);
        homeFrame.setVisibility(View.INVISIBLE);
        loginFrame.setVisibility(View.VISIBLE);
        signUpFrame.setVisibility(View.INVISIBLE);
        appointmentFrame.setVisibility(View.INVISIBLE);
        complaintsFrame.setVisibility(View.INVISIBLE);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home&&isLogin){
                    homeFrame.setVisibility(View.VISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    haircutFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                }
                if(item.getItemId()==R.id.menu_appointment&&isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.VISIBLE);
                    haircutFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                }
                if(item.getItemId()==R.id.menu_complaints&&isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    haircutFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.VISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                }
                if(item.getItemId()==R.id.menu_logIn&&!isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    haircutFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.VISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                }
                if(item.getItemId()==R.id.menu_signUp&&!isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    haircutFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.VISIBLE);
                }
                return true;
            }
        });
    }
}