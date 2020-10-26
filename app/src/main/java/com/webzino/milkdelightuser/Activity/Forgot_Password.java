package com.webzino.milkdelightuser.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

public class Forgot_Password extends BaseActivity {


    TextView tvLogin;
    EditText edNumberEmail;
    Button btn_sendOtp;

    String txt_NumberEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot__password);


        tvLogin=findViewById(R.id.tvLogin);
        edNumberEmail=findViewById(R.id.edNumberEmail);
        btn_sendOtp=findViewById(R.id.btn_sendOtp);

        edNumberEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edNumberEmail.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });


        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Forgot_Password.this, Login.class);
                startActivity(intent);
            }
        });

        btn_sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_NumberEmail=edNumberEmail.getText().toString();
                if (validateDetails(txt_NumberEmail)){

                    if (isInternetConnected()) {
                        showDialog("");
                        ForgotPassword(txt_NumberEmail);
                    }
                  /*  Intent intent=new Intent(Forgot_Password.this,otp.class);
                    intent.putExtra("txt_send","forgot_pass_screen");
                    startActivity(intent);*/
                }

            }
        });
    }

    private void ForgotPassword(String txt_numberEmail) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", txt_numberEmail);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.forgot_password, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("forgot_tag", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(Forgot_Password.this, message, Toast.LENGTH_SHORT).show();

                        JSONObject jsonObject=response.getJSONObject("data");
                        String id = jsonObject.getString("user_id");
                        String user_name = jsonObject.getString("user_name");
                        String user_email = jsonObject.getString("user_email");
                        String user_image = jsonObject.getString("user_image");
                        String user_phone = jsonObject.getString("user_phone");
                        String otp = jsonObject.getString("otp");

                        Intent intent=new Intent(Forgot_Password.this, otp.class);
                        intent.putExtra("otp",otp);
                        intent.putExtra("nmbr",user_phone);
                        intent.putExtra("txt_send","forgot_pass_screen");
                        startActivity(intent);

                    }else if (status.contains("0")){
                        Toast.makeText(Forgot_Password.this, message, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Something goes wrong!!!", Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    private  boolean validateDetails(String txt_emailMobileNumber){
        if (!TextUtils.isEmpty(txt_emailMobileNumber)) {
            if (Global.isNumeric(txt_emailMobileNumber)){
                if (txt_emailMobileNumber.length() >= 10) {
                   return true;
                } else {
                    edNumberEmail.setError("Please enter valid Mobile Number!");
                    Global.showKeyBoard(Forgot_Password.this,edNumberEmail);
                }
            } else{
                if (Global.isValidEmail(txt_emailMobileNumber)) {
                    return true;
                } else {
                    edNumberEmail.setError("Please enter valid Email Id!");
                    Global.showKeyBoard(Forgot_Password.this,edNumberEmail);
                }
            }
        } else {
            edNumberEmail.setError("Email or Phone Number is Required!");
            Global.showKeyBoard(Forgot_Password.this,edNumberEmail);
        }
        return false;
    }
}
