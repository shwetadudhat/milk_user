package com.milkdelightuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milkdelightuser.R;
import com.milkdelightuser.utils.Global;

import androidx.appcompat.app.AppCompatActivity;

public class referFriend extends AppCompatActivity {
    TextView tvSkip;
    EditText edReferCode;
    Button btn_submit;
    String txt_refercode;
    String referCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_refer_friend);

        tvSkip=findViewById(R.id.tv_skip);
        edReferCode=findViewById(R.id.edReferCode);
        btn_submit=findViewById(R.id.btn_submit);

        referCode=getIntent().getStringExtra("referCode");

        if (referCode.equals("utm_source=google-play&utm_medium=organic")){
            edReferCode.setText("");
        }else{
            edReferCode.setText(referCode);
        }


        edReferCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    edReferCode.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                }else{
                    edReferCode.setBackground(getResources().getDrawable(R.drawable.bg_grey));
                }
            }
        });


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(referFriend.this, ChooseLoginSignup.class);
                startActivity(intent);
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_refercode=edReferCode.getText().toString();
                if (txt_refercode.equals("")){
                    edReferCode.setError("please enter the Refer Code");
                    Global.showKeyBoard(getApplicationContext(),edReferCode);
                }else{
                    Intent intent=new Intent(referFriend.this, ChooseLoginSignup.class);
                    intent.putExtra("referCode",txt_refercode);
                    startActivity(intent);
                }



            }
        });
    }
}
