package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AlumniProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference profileRef;
    ImageView alumniProfileImage;

    TextView pName, pEmail, pPhone, pDept, pSession, pId;
    Button pEdit, logout;

    Uri uriProfilePicture;
    String alumniprofilePictureUrl, type;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alumni_profile);

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
                        //feed.putExtra("Type", type);
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
                        assert profile != null;
                        profile.putExtra("Type", type);
                        startActivity(profile);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        alumniProfileImage = findViewById(R.id.alumni_image);
        pName = findViewById(R.id.alumni_name);
        pEmail = findViewById(R.id.alumni_email);
        pPhone = findViewById(R.id.alumni_phone);
        pDept = findViewById(R.id.alumni_dept);
        pSession = findViewById(R.id.alumni_session);
        pId = findViewById(R.id.alumni_id);

        pEdit = findViewById(R.id.btn_edit_profile);
        pEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(getApplicationContext(), AlumniProfileEditActivity.class );
                startActivity(edit);

            }
        });

        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        profileRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Alumni").child(user.getUid());
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("url").exists()) {
                    alumniprofilePictureUrl = dataSnapshot.child("url").getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(alumniprofilePictureUrl)
                            .into(alumniProfileImage);
                }
                //Picasso.get().load(alumniprofilePictureUrl).into(alumniProfileImage);

                String name = dataSnapshot.child("name").getValue().toString();
                pName.setText(name);
                String phone = dataSnapshot.child("phone_no").getValue().toString();
                pPhone.setText(phone);
                String email = dataSnapshot.child("email").getValue().toString();
                pEmail.setText(email);
                if (dataSnapshot.hasChild("dept")){
                    String dept = dataSnapshot.child("dept").getValue().toString();
                    pDept.setText(dept);
                }
                if (dataSnapshot.hasChild("session")){
                    String session = dataSnapshot.child("session").getValue().toString();
                    pSession.setText(session);
                }
                if (dataSnapshot.hasChild("id")){
                    String id = dataSnapshot.child("id").getValue().toString();
                    pId.setText(id);
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