package com.webzino.milkdelightuser.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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
import com.facebook.FacebookSdk;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class Login extends BaseActivity {

    EditText edemailMobileNumber, edpassword;
    RelativeLayout ll_fb_login,ll_google_login;
    Button login;
    TextView txt_signup,txt_forgot;

    String fileUri,action;
    String emailId;

    String txt_emailMobileNumber, txt_pass;

    String token;

    SharedPreferences sharedPreferences;

    DatabaseHandler db;
   // ProgressDialog progressDialog;
    SharedPreferences.Editor editor;

    SharedPreferences check_pref;
    SharedPreferences.Editor check_editor;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int AC_SIGN_IN = 100;

    CallbackManager callbackManager;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        action=getIntent().getStringExtra("action");

        db = new DatabaseHandler(Login.this);
        db.clearCart();
        check_pref = getSharedPreferences("check", MODE_PRIVATE);
        check_editor = check_pref.edit();
        check_editor.putString("value", "false");
        check_editor.commit();
        sharedPreferences = getSharedPreferences("userid", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edemailMobileNumber = findViewById(R.id.emailMobileNumber_login);

        edpassword = findViewById(R.id.password_login);
        login=findViewById(R.id.login);
        txt_forgot=findViewById(R.id.txt_forgot);
        txt_signup=findViewById(R.id.txt_signup);
        ll_fb_login=findViewById(R.id.ll_fb);
        ll_google_login=findViewById(R.id.ll_google);


        edemailMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edemailMobileNumber.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        edpassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edpassword.setBackground(getResources().getDrawable(R.drawable.bg_edit));
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logOut();


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

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_emailMobileNumber=edemailMobileNumber.getText().toString();
                txt_pass=edpassword.getText().toString();
                if (validateLoginDetails(txt_emailMobileNumber,txt_pass)){
                    if (isInternetConnected()) {
                        //perform in api calling in if success result get
                        showDialog("");
                        Login(txt_emailMobileNumber, txt_pass);

                    }
                }



            }
        });

        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });

        txt_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, Forgot_Password.class);
                startActivity(intent);
            }
        });

        ll_fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected()) {
                    showDialog("");
                    fbLogin();
                    ll_fb_login.setEnabled(false);
                }
            }
        });

        ll_google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetConnected()) {
                    showDialog("");
                    GooglesignIn();

                    ll_google_login.setEnabled(false);

                }
            }
        });

    }



    private void Login(String phone, String passs) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", phone);
        params.put("device_id", token);
        params.put("user_password", passs);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.login, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.d("LoginTag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                            JSONObject jsonObject = response.getJSONObject("data");

                            String userid = jsonObject.getString("user_id");
                            String user_email = jsonObject.getString("user_email");
                            String user_name = jsonObject.getString("user_name");
                            String user_phone = jsonObject.getString("user_phone");
                            String user_image = jsonObject.getString("user_image");
                            String phone_verified = jsonObject.getString("phone_verified");
                            String otp = jsonObject.getString("otp");
                         //   String pincode="";
                            if (phone_verified.equals("1")){
                                Session_management session_management = new Session_management(Login.this);
                                session_management.createLoginSession(userid, user_email, user_name, user_image, user_phone);

                                Intent intent = new Intent(getApplicationContext(), Home.class);
                                startActivity(intent);
                                finishAffinity();

                            }else{
                                Intent intent = new Intent(getApplicationContext(), com.webzino.milkdelightuser.Activity.otp.class);
                                intent.putExtra("otp",otp);
                                intent.putExtra("nmbr",user_phone);
                                intent.putExtra("txt_send","signup_screen");
                                startActivity(intent);

                            }

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
                dismissDialog();
                Log.e("error",error.toString());
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    private  boolean validateLoginDetails(String txt_emailMobileNumber, String txt_pass){
        if (!TextUtils.isEmpty(txt_emailMobileNumber)) {
            if (Global.isNumeric(txt_emailMobileNumber)){
                if (txt_emailMobileNumber.length() >= 10) {
                   // txt_emailMobileNumber.isErrorEnabled = false;
                    if (!TextUtils.isEmpty(txt_pass)) {
                        if (txt_pass.length() >= 6) {
                            return true;
                        } else {
                            edpassword.setError("Please enter valid Password!");
                            Global.showKeyBoard(Login.this,edpassword);
                        }
                    } else {
                        edpassword.setError("Password is Required!");
                        Global.showKeyBoard(this,edpassword);
                    }
                } else {
                    edemailMobileNumber.setError("Please enter valid Mobile Number!");
                    Global.showKeyBoard(this,edemailMobileNumber);
                }
            } else{
                if (Global.isValidEmail(txt_emailMobileNumber)) {
                    if (!TextUtils.isEmpty(txt_pass)) {
                        if (txt_pass.length() >= 6) {
                            return true;
                        } else {
                            edpassword.setError("Please enter valid Password!");
                            Global.showKeyBoard(this,edpassword);
                        }
                    } else {
                        edpassword.setError("Password is Required!");
                        Global.showKeyBoard(this,edpassword);
                    }
                } else {
                    edemailMobileNumber.setError("Please enter valid Email Id!");
                    Global.showKeyBoard(this,edemailMobileNumber);
                }
            }
        } else {
            edemailMobileNumber.setError("Email or Phone Number is Required!");
            Global.showKeyBoard(this,edemailMobileNumber);
        }
        return false;
    }

    private void GooglesignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, AC_SIGN_IN);
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
            handleSignInResult(task);
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
       // CommonFunctions.setOnClickEnableRelativeLayout(false, relGoogleConnect);
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
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

                Log.e("GoogleEmail_login",GoogleEmail);

                Log.e("GoogleProfileUrl",GoogleProfileUrl);

                if (GoogleProfileUrl.equals("null") || GoogleProfileUrl.equals(""))
                {
                    Log.e("nulll12","nulll12");
                    dismissDialog();
//                    Toast.makeText(this, "Profile Url not found", Toast.LENGTH_SHORT).show();
                    View v = new ImageView(getBaseContext());
                    ImageView image;
                    image = new ImageView(v.getContext());
                    image.setImageDrawable(v.getResources().getDrawable(R.drawable.logo));

                    BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();

                    Log.e("bitmap", "handleSignInResult: " + bitmap);

                    social_sign_up(GoogleName, GoogleEmail, GoogleId, "google", token, "", bitmap);
                    finish();
                }else {
                    Glide.with(this).asBitmap().load(GoogleProfileUrl).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                            String root = Environment.getExternalStorageDirectory().toString();
                            File myDir = new File(root + "/saved_images");
                            myDir.mkdirs();
                            Random generator = new Random();
                            int n = 10000;
                            n = generator.nextInt(n);
                            String fname = "Image-"+ n +".jpg";
                            File file = new File (myDir, fname);
                            if (file.exists ()) file.delete ();
                            try {
                                FileOutputStream out = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                out.flush();
                                out.close();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            Log.e("bitmap123",bitmap.toString());

                            social_sign_up(GoogleName,GoogleEmail,GoogleId,"google",token,"",bitmap);

                        }
                        @Override
                        public void onLoadCleared(Drawable placeholder) {
                        }
                    });
                }

            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(this.getLocalClassName(), "signInResult:failed code=" + e.getStatusCode());
        }
    }


    private void fbLogin() {

        LoginManager.getInstance().logInWithReadPermissions(Login.this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.e("LoginResult:", loginResult.getAccessToken().getToken());
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                (object, response) -> {
                                    Log.e("Main", response.toString());

                                    if (object.has("name") /*&& object.has("email")*/) {
                                        if ( !object.has("email")){
//                                          String email_id=  getEmailAddress();
                                            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                            ViewGroup viewGroup = findViewById(android.R.id.content);
                                            View dialogView = LayoutInflater.from(Login.this).inflate(R.layout.custom_email, viewGroup, false);
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

                                                            Glide.with(Login.this).asBitmap().load(Fbimage_url).into(new CustomTarget<Bitmap>() {
                                                                @Override
                                                                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                                                    String root = Environment.getExternalStorageDirectory().toString();
                                                                    File myDir = new File(root + "/saved_images");
                                                                    myDir.mkdirs();
                                                                    Random generator = new Random();
                                                                    int n = 10000;
                                                                    n = generator.nextInt(n);
                                                                    String fname = "Image-"+ n +".jpg";
                                                                    File file = new File (myDir, fname);
                                                                    if (file.exists ()) file.delete ();
                                                                    try {
                                                                        FileOutputStream out = new FileOutputStream(file);
                                                                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                                                        out.flush();
                                                                        out.close();

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                    Log.e("bitmap123",bitmap.toString());

                                                                    social_sign_up(Fbname,emailId,Fbid,"facebook",token,"",bitmap);

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

                                        }
                                        else{
                                            try {
                                                String Fbid = object.getString("id");
                                                String Fbname = object.getString("name");
                                                String Fbemail = object.getString("email");
                                                String Fbimage_url = "https://graph.facebook.com/" + Fbid + "/picture?type=normal";
                                                Log.e("Fbid",Fbid);
                                                Log.e("fbImageurl",Fbimage_url);

                                                Glide.with(Login.this).asBitmap().load(Fbimage_url).into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {

                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                           if (isStoragePermissionGranted()){
                                                               String root = Environment.getExternalStorageDirectory().toString();
                                                               File myDir = new File(root + "/saved_images");
                                                               myDir.mkdirs();
                                                               Random generator = new Random();
                                                               int n = 10000;
                                                               n = generator.nextInt(n);
                                                               String fname = "Image-"+ n +".jpg";
                                                               File file = new File (myDir, fname);
                                                               if (file.exists ()) file.delete ();
                                                               try {
                                                                   FileOutputStream out = new FileOutputStream(file);
                                                                   bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                                                   out.flush();
                                                                   out.close();

                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                               }

                                                               Log.e("bitmap123", bitmap.toString());

                                                               if (Fbemail.equals("null")) {
                                                                   social_sign_up(Fbname, Fbemail, Fbid, "facebook", token, "", bitmap);

                                                               } else {
                                                                   social_sign_up(Fbname, Fbemail, Fbid, "facebook", token, "", bitmap);
                                                               }

//
                                                           }

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
                                        Toast.makeText(Login.this, "data not found", Toast.LENGTH_SHORT).show();
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
                        Log.e("ERROR", "FB_LOGIN:> " + exception.toString());
                    }
                });

    }

    private void social_sign_up(String txt_name, String txt_email, String txt_providerId, String txt_providerType, String deviceId, String referCode, Bitmap bitmap) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_name", txt_name);
        params.put("user_email", txt_email);
        params.put("provider_id", txt_providerId);
        params.put("provider_type", txt_providerType);
        params.put("device_id", deviceId);
        params.put("refer_code", referCode);
        // params.put("profile_url", profile_url);

        Log.e("params123",params.toString());


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

                       // String pincode="";


                        Session_management session_management = new Session_management(Login.this);
                        session_management.createLoginSession(user_id, user_email, user_name, user_image, user_phone);

                        Intent intent = new Intent(Login.this, Home.class);
                        startActivity(intent);
//                        finishAffinity();
                        finish();


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
                params.put("device_id", deviceId);
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



        AppController.getInstance().addToRequestQueue(multipartRequest);
    }


    @Override
    public void onBackPressed() {
//
        finishAffinity();

       /* if (action!=null){
            Log.e("action",action);
            finishAffinity();
        }else {
            super.onBackPressed();
        }
*/
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("TAG","Permission is granted");
                return true;
            } else {

                Log.e("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.e("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.e("TAG","Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }

}
