package com.Tregaki.designooq;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendsFragment extends Fragment {

    private RecyclerView friendsRecyclerView;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private String userUid;
    private View mainView;

    public FriendsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
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
        mainView = inflater.inflate(R.layout.fragment_friends,container,false);
        friendsRecyclerView = (RecyclerView)mainView.findViewById(R.id.friends_recycler_view);
        firebaseAuth = FirebaseAuth.getInstance();
        userUid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("user");
        friendsRecyclerView.setHasFixedSize(true);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return mainView;

    }

    @Override
    public void onStart() {
        super.onStart();
        //databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("online").setValue(true);

        FirebaseRecyclerAdapter<User,UsersViewHolder> usersViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, UsersViewHolder>(
                User.class,
                R.layout.user_list_item,
                UsersViewHolder.class,
                databaseReference
        ) {
            @Override
            protected void populateViewHolder(UsersViewHolder usersViewHolder, final User user, int i) {
                usersViewHolder.setUsername(user.username);
                usersViewHolder.setImage(user.image);
                final String user_id = getRef(i).getKey();

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
                                    Intent profileIntent = new Intent(getContext(),CustomerAccountActivity.class);
                                    profileIntent.putExtra("user_id",user_id);
                                    profileIntent.putExtra("user_name",user.username);
                                    startActivity(profileIntent);
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
                //add on click method here

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
        public void setOnline(String online){

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