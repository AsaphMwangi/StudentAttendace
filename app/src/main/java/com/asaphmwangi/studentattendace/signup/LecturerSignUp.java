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

public class LecturerSignUp extends AppCompatActivity {

    EditText lecID,fullName,email,password;
    AppCompatButton lecSignUpBtn;
    Authentication authentication;


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

        String lecSignUpMessages = authentication.lecSignUp(lecName,lecEmail,lecPass,lecIDNo);

        Toast.makeText(this, lecSignUpMessages, Toast.LENGTH_SHORT).show();



    }
}