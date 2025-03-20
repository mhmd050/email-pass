package com.example.bottommenu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
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
private TextInputEditText family,name,phone,email,password,confirm_password;
private Button sign_up;
private FirebaseAuth mAuth;
private FirebaseFirestore db;

    public SignUpFrag() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        name = view.findViewById(R.id.Name);
        family = view.findViewById(R.id.family);
        phone = view.findViewById(R.id.phone);
        sign_up = view.findViewById(R.id.Sign_Up);
        email = view.findViewById(R.id.Email);
        password = view.findViewById(R.id.Password);
        confirm_password = view.findViewById(R.id.Confirm_Password);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
        return view;
    }
    private void signUp(){
        if(check_name()){
            if (check_family()) {
                if(check_phone()){
                    if(check_email()){
                        if(check_password()){
                            if((password.getText().toString()).equals(confirm_password.getText().toString())){
                                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(getActivity(),"✅ Account created successfully!",Toast.LENGTH_LONG).show();
                                            addUserToFireStore();
                                            updateUI();
                                        }
                                        else{
                                            Toast.makeText(getActivity(),"❌ Registration failed. Please try again.",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                            else{
                                Toast.makeText(getActivity(),"⚠️ Passwords do not match. Please re-enter.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }

    }
    private boolean check_phone() {
        if (phone.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phone.getText().toString().matches("^\\d{10}$")) {
            Toast.makeText(getActivity(), "❌ Phone number must be exactly 10 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phone.getText().toString().startsWith("05")) {
            Toast.makeText(getActivity(), "❌ Phone number must start with '05'.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean check_email() {
        if (email.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String emailText = email.getText().toString();

        if (emailText.contains(" ")) {
            Toast.makeText(getActivity(), "❌ Email cannot contain spaces.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(getActivity(), "❌ Invalid email format.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void addUserToFireStore(){
        com.example.bottommenu.User user=new User(name.getText().toString(),family.getText().toString(),phone.getText().toString(),email.getText().toString());
        db.collection("users").document(user.getEmail()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(),"user added",Toast.LENGTH_SHORT).show();
                    updateUI();
                    name.setText(null);
                    family.setText(null);
                    phone.setText(null);
                    email.setText(null);
                    password.setText(null);
                    confirm_password.setText(null);
                }
                else{
                    Toast.makeText(getActivity(),"adding user failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUI(){
        MainActivity.loginFrame.setVisibility(View.INVISIBLE);
        MainActivity.homeFrame.setVisibility(View.VISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.haircutFrame.setVisibility(View.INVISIBLE);
        MainActivity.isLogin=true;
    }
    private boolean check_password() {
        if (password.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String passwordText = password.getText().toString();

        if (passwordText.length() < 8) {
            Toast.makeText(getActivity(), "⚠️ Password must be at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordText.contains(" ")) {
            Toast.makeText(getActivity(), "❌ Password cannot contain spaces.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (confirm_password.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private boolean check_name() {
        if (name.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        String trimmedName = name.getText().toString();
        if (trimmedName.length() < 3) {
            Toast.makeText(getActivity(), "⚠️ Name must be at least 3 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!trimmedName.matches("^[a-zA-Z]+$")) {
            Toast.makeText(getActivity(), "❌ Name can only contain letters (no spaces, numbers, or symbols).", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean check_family() {
        String familyText = family.getText().toString();
        if (familyText.isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your family name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // منع الفراغات في البداية أو النهاية أثناء الكتابة
        family.setFilters(new InputFilter[]{ new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        // منع الفراغات في البداية
                        if (dstart == 0 && source.length() > 0 && source.charAt(0) == ' ') {
                            Toast.makeText(getActivity(), "⚠️ Family name cannot start with a space.", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                        // منع الفراغات المتتالية
                        if (source.toString().matches("\\s{2,}")) {
                            Toast.makeText(getActivity(), "⚠️ Only one space is allowed between words.", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                        return null; // السماح بالإدخال الصحيح
                    }
                }
        });

        // التحقق من أن الاسم لا يقل عن 3 أحرف
        if (familyText.length() < 3) {
            Toast.makeText(getActivity(), "⚠️ Family name must be at least 3 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // التحقق من أن الاسم لا ينتهي بفراغ
        if (familyText.endsWith(" ")) {
            Toast.makeText(getActivity(), "⚠️ Family name cannot end with a space.", Toast.LENGTH_SHORT).show();
            return false;
        }
        // التحقق من أن الاسم يحتوي فقط على أحرف مع السماح بفراغ واحد بين كلمتين
        if (!familyText.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)?$")) {
            Toast.makeText(getActivity(), "❌ Family name can only contain letters and a single space between two words.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // الاسم صالح
    }

}