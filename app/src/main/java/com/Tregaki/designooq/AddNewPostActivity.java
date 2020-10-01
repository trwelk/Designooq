package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class AddNewPostActivity extends AppCompatActivity {
    private ImageButton uploadButton;
    private EditText postDescription;
    private EditText postTitle;
    private StorageReference mStorageRef;
    private ProgressDialog progressDialog;
    private DatabaseReference postDb;
    private String user;
    private String postId;
    private  String titleHasErrors = null;
    private  String descriptionHasErrors = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_post);
        uploadButton = (ImageButton) findViewById(R.id.post_fragment_upload_image_button);
        postDescription = (EditText)findViewById(R.id.post_fragment_post_status_edit_text);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        user = FirebaseAuth.getInstance().getUid();
        postDb = FirebaseDatabase.getInstance().getReference("post");
        postTitle = (EditText)findViewById(R.id.add_new_post_title);


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ADD_POST",postTitle.getText().toString() );
                if(postDescription.getText().toString().isEmpty() && postTitle.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please provide a value to all fields",Toast.LENGTH_SHORT).show();
                }
                else{
                    Random rand = new Random();
                    HashMap<String,String> userMap = new HashMap<String,String>();
                    userMap.put("description",postDescription.getText().toString());
                    userMap.put("image","default");
                    userMap.put("month", (Integer.toString(new Date().getMonth())));
                    userMap.put("title",postTitle.getText().toString());
                    userMap.put("user",user);
                    postId = Integer.toString(rand.nextInt(100000));
                    descriptionHasErrors = isDescriptionValid(postDescription.getText().toString());
                    titleHasErrors = isValidTitle(postTitle.getText().toString());
                    if (titleHasErrors != null ){
                        Toast.makeText(getApplicationContext(),"Error:" + titleHasErrors,Toast.LENGTH_LONG).show();
                    }
                    else if( descriptionHasErrors != null )
                        Toast.makeText(getApplicationContext(),"Error:" + descriptionHasErrors,Toast.LENGTH_LONG).show();
                    else {
                        postDb.child(postId).setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("ADD_NEW_POST_ACTIVITY", "SUCCESFULL");
                                } else {
                                    Log.d("ADD_NEW_POST_ACTIVITY", "Fail");
                                }
                            }
                        });
                        Intent gallaryIntent = new Intent();
                        gallaryIntent.setType("image/*");
                        gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);

                        startActivityForResult(Intent.createChooser(gallaryIntent, "Select image"), 2);
                    }
                }
            }
        });
    }

    public String isValidTitle(String title) {

        if(title == null || title.isEmpty()){
            return "Pleas enter a title";
        }
        else
            return null;
    }

    public String isDescriptionValid(String description) {
        if(description == null || description.isEmpty()){
            return "Pleas enter a description";
        }
        else if(description.length() > 100)
            return "The description should be less than 100 characters";
        else
            return null;    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            //CropImage.activity(imageUri)
            //       .start(this);
            progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setTitle("Uploading Image");
            progressDialog.setMessage("Please wait while we upload the profile image");
            progressDialog.setCanceledOnTouchOutside(false);
            //progressDialog.show();
            Random rand = new Random();

            final StorageReference filePath = mStorageRef.child("posts").child(rand.nextInt(100000) + ".jpg");

            filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {


                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful()){
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                postDb.child(postId).child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        Intent homeIntent = new Intent(AddNewPostActivity.this,CustomerHomeActivity.class);
                        startActivity(homeIntent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Image Upload failed",Toast.LENGTH_SHORT);
                    }
                }
            });
        }
        else{
            Log.d("Error","ERROR uploading");

        }


    }
}