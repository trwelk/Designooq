package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerHomeActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ViewPager viewPager;

    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;
    private DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);

        mAuth = FirebaseAuth.getInstance();
        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("Designooq");
        viewPager = findViewById(R.id.mainPager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = (TabLayout)findViewById(R.id.main_tab_layout);
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("LOG_AUTH", String.valueOf(currentUser == null));

        if(currentUser == null){
            Log.d("LOG_AUTH","AUTH FAILED");
            Intent signoutLogin = new Intent(CustomerHomeActivity.this, LoginActivity.class);
            startActivity(signoutLogin);
            sendToLogin();
        }
        else{
            userDatabase = FirebaseDatabase.getInstance().getReference("user").child(mAuth.getCurrentUser().getUid());

            userDatabase.child("online").setValue(true);
            //Toast.makeText(getApplicationContext(),"Online",Toast.LENGTH_LONG);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        userDatabase.child("online").setValue(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         super.onCreateOptionsMenu(menu);
            getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.main_logout_btn) {
            mAuth.signOut();
            sendToLogin();
        }
        else if(item.getItemId() == R.id.main_settings_btn){
            Intent settingsIntent = new Intent(CustomerHomeActivity.this,CustomerAccountActivity.class);
            startActivity(settingsIntent);
        }
        else if(item.getItemId() == R.id.main_allUsers_btn){
            Intent usersIntent = new Intent(CustomerHomeActivity.this,AddNewPostActivity.class);
            startActivity(usersIntent);
        }
        return true;

    }

    private void sendToLogin() {
        Intent signoutLogin = new Intent(CustomerHomeActivity.this, LoginActivity.class);
        startActivity(signoutLogin);
    }
}