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
import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    AppCompatButton student,lecturer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        student = findViewById(R.id.student_login_button);
        lecturer = findViewById(R.id.lecturer_login_button);
     


        student.setOnClickListener(v -> {

            startActivity(new Intent(MainActivity.this, StudentLogin.class));



        });
        lecturer.setOnClickListener(v -> {

            startActivity(new Intent(MainActivity.this, LecturerLogin.class));


        });
    }
}