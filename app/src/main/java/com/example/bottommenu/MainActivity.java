package com.example.bottommenu;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    public static FrameLayout homeFrame, loginFrame, signUpFrame, appointmentFrame, complaintsFrame, managerFrame, hairCutFram;

    private HomeFrag homeFrag;
    public static manager managerFrag;
    private HairCut hairCutFrag;
    private appointment appointmentFrag;
    private LoginFrag loginFrag;
    private SignUpFrag signUpFrag;
    private complaints complaintsFrag;
    public static BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        homeFrame = findViewById(R.id.home_frame);
        loginFrame = findViewById(R.id.log_In_frame);
        signUpFrame = findViewById(R.id.Sign_Up_frame);
        hairCutFram = findViewById(R.id.hair_Cut_fram);
        appointmentFrame = findViewById(R.id.appointment_frame);
        complaintsFrame = findViewById(R.id.complaints_frame);
        managerFrame = findViewById(R.id.managerFrame);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        begin();
    }

    private void begin() {
        homeFrag = new HomeFrag();
        loginFrag = new LoginFrag();
        signUpFrag = new SignUpFrag();
        managerFrag = new manager();
        appointmentFrag = new appointment();
        complaintsFrag = new complaints();
        hairCutFrag = new HairCut();

        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame, homeFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.hair_Cut_fram, hairCutFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.managerFrame, managerFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.log_In_frame, loginFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.Sign_Up_frame, signUpFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.appointment_frame, appointmentFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.complaints_frame, complaintsFrag).commit();


        hairCutFram.setVisibility(View.INVISIBLE);
        homeFrame.setVisibility(View.INVISIBLE);
        loginFrame.setVisibility(View.VISIBLE);
        signUpFrame.setVisibility(View.INVISIBLE);
        appointmentFrame.setVisibility(View.INVISIBLE);
        complaintsFrame.setVisibility(View.INVISIBLE);
        managerFrame.setVisibility(View.INVISIBLE);

        bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(true);
        bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(true);
        bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
        bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_home) {
                    homeFrame.setVisibility(View.VISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                    managerFrame.setVisibility(View.INVISIBLE);
                    hairCutFram.setVisibility(View.INVISIBLE);
                }

                if (item.getItemId() == R.id.menu_appointment) {
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.VISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                    managerFrame.setVisibility(View.INVISIBLE);
                    hairCutFram.setVisibility(View.INVISIBLE);

                    //  تحميل البيانات من Firebase عند الدخول إلى صفحة المواعيد
                    if (appointmentFrag != null) {
                        appointmentFrag.reloadAppointments();
                    }
                }

                if (item.getItemId() == R.id.menu_complaints) {
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.VISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                    managerFrame.setVisibility(View.INVISIBLE);
                    hairCutFram.setVisibility(View.INVISIBLE);
                }

                if (item.getItemId() == R.id.menu_logIn) {
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.VISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);
                    managerFrame.setVisibility(View.INVISIBLE);
                    hairCutFram.setVisibility(View.INVISIBLE);
                }

                if (item.getItemId() == R.id.menu_signUp) {
                    homeFrame.setVisibility(View.INVISIBLE);
                    appointmentFrame.setVisibility(View.INVISIBLE);
                    complaintsFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.VISIBLE);
                    managerFrame.setVisibility(View.INVISIBLE);
                    hairCutFram.setVisibility(View.INVISIBLE);
                }
                return true;
            }
        });
    }
}
