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
    private HomeFrag homeFrag;
    private DashFrag dashFrag;
    public static boolean isLogin = false;
    private LoginFrag loginFrag;
    private SignUpFrag signUpFrag;
    public static FrameLayout homeFrame,dashFrame,loginFrame,signUpFrame;
    private BottomNavigationView bottomNavigationView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        homeFrame=findViewById(R.id.home_frame);
        dashFrame=findViewById(R.id.dash_frame);
        loginFrame=findViewById(R.id.login_frame);
        signUpFrame=findViewById(R.id.signup_frame);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        begin();
    }
    private void begin(){
        homeFrag=new HomeFrag();
        dashFrag=new DashFrag();
        loginFrag=new LoginFrag();
        signUpFrag=new SignUpFrag();
        getSupportFragmentManager().beginTransaction().replace(R.id.home_frame,homeFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.dash_frame,dashFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.login_frame,loginFrag).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.signup_frame,signUpFrag).commit();

        //hiding dash and login Fragments
        dashFrame.setVisibility(View.INVISIBLE);
        homeFrame.setVisibility(View.INVISIBLE);
        loginFrame.setVisibility(View.VISIBLE);
        signUpFrame.setVisibility(View.INVISIBLE);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.menu_home&&isLogin){
                    homeFrame.setVisibility(View.VISIBLE);
                    dashFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);


                }
                if(item.getItemId()==R.id.menu_dashboard&&isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    dashFrame.setVisibility(View.VISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);

                }
                if(item.getItemId()==R.id.menu_login&&!isLogin){
                    homeFrame.setVisibility(View.INVISIBLE);
                    dashFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.VISIBLE);
                    signUpFrame.setVisibility(View.INVISIBLE);

                }

                if(item.getItemId()==R.id.menu_signup){
                    homeFrame.setVisibility(View.INVISIBLE);
                    dashFrame.setVisibility(View.INVISIBLE);
                    loginFrame.setVisibility(View.INVISIBLE);
                    signUpFrame.setVisibility(View.VISIBLE);


                }
                return true;
            }
        });
    }
}