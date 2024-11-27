package com.example.bottommenu;

public class User {
    private String name,userName,phone,email;

    public User(String name, String userName, String phone, String email) {
        this.name = name;
        this.userName = userName;
        this.phone = phone;
        this.email = email;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
