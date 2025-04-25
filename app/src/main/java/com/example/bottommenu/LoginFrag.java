package com.example.bottommenu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginFrag extends Fragment {
    private TextInputEditText txtEmail, txtPassword;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private boolean x = true;

    public LoginFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Start animated background
        ImageView bgImage = view.findViewById(R.id.bg_image);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move_background);
        bgImage.startAnimation(animation);

        // Initialize UI
        txtEmail = view.findViewById(R.id.et_Name);
        txtPassword = view.findViewById(R.id.et_Pass);
        btnLogin = view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkManager();
            }
        });
        return view;
    }

    private void checkManager() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (password.equals("00000000") && email.equals("manager@gmail.com")) {
            MainActivity.loginFrame.setVisibility(View.INVISIBLE);
            MainActivity.homeFrame.setVisibility(View.INVISIBLE);
            MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
            MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
            MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
            MainActivity.managerFrame.setVisibility(View.VISIBLE);
            MainActivity.hairCutFram.setVisibility(View.INVISIBLE);
            MainActivity.isManager = true;
            txtEmail.setText(null);
            txtPassword.setText(null);
            MainActivity.isLogin = true;
        } else {
            checkEmailPass();
        }
    }

    private void checkEmailPass() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (!(password.equals("") || email.equals(""))) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "✅ Login successful!", Toast.LENGTH_SHORT).show();
                                checkIfUserHasAppointment();
                            } else {
                                Toast.makeText(getActivity(), "❌ Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "⚠️ Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfUserHasAppointment() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        MainActivity.isAppointment = false;
        if (currentUser != null) {
            String email = currentUser.getEmail();
            FirebaseFirestore.getInstance().collection("appointments")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            MainActivity.isAppointment = true;

                            String time = querySnapshot.getDocuments().get(0).getString("time");
                            if (time != null) {
                                HomeFrag.time.setText(time);
                            }

                            // التحقق من وجود صورة قصة شعر
                            String styleImg = querySnapshot.getDocuments().get(0).getString("style_img");
                            if (!(styleImg != null && !styleImg.isEmpty())) {
                                MainActivity.isHaircut=false;
                                MainActivity.loginFrame.setVisibility(View.INVISIBLE);
                                MainActivity.homeFrame.setVisibility(View.INVISIBLE);
                                MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
                                MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
                                MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
                                MainActivity.managerFrame.setVisibility(View.INVISIBLE);
                                MainActivity.hairCutFram.setVisibility(View.VISIBLE);
                                x=false;
                            }
                        }
                        if (x) {
                            updateUI();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Failed to check appointment", Toast.LENGTH_SHORT).show();
                        updateUI();
                    });
        }
    }

    private void updateUI() {
        MainActivity.loginFrame.setVisibility(View.INVISIBLE);
        MainActivity.homeFrame.setVisibility(View.VISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
        MainActivity.hairCutFram.setVisibility(View.INVISIBLE);
        txtEmail.setText(null);
        txtPassword.setText(null);
        MainActivity.isLogin = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            checkIfUserHasAppointment(); // Check appointment on start too
        }
    }
}