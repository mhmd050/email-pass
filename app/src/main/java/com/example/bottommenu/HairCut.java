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
import com.google.firebase.firestore.DocumentReference;

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

    // ✅ نستخدمها لتخزين مرجع الموعد القادم
    public static DocumentReference latestAppointmentRef = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hair_cut, container, false);

        for (int i = 0; i < imageIds.length; i++) {
            int index = i;
            ImageView imageView = view.findViewById(imageIds[i]);
            imageView.setOnClickListener(v -> {
                if (latestAppointmentRef != null) {
                    showConfirmationDialog(imageNames[index]);
                } else {
                    Toast.makeText(getContext(), "⚠️ Please wait, appointment is still being processed.", Toast.LENGTH_SHORT).show();
                }
            });
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
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextSize(18);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextSize(18);

            TextView messageView = dialog.findViewById(android.R.id.message);
            if (messageView != null) {
                messageView.setTextSize(20);
            }
        });

        dialog.show();
    }

    private void saveHaircutToFirestore(String imageName) {
        if (latestAppointmentRef == null) {
            Toast.makeText(getContext(), "⚠️ Appointment reference is missing", Toast.LENGTH_SHORT).show();
            return;
        }

        latestAppointmentRef.update("style_img", imageName)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Haircut saved", Toast.LENGTH_SHORT).show();

                    // الانتقال للصفحة الرئيسية
                    MainActivity.loginFrame.setVisibility(View.INVISIBLE);
                    MainActivity.homeFrame.setVisibility(View.VISIBLE);
                    MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
                    MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
                    MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
                    MainActivity.managerFrame.setVisibility(View.INVISIBLE);
                    MainActivity.hairCutFram.setVisibility(View.INVISIBLE);

                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_home);
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(false);
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(false);
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(true);
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(true);
                    MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "❌ Failed to save haircut", Toast.LENGTH_SHORT).show());
    }
}
