package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class CustomerAccountActivity extends AppCompatActivity {
    private DatabaseReference userDb;
    private FirebaseUser currentUser;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button changeImageBtn;
    private Button changeDetailsButton;
    private Button myDownloadsButton;
    private String user;
    private StorageReference mStorageRef;
    private CircleImageView customerImg;
    private ProgressDialog progressDialog;
    private String type;
    private Button changePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);

        Toolbar mainTooldbar = (Toolbar) findViewById(R.id.main_page_toolbar);

        setSupportActionBar(mainTooldbar);
        getSupportActionBar().setTitle("My Account");

        mStorageRef = FirebaseStorage.getInstance().getReference();
        username = (EditText)findViewById(R.id.customer_account_username);
        email = (EditText) findViewById(R.id.customer_account_email);
        password =(EditText) findViewById(R.id.customer_account_password);
        changeImageBtn = (Button)findViewById(R.id.customer_account_change_image_button);
        customerImg = (CircleImageView) findViewById(R.id.customer_account_image_view);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        changeDetailsButton = (Button)findViewById(R.id.customer_account_change_details_button);
        myDownloadsButton = (Button)findViewById(R.id.account_my_downloads_button);
        user = currentUser.getUid();
        userDb = FirebaseDatabase.getInstance().getReference("user").child(user);
        userDb.keepSynced(true);
        changePassword = (Button)findViewById(R.id.customer_account_change_password_button);

        userDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String nameString = snapshot.child("username").getValue().toString();
                String emailString = snapshot.child("email").getValue().toString();
                type = snapshot.child("type").getValue().toString();
                if(!type.equalsIgnoreCase("designer")){
                    myDownloadsButton.setVisibility(View.INVISIBLE);
                    myDownloadsButton.setEnabled(false);
                }
                final String imageString = snapshot.child("image").getValue().toString();
                //String phoneString = snapshot.child("phone").getValue().toString();
                System.out.println(nameString);
                username.setText(nameString);
                email.setText(emailString);
                if(!imageString.equalsIgnoreCase("default")) {
                    //Picasso.get().load(imageString).placeholder(R.drawable.account_image).into(customerImg);
                    Picasso.get().load(imageString).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.account_image)
                            .into(customerImg, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(Exception e) {
                                    Picasso.get().load(imageString).placeholder(R.drawable.account_image).into(customerImg);
                                }
                            });

                }
                //phone.setText(phoneString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallaryIntent,"Select image"),1);

            }
        });

        changeDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map userMap = new HashMap();
                userMap.put("username",username.getText().toString());
                userDb.updateChildren(userMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        if (error == null){
                            Toast.makeText(getApplicationContext(),"Updated succesfully",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"An Error occured",Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        myDownloadsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadsIntent = new Intent(CustomerAccountActivity.this,DownloadsActivty.class);
                startActivity(downloadsIntent);
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //----
                FirebaseAuth.getInstance().sendPasswordResetEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Password Reset Email Sent!",Toast.LENGTH_LONG);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"An Error occured",Toast.LENGTH_LONG);
                                }
                            }
                        });
                //---
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            //CropImage.activity(imageUri)
             //       .start(this);
            progressDialog = new ProgressDialog(CustomerAccountActivity.this);
            progressDialog.setTitle("Uploading Image");
            progressDialog.setMessage("Please wait while we upload the profile image");
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();

            final StorageReference filePath = mStorageRef.child("profile_image").child(user + ".jpg");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                userDb.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        progressDialog.dismiss();
                                    }
                             }
                            });
                            }
                        });

                        Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Image Upload failed",Toast.LENGTH_SHORT);
                    }
                }
            });
        }


    }
}