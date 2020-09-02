package com.milkdelightuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomPinview;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class otp extends BaseActivity {

    Session_management session_management;

    TextView tvNmbr,tvResendOtp;
    EditText edOTP1,edOTP2,edOTP3,edOTP4,edOTP5,edOTP6;
    Button btn_verify;
    ImageView ivBack;
    String txt_receive,otp;

    String mobile,nmbr;
    CustomPinview customPinview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_otp);

        Intent intent=getIntent();
        txt_receive= intent.getStringExtra("txt_send");
        otp= intent.getStringExtra("otp");
        nmbr= intent.getStringExtra("nmbr");

        /*if (txt_receive.equals("forgot_pass_screen")){
            btn_verify.setText(R.string.verify_proceed);
        }else{
            btn_verify.setText(R.string.verify_sign_up);
        }*/

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvNmbr=findViewById(R.id.tvNmbr);
        customPinview=findViewById(R.id.otp_view);
        edOTP1=findViewById(R.id.edOTP1);
        edOTP2=findViewById(R.id.edOTP2);
        edOTP3=findViewById(R.id.edOTP3);
        edOTP4=findViewById(R.id.edOTP4);
       /* edOTP5=findViewById(R.id.edOTP5);
        edOTP6=findViewById(R.id.edOTP6);*/
        tvResendOtp=findViewById(R.id.tvResendOtp);
        btn_verify=findViewById(R.id.btn_verify);

        customPinview.setPinViewEventListener((pinview, fromUser) -> Toast.makeText(com.milkdelightuser.Activity.otp.this, pinview.getValue(), Toast.LENGTH_SHORT).show());

        edOTP1.addTextChangedListener(new GenericTextWatcher(edOTP1));
        edOTP2.addTextChangedListener(new GenericTextWatcher(edOTP2));
        edOTP3.addTextChangedListener(new GenericTextWatcher(edOTP3));
        edOTP4.addTextChangedListener(new GenericTextWatcher(edOTP4));
       /* edOTP5.addTextChangedListener(new GenericTextWatcher(edOTP5));
        edOTP6.addTextChangedListener(new GenericTextWatcher(edOTP6));*/

        session_management=new Session_management(getApplicationContext());
        mobile = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        tvNmbr.setText(nmbr/*mobile*/);
      //  Log.e("otp",otp);

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /************call service ************/
                    if (isInternetConnected()) {
                        try {
                            showDialog("");
                            ResendOtp(nmbr/*mobile*/);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
            }
        });

        btn_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (isInternetConnected()) {
                        try {
                            showDialog("");
                            Log.e("otp123",customPinview.getValue());
                            verifyPhn(nmbr/*mobile*/,otp);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

            }
        });

    }

    private void verifyPhn(String mobile, String otp) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", mobile);
        params.put("otp", otp);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.verify_otp, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("TagOtp", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(com.milkdelightuser.Activity.otp.this, message, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject=response.getJSONObject("data");
                        String id = jsonObject.getString("user_id");
                        String user_name = jsonObject.getString("user_name");
                        String user_email = jsonObject.getString("user_email");
                        String user_image = jsonObject.getString("user_image");
                        String user_phone = jsonObject.getString("user_phone");

                        Log.e("id",id);




                        Session_management session_management = new Session_management(com.milkdelightuser.Activity.otp.this);
                        session_management.createLoginSession(id, user_email, user_name, user_image, user_phone);

/*
                        Session_management session_management = new Session_management(otp.this);
                        session_management.createLoginSession(id, user_email, user_name, user_image, nmbr);*/
                        if (txt_receive.equals("forgot_pass_screen")){
                            btn_verify.setText(R.string.verify_proceed);
                            Intent intent1=new Intent(com.milkdelightuser.Activity.otp.this, ResetPassword.class);
                            intent1.putExtra("id",id);
                            startActivity(intent1);
                        }else {
                            Intent intent = new Intent(com.milkdelightuser.Activity.otp.this, drawer.class);
                            startActivity(intent);
                            finishAffinity();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
             //   Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void ResendOtp(String mobile) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", mobile);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.re_send_otp, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(com.milkdelightuser.Activity.otp.this, message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
              //  Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.edOTP1:
                    if (text.length() == 1)
                        edOTP2.requestFocus();
                    break;
                case R.id.edOTP2:
                    if (text.length() == 1)
                        edOTP3.requestFocus();
                    else if (text.length() == 0)
                        edOTP1.requestFocus();
                    break;
                case R.id.edOTP3:
                    if (text.length() == 1)
                        edOTP4 .requestFocus();
                    else if (text.length() == 0)
                        edOTP2.requestFocus();
                    break;
                case R.id.edOTP4:
                   /* if (text.length() == 1)
                        edOTP5.requestFocus();
                    else*/ if (text.length() == 0)
                        edOTP3.requestFocus();
                    break;
               /* case R.id.edOTP5:
                    if (text.length() == 1)
                        edOTP6.requestFocus();
                    else if (text.length() == 0)
                        edOTP4.requestFocus();
                    break;
                case R.id.edOTP6:
                    if (text.length() == 0)
                        edOTP5.requestFocus();
                    break;*/
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            if(!edOTP1.getText().toString().equals("")){
                edOTP1.setBackgroundColor(R.drawable.bg_bluebtn);
            }else if(!edOTP2.getText().toString().equals("")){
                edOTP2.setBackgroundColor(R.drawable.bg_bluebtn);
            }else if(!edOTP3.getText().toString().equals("")){
                edOTP3.setBackgroundColor(R.drawable.bg_bluebtn);
            }else if(!edOTP4.getText().toString().equals("")){
                edOTP4.setBackgroundColor(R.drawable.bg_bluebtn);
            }else if(!edOTP5.getText().toString().equals("")){
                edOTP5.setBackgroundColor(R.drawable.bg_bluebtn);
            }else if(!edOTP6.getText().toString().equals("")){
                edOTP6.setBackground(getResources().getDrawable(R.drawable.bg_bluebtn));
            }

        }
    }
}
