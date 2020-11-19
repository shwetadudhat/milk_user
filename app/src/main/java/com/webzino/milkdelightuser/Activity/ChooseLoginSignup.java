package com.webzino.milkdelightuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.webzino.milkdelightuser.R;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseLoginSignup extends AppCompatActivity {

    Button btn_login,btn_signup;
    String referCode;
    String txt_refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_login_signup);

        btn_login=findViewById(R.id.btn_login);
        btn_signup=findViewById(R.id.btn_signup);

        referCode=getIntent().getStringExtra("referCode");

        if (referCode.equals("utm_source=google-play&utm_medium=organic")){
            referCode="";
        }



        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseLoginSignup.this, Login.class);
                startActivity(intent);
            }
        });


        if (referCode!=null){
            txt_refer=referCode;

        }else {
            txt_refer="";
        }

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChooseLoginSignup.this, Signup.class);
                intent.putExtra("referCode",referCode);
                startActivity(intent);
            }
        });

    }
}
