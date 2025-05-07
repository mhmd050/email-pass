package com.example.bottommenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class manager extends Fragment {

    private FloatingActionButton btnLogout;
    private ListView appointmentsList, complaintsList;
    private ArrayList<String> appointmentsArray, complaintsArray;
    private FirebaseFirestore db;
    private AppointmentAdapter appointmentsAdapter;
    private ComplaintAdapter complaintsAdapter;

    public manager() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manager, container, false);

        btnLogout = view.findViewById(R.id.btn_logout);
        appointmentsList = view.findViewById(R.id.appointments_list);
        complaintsList = view.findViewById(R.id.complains_list);
        db = FirebaseFirestore.getInstance();

        appointmentsArray = new ArrayList<>();
        complaintsArray = new ArrayList<>();

        appointmentsAdapter = new AppointmentAdapter(getContext(), appointmentsArray);
        complaintsAdapter = new ComplaintAdapter(getContext(), complaintsArray);

        appointmentsList.setAdapter(appointmentsAdapter);
        complaintsList.setAdapter(complaintsAdapter);

        // تحميل البيانات عند الإنشاء
        reloadAppointmentsAndComplaints();

        btnLogout.setOnClickListener(view1 -> {
            MainActivity.loginFrame.setVisibility(View.VISIBLE);
            MainActivity.homeFrame.setVisibility(View.INVISIBLE);
            MainActivity.appointmentFrame.setVisibility(View.INVISIBLE);
            MainActivity.complaintsFrame.setVisibility(View.INVISIBLE);
            MainActivity.signUpFrame.setVisibility(View.INVISIBLE);
            MainActivity.managerFrame.setVisibility(View.INVISIBLE);
            MainActivity.hairCutFram.setVisibility(View.INVISIBLE);

            MainActivity.bottomNavigationView.setSelectedItemId(R.id.menu_logIn);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_signUp).setEnabled(true);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_logIn).setEnabled(true);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_home).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_complaints).setEnabled(false);
            MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(false);
        });

        return view;
    }

    public void reloadAppointmentsAndComplaints() {
        loadAppointments();
        loadComplaints();
    }

    private void loadAppointments() {
        db.collection("appointments").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                appointmentsArray.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String name = doc.getString("firstName");
                    String familyName = doc.getString("familyName");
                    String time = doc.getString("time");
                    if (name != null && familyName != null && time != null) {
                        appointmentsArray.add(name + " " + familyName + " - " + time);
                    }
                }
                appointmentsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "❌ Failed to load appointments", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadComplaints() {
        db.collection("complaints").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                complaintsArray.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    String text = doc.getString("text");
                    if (text != null) {
                        complaintsArray.add(text);
                    }
                }
                complaintsAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), "❌ Failed to load complaints", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
