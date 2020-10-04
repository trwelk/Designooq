package com.Tregaki.designooq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatlistFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private String userUid;
    private View mainView;
    private User user;
    public ChatlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChatlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChatlistFragment newInstance(String param1, String param2) {
        ChatlistFragment fragment = new ChatlistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_chatlist,container,false);
        friendsRecyclerView = (RecyclerView)mainView.findViewById(R.id.friends_recycler_vievv);
        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("chat").child(userUid);
        friendsRecyclerView.setHasFixedSize(true);
        //friendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
        //      getContext()
        //));
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        //databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online").setValue(true);

        FirebaseRecyclerAdapter<Chat, FriendsFragment.UsersViewHolder> usersViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Chat, FriendsFragment.UsersViewHolder>(
                Chat.class,
                R.layout.user_list_item,
                FriendsFragment.UsersViewHolder.class,
                databaseReference
        ) {

            @Override
            protected void populateViewHolder(final FriendsFragment.UsersViewHolder usersViewHolder, final Chat chat, int i) {
                final String user_id = getRef(i).getKey();
                FirebaseDatabase.getInstance().getReference().child("user").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        user= new User();
                        user.setUsername(snapshot.child("username").getValue().toString());
                        usersViewHolder.setUsername(user.username);
                        usersViewHolder.setImage(snapshot.child("image").getValue().toString());
                        usersViewHolder.setType(snapshot.child("type").getValue().toString());
                        usersViewHolder.setOnline(Boolean.parseBoolean(snapshot.child("online").getValue().toString()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                /*usersViewHolder.setImage(user.image);
                usersViewHolder.setType(user.getType());
                usersViewHolder.setOnline(user.isOnline());*/

                usersViewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"View Profile","Send Message"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    //Intent profileIntent = new Intent(getContext(),DesignerAccountDetailsActivity.class);
                                    //profileIntent.putExtra("designer_id",user_id);
                                    //profileIntent.putExtra("type",user.getType());
                                   // profileIntent.putExtra("user_name",user.username);
                                   // startActivity(profileIntent);
                                }
                                else if(i == 1){
                                    Intent chatIntent = new Intent(getContext(),ChatActivity.class);
                                    chatIntent.putExtra("user_id",user_id);
                                    startActivity(chatIntent);
                                }
                            }
                        });
                        builder.show();
                    }
                });

            }
        };
        friendsRecyclerView.setAdapter(usersViewHolderFirebaseRecyclerAdapter);
    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
        }
        public void setOnline(boolean online){
            Button onlineButton = (Button)mview.findViewById(R.id.online_button);
            if(online)
                onlineButton.setBackgroundColor(Color.GREEN);
            else
                onlineButton.setBackgroundColor(Color.GRAY);

        }

        public void setUsername(String username){
            TextView itemUsernameView = (TextView)mview.findViewById(R.id.user_list_item_name);
            itemUsernameView.setText(username);
        }

        public void setImage(String image ){
            CircleImageView custImage = (CircleImageView)mview.findViewById(R.id.users_item_circular_image);
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(custImage);
        }

        public void setType(String type) {
            TextView itemType = (TextView)mview.findViewById(R.id.user_list_item_phone);
            itemType.setText(type);
        }
    }
}