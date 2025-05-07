package com.example.bottommenu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    private Button deleteButton;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    public static String documentIdToDelete = null;

    public HomeFrag() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        date = view.findViewById(R.id.date);
        time = view.findViewById(R.id.time);
        deleteButton = view.findViewById(R.id.delete);
        btnLogout = view.findViewById(R.id.btn_logout);

        // عرض التاريخ الحالي
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentDate);

        btnLogout.setOnClickListener(v -> LogOut());

        mAuth = FirebaseAuth.getInstance();

        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                loadUserAppointment(user.getEmail());
            } else {
                time.setText("Not logged in");
            }
        };

        // زر حذف الموعد
        deleteButton.setOnClickListener(v -> {
            if (documentIdToDelete != null) {
                FirebaseFirestore.getInstance()
                        .collection("appointments")
                        .document(documentIdToDelete)
                        .delete()
                        .addOnSuccessListener(unused -> {
                            time.setText("----------------");
                            documentIdToDelete = null;
                            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(true);

                            Toast.makeText(getContext(), "تم حذف الموعد", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "فشل الحذف", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "لا يوجد موعد لحذفه", Toast.LENGTH_SHORT).show();
            }
        });

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
                                documentIdToDelete = doc.getId();
                                return;
                            }
                        }
                    } else {
                        time.setText("No appointment yet");
                        documentIdToDelete = null;
                    }
                })
                .addOnFailureListener(e -> {
                    time.setText("Failed to load appointment");
                    documentIdToDelete = null;
                });
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
        LoginFrag.x=true;
        MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_logIn);
        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(true);
        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(true);
        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);
        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
    }
}
