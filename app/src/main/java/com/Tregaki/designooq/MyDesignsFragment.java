package com.Tregaki.designooq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyDesignsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyDesignsFragment extends Fragment {

    private String type;
    private RecyclerView friendsRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String user;
    private String userUid;
    private View mainView;

    public MyDesignsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            Intent signoutLogin = new Intent(getContext(), LoginActivity.class);
            startActivity(signoutLogin);
        }
            user = firebaseAuth.getCurrentUser().getUid().toString();
        FirebaseDatabase.getInstance().getReference().child("user").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                type = snapshot.child("type").getValue().toString();
                Log.d("ASDW",type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_friends,container,false);
        friendsRecyclerView = (RecyclerView)mainView.findViewById(R.id.friends_recycler_view);
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        friendsRecyclerView.setHasFixedSize(true);
        friendsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getContext()
        ));
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, MyDesignsFragment.UsersViewHolder> usersViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, MyDesignsFragment.UsersViewHolder>(
                Post.class,
                R.layout.user_list_item,
                MyDesignsFragment.UsersViewHolder.class,
                databaseReference.orderByChild("user").equalTo(userUid)
        ) {
            @Override
            protected void populateViewHolder(final MyDesignsFragment.UsersViewHolder usersViewHolder, final Post post, int i) {
                final String post_id = getRef(i).getKey();
                usersViewHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"Edit Post","Remove post"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    Intent profileIntent = new Intent(getContext(),DesignerAccountDetailsActivity.class);
                                    profileIntent.putExtra("post_id",post_id);
                                    profileIntent.putExtra("post_title",post.title);
                                    profileIntent.putExtra("post_description",post.getDescription());
                                    startActivity(profileIntent);
                                }
                                else if(i == 1){
                                    databaseReference.child(post_id).removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            Toast.makeText(getContext(),"Post removed succesfully",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        });
                        builder.show();
                    }
                });

                //-----------------------if user is a designer---------------------------------------------------------------------

                databaseReference.orderByChild("user").equalTo(userUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Log.d("sd", snapshot.getValue().toString());
                            usersViewHolder.setUsername(post.title);
                            usersViewHolder.setImage(post.image);
                            usersViewHolder.setDescription(post.description);

                        }
                        else{
                            usersViewHolder.mview.setEnabled(false);
                            usersViewHolder.mview.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //-------------------------------------------if user is a customer ------------------------------------
            }
        };
        friendsRecyclerView.setAdapter(usersViewHolderFirebaseRecyclerAdapter);

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder{

        View mview;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
            mview.findViewById(R.id.online_button).setVisibility(View.INVISIBLE);
        }
        public void setDescription(String description){
            TextView postDesc = (TextView)mview.findViewById(R.id.user_list_item_phone);
            postDesc.setText(description);
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