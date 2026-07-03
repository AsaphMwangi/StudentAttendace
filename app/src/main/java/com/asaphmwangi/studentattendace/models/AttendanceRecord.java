package com.asaphmwangi.studentattendace.models;

import java.util.Date;

public class AttendanceRecord {
    private String id;
    private String subjectId;
    private String subjectName;
    private String studentId;
    private String studentName;
    private String studentRegNo;
    private Date timestamp;

    public AttendanceRecord() {}

    public AttendanceRecord(String id, String subjectId, String subjectName, String studentId, String studentName, String studentRegNo, Date timestamp) {
        this.id = id;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentRegNo = studentRegNo;
        this.timestamp = timestamp;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getSubjectId() { return subjectId; }
    public void setSubjectId(String subjectId) { this.subjectId = subjectId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getStudentRegNo() { return studentRegNo; }
    public void setStudentRegNo(String studentRegNo) { this.studentRegNo = studentRegNo; }
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}
