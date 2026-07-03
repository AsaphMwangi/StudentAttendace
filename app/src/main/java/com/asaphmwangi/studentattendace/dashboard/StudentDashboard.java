package com.asaphmwangi.studentattendace.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.models.AttendanceRecord;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.journeyapps.barcodescanner.ScanContract;
import com.asaphmwangi.studentattendace.models.AttendanceRecord;
import com.asaphmwangi.studentattendace.utils.LoadingDialog;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StudentDashboard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private android.widget.Button scanBtn;
    private android.widget.ImageView logoutBtn;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<AttendanceRecord> attendanceList;
    private AttendanceAdapter adapter;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(this);

        recyclerView = findViewById(R.id.attendance_recycler_view);
        scanBtn = findViewById(R.id.scan_qr_button);
        logoutBtn = findViewById(R.id.logout_btn);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        attendanceList = new ArrayList<>();
        adapter = new AttendanceAdapter(attendanceList, this, false);
        recyclerView.setAdapter(adapter);

        scanBtn.setOnClickListener(v -> {
            checkCameraPermission();
        });

        logoutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, com.asaphmwangi.studentattendace.MainActivity.class));
            finish();
        });

        loadAttendanceHistory();
    }

    private void checkCameraPermission() {
        if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            scanQRCode();
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA);
        }
    }

    private final androidx.activity.result.ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new androidx.activity.result.contract.ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    scanQRCode();
                } else {
                    Toast.makeText(this, "Camera permission is required to scan QR code", Toast.LENGTH_SHORT).show();
                }
            });

    private void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Scan Class QR Code");
        options.setBeepEnabled(true);
        options.setOrientationLocked(false);
        options.setCaptureActivity(CaptureAct.class);
        barcodeLauncher.launch(options);
    }

    private final androidx.activity.result.ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    processScannedData(result.getContents());
                }
            });

    private void processScannedData(String data) {
        loadingDialog.show();
        String uid = mAuth.getCurrentUser().getUid();
        
        // Fetch student details
        db.collection("users").document(uid).get().addOnSuccessListener(studentDoc -> {
            String studentName = studentDoc.getString("name");
            String studentRegNo = studentDoc.getString("idNumber");

            // Fetch subject details
            db.collection("subjects").document(data).get().addOnSuccessListener(subjectDoc -> {
                String subjectName = subjectDoc.getString("name");
                
                // If subject exists, mark attendance
                if (subjectName != null) {
                    AttendanceRecord record = new AttendanceRecord(null, data, subjectName, uid, studentName, studentRegNo, new Date());
                    db.collection("attendance").add(record).addOnSuccessListener(documentReference -> {
                        loadingDialog.dismiss();
                        Toast.makeText(this, "Attendance marked for " + subjectName, Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        loadingDialog.dismiss();
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(this, "Invalid QR Code Content", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> {
                loadingDialog.dismiss();
                Toast.makeText(this, "Subject not found or Invalid QR", Toast.LENGTH_SHORT).show();
            });
        }).addOnFailureListener(e -> {
            loadingDialog.dismiss();
            Toast.makeText(this, "Student error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void loadAttendanceHistory() {
        String uid = mAuth.getCurrentUser().getUid();
        db.collection("attendance")
                .whereEqualTo("studentId", uid)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
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
