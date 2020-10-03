package com.Tregaki.designooq;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private DatabaseReference databaseReference;
    private DatabaseReference userdatabaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView postRecylerView;

    private String userUid;
    private View mainView;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
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
        mainView = inflater.inflate(R.layout.fragment_chat,container,false);
        postRecylerView = (RecyclerView)mainView.findViewById(R.id.post_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        postRecylerView.setHasFixedSize(true);
        postRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));



        return mainView;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Post,PostHolder> postFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, PostHolder>(
                Post.class,
                R.layout.single_post_item,
                PostHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final PostHolder postHolder, final Post post, int i) {
                postHolder.setImage(post.getImage());
                postHolder.setDescription(post.getDescription());
                userdatabaseReference.child(post.getUser()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        postHolder.setuserImage(snapshot.child("image").getValue().toString());
                        postHolder.setUserName(snapshot.child("username").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        };
        postRecylerView.setAdapter(postFirebaseRecyclerAdapter);
    }

    public static class PostHolder extends RecyclerView.ViewHolder{
        View mview;
        public PostHolder(@NonNull View itemView) {
            super(itemView);
            mview = itemView;

        }


        public void setImage(String image) {
            ImageView postImage = mview.findViewById(R.id.single_post_post_image);
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(postImage);

        }

        public void setUserName(String user) {
            //TextView postUploader = mview.findViewById(R.id.single_post_user_image);
            TextView userUserName = mview.findViewById(R.id.single_post_user_name);
            userUserName.setText(user);
        }

        public void setDescription(String description) {
            TextView postDescription = mview.findViewById(R.id.single_post_description);
            postDescription.setText(description);
        }
        public void setTitle(String title) {
            TextView postTitle = mview.findViewById(R.id.single_post_title);
            postTitle.setText(title);
        }
        public void setuserImage(String image) {
            CircleImageView userImage = (CircleImageView)mview.findViewById(R.id.single_post_user_image);
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(userImage);

        }
    }


}