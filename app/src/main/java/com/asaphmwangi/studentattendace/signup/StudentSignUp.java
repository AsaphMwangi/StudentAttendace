package com.asaphmwangi.studentattendace.signup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.login.Authentication;
import com.asaphmwangi.studentattendace.models.User;
import com.asaphmwangi.studentattendace.utils.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentSignUp extends AppCompatActivity {
    EditText studID,fullName,email,password;
    AppCompatButton studSignUpBtn;

    Authentication authentication;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog(this);

        studID = findViewById(R.id.student_signup_regno);
        fullName = findViewById(R.id.student_signup_full_name);
        email = findViewById(R.id.student_signup_email);
        password = findViewById(R.id.student_signup_password);

        studSignUpBtn = findViewById(R.id.student_signup_button);

        studSignUpBtn.setOnClickListener(v ->
        {
            signUpVerification();
        });
    }
    private void signUpVerification()
    {
        String studIDNo = studID.getText().toString().trim();
        String studName = fullName.getText().toString().trim();
        String studEmail = email.getText().toString().trim();
        String studPass = password.getText().toString().trim();

        authentication = new Authentication();

        String validationResult = authentication.studSignUp(studName,studEmail,studPass,studIDNo);

        if (validationResult.contains("❌") || validationResult.contains("Fill") || validationResult.contains("Invalid")) {
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog.show();
        mAuth.createUserWithEmailAndPassword(studEmail, studPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uid, studName, studEmail, "Student", studIDNo);
                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(StudentSignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(StudentSignUp.this, com.asaphmwangi.studentattendace.dashboard.StudentDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(StudentSignUp.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(StudentSignUp.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}