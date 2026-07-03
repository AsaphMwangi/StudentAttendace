package com.asaphmwangi.studentattendace.login;

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
import com.asaphmwangi.studentattendace.signup.StudentSignUp;
import com.asaphmwangi.studentattendace.utils.LoadingDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentLogin extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    AppCompatButton loginBtn;
    Authentication authentication;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatButton signup_link;
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        loadingDialog = new LoadingDialog(this);

        signup_link = findViewById(R.id.student_signup_link);
        signup_link.setOnClickListener(v->{
            startActivity(new Intent(this, StudentSignUp.class));
        });


        loginEmail = findViewById(R.id.student_login_email);
        loginPassword = findViewById(R.id.student_login_password);
        loginBtn = findViewById(R.id.student_login_button);

        loginBtn.setOnClickListener(v ->
        {
            loginVerification();
        });

    }

    private void loginVerification()
    {
        String email = loginEmail.getText().toString().trim();
        String password =loginPassword.getText().toString().trim();

        authentication = new Authentication();
        String authMessage =authentication.studLogin(email,password);
        
        if (!authMessage.equals("success")) {
            Toast.makeText(this, authMessage, Toast.LENGTH_SHORT).show();
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
                                        if ("Student".equals(role)) {
                                            Toast.makeText(StudentLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(StudentLogin.this, com.asaphmwangi.studentattendace.dashboard.StudentDashboard.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            mAuth.signOut();
                                            Toast.makeText(StudentLogin.this, "Access Denied: Not a Student", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    loadingDialog.dismiss();
                                    Toast.makeText(StudentLogin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        loadingDialog.dismiss();
                        Toast.makeText(StudentLogin.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}