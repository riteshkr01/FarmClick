package com.vthree.rentbaseapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vthree.rentbaseapplication.ModelClass.UserModel;
import com.vthree.rentbaseapplication.R;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends AppCompatActivity {

    private EditText editTextMobile;
    private TextView registertext;
    DatabaseReference databaseReference;
    List<UserModel> data;
    int flag = 0;
    String user_id,address,taluka,city,user_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        editTextMobile = findViewById(R.id.editTextMobile);
        registertext = (TextView) findViewById(R.id.registertext);
        data = new ArrayList<>();


        databaseReference = FirebaseDatabase.getInstance().getReference("RentBase").child("user");
        findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();

                if (mobile.isEmpty() || mobile.length() < 10) {
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                } else if (!mobile.equals("")) {
                    for (int i = 0; i < data.size(); i++) {
                        if (mobile.equals(data.get(i).getMobile())) {
                            Log.d("da11", data.get(i).getMobile());
                            user_id = data.get(i).getUser_id();
                            address=data.get(i).getAddress();
                            taluka=data.get(i).getTaluka();
                            city=data.get(i).getCity();
                            user_name=data.get(i).getUser_name();
                            flag = 1;
                        }
                    }
                    Log.d("da11", String.valueOf(flag));
                    if (flag == 1) {

                        Intent intent = new Intent(String.valueOf(LoginActivity.this));
                        intent.putExtra("mobile", mobile);
                        intent.putExtra("user_id", user_id);
                        intent.putExtra("user_name",user_name);
                        intent.putExtra("address",address);
                        intent.putExtra("taluka",taluka);
                        intent.putExtra("city",city);
                        startActivity(intent);

                    } else {
                        Toast.makeText(LoginActivity.this, "This number is not registered", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

        registertext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model = snapshot.getValue(UserModel.class);
                        data.add(model);

                        Log.d("da", model.getMobile().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

