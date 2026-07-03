package com.asaphmwangi.studentattendace.dashboard;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.models.AttendanceRecord;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LecturerAttendanceView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private List<AttendanceRecord> attendanceList;
    private AttendanceAdapter adapter;
    private String subjectId;

    private int totalStudents = 1; // Default to avoid division by zero

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_attendance_view);

        subjectId = getIntent().getStringExtra("subjectId");
        if (subjectId == null) {
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.attendance_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList, this, true);
        recyclerView.setAdapter(adapter);

        fetchSubjectDetails();
        loadAttendance();
    }

    private void fetchSubjectDetails() {
        db.collection("subjects").document(subjectId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Long total = documentSnapshot.getLong("totalStudents");
                if (total != null && total > 0) {
                    totalStudents = total.intValue();
                }
            }
        });
    }

    private void loadAttendance() {
        db.collection("attendance")
                .whereEqualTo("subjectId", subjectId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    attendanceList.clear();
                    if (value != null) {
                        for (QueryDocumentSnapshot doc : value) {
                            AttendanceRecord record = doc.toObject(AttendanceRecord.class);
                            attendanceList.add(record);
                        }
                    }
                    Collections.sort(attendanceList, (a, b) -> {
                        if (a.getTimestamp() == null || b.getTimestamp() == null) return 0;
                        return b.getTimestamp().compareTo(a.getTimestamp());
                    });
                    adapter.notifyDataSetChanged();

                });
    }


}
