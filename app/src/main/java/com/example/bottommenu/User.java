package com.example.bottommenu;

public class User {
    private String firstName;
    private String familyName;
    private String phone;
    private String email;

    public User() {
        // Required empty constructor for Firestore
    }

    public User(String firstName, String familyName, String phone, String email) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }
}