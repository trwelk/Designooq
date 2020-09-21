package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private Toolbar mainTooldbar;
    private DatabaseReference userDatabase;
    private DatabaseReference rootDatabase;


    private CircleImageView friendImage;
    private TextView friendUserName;
    private ImageButton chatSendButton;
    private ImageButton chatAddButton;
    private EditText chatEditText;


    private String friendNameString ;
    private String friendsImageString ;
    private String friendIdString;
    private String currentUserString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userDatabase = FirebaseDatabase.getInstance().getReference("user");
        rootDatabase = FirebaseDatabase.getInstance().getReference();
        currentUserString = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mainTooldbar = (Toolbar) findViewById(R.id.chat_app_bar);
        chatAddButton = (ImageButton)findViewById(R.id.chat_add_button);
        chatSendButton = (ImageButton) findViewById(R.id.chat_send_button);
        chatEditText = (EditText)findViewById(R.id.chat_edit_test);

        friendIdString = getIntent().getStringExtra("user_id");
        setSupportActionBar(mainTooldbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(actionBarView);

        friendImage = (CircleImageView)findViewById(R.id.custom_bar_image);
        friendUserName = (TextView) findViewById(R.id.custom_bar_username);


        userDatabase.child(friendIdString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendNameString = snapshot.child("username").getValue().toString();
                //actionBar.setTitle(friendName);
                friendsImageString = snapshot.child("image").getValue().toString();

                friendUserName.setText(friendNameString);
                Picasso.get().load(friendsImageString).placeholder(R.drawable.account_image).into(friendImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rootDatabase.child(currentUserString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map chatAddMap = new HashMap();
                chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                chatAddMap.put("seen", false);

                Map chatUserMap = new HashMap();
                chatUserMap.put("chat/" + currentUserString + "/" + friendIdString ,chatAddMap);
                chatUserMap.put("chat/" + friendIdString + "/" + currentUserString ,chatAddMap);

                rootDatabase.updateChildren(chatUserMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if(error != null){
                            Log.d("CHAT_LOG",error
                            .getMessage().toString());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageString = chatEditText.getText().toString();

        if(!TextUtils.isEmpty(messageString)){
            Map messageMap = new HashMap();
            String currentUserRef = "messages/" + currentUserString + "/" + friendIdString;
            String friendRef = "messages/" + friendIdString + "/" + currentUserString;
            messageMap.put(currentUserRef + "/message",messageString);
            //messageMap.put(currentUserRef + "/seen",false);


        }
    }
}