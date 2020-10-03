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
public class MyFavouritesFragment extends Fragment {
    private RecyclerView favRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String user;
    private String type;

    private String userUid;
    private View mainView;

    public MyFavouritesFragment() {
        // Required empty public constructor
    }

    public static MyFavouritesFragment newInstance(String param1, String param2) {
        MyFavouritesFragment fragment = new MyFavouritesFragment();
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
        mainView = inflater.inflate(R.layout.fragment_my_favourites,container,false);
        favRecyclerView = (RecyclerView)mainView.findViewById(R.id.my_fav_recycler_view);
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        favRecyclerView.setHasFixedSize(true);
        favRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Post, MyFavouritesFragment.PostViewHolder> postViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, MyFavouritesFragment.PostViewHolder>(
                Post.class,
                R.layout.user_list_item,
                MyFavouritesFragment.PostViewHolder.class,
                databaseReference//.orderByChild("user").equalTo(userUid)
        ) {
            @Override
            protected void populateViewHolder(final MyFavouritesFragment.PostViewHolder postViewHolder, final Post post, int i) {
                Log.d("DS",userUid);

                final String post_id = getRef(i).getKey();

                postViewHolder.mview.setOnClickListener(new View.OnClickListener() {
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
                                    Log.d("USER_TYPE_ONCLICK","USER " + type);
                                    if (type.equalsIgnoreCase("customer")) {
                                        databaseReference.child(post_id).child("favourite").child(userUid).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(getContext(), "Post removed from favourites succesfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                    else if (type.equalsIgnoreCase("designer")){
                                        databaseReference.child(post_id).removeValue(new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                                Toast.makeText(getContext(), "Post removed succesfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                }
                            }
                        });
                        builder.show();
                    }
                });

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getValue() != null) {
                            Log.d("sd", snapshot.getValue().toString());
                            Log.d("yoo", type.toString());

                            if (type.equalsIgnoreCase("customer")){
                                Log.d("yooo", type.toString());
                                if (post.getFavourite() != null) {
                                    Log.d("yoooo", type.toString());
                                    if (post.getFavourite().containsKey(userUid)) {
                                        Log.d("postDesc", post.description);
                                        Log.d("postFav", post.getFavourite().toString());
                                        postViewHolder.postDesc.setVisibility(View.VISIBLE);
                                        postViewHolder.custImage.setVisibility(View.VISIBLE);
                                        postViewHolder.itemUsernameView.setVisibility(View.VISIBLE);
                                        postViewHolder.setUsername(post.title);
                                        postViewHolder.setImage(post.image);
                                        postViewHolder.setDescription(post.description);
                                    }
                                } else {
                                    postViewHolder.postDesc.setVisibility(View.INVISIBLE);
                                    postViewHolder.custImage.setVisibility(View.INVISIBLE);
                                    postViewHolder.itemUsernameView.setVisibility(View.INVISIBLE);
                                }
                        }
                            else{
                                    if (post.user.equalsIgnoreCase(userUid)) {
                                        Log.d("postDesc", post.description);
                                        postViewHolder.postDesc.setVisibility(View.VISIBLE);
                                        postViewHolder.custImage.setVisibility(View.VISIBLE);
                                        postViewHolder.itemUsernameView.setVisibility(View.VISIBLE);
                                        postViewHolder.setUsername(post.title);
                                        postViewHolder.setImage(post.image);
                                        postViewHolder.setDescription(post.description);
                                    }

                                else {
                                    postViewHolder.postDesc.setVisibility(View.INVISIBLE);
                                    postViewHolder.custImage.setVisibility(View.INVISIBLE);
                                    postViewHolder.itemUsernameView.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                        else{
                            postViewHolder.mview.setEnabled(false);
                            postViewHolder.mview.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                //-------------------------------------------if user is a customer ------------------------------------


            }
        };
        favRecyclerView.setAdapter(postViewHolderFirebaseRecyclerAdapter);

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{


        View mview;
        TextView postDesc;
        TextView itemUsernameView;
        CircleImageView custImage;


        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;
            mview.findViewById(R.id.online_button).setVisibility(View.INVISIBLE);
            postDesc = (TextView)mview.findViewById(R.id.user_list_item_phone);
            itemUsernameView = (TextView)mview.findViewById(R.id.user_list_item_name);
            custImage = (CircleImageView)mview.findViewById(R.id.users_item_circular_image);
        }
        public void setDescription(String description){
            postDesc.setText(description);
        }

        public void setUsername(String username){
            itemUsernameView.setText(username);
        }

        public void setImage(String image ){
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(custImage);
        }

    }
}