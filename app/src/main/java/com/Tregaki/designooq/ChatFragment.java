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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

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
        postRecylerView.setHasFixedSize(true);
        postRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mainView =  inflater.inflate(R.layout.fragment_chat, container, false);



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
            protected void populateViewHolder(PostHolder postHolder, Post post, int i) {
                postHolder.setImage(post.getImage());
                postHolder.setUser(post.getUser());
                postHolder.setDescription(post.getDescription());
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

        public void setUser(String user) {
            //TextView postUploader = mview.findViewById(R.id.single_post_user_image);
        }

        public void setDescription(String description) {
            TextView postDescription = mview.findViewById(R.id.single_post_description);
            postDescription.setText(description);
        }
    }


}