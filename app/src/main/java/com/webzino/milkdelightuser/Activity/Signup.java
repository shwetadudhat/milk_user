package com.webzino.milkdelightuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.enter_detail_model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.AppHelper;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.CustomVolleyMultipartRequest;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

public class Signup extends BaseActivity {

    String txtreferCode;

    TextView txt_login;
    EditText et_name,et_phn,et_email,et_pass,et_confpass;
    RelativeLayout ll_signup_fb,ll_signup_google;
    Button btn_signup;
    String txt_name,txt_nmbr,txt_email,txt_pass,txt_confpass;
    ImageView ivBack;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int AC_SIGN_IN = 100;

    CallbackManager callbackManager;

    Button submit4;
    List<enter_detail_model> enterDetailModels = new ArrayList<>();
    Context context;
    EditText name, email, phoneno, password;
    DatabaseHandler db;
    String value;
    SharedPreferences check_pref;
    SharedPreferences.Editor check_editor;

    String token;
    String referCode;
    String fileUri;
    String emailId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        getKeyHashes(Signup.this);

        txt_login=findViewById(R.id.txt_login);
        et_name=findViewById(R.id.et_name);
        et_phn=findViewById(R.id.et_nmbr);
        et_email=findViewById(R.id.et_email);
        et_pass=findViewById(R.id.et_pass);
        et_confpass=findViewById(R.id.et_confpass);
        ll_signup_fb=findViewById(R.id.ll_signup_fb);
        ll_signup_google=findViewById(R.id.ll_signup_google);
        btn_signup=findViewById(R.id.btn_signup);

        txtreferCode=getIntent().getStringExtra("referCode");
        if (txtreferCode!=null){
            referCode=txtreferCode;
        }else{
            referCode="";
        }
        Log.e("referCodeSignup",referCode+"123");

        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "getInstanceId failed", task.getException());
                        return;
                    }
                    // Get new Instance ID token
                    token = task.getResult().getToken();
                    Log.e("token123",token);
