package com.example.bottommenu;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class appointment extends Fragment {

    private RadioGroup radioGroup;
    private Button next;
    private RadioButton selectedButton = null;

    public appointment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        next = view.findViewById(R.id.next);
        next.setEnabled(false);

        // Load booked appointments from Firestore
        FirebaseFirestore.getInstance().collection("appointments").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String bookedTime = doc.getString("time");
                        int count = radioGroup.getChildCount();
                        for (int i = 0; i < count; i++) {
                            View child = radioGroup.getChildAt(i);
                            if (child instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) child;
                                if (radioButton.getText().toString().equals(bookedTime)) {
                                    radioButton.setBackgroundColor(Color.parseColor("#FF7F7F")); // light red
                                    radioButton.setEnabled(false);
                                }
                            }
                        }
                    }
                });

        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setOnClickListener(v -> changeButtonColor(radioButton));
            }
        }

        next.setOnClickListener(view1 -> {
            if (selectedButton != null) {
                String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                if (email != null) {
                    FirebaseFirestore.getInstance().collection("users").document(email)
                            .get().addOnSuccessListener(documentSnapshot -> {
                                if (documentSnapshot.exists()) {
                                    String firstName = documentSnapshot.getString("firstName");
                                    String familyName = documentSnapshot.getString("familyName");

                                    if (firstName != null && familyName != null) {
                                        HashMap<String, Object> appointment = new HashMap<>();
                                        appointment.put("firstName", firstName);
                                        appointment.put("familyName", familyName);
                                        appointment.put("time", selectedButton.getText().toString());

                                        FirebaseFirestore.getInstance().collection("appointments")
                                                .add(appointment)
                                                .addOnSuccessListener(doc -> {
                                                    Toast.makeText(getContext(), "Appointment confirmed for: " + selectedButton.getText(), Toast.LENGTH_SHORT).show();
                                                    goToHaircutPage();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(getContext(), "Failed to save appointment", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        Toast.makeText(getContext(), "Name data is missing", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "User not found", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "You have to choose an appointment", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void changeButtonColor(RadioButton clickedButton) {
        int defaultColor = Color.parseColor("#A1E3F9");
        int selectedColor = getContext().getResources().getColor(android.R.color.holo_green_light);

        if (selectedButton != null) {
            selectedButton.setBackgroundColor(defaultColor);
        }

        clickedButton.setBackgroundColor(selectedColor);
        selectedButton = clickedButton;
        next.setEnabled(true);
    }

    private void goToHaircutPage() {
        MainActivity.homeFrame.setVisibility(View.INVISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.haircutFrame.setVisibility(View.VISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.loginFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
    }
}