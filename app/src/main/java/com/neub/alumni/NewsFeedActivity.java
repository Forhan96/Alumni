package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsFeedActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference postRef, dataRef, uniqueRef;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    SharedPreferences sp;

    private RecyclerView recyclerView;
    public List<ModelClass> modelClassList;
    Adapter adapter;

    EditText postET;
    Button postBtn;
    String type, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        setTitle("News Feed");

        sp = getSharedPreferences("login",MODE_PRIVATE);
        type = sp.getString("userType", null);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.news_feed);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.news_feed:
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

        recyclerView= findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager= new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        modelClassList=new ArrayList<>();

        adapter = new Adapter(modelClassList);
        recyclerView.setAdapter(adapter);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        postRef = database.getReference().child("Posts").child(user.getUid());

        if (type.equals("Student")){
            dataRef = database.getReference().child("Users").child("Student").child(user.getUid());
        }
        else if (type.equals("Faculty")){
            dataRef = database.getReference().child("Users").child("Faculty").child(user.getUid());
        }
        else {
            dataRef = database.getReference().child("Users").child("Alumni").child(user.getUid());
        }

        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = snapshot.child("name").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        postET = findViewById(R.id.et_post_id);
        postBtn = findViewById(R.id.btn_post_id);

        postBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                String uniqueKey = rootRef.child("Posts").push().getKey();
                uniqueRef = rootRef.child("Posts").child(uniqueKey);
                postStatus();
            }
        });

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Posts");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelClassList.clear();
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                    String user = String.valueOf(dataSnapshot1.child("name").getValue());
                    String text = String.valueOf(dataSnapshot1.child("text").getValue());

                    modelClassList.add(new ModelClass(user, text));

                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void postStatus() {
        final String text  = postET.getText().toString();

        Map newPost = new HashMap();
        newPost.put("name", name);
        newPost.put("text", text);

        uniqueRef.setValue(newPost);
        Toast.makeText(getApplicationContext(),"Successfully Posted", Toast.LENGTH_SHORT).show();
    }
}