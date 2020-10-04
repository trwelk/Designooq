package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView usersRecyclerView;
    private DatabaseReference databaseReference;
    //private itemView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        toolbar = (Toolbar)findViewById(R.id.users_appbar);
        usersRecyclerView = (RecyclerView)findViewById(R.id.users_recycler_view);
        usersRecyclerView.setHasFixedSize(true);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");


    setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<User,UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
                User.class,R.layout.user_list_item,UsersViewHolder.class,databaseReference
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, final User user, int i) {
                usersViewHolder.setUsername(user.username);
                usersViewHolder.setImage(user.image);
                usersViewHolder.setType(user.getType());
                usersViewHolder.setOnline(user.isOnline());

                final String user_id = getRef(i).getKey();
                usersViewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"View Profile","Send Message"};


                                    Intent profileIntent = new Intent(getApplicationContext(),DesignerAccountDetailsActivity.class);
                                    profileIntent.putExtra("designer_id",user_id);
                                    profileIntent.putExtra("type",user.getType());
                                    profileIntent.putExtra("user_name",user.username);
                                    startActivity(profileIntent);
                              
                    }
                });

            }
        } ;
        usersRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setUsername(String username){
            TextView itemUsernameView = (TextView)mview.findViewById(R.id.user_list_item_name);
            itemUsernameView.setText(username);
        }

        public void setImage(String image ){
            CircleImageView custImage = (CircleImageView)mview.findViewById(R.id.users_item_circular_image);
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(custImage);
        }
        public void setOnline(boolean online){
            Button onlineButton = (Button)mview.findViewById(R.id.online_button);
            if(online)
                onlineButton.setBackgroundColor(Color.GREEN);
            else
                onlineButton.setBackgroundColor(Color.GRAY);

        }

        public void setType(String type) {
            TextView itemType = (TextView)mview.findViewById(R.id.user_list_item_phone);
            itemType.setText(type);
        }
    }
}