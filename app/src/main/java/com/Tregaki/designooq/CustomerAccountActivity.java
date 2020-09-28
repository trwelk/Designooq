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

import id.zelory.compressor.Compressor;

public class CustomerAccountActivity extends AppCompatActivity {
    private DatabaseReference userDb;
    private FirebaseUser currentUser;
    private EditText username;
    private EditText email;
    private EditText phone;
    private Button changeImageBtn;
    private Button myDownloadsButton;
    private String user;
    private StorageReference mStorageRef;
    private ImageView customerImg;
    private ProgressDialog progressDialog;
    private String type;
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
        phone =(EditText) findViewById(R.id.customer_account_phone);
        changeImageBtn = (Button)findViewById(R.id.customer_account_change_image_button);
        customerImg = (ImageView) findViewById(R.id.customer_account_image_view);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        myDownloadsButton = (Button)findViewById(R.id.account_my_downloads_button);
        user = currentUser.getUid();
        userDb = FirebaseDatabase.getInstance().getReference("user").child(user);
        userDb.keepSynced(true);

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

        myDownloadsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent downloadsIntent = new Intent(CustomerAccountActivity.this,DownloadsActivty.class);
                startActivity(downloadsIntent);
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

        /*if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                StorageReference filePath = mStorageRef.child("profile_image").child(user);

                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT);
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }*/
    }
}