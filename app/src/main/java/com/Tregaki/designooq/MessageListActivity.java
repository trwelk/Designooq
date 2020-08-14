package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MessageListActivity extends AppCompatActivity {
    private Toolbar mainTooldbar;
    private TabItem tabChats;
    private TabItem tabFriends;
    private TabItem tabPosts;
    private TabLayout mTabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        tabChats =  (TabItem) findViewById(R.id.chat_tab);
        tabFriends = (TabItem) findViewById(R.id.account_tab);
        tabPosts = (TabItem) findViewById(R.id.posts_tab);
        mTabLayout =(TabLayout)  findViewById(R.id.main_tab_layout);

    }
}