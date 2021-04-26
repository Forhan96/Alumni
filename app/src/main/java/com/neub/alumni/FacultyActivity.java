package com.neub.alumni;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FacultyActivity extends AppCompatActivity {

    String image, dept, designation;
    private SearchAdapter adapter;
    private List<SearchItem> searchList = new ArrayList<>();

    RecyclerView recyclerView;

    DatabaseReference studentReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Faculty");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        this.setTitle("Faculty");

        fillSearchList();
//        setUpRecyclerView();
    }

    private void fillSearchList() {

        studentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.child("name").getValue().toString();
                    if (ds.hasChild("dept")) {
                        dept = ds.child("dept").getValue().toString();
                    }

                    if (ds.hasChild("url")) {
                        image = ds.child("url").getValue().toString();
                    }
                    if (ds.hasChild("designation")) {
                        designation = ds.child("designation").getValue().toString();
                    }
                    SearchItem item = new SearchItem(image, name, dept, designation);
                    searchList.add(item);
                }

                recyclerView = (RecyclerView) findViewById(R.id.recycler);
                recyclerView.setHasFixedSize(true);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FacultyActivity.this);
                adapter = new SearchAdapter(searchList);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_student);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }
}