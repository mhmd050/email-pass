package com.example.bottommenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

public class haircut extends Fragment {

    public haircut() {
        // Required empty public constructor
    }

    private ImageView[] haircutImages;
    private Button sendButton;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_haircut, container, false);
        db = FirebaseFirestore.getInstance();
        sendButton = view.findViewById(R.id.button_send);

        haircutImages = new ImageView[]{
                view.findViewById(R.id.imageView1),
                view.findViewById(R.id.imageView2),
                view.findViewById(R.id.imageView3),
                view.findViewById(R.id.imageView4),
                view.findViewById(R.id.imageView5),
                view.findViewById(R.id.imageView6),
                view.findViewById(R.id.imageView7),
                view.findViewById(R.id.imageView8),
                view.findViewById(R.id.imageView9),
                view.findViewById(R.id.imageView10),
                view.findViewById(R.id.imageView11),
                view.findViewById(R.id.imageView12),
                view.findViewById(R.id.imageView13),
                view.findViewById(R.id.imageView14),
                view.findViewById(R.id.imageView15),
                view.findViewById(R.id.imageView16),
                view.findViewById(R.id.imageView17),
                view.findViewById(R.id.imageView18)
        };

        sendButton.setOnClickListener(v -> {
            String selectedTime = "10:00 AM"; // Example selected time
            String fullName = "John Doe"; // Example name

            for (ImageView image : haircutImages) {
                if (image.isPressed()) {
                    int styleImgId = image.getId();
                    Service service = new Service(fullName, selectedTime, styleImgId);
                    db.collection("appintment").add(service)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getContext(), "Appointment booked successfully!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
                            });
                    return;
                }
            }

            Toast.makeText(getContext(), "Please select a haircut style first.", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}