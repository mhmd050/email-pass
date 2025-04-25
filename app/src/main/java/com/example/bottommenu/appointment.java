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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class appointment extends Fragment {

    private RadioGroup radioGroup;
    private Button send;
    private RadioButton selectedButton = null;

    // Variable to hold the URL of the selected haircut image
    private String selectedImageUrl = null;

    public appointment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_appointment, container, false);

        radioGroup = view.findViewById(R.id.radioGroup);
        send = view.findViewById(R.id.send);
        send.setEnabled(false);

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

        send.setOnClickListener(view1 -> {
            if (selectedButton != null) {
                SpannableString title = new SpannableString("Confirmation");
                title.setSpan(new RelativeSizeSpan(1.5f), 0, title.length(), 0);

                SpannableString message = new SpannableString("Are you sure you want to save this appointment?");
                message.setSpan(new RelativeSizeSpan(1.3f), 0, message.length(), 0);

                androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(getContext())
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton("Yes", (dialogInterface, which) -> {
                            String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            if (email != null) {
                                FirebaseFirestore.getInstance().collection("users").document(email)
                                        .get().addOnSuccessListener(documentSnapshot -> {
                                            if (documentSnapshot.exists()) {
                                                String firstName = documentSnapshot.getString("firstName");
                                                String familyName = documentSnapshot.getString("familyName");

                                                // Assuming you've stored the selected image URL in 'selectedImageUrl'
                                                if (firstName != null && familyName != null) {
                                                    HashMap<String, Object> appointment = new HashMap<>();
                                                    appointment.put("firstName", firstName);
                                                    appointment.put("familyName", familyName);
                                                    appointment.put("time", selectedButton.getText().toString());
                                                    appointment.put("email", email);
                                                    appointment.put("style_img", selectedImageUrl);  // Add the selected image URL here

                                                    FirebaseFirestore.getInstance().collection("appointments")
                                                            .add(appointment)
                                                            .addOnSuccessListener(doc -> {
                                                                Toast.makeText(getContext(), "Appointment confirmed for: " + selectedButton.getText(), Toast.LENGTH_SHORT).show();
                                                                goToHairCutPage();
                                                                MainActivity.isAppointment = true;
                                                                MainActivity.isHaircut = false;
                                                                HomeFrag.time.setText(selectedButton.getText().toString());
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
                        })
                        .setNegativeButton("No", null)
                        .create();

                dialog.setOnShowListener(dlg -> {
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextSize(18); // Yes
                    dialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setTextSize(18); // No
                });

                dialog.show();
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
        MainActivity.isAppointment = true;
    }

    // Method to handle image selection (from gallery or camera)
    public void selectImage() {
        // Implement the logic to pick the image (either from the gallery or camera)
        // After selecting the image, set the selected URL into the 'selectedImageUrl' variable
        // For example, you can upload the image to Firebase Storage and get the URL back
        // This is an example, and you'll need to implement image picking logic yourself

        // Example:
        // selectedImageUrl = "URL_OF_THE_SELECTED_IMAGE";
    }
}