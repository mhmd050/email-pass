package com.example.bottommenu;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpFrag extends Fragment {
    private TextInputEditText family, name, phone, email, password, confirm_password;
    private Button sign_up;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public SignUpFrag() {}

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.Name);
        family = view.findViewById(R.id.family);
        phone = view.findViewById(R.id.phone);
        sign_up = view.findViewById(R.id.Sign_Up);
        email = view.findViewById(R.id.Email);
        password = view.findViewById(R.id.Password);
        confirm_password = view.findViewById(R.id.Confirm_Password);

        sign_up.setOnClickListener(v -> signUp());

        return view;
    }

    private void signUp() {
        if (check_name() && check_family() && check_phone() && check_email() && check_password()) {
            if (password.getText().toString().equals(confirm_password.getText().toString())) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                addUserToFireStore(); // فقط بعد إنشاء المستخدم بنجاح
                            } else {
                                Toast.makeText(getActivity(), "❌ Registration failed. Please try again.", Toast.LENGTH_LONG).show();
                            }
                        });
            } else {
                Toast.makeText(getActivity(), "⚠️ Passwords do not match. Please re-enter.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addUserToFireStore() {
        User user = new User(
                name.getText().toString(),
                family.getText().toString(),
                phone.getText().toString(),
                email.getText().toString()
        );

        db.collection("users").document(user.getEmail()).set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "✅ Account created and user saved!", Toast.LENGTH_SHORT).show();

                        // الانتقال فقط بعد نجاح الحفظ في Firestore
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
                        MainActivity.bottomNavigationView.getMenu().findItem(R.id.menu_appointment).setEnabled(true);

                        // تفريغ الحقول
                        name.setText(null);
                        family.setText(null);
                        phone.setText(null);
                        email.setText(null);
                        password.setText(null);
                        confirm_password.setText(null);

                    } else {
                        Toast.makeText(getActivity(), "❌ Failed to save user in Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean check_phone() {
        String phoneText = phone.getText().toString();
        if (phoneText.isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!phoneText.matches("^05\\d{8}$")) {
            Toast.makeText(getActivity(), "❌ Phone number must start with '05' and be 10 digits.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean check_email() {
        String emailText = email.getText().toString();
        if (emailText.isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter your email address.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (emailText.contains(" ") || !Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(getActivity(), "❌ Invalid email format.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean check_password() {
        String passwordText = password.getText().toString();
        if (passwordText.isEmpty() || confirm_password.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "⚠️ Please enter and confirm your password.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (passwordText.length() < 8 || passwordText.contains(" ")) {
            Toast.makeText(getActivity(), "⚠️ Password must be at least 8 characters and contain no spaces.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean check_name() {
        String nameText = name.getText().toString();
        if (nameText.isEmpty() || nameText.length() < 3 || !nameText.matches("^[a-zA-Z]+$")) {
            Toast.makeText(getActivity(), "❌ Invalid first name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean check_family() {
        String familyText = family.getText().toString();
        if (familyText.isEmpty() || familyText.length() < 3 || familyText.endsWith(" ")
                || !familyText.matches("^[a-zA-Z]+(\\s[a-zA-Z]+)?$")) {
            Toast.makeText(getActivity(), "❌ Invalid family name.", Toast.LENGTH_SHORT).show();
            return false;
        }
        family.setFilters(new InputFilter[]{(source, start, end, dest, dstart, dend) -> {
            if (dstart == 0 && source.length() > 0 && source.charAt(0) == ' ') return "";
            if (source.toString().matches("\\s{2,}")) return "";
            return null;
        }});
        return true;
    }
}
