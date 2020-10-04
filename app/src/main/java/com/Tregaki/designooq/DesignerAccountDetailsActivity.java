package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DesignerAccountDetailsActivity extends AppCompatActivity {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private CircleImageView imageView;
    private EditText userName;
    private EditText userEmail;
    private EditText userPhone;
    private EditText userWebsite;
    private TextView sendMsgButton;
    private ImageView sendMsgImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_account_details);

        //ui elements referene
        userEmail = (EditText)findViewById(R.id.designer_account_email);
        userName = (EditText) findViewById(R.id.designer_account_username);
        userPhone = (EditText)findViewById(R.id.designer_account_phone);
        userWebsite = (EditText)findViewById(R.id.designer_account_website);
        imageView = (CircleImageView) findViewById(R.id.designer_account_image);
    sendMsgButton = (TextView) findViewById(R.id.designer_send_message_text);
        sendMsgImg = (ImageView)  findViewById(R.id.designer_send_message_image);

        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("Account Details");

        Intent currentIntent = getIntent();
        final String designerId = currentIntent.getStringExtra("designer_id");
        final String[] type = {currentIntent.getStringExtra("type")};


        sendMsgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getApplicationContext(),ChatActivity.class);
                chatIntent.putExtra("user_id",designerId);
                startActivity(chatIntent);
            }
        });

        sendMsgImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(getApplicationContext(),ChatActivity.class);
                chatIntent.putExtra("user_id",designerId);
                startActivity(chatIntent);
            }
        });

        databaseReference.child("user").child(designerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userEmail.setText(snapshot.child("email").getValue().toString());
                userName.setText(snapshot.child("username").getValue().toString());
                type[0] = snapshot.child("type").getValue().toString();
                if(type[0].equals("designer")) {
                    userPhone.setText(snapshot.child("phone").getValue().toString());
                    userWebsite.setText(snapshot.child("website").getValue().toString());
                }
                else {
                    userPhone.setVisibility(View.INVISIBLE);
                    userWebsite.setVisibility(View.INVISIBLE);
                }
                Picasso.get().load(snapshot.child("image").getValue().toString()).placeholder(R.drawable.account_image).into(imageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}