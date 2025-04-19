package com.example.bottommenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFrag extends Fragment {
    private TextInputEditText txtEmail,txtPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;

    public LoginFrag(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        mAuth=FirebaseAuth.getInstance();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        txtEmail=view.findViewById(R.id.et_Name);
        txtPassword=view.findViewById(R.id.et_Pass);
        btnLogin=view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkManager();
            }
        });
        return view;
    }

    private void checkManager(){
        String email, password;
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        if(password.equals("00000000") && email.equals("manager@gmail.com")){
            MainActivity.loginFrame.setVisibility(View.INVISIBLE);
            MainActivity.homeFrame.setVisibility(View.INVISIBLE);
            MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
            MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
            MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
            MainActivity.haircutFrame.setVisibility(View.INVISIBLE);
            MainActivity.managerFrame.setVisibility(View.VISIBLE);
            MainActivity.isManager=true;
            txtEmail.setText(null);
            txtPassword.setText(null);
            MainActivity.isLogin=true;
        }
        else{
            checkEmailPass();
        }
    }
    private void checkEmailPass () {
        String email, password;
        email = txtEmail.getText().toString();
        password = txtPassword.getText().toString();
        if (!(password.equals("") || email.equals(""))) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "✅ Login successful!", Toast.LENGTH_SHORT).show();
                        updateUI();
                    } else {
                        Toast.makeText(getActivity(), "❌ Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(getActivity(), "⚠️ Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateUI(){
        MainActivity.loginFrame.setVisibility(View.INVISIBLE);
        MainActivity.homeFrame.setVisibility(View.VISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.haircutFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
        txtEmail.setText(null);
        txtPassword.setText(null);
        MainActivity.isLogin=true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            updateUI();
        }
    }
}