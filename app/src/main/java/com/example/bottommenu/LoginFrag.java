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
    public static boolean x = true;
    private ImageView scissorsImage;

    public LoginFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Start animated background
        ImageView bgImage = view.findViewById(R.id.bg_image);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move_background);
        bgImage.startAnimation(animation);
        scissorsImage = view.findViewById(R.id.scissorsImage);
        scissorsImage.setVisibility(View.GONE);

        // Initialize UI
        txtEmail = view.findViewById(R.id.et_Name);
        txtPassword = view.findViewById(R.id.et_Pass);
        btnLogin = view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(v -> {
            scissorsImage.setVisibility(View.VISIBLE);
            scissorsImage.setScaleX(0.5f);
            scissorsImage.setScaleY(0.5f);
            scissorsImage.setRotation(0f);
            scissorsImage.setTranslationY(0f);

            scissorsImage.animate()
                    .scaleX(1.5f)
                    .scaleY(1.5f)
                    .rotationBy(360f)
                    .translationY(1200f)
                    .setDuration(2000)
                    .setInterpolator(new android.view.animation.BounceInterpolator())
                    .start();

            scissorsImage.postDelayed(this::checkManager, 2000);
        });

        return view;
    }

    private void checkManager() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();

        if (email.equals("manager@gmail.com") && password.equals("00000000")) {
            // إظهار واجهة المدير
            MainActivity.loginFrame.setVisibility(View.INVISIBLE);
            MainActivity.homeFrame.setVisibility(View.INVISIBLE);
            MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
            MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
            MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
            MainActivity.managerFrame.setVisibility(View.VISIBLE);
            MainActivity.hairCutFram.setVisibility(View.INVISIBLE);

            // تحديث الـ Bottom Navigation
            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_home);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);

            // تحميل بيانات المدير
            if (MainActivity.managerFrag != null) {
                MainActivity.managerFrag.reloadAppointmentsAndComplaints();
            }

            txtEmail.setText(null);
            txtPassword.setText(null);
        } else {
            checkEmailPass();
        }
    }

    private void checkEmailPass() {
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        if (!(email.isEmpty() || password.isEmpty())) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "✅ Login successful!", Toast.LENGTH_SHORT).show();
                            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_home);
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(true);
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(true);
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(true);
                            checkIfUserHasAppointment();
                        } else {
                            Toast.makeText(getActivity(), "❌ Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "⚠️ Please fill in all required fields.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkIfUserHasAppointment() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String email = currentUser.getEmail();
            FirebaseFirestore.getInstance().collection("appointments")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener(querySnapshot -> {
                        if (!querySnapshot.isEmpty()) {
                            String time = querySnapshot.getDocuments().get(0).getString("time");
                            if (time != null) {
                                HomeFrag.time.setText(time);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
                            }

                            String styleImg = querySnapshot.getDocuments().get(0).getString("style_img");
                            if (!(styleImg != null && !styleImg.isEmpty())) {
                                MainActivity.loginFrame.setVisibility(View.INVISIBLE);
                                MainActivity.homeFrame.setVisibility(View.INVISIBLE);
                                MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
                                MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
                                MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
                                MainActivity.managerFrame.setVisibility(View.INVISIBLE);
                                MainActivity.hairCutFram.setVisibility(View.VISIBLE);
                                MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_appointment);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
                                MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
                                x = false;
                            }
                        }
                        if (x) updateUI();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_home);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(true);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(true);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(true);
            checkIfUserHasAppointment();
        }
    }
}
