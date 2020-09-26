package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    //Database reference
    private DatabaseReference userDatabase;
    private DatabaseReference rootDatabase;


    //UI ELEMENTS
    private CircleImageView friendImage;
    private Toolbar mainTooldbar;
    private TextView friendUserName;
    private ImageButton chatSendButton;
    private ImageButton chatAddButton;
    private EditText chatEditText;
    private SwipeRefreshLayout messagesSwipeLayout;
    private RecyclerView messagesListRecylerView;
    private final List<Messages> messagesList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private MessageAdapter messageAdapter;

    //Member variables
    private String friendNameString ;
    private String friendsImageString ;
    private String friendIdString;
    private String currentUserString;
    private static final int TOTAL_MESSAGES_TO_LOAD = 10;
    private static int currentPage = 0;
    private int messagePosition = 0;
    private String lastKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //Iniializing database reference
        userDatabase = FirebaseDatabase.getInstance().getReference("user");
        rootDatabase = FirebaseDatabase.getInstance().getReference();
        //accessing current user and friend info
        currentUserString = FirebaseAuth.getInstance().getCurrentUser().getUid();
        friendIdString = getIntent().getStringExtra("user_id");

        //Initializing ui elements
        mainTooldbar = (Toolbar) findViewById(R.id.chat_app_bar);
        chatAddButton = (ImageButton)findViewById(R.id.chat_add_button);
        chatSendButton = (ImageButton) findViewById(R.id.chat_send_button);
        chatEditText = (EditText)findViewById(R.id.chat_edit_test);
        messagesListRecylerView = (RecyclerView) findViewById(R.id.chat_messages_list_recycler_view);
        messagesSwipeLayout = (SwipeRefreshLayout)findViewById(R.id.chat_swipe_message_swipe_layout);
        linearLayoutManager = new LinearLayoutManager(this);



        //retrieing messages
        messagesListRecylerView.setHasFixedSize(true);
        messagesListRecylerView.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(messagesList);
        messagesListRecylerView.setAdapter(messageAdapter);

        //configuring the appbar
        setSupportActionBar(mainTooldbar);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater layoutInflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View actionBarView = layoutInflater.inflate(R.layout.chat_custom_bar,null);
        actionBar.setCustomView(actionBarView);

        //subscribing to user database to get friends information
        userDatabase.child(friendIdString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendImage = (CircleImageView)findViewById(R.id.custom_bar_image);
                friendUserName = (TextView) findViewById(R.id.custom_bar_username);
                friendNameString = snapshot.child("username").getValue().toString();
                //actionBar.setTitle(friendName);
                friendsImageString = snapshot.child("image").getValue().toString();

                //Setting friend user name and image in the app bar
                friendUserName.setText(friendNameString);
                Picasso.get().load(friendsImageString).placeholder(R.drawable.account_image).into(friendImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //-----------------------------------------------------------------------------------------------------------------------
        //creating a new document in the chat document if the user has not already chatted with this person
        rootDatabase.child(currentUserString).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> chatAddMap = new HashMap<>();
                chatAddMap.put("timestamp", ServerValue.TIMESTAMP);
                chatAddMap.put("seen", false);

                Map<String, Object> chatUserMap = new HashMap<>();
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
        //-----------------------------------------------------------------------------------------------------------------------
        //invoking the send message methon when the click event is fired
        chatSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
                chatEditText.setText("");
            }
        });
        //not implemented yet getting errors due to this
        messagesSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage++;
                messagePosition = 0 ;
                //loadMoreMessages();
                messagesSwipeLayout.setRefreshing(false);

            }
        });
    }

    //load all the current messages as the activity starts
    @Override
    protected void onStart() {
        super.onStart();
        loadMessages();
    }

    //-----------------------------------------------------------------------------------------------------------------------
    //logic to load the ui elements of the messages and display them
    private void loadMessages() {
        //query to fetch the messages in the messages document
        Query messageQuery = rootDatabase.child("messages")
                .child(currentUserString)
                .child(friendIdString);
                //.limitToLast(TOTAL_MESSAGES_TO_LOAD * (currentPage + 1));

        Log.d("CHAT_LOG","Messages Loading : " + this.getClass().getName());
        //subscriing to the messages of the current user
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message = snapshot.getValue(Messages.class);
                Log.d("CHAT_LOG","Adding" + message.getMessage());
                messagePosition++;
                if(messagePosition == 1){
                    String messageKey = snapshot.getKey();
                    lastKey = messageKey;
                }
                messagesList.add(message);
                messageAdapter.notifyDataSetChanged();
                messagesListRecylerView.scrollToPosition(messagesList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    //-----------------------------------------------------------------------------------------------------------------------
    //obsolete method for later use
    private void loadMoreMessages() {
        Query messageQuery = rootDatabase.child("messages")
                .child(currentUserString)
                .child(friendIdString)
                .orderByKey()
                .endAt(lastKey)
                .limitToLast(10);

        Log.d("CHAT_LOG","Loading");
        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Messages message = snapshot.getValue(Messages.class);
                Log.d("CHAT_LOG","Adding" + message.getMessage());
                if(messagePosition == 1){
                    String messageKey = snapshot.getKey();
                    lastKey = messageKey;
                }
                messagesList.add(messagePosition++,message);
                messageAdapter.notifyDataSetChanged();

                messagesListRecylerView.scrollToPosition(messagesList.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //Logic for sending the message is written here
    private void sendMessage() {
        //subscribing to the message
        String messageString = chatEditText.getText().toString();
        //checking if the user has typed anything

        if(!TextUtils.isEmpty(messageString)){

            Log.d("CHAT_LOG","MESSAGE VALIDATED : " + this.getClass().getName());
            Map<String, Object> messageMap = new HashMap<>();
            String currentUserRef = "messages/" + currentUserString + "/" + friendIdString;
            String friendRef = "messages/" + friendIdString + "/" + currentUserString;
            DatabaseReference userMessageRef = rootDatabase.child("messages")
                    .child(currentUserString).child(friendIdString).push();
            String pushId = userMessageRef.getKey();

            //values for a single message item
            messageMap.put("message",messageString);
            messageMap.put("seen",false);
            messageMap.put("type" , "text");
            messageMap.put("time" , ServerValue.TIMESTAMP);
            messageMap.put("from" , currentUserString);

            Map<String, Object> messageUserMap = new HashMap<String, Object>();

            messageUserMap.put(currentUserRef  + "/" + pushId, messageMap );
            messageUserMap.put(friendRef  + "/" + pushId, messageMap );

            rootDatabase.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                    if(error == null){
                        Log.d("CHAT_LOG","MESSAGE SENT SUCCESFULLY : " + this.getClass().getName());
                    }
                    else{
                        Log.d("CHAT_LOG","ERROR : " + error.getMessage() +  " : "+ this.getClass().getName());
                    }
                }
            });

        }
    }
}