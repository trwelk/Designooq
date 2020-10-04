package com.Tregaki.designooq;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class DesignerRegistrationActivity extends AppCompatActivity {

    private EditText username;
    private  EditText password;
    private  EditText email;
    private  EditText phone;
    private EditText website;
    private boolean hasErrors = false;

    private FirebaseAuth mAuth;
    private Button registerButton;
    private ProgressDialog registerProgressDialog;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designer_registration);

        registerButton = (Button) findViewById(R.id.customer_registration_button);
        mAuth = FirebaseAuth.getInstance();
        registerProgressDialog = new ProgressDialog(this);

        phone = (EditText)findViewById(R.id.designer_registration_phone);
        website = (EditText)findViewById(R.id.designer_registration_website);
        registerButton = (Button)findViewById(R.id.designer_registration_register_button);
        username = (EditText)findViewById(R.id.designer_registration_username);
        password = (EditText)findViewById(R.id.designer_registration_password);
        email = (EditText)findViewById(R.id.designer_registration_email);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webSiteText = website.getText().toString();
                String usernameText = username.getText().toString();
                String passwordText = password.getText().toString();
                String emailText = email.getText().toString();
                String phoneText = phone.getText().toString();


                if(!TextUtils.isEmpty(passwordText) || !TextUtils.isEmpty(emailText)){
                    registerProgressDialog.setTitle("Registering User");
                    registerProgressDialog.setMessage("Please wait while we create a new user");
                    registerProgressDialog.setCanceledOnTouchOutside(false);
                    registerProgressDialog.show();

                    registerDesigner(emailText,passwordText,usernameText,phoneText,webSiteText);
                }
            }
        });
    }

    private void registerDesigner(final String emailText, String passwordText, final String usernameText, final String phoneText, final String webSiteText) {
        mAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = firebaseUser.getUid();
                    database = FirebaseDatabase.getInstance().getReference("user").child(uid);
                    HashMap<String,String> userMap = new HashMap<String,String>();
                    userMap.put("username",usernameText);
                    userMap.put("email",emailText);
                    userMap.put("phone",phoneText);
                    userMap.put("website",webSiteText);
                    userMap.put("image","default");
                    userMap.put("type","designer");
                    //userMap.put("online","false");

                    hasErrors = userNameHasErrors(usernameText);
                    hasErrors = emailHasErrors(emailText);
                    hasErrors = phoneHasErrors(phoneText);
                    hasErrors = websiteHasErrors(webSiteText);


                    if(!hasErrors) {
                        database.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("REGISTER_ACTIVITY", "SUCCESFULL");
                                } else {
                                    Log.d("REGISTER_ACTIVITY", "Fail");
                                }
                            }
                        });
                        registerProgressDialog.dismiss();
                        Intent customerHomeIntent = new Intent(DesignerRegistrationActivity.this, CustomerHomeActivity.class);
                        customerHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(customerHomeIntent);
                        finish();
                    }
                    else{
                        registerProgressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Please enter valid details",Toast.LENGTH_LONG).show();

                    }
                }
                else{
                    registerProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"Sign up failed",Toast.LENGTH_SHORT);

                }
            }
        });
    }

    public boolean phoneHasErrors(String phoneText) {
        if(phoneText == null || phoneText.length() != 10)
            return true;
        else
            return false;
    }

    public boolean websiteHasErrors(String webSiteText) {

        if(webSiteText == null )
            return true;
        else{
            try {
                URI url = new URI(webSiteText);
                return false;
            } catch (URISyntaxException e) {
                return true;
            }
        }
    }

    public boolean emailHasErrors(String emailText) {
        if (emailText == null || !emailText.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"))
            return true;
        else
            return false;
    }

    public boolean userNameHasErrors(String usernameText) {
        if(usernameText == null || usernameText.length() < 6)
            return true;
        else
            return false;
    }
}