package com.example.bottommenu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignUpFrag extends Fragment {
private TextInputEditText user_name,name,phone,email,password,confirm_password;
private Button sign_up,cancel;
private FirebaseAuth mAuth;
private FirebaseFirestore db;

    public SignUpFrag() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        name=view.findViewById(R.id.Name);
        user_name=view.findViewById(R.id.User_Name);
        phone=view.findViewById(R.id.phone);
        sign_up=view.findViewById(R.id.Sign_Up);
        email=view.findViewById(R.id.Email);
        password=view.findViewById(R.id.Password);
        cancel=view.findViewById(R.id.cancel);
        confirm_password=view.findViewById(R.id.Confirm_Password);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.loginFrame.setVisibility(View.VISIBLE);
                MainActivity.homeFrame.setVisibility(View.INVISIBLE);
                MainActivity.dashFrame.setVisibility(View.INVISIBLE);
                MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }
    private void check(){
        if(user_name.getText().toString().isEmpty()){
            Toast.makeText(getActivity(),"please fill the user name",Toast.LENGTH_SHORT).show();
        }
        else{
            if(name.getText().toString().isEmpty()){
                Toast.makeText(getActivity(),"please fill the name",Toast.LENGTH_SHORT).show();
            }
            else{
                if(phone.getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"please fill the phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(email.getText().toString().isEmpty()){
                        Toast.makeText(getActivity(),"please fill the email",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if(password.getText().toString().isEmpty()){
                            Toast.makeText(getActivity(),"please fill the password",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(confirm_password.getText().toString().isEmpty()){
                                Toast.makeText(getActivity(),"please confirm the password",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                signUp();
                            }
                        }
                    }
                }
            }
        }
    }
    private void signUp(){
        if(name.getText().toString().length()>=4){
            if(check_phone()){
                if(check_email()){
                    if(check_password()){
                        if((password.getText().toString()).equals(confirm_password.getText().toString())){
                            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString());
                                        Toast.makeText(getActivity(),"sign up successfully",Toast.LENGTH_SHORT).show();
                                        addUserToFireStore();
                                        updateUI();
                        }
                        else{
                            Toast.makeText(getActivity(),"password is not match",Toast.LENGTH_SHORT).show();

                        }
                    }
                }
                else{
                    Toast.makeText(getActivity(),"invalid email",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(getActivity(),"phone number must be 10 digits",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getActivity(),"name must be at least 4 digits",Toast.LENGTH_SHORT).show();
        }
    }
    private boolean check_phone() {
        char[] array = phone.getText().toString().toCharArray();
        boolean x = true;
        if(array.length != 10){
            return false;
        }
        else{
            for (int i = 0; i < array.length; i++) {
                if (!Character.isDigit(array[i]) )
                    x = false;
            }
        }
        return x;
    }
    private boolean check_email(){
        if(Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            return true;
        }
        return false;
    }
    private void addUserToFireStore(){
        com.example.bottommenu.User user=new User(name.getText().toString(),user_name.getText().toString(),phone.getText().toString(),email.getText().toString());
        db.collection("users").document(user.getEmail()).set(user);
        name.setText(null);
        phone.setText(null);
        email.setText(null);
        password.setText(null);
        confirm_password.setText(null);


    }
    private void updateUI(){
        MainActivity.loginFrame.setVisibility(View.VISIBLE);
        MainActivity.homeFrame.setVisibility(View.INVISIBLE);
        MainActivity.dashFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
    }
    private boolean check_password(){
            char [] array=password.getText().toString().toCharArray();
            boolean capital=false;
            boolean small=false;
            boolean number=false;
        if(password.getText().toString().length() <6) {
            Toast.makeText(getActivity(), "password must be at least 6 digits", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            for(int i=0;i<array.length;i++){
                if(Character.isUpperCase(array[i]))
                    capital=true;
                if(Character.isLowerCase(array[i]))
                    small=true;
                if(Character.isDigit(array[i]))
                    number=true;
            }
            if(capital && small && number)
                return true;
        }
        if(!capital){
            Toast.makeText(getActivity(),"password must have a capital letter",Toast.LENGTH_SHORT).show();
        }
        else{
            if(!small){
                Toast.makeText(getActivity(),"password must have a small letter",Toast.LENGTH_SHORT).show();
            }
            else {
                if(!number){
                    Toast.makeText(getActivity(),"password must have a number",Toast.LENGTH_SHORT).show();
                }
            }
        }
            return false;
    }
}