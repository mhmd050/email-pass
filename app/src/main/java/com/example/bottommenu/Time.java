package com.example.bottommenu;

public class Time {

    private String firstName;
    private String familyName;
    private String time;

    public Time() {
        // Required empty constructor for Firestore
    }

    public Time(String firstName, String familyName, String time) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.time = time;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public String getTime() {
        return time;
    }
}