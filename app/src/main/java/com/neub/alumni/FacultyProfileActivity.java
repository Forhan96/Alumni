package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FacultyProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference profileRef;
    ImageView facultyprofileImage;

    TextView pFName, pFEmail, pFPhone, pFDesignation, pFBio, pFDept;
    Button pFEditProfile, logout;
    String facultyprofilePicture, type;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        type = sp.getString("userType", null);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
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
                        Intent members = new Intent(getApplicationContext(), MembersActivity.class);
                        members.putExtra("Type", type);
                        startActivity(members);
                        overridePendingTransition(0,0);
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
                        profile.putExtra("Type", type);
                        Log.i("Type", type);
                        startActivity(profile);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        facultyprofileImage = findViewById(R.id.faculty_profile_image_id);
        pFName = findViewById(R.id.faculty_profile_name_id);
        pFEmail = findViewById(R.id.faculty_profile_email_id);
        pFPhone = findViewById(R.id.faculty_profile_phone_id);
        pFDesignation = findViewById(R.id.faculty_profile_designation);
        pFBio = findViewById(R.id.faculty_profile_bio);
        pFDept = findViewById(R.id.faculty_profile_dept);

        pFEditProfile = findViewById(R.id.btn_edit_faculty_profile);
        pFEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_intent = new Intent(getApplicationContext(), FacultyProfileEditActivity.class);
                edit_intent.putExtra("Type", type);
                startActivity(edit_intent);
            }
        });

        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });

        profileRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Faculty").child(user.getUid());
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("url")){
                    facultyprofilePicture = dataSnapshot.child("url").getValue().toString();
                }
                //String facultyprofilePicture = dataSnapshot.child("url").getValue().toString();
                Picasso.get().load(facultyprofilePicture).into(facultyprofileImage);

                String name = dataSnapshot.child("name").getValue().toString();
                pFName.setText(name);
                String phone = dataSnapshot.child("phone_no").getValue().toString();
                pFPhone.setText(phone);
                String email = dataSnapshot.child("email").getValue().toString();
                pFEmail.setText(email);

                if (dataSnapshot.hasChild("dept")){
                    String dept = dataSnapshot.child("dept").getValue().toString();
                    pFDept.setText(dept);
                }

                if (dataSnapshot.hasChild("designation")){
                    String designation = dataSnapshot.child("designation").getValue().toString();
                    pFDesignation.setText(designation);
                }

                if (dataSnapshot.hasChild("bio")){
                    String bio = dataSnapshot.child("bio").getValue().toString();
                    pFBio.setText(bio);
                }






                //Picasso.get().load(picture).into(profileImage);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Logout() {
        Intent home = new Intent(getApplicationContext(), LaunchingActivity.class);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
        editor = sp.edit();
        editor.clear();
        editor.apply();
        finish();
    }
}