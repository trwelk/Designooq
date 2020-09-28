package com.Tregaki.designooq;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    private String user = FirebaseAuth.getInstance().getUid();
    private String userType = new String();


    public void SectionsPagerAdapter(){
        FirebaseDatabase.getInstance().getReference().child("user").child(user).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("PAGER_ADAPTER",snapshot.toString());
                userType = snapshot.child("type").getValue().toString();
                Log.d("PAGER_ADAPTER",userType.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FriendsFragment friendsFragment = new FriendsFragment();
                return  friendsFragment;
            case 1:
                MyDesignsFragment myDesignsFragment = new MyDesignsFragment();
                return myDesignsFragment;
            case 2:
                PostsFragment postsFragment = new PostsFragment();
                return postsFragment;
            default:
                    ChatFragment chattFragment = new ChatFragment();
                    return chattFragment;        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:

                return  "Users";
            case 1:
                return "Over View";
            case 2:
                return "Designs";
            default:
                return "Something";
        }
    }
}
