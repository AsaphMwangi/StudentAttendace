package com.asaphmwangi.studentattendace.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.asaphmwangi.studentattendace.R;
import com.asaphmwangi.studentattendace.signup.LecturerSignUp;

import org.w3c.dom.Text;

public class LecturerLogin extends AppCompatActivity {
    AppCompatButton signup_link;

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
        signup_link = findViewById(R.id.lecturer_signup_link);
        signup_link.setOnClickListener(v->{
            startActivity(new Intent(this, LecturerSignUp.class));
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}