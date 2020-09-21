package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private TextView username;
    private TextView phoneNumber;
    private  TextView email;
    private Button sendMsgBtn;
    private ImageView profileImage ;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String uid = getIntent().getStringExtra("user_id");
        setContentView(R.layout.activity_profile);
        username = findViewById(R.id.profile_username_ext);
        phoneNumber = findViewById(R.id.profile_phone_text);
        email = findViewById(R.id.profile_email_text);
        sendMsgBtn = findViewById(R.id.profile_message_button);
        profileImage = findViewById(R.id.profile_image);
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading user data");
        progressDialog.setMessage("Please wait while the users data gets loaded");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String usernameText = snapshot.child("username").getValue().toString();
                String imageurl = snapshot.child("image").getValue().toString();
                String emailTxt = snapshot.child("email").getValue().toString();

                username.setText(usernameText);
                email.setText(emailTxt);
                Picasso.get().load(imageurl).placeholder(R.drawable.account_image).into(profileImage);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}