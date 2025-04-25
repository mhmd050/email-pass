package com.example.bottommenu;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HairCut extends Fragment {

    int[] imageIds = {
            R.id.imageView1, R.id.imageView2, R.id.imageView3, R.id.imageView4, R.id.imageView5, R.id.imageView6,
            R.id.imageView7, R.id.imageView8, R.id.imageView9, R.id.imageView10, R.id.imageView11, R.id.imageView12,
            R.id.imageView13, R.id.imageView14, R.id.imageView15, R.id.imageView16, R.id.imageView17, R.id.imageView18
    };

    String[] imageNames = {
            "haircut_1", "haircut_2", "haircut_3", "haircut_4", "haircut_5", "haircut_6",
            "haircut_7", "haircut_8", "haircut_9", "haircut_10", "haircut_11", "haircut_12",
            "haircut_13", "haircut_14", "haircut_15", "haircut_16", "haircut_17", "haircut_18"
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hair_cut, container, false);

        for (int i = 0; i < imageIds.length; i++) {
            int index = i;
            ImageView imageView = view.findViewById(imageIds[i]);
            imageView.setOnClickListener(v -> showConfirmationDialog(imageNames[index]));
        }

        return view;
    }

    private void showConfirmationDialog(String imageName) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Confirmation")
                .setMessage("Are you sure you want to choose this haircut?")
                .setPositiveButton("Yes", (dialogInterface, which) -> saveHaircutToFirestore(imageName))
                .setNegativeButton("No", null)
                .create();

        dialog.setOnShowListener(d -> {
            // تكبير الأزرار
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(18);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(18);

            // تكبير نص الرسالة
            TextView messageView = dialog.findViewById(android.R.id.message);
            if (messageView != null) {
                messageView.setTextSize(20);
            }
        });

        dialog.show();
    }

    private void saveHaircutToFirestore(String imageName) {
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if (email == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String time = HomeFrag.time.getText().toString();

        FirebaseFirestore.getInstance().collection("appointments")
                .whereEqualTo("email", email)
                .whereEqualTo("time", time)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        querySnapshot.getDocuments().get(0).getReference()
                                .update("style_img", imageName)
                                .addOnSuccessListener(unused -> {
                                    Toast.makeText(getContext(), "Haircut saved", Toast.LENGTH_SHORT).show();
                                    MainActivity.isHaircut=false;
                                    // إظهار home وإخفاء باقي الفريمات
                                    MainActivity.loginFrame.setVisibility(View.INVISIBLE);
                                    MainActivity.homeFrame.setVisibility(View.VISIBLE);
                                    MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
                                    MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
                                    MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
                                    MainActivity.managerFrame.setVisibility(View.INVISIBLE);
                                    MainActivity.hairCutFram.setVisibility(View.INVISIBLE);
                                })
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Failed to save", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getContext(), "No matching appointment found", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}