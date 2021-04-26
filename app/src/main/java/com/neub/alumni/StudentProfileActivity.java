package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class StudentProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference profileRef;

    ImageView profileImage;
    TextView pSName, pSEmail, pSPhone,pSDept,pSSession,pSId;
    Button pSEditButton, logout;
    String profilePicture, type;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

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
                        assert profile != null;
                        profile.putExtra("Type", type);
                        startActivity(profile);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        profileImage = findViewById(R.id.student_profile_image_id);
        pSName = findViewById(R.id.student_name);
        pSEmail = findViewById(R.id.student_email);
        pSPhone = findViewById(R.id.student_phone);
        pSDept = findViewById(R.id.student_dept);
        pSSession = findViewById(R.id.student_session);
        pSId = findViewById(R.id.student_id);

        pSEditButton = findViewById(R.id.btn_student_edit_profile);
        pSEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit_student = new Intent(getApplicationContext(), StudentProfileEditActivity.class );
                startActivity(edit_student);
            }
        });

        logout = findViewById(R.id.btn_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout();
            }
        });


        profileRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(user.getUid());
        profileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChild("url")){
                    profilePicture = dataSnapshot.child("url").getValue().toString();
                }



                Picasso.get().load(profilePicture).into(profileImage);



                //String picture = dataSnapshot.child("url").getValue().toString();
                //String picture = dataSnapshot.child("url").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                pSName.setText(name);
                String phone = dataSnapshot.child("phone_no").getValue().toString();
                pSPhone.setText(phone);
                String email = dataSnapshot.child("email").getValue().toString();
                pSEmail.setText(email);
                if (dataSnapshot.hasChild("dept")){
                    String dept = dataSnapshot.child("dept").getValue().toString();
                    pSDept.setText(dept);
                }
                if (dataSnapshot.hasChild("session")){
                    String session = dataSnapshot.child("session").getValue().toString();
                    pSSession.setText(session);
                }
                if (dataSnapshot.hasChild("id")){
                    String id = dataSnapshot.child("id").getValue().toString();
                    pSId.setText(id);
                }
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