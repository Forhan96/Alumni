package com.neub.alumni;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchingActivity extends AppCompatActivity implements View.OnClickListener {

    TextView sign_up_tv;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launching);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        String type = sp.getString("userType", null);

        if(sp.getBoolean("logged",false)){
            Intent intent = new Intent(getApplicationContext(), NewsFeedActivity.class);
            intent.putExtra("Type", type);
            startActivity(intent);
        }


        Button btn_sign_in = findViewById(R.id.btn_sign_in);
        sign_up_tv = findViewById(R.id.sign_up_tv);


        btn_sign_in.setOnClickListener(this);
        sign_up_tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_sign_in:
                Intent intent = new Intent(getApplicationContext(), LoginChoiseActivity.class);
                startActivity(intent);
                break;

            case R.id.sign_up_tv:
                Intent intent1 = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent1);
                break;


        }

    }
}