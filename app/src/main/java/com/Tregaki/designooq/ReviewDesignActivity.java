package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReviewDesignActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_review_design);

        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("Add Review");

        username = findViewById(R.id.profile_username_ext);
        phoneNumber = findViewById(R.id.profile_phone_text);
        email = findViewById(R.id.profile_email_text);
        sendMsgBtn = findViewById(R.id.profile_message_button);
        profileImage = findViewById(R.id.profile_image);
        databaseReference = FirebaseDatabase.getInstance().getReference("user").child("oo");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading user data");
        progressDialog.setMessage("Please wait while the users data gets loaded");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }
}