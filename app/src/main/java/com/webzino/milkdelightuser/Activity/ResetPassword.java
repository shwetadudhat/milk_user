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
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

public class ResetPassword extends BaseActivity {

    Button btn_passSubmit;
    EditText ed_pass,ed_rePass;
    String txt_pass,txt_rePass;
    Session_management sessionManagement;
    String u_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_password);

        ed_pass=findViewById(R.id.edPass);
        ed_rePass=findViewById(R.id.edRePass);
        btn_passSubmit=findViewById(R.id.btn_passSubmit);

      /*  sessionManagement=new Session_management(ResetPassword.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);*/
        u_id=getIntent().getStringExtra("id");

        btn_passSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_pass=ed_pass.getText().toString();
                txt_rePass=ed_rePass.getText().toString();

                if (validateDetails(txt_pass,txt_rePass)){
                    if (isInternetConnected()) {
                        try {
                            showDialog(" ");
                            ResetPass(u_id,txt_pass,txt_rePass);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                }
            }
        });

    }

    private void ResetPass(String u_id, String txt_pass, String txt_rePass) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("password", txt_pass);
        params.put("re_password", txt_rePass);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.reset_password, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(ResetPassword.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){

                        Intent intent=new Intent(ResetPassword.this, Login.class);
                        startActivity(intent);

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
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }

    private  boolean validateDetails(String txt_pass, String txt_rePass){
        if (!TextUtils.isEmpty(txt_pass)) {
                if (txt_pass.length() >= 6) {

                    if (!TextUtils.isEmpty(txt_rePass)) {
                        if (txt_pass.length() >= 6) {
                            if (txt_pass.equals(txt_rePass)){
                                return true;
                            }else{
                                ed_rePass.setError("Both Feild are must be same!");
                                Global.showKeyBoard(ResetPassword.this,ed_rePass);
                            }

                        }else{
                            ed_rePass.setError("Please re-enter valid Password!");
                            Global.showKeyBoard(ResetPassword.this,ed_rePass);
                        }
                    }else{
                        ed_rePass.setError("Re-enter Password Feild is Required!");
                        Global.showKeyBoard(ResetPassword.this,ed_rePass);
                    }
                   // return true;
                } else {
                    ed_pass.setError("Please enter valid Password!");
                    Global.showKeyBoard(ResetPassword.this,ed_pass);
                }

        } else {
            ed_pass.setError("Password Feild is Required!");
            Global.showKeyBoard(ResetPassword.this,ed_pass);
        }
        return false;
    }
}
