package com.neub.alumni;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginChoiseActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choise);

        Button alumniBtn = findViewById(R.id.alumni_btn_id);
        Button studentBtn = findViewById(R.id.student_btn_id);
        Button facultyBtn = findViewById(R.id.faculty_btn_id);

        alumniBtn.setOnClickListener(this);
        studentBtn.setOnClickListener(this);
        facultyBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alumni_btn_id:
                Intent alumni = new Intent(getApplicationContext(), AlumniLoginActivity.class);
                startActivity(alumni);
                break;
            case R.id.student_btn_id:
                Intent student = new Intent(getApplicationContext(), StudentLoginActivity.class);
                startActivity(student);
                break;
            case R.id.faculty_btn_id:
                Intent faculty = new Intent(getApplicationContext(), FacultyLoginActivity.class);
                startActivity(faculty);
                break;
        }
    }
}