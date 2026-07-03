package com.asaphmwangi.studentattendace.models;

public class User {
    private String uid;
    private String name;
    private String email;
    private String role; // "Lecturer" or "Student"
    private String idNumber; // Reg No or Staff ID

    public User() {
    }

    public User(String uid, String name, String email, String role, String idNumber) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.role = role;
        this.idNumber = idNumber;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }
}
