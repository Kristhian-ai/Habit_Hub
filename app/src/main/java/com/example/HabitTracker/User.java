package com.example.HabitTracker;

public class User {
    public String username;
    public String email;
    public String phone;
    public String country;
    public String province;
    public String gender;
    public String interest;
    public String birthdate;
    public String birthtime;

    public User() {
        // Default constructor required for Firestore
    }

    public User(String username, String email, String phone, String country, String province, String gender, String interest, String birthdate, String birthtime) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.country = country;
        this.province = province;
        this.gender = gender;
        this.interest = interest;
        this.birthdate = birthdate;
        this.birthtime = birthtime;
    }
}
