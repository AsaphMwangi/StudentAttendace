package com.asaphmwangi.studentattendace.signup;

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

public class StudentSignUp extends AppCompatActivity {
    EditText studID,fullName,email,password;
    AppCompatButton studSignUpBtn;

    Authentication authentication;

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
        studID = findViewById(R.id.student_signup_regno);
        fullName = findViewById(R.id.student_signup_full_name);
        email = findViewById(R.id.student_signup_email);
        password = findViewById(R.id.student_signup_password);

        studSignUpBtn = findViewById(R.id.lecturer_signup_button);

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

        String studSignUpMessages = authentication.studSignUp(studName,studEmail,studPass,studIDNo);

        Toast.makeText(this, studSignUpMessages, Toast.LENGTH_SHORT).show();



    }
}