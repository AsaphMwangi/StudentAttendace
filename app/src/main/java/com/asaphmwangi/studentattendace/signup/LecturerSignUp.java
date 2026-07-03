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

public class LecturerSignUp extends AppCompatActivity {

    EditText lecID,fullName,email,password;
    AppCompatButton lecSignUpBtn;
    Authentication authentication;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecturer_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog(this);

        lecID = findViewById(R.id.signup_regno);
        fullName = findViewById(R.id.signup_full_name);
        email = findViewById(R.id.lecturer_signup_email);
        password = findViewById(R.id.lecturer_lecturer_signup_password);

        lecSignUpBtn = findViewById(R.id.lecturer_signup_button);

        lecSignUpBtn.setOnClickListener(v ->
        {
            signUpVerification();
        });

    }
    private void signUpVerification()
    {
        String lecIDNo = lecID.getText().toString().trim();
        String lecName = fullName.getText().toString().trim();
        String lecEmail = email.getText().toString().trim();
        String lecPass = password.getText().toString().trim();

        authentication = new Authentication();

        String validationResult = authentication.lecSignUp(lecName,lecEmail,lecPass,lecIDNo);

        if (!validationResult.equals("true")) {
            Toast.makeText(this, validationResult, Toast.LENGTH_SHORT).show();
            return;
        }

        loadingDialog.show();
        mAuth.createUserWithEmailAndPassword(lecEmail, lecPass)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uid, lecName, lecEmail, "Lecturer", lecIDNo);
                        db.collection("users").document(uid).set(user)
                                .addOnSuccessListener(aVoid -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(LecturerSignUp.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LecturerSignUp.this, com.asaphmwangi.studentattendace.dashboard.LecturerDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(LecturerSignUp.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(LecturerSignUp.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}