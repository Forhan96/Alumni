package com.neub.alumni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MembersActivity extends AppCompatActivity implements View.OnClickListener{

    private Button studentsBtn, alumnisBtn, facultiesBtn;
    String type;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        this.setTitle("Members");

        sp = getSharedPreferences("login",MODE_PRIVATE);
        type = sp.getString("userType", null);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.members);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news_feed:
                        Intent feed = new Intent(getApplicationContext(), NewsFeedActivity.class);
                        feed.putExtra("Type", type);
                        startActivity(feed);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.events:
                        Intent event = new Intent(getApplicationContext(), EventActivity.class);
                        event.putExtra("Type", type);
                        startActivity(event);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.members:
                        return true;

                    case R.id.profile:
                        Intent profile = null;
                        if (type.equals("Student")){
                            profile = new Intent(getApplicationContext(), StudentProfileActivity.class);
                        }
                        else if (type.equals("Alumni")){
                            profile = new Intent(getApplicationContext(), AlumniProfileActivity.class);
                        }
                        else if (type.equals("Faculty")){
                            profile = new Intent(getApplicationContext(), FacultyProfileActivity.class);
                        }
                        assert profile != null;
                        profile.putExtra("Type", type);
                        startActivity(profile);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        studentsBtn = findViewById(R.id.students);
        alumnisBtn = findViewById(R.id.alumnis);
        facultiesBtn = findViewById(R.id.faculties);

        studentsBtn.setOnClickListener(this);
        alumnisBtn.setOnClickListener(this);
        facultiesBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.students:
                Intent students = new Intent(getApplicationContext(), StudentsActivity.class);
                startActivity(students);
                break;

            case R.id.alumnis:
                Intent alumnis = new Intent(getApplicationContext(), AlumniActivity.class);
                startActivity(alumnis);
                break;

            case R.id.faculties:
                Intent faculties = new Intent(getApplicationContext(), FacultyActivity.class);
                startActivity(faculties);
                break;

        }

    }
}