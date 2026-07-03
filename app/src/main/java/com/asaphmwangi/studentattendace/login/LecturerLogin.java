package com.asaphmwangi.studentattendace.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.EditText;
import android.widget.Toast;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.signup.LecturerSignUp;
import com.asaphmwangi.studentattendace.utils.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LecturerLogin extends AppCompatActivity {
    AppCompatButton signup_link, login_btn;
    EditText email_field, password_field;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Authentication authentication;
    LoadingDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecturer_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        authentication = new Authentication();
        loadingDialog = new LoadingDialog(this);

        email_field = findViewById(R.id.lecturer_login_email);
        password_field = findViewById(R.id.lecturer_lecturer_login_password);
        login_btn = findViewById(R.id.lecturer_login_button);
        signup_link = findViewById(R.id.lecturer_signup_link);

        signup_link.setOnClickListener(v->{
            startActivity(new Intent(this, LecturerSignUp.class));
        });

        login_btn.setOnClickListener(v -> {
            loginUser();
        });
    }

    private void loginUser() {
        String email = email_field.getText().toString().trim();
        String password = password_field.getText().toString().trim();

        String validation = authentication.lecLogin(email, password);
        
        if (!validation.equals("success")) {
             Toast.makeText(this, validation, Toast.LENGTH_SHORT).show();
             return;
        }

        loadingDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    loadingDialog.dismiss();
                                    if (documentSnapshot.exists()) {
                                        String role = documentSnapshot.getString("role");
                                        if ("Lecturer".equals(role)) {
                                            Toast.makeText(LecturerLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LecturerLogin.this, com.asaphmwangi.studentattendace.dashboard.LecturerDashboard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            mAuth.signOut();
                                            Toast.makeText(LecturerLogin.this, "Access Denied: Not a Lecturer", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(LecturerLogin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(LecturerLogin.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}