package com.asaphmwangi.studentattendace.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.models.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class LecturerDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private android.widget.ImageView logoutBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<Subject> subjectList;
    private SubjectAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecturer_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.classes_recycler_view);
        fab = findViewById(R.id.add_class_fab);
        logoutBtn = findViewById(R.id.logout_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        subjectList = new ArrayList<>();
        adapter = new SubjectAdapter(subjectList, this);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            // TODO: Show dialog to add class
            showAddClassDialog();
        });

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, com.asaphmwangi.studentattendace.MainActivity.class));
            finish();
        });

        loadSubjects();
    }

    private void loadSubjects() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("subjects")
                .whereEqualTo("lecturerId", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Toast.makeText(this, "Error loading subjects", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    subjectList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Subject subject = doc.toObject(Subject.class);
                        subject.setId(doc.getId());
                        subjectList.add(subject);
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private void showAddClassDialog() {
        // For brevity in this task, I'll just add a dummy class or I should implement a real dialog
        // Let's implement a simple dialog.
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add New Subject");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final android.widget.EditText nameInput = new android.widget.EditText(this);
        nameInput.setHint("Subject Name");
        layout.addView(nameInput);

        final android.widget.EditText roomInput = new android.widget.EditText(this);
        roomInput.setHint("Room Number");
        layout.addView(roomInput);

        final android.widget.EditText totalInput = new android.widget.EditText(this);
        totalInput.setHint("Total Students");
        totalInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(totalInput);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = nameInput.getText().toString().trim();
            String room = roomInput.getText().toString().trim();
            String totalStr = totalInput.getText().toString().trim();
            if (!name.isEmpty() && !room.isEmpty() && !totalStr.isEmpty()) {
                int total = Integer.parseInt(totalStr);
                addSubjectToFirestore(name, room, total);
            } else {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void addSubjectToFirestore(String name, String room, int total) {
        String uid = mAuth.getCurrentUser().getUid();
        Subject subject = new Subject(null, name, uid, room, total);
        db.collection("subjects").add(subject)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Subject added", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to add subject", Toast.LENGTH_SHORT).show();
                });
    }
}
