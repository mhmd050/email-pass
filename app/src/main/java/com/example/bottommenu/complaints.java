package com.example.bottommenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class complaints extends Fragment {

    private TextInputEditText complainsEditText;
    private Button sendButton;
    private FirebaseFirestore db;

    public complaints() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);

        complainsEditText = view.findViewById(R.id.complains_button);
        sendButton = view.findViewById(R.id.button2);
        db = FirebaseFirestore.getInstance();

        sendButton.setOnClickListener(v -> {
            String complaintText = complainsEditText.getText().toString().trim();

            if (!complaintText.isEmpty()) {
                Map<String, Object> complaint = new HashMap<>();
                complaint.put("text", complaintText);
                db.collection("complaints").add(complaint)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getContext(), "Complaint sent successfully", Toast.LENGTH_SHORT).show();
                            complainsEditText.setText("");
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to send complaint", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getContext(), "Please write your complaint", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}