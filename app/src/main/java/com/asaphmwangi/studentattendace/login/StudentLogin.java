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
import com.asaphmwangi.studentattendace.signup.LecturerSignUp;
import com.asaphmwangi.studentattendace.signup.StudentSignUp;

public class StudentLogin extends AppCompatActivity {

    EditText loginEmail,loginPassword;
    AppCompatButton loginBtn;
    Authentication authentication;

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
        Toast.makeText(this, authMessage , Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}