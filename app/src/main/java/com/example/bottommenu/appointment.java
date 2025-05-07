package com.example.bottommenu;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class appointment extends Fragment {

    private RadioGroup radioGroup;
    private Button send;
    private RadioButton selectedButton = null;
    private String selectedImageUrl = null;
    private String email;

    private String firstName, familyName;

    public appointment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        send = view.findViewById(R.id.send);
        send.setEnabled(false);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "⚠️ Please login first", Toast.LENGTH_SHORT).show();
            return view;
        }

        email = currentUser.getEmail();

        view.post(() -> {
            loadAppointmentsFromFirebase();
            loadUserInfoFromFirebase();
            setupRadioButtons();
        });

        send.setOnClickListener(view1 -> {
            if (selectedButton != null) {
                SpannableString title = new SpannableString("Confirmation");
                title.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);

                SpannableString message = new SpannableString("Are you sure you want to save this appointment?");
                message.setSpan(new RelativeSizeSpan(1.3f), 0, message.length(), 0);

                new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Yes", (dialogInterface, which) -> {
                            if (firstName != null && familyName != null) {
                                HashMap<String, Object> appointment = new HashMap<>();
                                appointment.put("firstName", firstName);
                                appointment.put("familyName", familyName);
                                appointment.put("time", selectedButton.getText().toString());
                                appointment.put("email", email);
                                appointment.put("style_img", selectedImageUrl);

                                FirebaseFirestore.getInstance().collection("appointments")
                                        .add(appointment)
                                        .addOnSuccessListener(docRef -> {
                                            HomeFrag.time.setText(selectedButton.getText().toString());
                                            HomeFrag.documentIdToDelete = docRef.getId();

                                            // ✅ نمرر المرجع مباشرة لـ HairCut
                                            HairCut.latestAppointmentRef = docRef;

                                            Toast.makeText(getContext(), "Appointment confirmed for: " + selectedButton.getText(), Toast.LENGTH_SHORT).show();
                                            disableAllBottomItems();
                                            goToHairCutPage();
                                        })
                                        .addOnFailureListener(e ->
                                                Toast.makeText(getContext(), "Failed to save appointment", Toast.LENGTH_SHORT).show());
                            } else {
                                Toast.makeText(getContext(), "⚠️ User data not loaded. Try again.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(getContext(), "You have to choose an appointment", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void reloadAppointments() {
        loadAppointmentsFromFirebase();
        loadUserInfoFromFirebase();
    }

    private void loadAppointmentsFromFirebase() {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setEnabled(true);
                radioButton.setBackgroundColor(Color.parseColor("#A1E3F9"));
            }
        }

        FirebaseFirestore.getInstance().collection("appointments")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String bookedTime = doc.getString("time");
                        if (bookedTime == null) continue;

                        for (int i = 0; i < radioGroup.getChildCount(); i++) {
                            View child = radioGroup.getChildAt(i);
                            if (child instanceof RadioButton) {
                                RadioButton radioButton = (RadioButton) child;
                                String radioText = radioButton.getText().toString().trim();
                                if (bookedTime.trim().equalsIgnoreCase(radioText)) {
                                    radioButton.setBackgroundColor(Color.parseColor("#FF7F7F"));
                                    radioButton.setEnabled(false);
                                }
                            }
                        }
                    }
                });
    }

    private void setupRadioButtons() {
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = radioGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setOnClickListener(v -> changeButtonColor(radioButton));
            }
        }
    }

    private void changeButtonColor(RadioButton clickedButton) {
        int defaultColor = Color.parseColor("#A1E3F9");
        int selectedColor = getContext().getResources().getColor(android.R.color.holo_green_light);

        if (selectedButton != null) {
            selectedButton.setBackgroundColor(defaultColor);
        }

        clickedButton.setBackgroundColor(selectedColor);
        selectedButton = clickedButton;
        send.setEnabled(true);
    }

    private void goToHairCutPage() {
        MainActivity.homeFrame.setVisibility(View.INVISIBLE);
        MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
        MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
        MainActivity.loginFrame.setVisibility(View.INVISIBLE);
        MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
        MainActivity.managerFrame.setVisibility(View.INVISIBLE);
        MainActivity.hairCutFram.setVisibility(View.VISIBLE);
        disableAllBottomItems();
    }

    private void disableAllBottomItems() {
        if (MainActivity.bottomNavigationView != null) {
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
        }
    }

    private void loadUserInfoFromFirebase() {
        FirebaseFirestore.getInstance().collection("users").document(email)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        firstName = documentSnapshot.getString("firstName");
                        familyName = documentSnapshot.getString("familyName");
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "❌ Failed to load user info", Toast.LENGTH_SHORT).show();
                });
    }
}
