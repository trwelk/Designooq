package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
            protected void populateViewHolder(UsersViewHolder usersViewHolder, User user, int i) {
                usersViewHolder.setUsername(user.username);
                usersViewHolder.setImage(user.image);

                final String user_id = getRef(i).getKey();
                usersViewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(UsersActivity.this,ProfileActivity.class);
                        profileIntent.putExtra("user_id",user_id);
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

    }
}