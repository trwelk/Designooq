package com.Tregaki.designooq;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int RESULT_OK = -1;
    private DatabaseReference databaseReference;
    private DatabaseReference userdatabaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView postRecylerView;
    private                  String uploaderId;
    private String type;
    private ImageButton addPostButton;


    private String userUid;
    private View mainView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostsFragment() {
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
        mainView = inflater.inflate(R.layout.fragment_chat, container, false);
        postRecylerView = (RecyclerView) mainView.findViewById(R.id.post_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("post");
        userdatabaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        postRecylerView.setHasFixedSize(true);
        postRecylerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addPostButton = (ImageButton)mainView.findViewById(R.id.posts_fragment_add_new_post);

        return mainView;
    }

    public void downloadFile(String uRl) {
        File direct = new File(String.valueOf(Environment.getExternalStorageDirectory()));

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, "fileName.jpg");

        Toast.makeText(getContext(),"Image Downloaded",Toast.LENGTH_SHORT).show();
        mgr.enqueue(request);

    }

    @Override
    public void onStart() {
        super.onStart();

        String currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        FirebaseDatabase.getInstance().getReference().child("user").child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String userType;
                userType = snapshot.child("type").getValue().toString();
                Log.d("POSTSFRAGMENT",userType);

                if(!userType.equalsIgnoreCase("designer")){
                    Log.d("POSTSFRAGMENT",userType);
                    addPostButton.setVisibility(View.INVISIBLE);
                    addPostButton.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addPostIntent = new Intent(getContext(),AddNewPostActivity.class);
                startActivity(addPostIntent);
            }
        });
        FirebaseRecyclerAdapter<Post, ChatFragment.PostHolder> postFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Post, ChatFragment.PostHolder>(
                Post.class,
                R.layout.single_post_item,
                ChatFragment.PostHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(final ChatFragment.PostHolder postHolder, final Post post, int i) {
                postHolder.setImage(post.getImage());
                postHolder.setDescription(post.getDescription());
                final String post_id = getRef(i).getKey();
                ImageView downloadButton = (ImageView)postHolder.mview.findViewById(R.id.post_download_button);
                downloadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        downloadFile(post.getImage());
                    }
                });

                ImageButton favButton = (ImageButton)postHolder.mview.findViewById(R.id.post_favourite_button);
                favButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String, Object> postAddMap = new HashMap<>();
                        postAddMap.put("user", userUid);
                        postAddMap.put("month", Integer.toString(new Date().getMonth()));


                        Map<String, Object> postMainMap = new HashMap<>();
                        postMainMap.put("post/" + post_id + "/favourite/" + userUid, postAddMap);
                        if(post.getFavourite() != null) {
                            if (post.getFavourite().containsKey(userUid)) {
                                FirebaseDatabase.getInstance().getReference().child("post").child(post_id).child("favourite").child(userUid).removeValue();
                            } else {
                                FirebaseDatabase.getInstance().getReference().updateChildren(postMainMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error != null) {
                                            Log.d("POST_LOG", error
                                                    .getMessage().toString());
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            FirebaseDatabase.getInstance().getReference().updateChildren(postMainMap, new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    if (error != null) {
                                        Log.d("POST_LOG", error
                                                .getMessage().toString());
                                    }
                                }
                            });
                        }
                    }
                });


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

                if( post.getFavourite() != null){
                    if (post.getFavourite().containsKey(userUid))
                    ((ImageButton) postHolder.mview.findViewById(R.id.post_favourite_button)).setImageResource(R.drawable.heartred);
                }

                getRef(i).child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        uploaderId = snapshot.getValue().toString();
                        Log.d("Trs",uploaderId);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                postHolder.mview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"View designer Profile","Send Message"};

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0){
                                    Intent profileIntent = new Intent(getContext(),DesignerAccountDetailsActivity.class);
                                    profileIntent.putExtra("designer_id",uploaderId);
                                    profileIntent.putExtra("type",type);
                                    startActivity(profileIntent);
                                }
                                else if(i == 1){
                                    Intent chatIntent = new Intent(getContext(),ChatActivity.class);
                                    chatIntent.putExtra("user_id",uploaderId);
                                    startActivity(chatIntent);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        };
        postRecylerView.setAdapter(postFirebaseRecyclerAdapter);
    }

    public static class PostHolder extends RecyclerView.ViewHolder {
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

        public void setuserImage(String image) {
            CircleImageView userImage = (CircleImageView) mview.findViewById(R.id.single_post_user_image);
            Picasso.get().load(image).placeholder(R.drawable.account_image).into(userImage);

        }
    }

}