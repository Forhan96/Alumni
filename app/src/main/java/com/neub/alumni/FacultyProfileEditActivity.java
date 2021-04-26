package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class FacultyProfileEditActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference profileEditRef;
    ImageView facultyProfileImage;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    private static final int CHOOSE_IMAGE = 101;
    String nameR, phoneR, emailR, deptR, designationR, bioR;
    EditText pName, pPhone, pDept, pDesignation, pBio;
    Button submit;

    Uri uriProfilePicture;
    String profilePictureUrl, picture, type;

    SharedPreferences sp;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile_edit);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        sp = getSharedPreferences("login",MODE_PRIVATE);
        type = sp.getString("userType", null);

        //profileImage = findViewById(R.id.profileImage);
        facultyProfileImage = findViewById(R.id.faculty_image_edit);
        pName = findViewById(R.id.faculty_name_edit);
        pPhone = findViewById(R.id.faculty_phone_edit);
        pDept = findViewById(R.id.faculty_department_edit);
        pDesignation = findViewById(R.id.faculty_designation_edit);
        pBio = findViewById(R.id.faculty_bio_edit);

        submit = findViewById(R.id.btn_faculty_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
                Intent profile = new Intent(getApplicationContext(), FacultyProfileActivity.class);
                profile.putExtra("Type", type);
                startActivity(profile);
            }
        });
        facultyProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        profileEditRef = database.getReference().child("Users").child("Faculty").child(user.getUid());
        profileEditRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("url")){
                    picture = dataSnapshot.child("url").getValue().toString();
                    Glide.with(getApplicationContext())
                            .load(picture)
                            .into(facultyProfileImage);
                }
                nameR = dataSnapshot.child("name").getValue().toString();
                pName.setText(nameR);
                phoneR = dataSnapshot.child("phone_no").getValue().toString();
                pPhone.setText(phoneR);
                emailR = dataSnapshot.child("email").getValue().toString();
                if (dataSnapshot.hasChild("dept")){
                    deptR = dataSnapshot.child("dept").getValue().toString();
                    pDept.setText(deptR);
                }
                if (dataSnapshot.hasChild("designation")){
                    designationR = dataSnapshot.child("designation").getValue().toString();
                    pDesignation.setText(designationR);
                }
                if (dataSnapshot.hasChild("bio")){
                    bioR = dataSnapshot.child("bio").getValue().toString();
                    pBio.setText(bioR);
                }

//                if (picture != null){
//                    Picasso.get().load(picture).into(alumniProfileImage);
//                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void uploadImageToFirebaseStorage() {
        final StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference("profilePics/"+System.currentTimeMillis() + ".jpg");

        if(uriProfilePicture != null){
//            imageProgressbar.setVisibility(View.VISIBLE);
            profilePictureRef.putFile(uriProfilePicture)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profilePictureRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profilePictureUrl = uri.toString();
                                    Log.i("Url", profilePictureUrl);
                                }
                            });
//                            imageProgressbar.setVisibility(View.GONE);
                            //profilePictureUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
//                            imageProgressbar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImageChooser(){
        Intent intent1 = new Intent();
        intent1.setType("image/*");
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent1, CHOOSE_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null){
            uriProfilePicture = data.getData();
            Picasso.get().load(uriProfilePicture).into(facultyProfileImage);
            uploadImageToFirebaseStorage();
        }
    }

    private void updateProfile() {
        final String name  = pName.getText().toString();
        final String phone  = pPhone.getText().toString();
        final String dept  = pDept.getText().toString();
        final String designation  = pDesignation.getText().toString();
        final String bio  = pBio.getText().toString();

//        String user_id = mAuth.getCurrentUser().getUid();
//        profileEditRef = database.getReference().child("Users").child("Alumni").child(user_id);
        Map newPost = new HashMap();
        newPost.put("name", name);
        newPost.put("phone_no", phone);
        newPost.put("email", emailR);
        newPost.put("dept", dept);
        newPost.put("designation", designation);
        newPost.put("bio", bio);
        newPost.put("url", profilePictureUrl);

        profileEditRef.setValue(newPost);
        Toast.makeText(getApplicationContext(),"Profile Updated Successfully", Toast.LENGTH_SHORT).show();
    }
}