package com.asaphmwangi.studentattendace;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.asaphmwangi.studentattendace.login.LecturerLogin;
import com.asaphmwangi.studentattendace.login.StudentLogin;
import com.asaphmwangi.studentattendace.utils.LoadingDialog;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    AppCompatButton student,lecturer;
    FirebaseAuth mAuth;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            setContentView(R.layout.layout_loading);
            checkUserRoleAndRedirect(currentUser.getUid());
            return;
        }

        showPortalUI();
    }

    private void checkUserRoleAndRedirect(String uid) {
        db.collection("users").document(uid).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String role = documentSnapshot.getString("role");
                if ("Lecturer".equals(role)) {
                    startActivity(new Intent(MainActivity.this, com.asaphmwangi.studentattendace.dashboard.LecturerDashboard.class));
                    finish();
                } else if ("Student".equals(role)) {
                    startActivity(new Intent(MainActivity.this, com.asaphmwangi.studentattendace.dashboard.StudentDashboard.class));
                    finish();
                } else {
                    showPortalUI();
                }
            } else {
                mAuth.signOut();
                showPortalUI();
            }
        }).addOnFailureListener(e -> {
            mAuth.signOut();
            showPortalUI();
        });
    }

    private void showPortalUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setupButtons();
    }

    private void setupButtons() {
        student = findViewById(R.id.student_login_button);
        lecturer = findViewById(R.id.lecturer_login_button);

        student.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, StudentLogin.class));
        });
        lecturer.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, LecturerLogin.class));
        });

        View mainView = findViewById(R.id.main);
        if (mainView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}