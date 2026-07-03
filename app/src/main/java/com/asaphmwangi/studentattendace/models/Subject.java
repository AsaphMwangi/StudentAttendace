package com.asaphmwangi.studentattendace.models;

public class Subject {
    private String id;
    private String name;
    private String lecturerId;
    private String room;
    private int totalStudents;

    public Subject() {}

    public Subject(String id, String name, String lecturerId, String room, int totalStudents) {
        this.id = id;
        this.name = name;
        this.lecturerId = lecturerId;
        this.room = room;
        this.totalStudents = totalStudents;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLecturerId() { return lecturerId; }
    public void setLecturerId(String lecturerId) { this.lecturerId = lecturerId; }
    public String getRoom() { return room; }
    public void setRoom(String room) { this.room = room; }
    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
}