//                    token = token;
//                    SharePreferenceManager.saveString(ActivityLoginType.this, SharePreferenceManager.DEVICE_TOKEN, token);
                });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        callbackManager = CallbackManager.Factory.create();


        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });


        db = new DatabaseHandler(Signup.this);
        db.clearCart();
        check_pref = getSharedPreferences("check", MODE_PRIVATE);
        check_editor = check_pref.edit();
        check_editor.putString("value", "true");
        check_editor.commit();

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_name.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        et_phn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_phn.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_email.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        et_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_pass.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        et_confpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                et_confpass.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });


        et_phn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (et_phn.getText().toString().length() <= 0 && et_phn.getText().toString().length() > 10) {
                    et_phn.setError("Please enter the valid mobile number!");
                } else {
                    et_phn.setError(null);
                }

            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_name=et_name.getText().toString();
                txt_nmbr=et_phn.getText().toString();
                txt_email=et_email.getText().toString();
                txt_pass=et_pass.getText().toString();
                txt_confpass=et_confpass.getText().toString();
                if (validateSignupDetails(txt_name,txt_nmbr,txt_email,txt_pass,txt_confpass)){

                    if (isInternetConnected()) {
                        try {
                            showDialog("");
                            sign_up(txt_name,txt_nmbr,txt_email,txt_pass,txt_confpass);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        });

        ll_signup_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetConnected()) {
                    showDialog("");
                    fbLogin(ll_signup_fb);
                }

            }
        });

        ll_signup_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isInternetConnected()) {
                    showDialog("");
                    GooglesignIn();
                }

            }
        });
    }

    private void sign_up(String txt_name, String txt_nmbr, String txt_email, String txt_pass, String txt_confpass) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_name", txt_name);
        params.put("user_email", txt_email);
        params.put("user_phone", txt_nmbr);
      //  params.put("user_image", "ggju.png");
        params.put("user_password", txt_pass);
        params.put("confirm_password", txt_confpass);
        params.put("device_id", token);
        params.put("refer_code", referCode);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.sign_up, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tag", response.toString());
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");
                    Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT).show();

                    if (status.contains("1")) {
                        JSONObject jsonObject = response.getJSONObject("data");
                        String user_phone = jsonObject.getString("user_phone");
                        String otp=jsonObject.getString("otp");
                        String user_refer_code=jsonObject.getString("user_refer_code");

                        Intent intent = new Intent(getApplicationContext(), com.webzino.milkdelightuser.Activity.otp.class);
                        intent.putExtra("otp",otp);

                        intent.putExtra("nmbr",user_phone);
                        intent.putExtra("txt_send","signup_screen");
                        startActivity(intent);


                    // Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
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


    private void social_sign_up(String txt_name, String txt_email, String txt_providerId, String txt_providerType, Bitmap bitmap) {

        CustomVolleyMultipartRequest multipartRequest = new CustomVolleyMultipartRequest(Request.Method.POST, BaseURL.social_login, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);


                Log.e("resultRespones",resultResponse);
                dismissDialog();
                Log.e("social_login", response.toString());

                try {
                    JSONObject jsonObject=new JSONObject(String.valueOf(resultResponse));

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.e("status",status);

                    if (status.contains("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String user_id = jsonObject1.getString("user_id");
                        String user_name = jsonObject1.getString("user_name");
                        String user_email = jsonObject1.getString("user_email");
                        String user_image = jsonObject1.getString("user_image");
                        String user_phone = jsonObject1.getString("user_phone");
                        String user_refer_code = jsonObject1.getString("user_refer_code");
                        String profile_url = jsonObject1.getString("profile_url");
                        String wallet_credits = jsonObject1.getString("wallet_credits");
                        String otp=jsonObject1.getString("otp");
                       // String user_pincode=jsonObject1.getString("otp");
                      //  String user_pincode="";


                        Session_management session_management = new Session_management(Signup.this);
                        session_management.createLoginSession(user_id, user_email, user_name, user_image, user_phone);

                        Intent intent = new Intent(Signup.this, Home.class);
                        startActivity(intent);
                        finishAffinity();


                         Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();
                    } else if (status.contains("0")){
                        dismissDialog();
                        Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

             /*   try {

                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
                // parse success output


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("errrrrr",error.toString());
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", txt_name);
                params.put("user_email", txt_email);
                params.put("provider_id", txt_providerId);
                params.put("provider_type", txt_providerType);
                params.put("device_id", token);
                params.put("refer_code", referCode);
              //  params.put("profile_url", profile_url);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

            //    params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
                params.put("profile_url", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(bitmap)));

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(multipartRequest);
    }


    private  boolean validateSignupDetails(String txt_name, String txt_nmbr, String txt_email, String txt_pass, String txt_confpass){
        if (!TextUtils.isEmpty(txt_name)) {
            if (!TextUtils.isEmpty(txt_nmbr)){
                if (Global.isNumeric(txt_nmbr)){
                    if (txt_nmbr.length()==10/*txt_nmbr.length()> 6 && txt_nmbr.length() <= 13*/){
                        if (!TextUtils.isEmpty(txt_pass)) {
                            if (Global.isValidEmail(txt_email)){
                                if (!TextUtils.isEmpty(txt_pass)) {
                                    if (txt_pass.length() >= 6) {
                                        if (!TextUtils.isEmpty(txt_confpass)){
                                            if (txt_confpass.length() >= 6) {
                                                if (txt_confpass.equals(txt_pass)){
                                                    return true;
                                                }else{
                                                    et_confpass.setError("Password And Confirm Password must be same");
                                                    Global.showKeyBoard(this,et_confpass);
                                                }
                                            }else{
                                                et_confpass.setError("Please enter valid Password!");
                                                Global.showKeyBoard(this,et_confpass);
                                            }
                                        }else{
                                            et_confpass.setError("Confirm Password field is required");
                                            Global.showKeyBoard(this,et_confpass);
                                        }
                                    }else{
                                        et_pass.setError("Please enter minimum 6 digit Password!");
                                        Global.showKeyBoard(this,et_pass);
                                    }
                                }else{
                                    et_pass.setError("Password field is required");
                                    Global.showKeyBoard(this,et_pass);
                                }
                            }else{
                                et_email.setError("Please enter a valid Email Address");
                                Global.showKeyBoard(this,et_email);
                            }
                        }else{
                            et_email.setError(" Email Address  is required");
                            Global.showKeyBoard(this,et_email);
                        }

                    }else{
                        ;
                        et_phn.setError("Please enter a valid Mobile Number");
                        Global.showKeyBoard(this,et_phn);
                    }
                }else{
                    et_phn.setError("Please enter a valid Number");
                    Global.showKeyBoard(this,et_phn);
                }

            }else{
                et_phn.setError("Mobile Number is required");
                Global.showKeyBoard(this,et_phn);
            }

        } else {
            et_name.setError("Name is Required!");
            Global.showKeyBoard(this,et_name);

        }
        return false;
    }

    private void GooglesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, AC_SIGN_IN);
       /* Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == AC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Log.e("task123",task.toString());
            handleSignInResult(task);
        }
    }

    public static void getKeyHashes(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        // CommonFunctions.setOnClickEnableRelativeLayout(false, relGoogleConnect);
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e("account",account.toString());
            // Signed in successfully, show authenticated UI.
            if (account != null) {
                String GoogleName = account.getDisplayName();
                String GoogleGivenName = account.getGivenName();
                String GoogleFamilyName = account.getFamilyName();
                String GoogleEmail = account.getEmail();
                String GoogleId = account.getId();
                String GoogleProfileUrl = String.valueOf(account.getPhotoUrl());
                // SocialMediaAccountDetailModel socialMediaAccountDetailModel = new SocialMediaAccountDetailModel(GoogleId, GoogleName, GoogleEmail, GoogleProfileUrl);
                // String GoogleAccountDetail = new Gson().toJson(socialMediaAccountDetailModel);
                // GoogleLogin(GoogleId, GoogleAccountDetail);

                Log.e("GoogleProfileUrl",GoogleProfileUrl);

                if (GoogleProfileUrl.equals("null") || GoogleProfileUrl.equals(""))
                {
                    Log.e("nulll12","nulll12");
                    social_sign_up1(GoogleName,GoogleEmail,GoogleId,"google");
                }else {
                    Glide.with(this).asBitmap().load(GoogleProfileUrl).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            File file = Global.getOutputMediaFile();
                            Log.e("filllleee",file.toString());
                            Global.saveBitmap(bitmap, file);

                            // Log.e("Fillllleee",file.toString());


                            //   userSignUp("$firstName $lastName", Global.getPref(getActivity(), StaticDataUtility.sMOBILE_NUMBER, "")!!, email, thirdPartyId, file)
                            try {
                                File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                                if (!mydir.exists()) {
                                    mydir.mkdirs();
                                }

                                fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";

                                Log.e   ("fileUri",fileUri);
                                FileOutputStream outputStream = new FileOutputStream(fileUri);

                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.flush();
                                outputStream.close();

                                Log.e("bitmap123",bitmap.toString());

                                social_sign_up(GoogleName,GoogleEmail,GoogleId,"google",bitmap);
                            } catch(IOException e) {
                                e.printStackTrace();
                            }
                            // Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                        }
                    });
                }

                Log.e("GoogleEmail",GoogleEmail);
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(this.getLocalClassName(), "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void social_sign_up1(String txt_name, String txt_email, String txt_providerId, String txt_providerType) {
        CustomVolleyMultipartRequest multipartRequest = new CustomVolleyMultipartRequest(Request.Method.POST, BaseURL.social_login, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);


                Log.e("resultRespones",resultResponse);
                dismissDialog();
                Log.e("social_login", response.toString());

                try {
                    JSONObject jsonObject=new JSONObject(String.valueOf(resultResponse));

                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.e("status",status);

                    if (status.contains("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String user_id = jsonObject1.getString("user_id");
                        String user_name = jsonObject1.getString("user_name");
                        String user_email = jsonObject1.getString("user_email");
                        String user_image = jsonObject1.getString("user_image");
                        String user_phone = jsonObject1.getString("user_phone");
                        String user_refer_code = jsonObject1.getString("user_refer_code");
                        String profile_url = jsonObject1.getString("profile_url");
                        String wallet_credits = jsonObject1.getString("wallet_credits");
                        String otp=jsonObject1.getString("otp");
                        // String user_pincode=jsonObject1.getString("otp");
                        //  String user_pincode="";


                        Session_management session_management = new Session_management(Signup.this);
                        session_management.createLoginSession(user_id, user_email, user_name, user_image, user_phone);

                        Intent intent = new Intent(Signup.this, Home.class);
                        startActivity(intent);
                        finishAffinity();


                        Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();
                    } else if (status.equals("0")){
                        dismissDialog();
                        Log.e("msggg",message);
                        Toast.makeText(getApplicationContext(),  message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("errrrrr",error.toString());
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_name", txt_name);
                params.put("user_email", txt_email);
                params.put("provider_id", txt_providerId);
                params.put("provider_type", txt_providerType);
                params.put("device_id", token);
                params.put("refer_code", referCode);
                //  params.put("profile_url", profile_url);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                //    params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
             //   params.put("profile_url", new CustomVolleyMultipartRequest.DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(bitmap)));

                return params;
            }
        };

        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        AppController.getInstance().addToRequestQueue(multipartRequest);

    }


    private void fbLogin(RelativeLayout ll_signup_fb) {

        LoginManager.getInstance().logInWithReadPermissions(Signup.this, Arrays.asList("email", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("LoginResult:", loginResult.getAccessToken().getToken()+"\n"+loginResult.toString());
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    Log.e("Main", response.toString());
                                    if (object.has("name") /*&& object.has("email")*/) {
                                        if ( !object.has("email")){
//                                          String email_id=  getEmailAddress();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Signup.this);
                                            ViewGroup viewGroup = findViewById(android.R.id.content);
                                            View dialogView = LayoutInflater.from(Signup.this).inflate(R.layout.custom_email, viewGroup, false);
                                            builder.setView(dialogView);
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.setCancelable(true);
                                            alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                                            EditText et_email=dialogView.findViewById(R.id.et_email);
                                            Button btnApply=dialogView.findViewById(R.id.btnApply);

                                            et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                                @Override
                                                public void onFocusChange(View view, boolean b) {
                                                    et_email.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                                                }
                                            });

                                            btnApply.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    emailId=et_email.getText().toString();


                                                    if (emailId.length()==0) {
                                                        et_email.setError(getString(R.string.enter_email_id));
                                                        Global.showKeyBoard(getApplicationContext(), et_email);
                                                    }else if (!Global.isValidEmail(emailId)){
                                                        //api call
                                                        et_email.setError("Enter Valid email id");
                                                        Global.showKeyBoard(getApplicationContext(), et_email);
                                                    }else{
                                                        Log.e("emailId",emailId);
                                                        try {
                                                            String Fbid = object.getString("id");
                                                            String Fbname = object.getString("name");
                                                            String Fbimage_url = "https://graph.facebook.com/" + Fbid + "/picture?type=normal";
                                                            Log.e("Fbid",Fbid);
                                                            Log.e("fbImageurl",Fbimage_url);

                                                            Glide.with(Signup.this).asBitmap().load(Fbimage_url).into(new CustomTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                                                    try {
                                                                        File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                                                                        if (!mydir.exists()) {
                                                                            mydir.mkdirs();
                                                                        }

                                                                        fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";

                                                                        Log.e   ("fileUri",fileUri);
                                                                        FileOutputStream outputStream = new FileOutputStream(fileUri);

                                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                                                        outputStream.flush();
                                                                        outputStream.close();

                                                                        Log.e("bitmap123",bitmap.toString());


                                                                        social_sign_up(Fbname,emailId,Fbid,"facebook",bitmap);


//
                                                                    } catch(IOException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    //  Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
                                                                }
                                                                @Override
                                                                public void onLoadCleared(Drawable placeholder) {
                                                                }
                                                            });

                                                        } catch (JSONException e) {
                                                            Log.e("ERROR", "FB_LOGIN:> " + e.toString());
                                                        }
                                                        alertDialog.dismiss();

                                                    }

                                                }
                                            });
                                            alertDialog.show();

                                        }else{
                                            try {
                                                String Fbid = object.getString("id");
                                                String Fbname = object.getString("name");
                                                String Fbemail = object.getString("email");
                                                String Fbimage_url = "https://graph.facebook.com/" + Fbid + "/picture?type=normal";
                                                Log.e("Fbid",Fbid);
                                                Log.e("fbImageurl",Fbimage_url);

                                                Glide.with(Signup.this).asBitmap().load(Fbimage_url).into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                                        try {
                                                            File mydir = new File(Environment.getExternalStorageDirectory() + "/11zon");
                                                            if (!mydir.exists()) {
                                                                mydir.mkdirs();
                                                            }

                                                            fileUri = mydir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";

                                                            Log.e   ("fileUri",fileUri);
                                                            FileOutputStream outputStream = new FileOutputStream(fileUri);

                                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                                            outputStream.flush();
                                                            outputStream.close();

                                                            Log.e("bitmap123",bitmap.toString());

                                                            if (Fbemail.equals("null")){
                                                                social_sign_up(Fbname,Fbemail,Fbid,"facebook",bitmap);
                                                            }else{
                                                                social_sign_up(Fbname,emailId,Fbid,"facebook",bitmap);
                                                            }

//
                                                        } catch(IOException e) {
                                                            e.printStackTrace();
                                                        }
                                                        //  Toast.makeText(getApplicationContext(), "Image Saved", Toast.LENGTH_LONG).show();
                                                    }
                                                    @Override
                                                    public void onLoadCleared(Drawable placeholder) {
                                                    }
                                                });

                                            } catch (JSONException e) {
                                                Log.e("ERROR", "FB_LOGIN:> " + e.toString());
                                            }
                                        }

                                    } else {
                                        dismissDialog();
                                        Toast.makeText(Signup.this, "data not found", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        dismissDialog();
                        Log.e("ERROR", "FB_LOGIN:> " + exception.toString());
                    }
                });
    }



}
