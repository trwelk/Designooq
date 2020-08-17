package com.Tregaki.designooq;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerMyDownloads extends AppCompatActivity{

    private Toolbar mainTooldbar;
    private TabItem tabChats;
    private TabItem tabFriends;
    private TabItem tabPosts;
    private TabLayout mTabLayout;

    // Array of strings for ListView Title
    String[] listviewTitle = new String[]{
            "Design 001", "Design 002", "Design 003", "Design 004",
            "Design 005",
    };


    int[] listviewImage = new int[]{
            R.drawable.account_icon_tab, R.drawable.account_icon_tab, R.drawable.account_icon_tab, R.drawable.account_icon_tab,
            R.drawable.account_icon_tab,
    };

    String[] listviewShortDescription = new String[]{
            "Web Application UI", "Digital Arts", "Web Application UI", "Digital Arts",
            "Web Application UI",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        tabChats =  (TabItem) findViewById(R.id.chat_tab);
        tabFriends = (TabItem) findViewById(R.id.account_tab);
        tabPosts = (TabItem) findViewById(R.id.posts_tab);
        mTabLayout =(TabLayout)  findViewById(R.id.main_tab_layout);
        mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("My Downloads");


        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 5; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("listview_title", listviewTitle[i]);
            hm.put("listview_discription", listviewShortDescription[i]);
            hm.put("listview_image", Integer.toString(listviewImage[i]));
            aList.add(hm);
        }

        String[] from = {"listview_image", "listview_title", "listview_discription"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_short_description};

        SimpleAdapter simpleAdapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_activity, from, to);
        ListView androidListView = (ListView) findViewById(R.id.list_view);
        androidListView.setAdapter(simpleAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return  true;
    }
}