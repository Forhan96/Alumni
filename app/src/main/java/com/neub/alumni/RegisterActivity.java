package com.neub.alumni;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.neub.alumni.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText reg_name_et, reg_email_et, reg_phone_no_et, reg_password_et;
    RadioGroup radio_group;

    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    int selectedId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.setTitle("Sign Up");

        mAuth = FirebaseAuth.getInstance();


        Button btn_sign_up = findViewById(R.id.btn_sign_up_id);
        radio_group = findViewById(R.id.radio_group_id);
        reg_name_et = findViewById(R.id.reg_name_et_id);
        reg_password_et = findViewById(R.id.reg_password_et_id);
        reg_email_et = findViewById(R.id.reg_email_et_id);
        reg_phone_no_et = findViewById(R.id.reg_phone_no_et_id);


        btn_sign_up.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_sign_up_id:
                userRegister();
                Intent intent = new Intent(getApplicationContext(), LoginChoiseActivity.class );
                startActivity(intent);

        }
    }

    private void userRegister(){

        final String name  = reg_name_et.getText().toString();
        final String email  = reg_email_et.getText().toString();
        final String password  = reg_password_et.getText().toString();
        final String phone_no  = reg_phone_no_et.getText().toString().trim();

        if(name.isEmpty()){
            reg_name_et.setError("Enter your name");
            reg_name_et.requestFocus();
            return;
        }

        if(email.isEmpty()){
            reg_email_et.setError("Enter an email address");
            reg_email_et.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            reg_email_et.setError("Enter a valid email address");
            reg_email_et.requestFocus();
            return;
        }

        if(phone_no.isEmpty()){
            reg_phone_no_et.setError("Enter your phone no.");
            reg_phone_no_et.requestFocus();
            return;
        }

        //Validity check for password
        if(password.isEmpty()){
            reg_password_et.setError("Enter a password");
            reg_password_et.requestFocus();
            return;
        }

        if(password.length()<6){
            reg_password_et.setError("Minimum length of password should be 6");
            reg_password_et.requestFocus();
        }

        selectedId = radio_group.getCheckedRadioButtonId();

        //DatabaseReference current_user_db;
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    String user_id = mAuth.getCurrentUser().getUid();
                    DatabaseReference current_user_db;

                    if (selectedId == R.id.rb_alumni_id){
                        current_user_db = database.getReference().child("Users").child("Alumni").child(user_id);
                    }
                    else if(selectedId == R.id.rb_student_id){
                        current_user_db = database.getReference().child("Users").child("Student").child(user_id);
                    }
                    else{
                        current_user_db = database.getReference().child("Users").child("Faculty").child(user_id);
                    }

                    Map newPost = new HashMap();
                    newPost.put("name", name);
                    newPost.put("email", email);
                    newPost.put("phone_no", phone_no);

                    current_user_db.setValue(newPost);
                    Toast.makeText(getApplicationContext(),"Registration Successful", Toast.LENGTH_SHORT).show();
                }

                else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(),"User is already Registered", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error : "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });



    }


}