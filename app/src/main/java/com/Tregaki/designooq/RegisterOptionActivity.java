package com.Tregaki.designooq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class RegisterOptionActivity extends AppCompatActivity {

    ImageView customerImg ;
    ImageView designerImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_option);
        customerImg = (ImageView)findViewById(R.id.register_option_customer_img);
        designerImg = (ImageView)findViewById(R.id.register_option_designer_img);
        customerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent custSignup  = new Intent(RegisterOptionActivity.this,CustomerRegistrationActivity.class);
                startActivity(custSignup);

            }
        });

        designerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent desSignup  = new Intent(RegisterOptionActivity.this,DesignerRegistrationActivity.class);
                startActivity(desSignup);
            }
        });
    }
}