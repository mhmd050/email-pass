package com.example.bottommenu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.DateFormat;
import java.util.Calendar;

public class HomeFrag extends Fragment {

    private FloatingActionButton btnLogout;
    private TextView date;
    public static TextView time;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;

    public HomeFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);

        // عرض التاريخ الحالي
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentDate);

        btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(v -> LogOut());

        // تهيئة FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // الاستماع لتغيّر حالة المستخدم
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                loadUserAppointment(user.getEmail());
            } else {
                time.setText("Not logged in");
            }
        };

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    private void loadUserAppointment(String email) {
        if (email == null) {
            time.setText("Not logged in");
            return;
        }

        FirebaseFirestore.getInstance().collection("appointments")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            String appointmentTime = doc.getString("time");
                            if (appointmentTime != null) {
                                time.setText(appointmentTime);
                                return;
                            }
                        }
                    }
                    time.setText("No appointment yet");
                })
                .addOnFailureListener(e -> time.setText("Failed to load appointment"));
    }

    private void LogOut() {
        FirebaseAuth.getInstance().signOut();
        MainActivity.loginFrame.setVisibility(View.VISIBLE);
        MainActivity.homeFrame.setVisibility(View.INVISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
        MainActivity.hairCutFram.setVisibility(View.INVISIBLE);
        MainActivity.isLogin = false;
    }
}