package com.milkdelightuser.Fragments.MainFragment;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.milkdelightuser.Activity.Addresslist;
import com.milkdelightuser.Activity.Home;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.AppHelper;
import com.milkdelightuser.utils.BaseFragment;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;
import com.milkdelightuser.utils.CustomVolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Fragment extends BaseFragment {

    Context context;
    CircleImageView image_profile;
    Bitmap photo;
    TextView userName,userNmbr;

    ImageView ibProEdit,ibProSave;
    EditText edProName,edProNmbr,edProEmail;

    String profName,profNmbr,profEmail;

    RelativeLayout rlProAddress;
    ImageView ivAddressList;

    TextView tvChangePass;

    Session_management  sessionManagement;
    String u_id,name,email,mobile,user_profilee;

    private static int RESULT_LOAD_IMAGE = 1;



    public Profile_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sessionManagement=new Session_management(getContext());
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        name=sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        mobile =sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        email=sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        user_profilee=sessionManagement.getUserDetails().get(BaseURL.KEY_IMAGE);

        image_profile=view.findViewById(R.id.image_profile);
        userName=view.findViewById(R.id.userName);
        userNmbr=view.findViewById(R.id.userNmbr);
        ibProEdit=view.findViewById(R.id.ibProEdit);
        ibProSave=view.findViewById(R.id.ibProSave);
        edProEmail=view.findViewById(R.id.edProEmail);
        edProNmbr=view.findViewById(R.id.edProNmbr);
        edProName=view.findViewById(R.id.edProName);
        rlProAddress=view.findViewById(R.id.rlProAddress);
        ivAddressList=view.findViewById(R.id.ivAddressList);
        tvChangePass=view.findViewById(R.id.tvChangePass);


       /* edProName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    edProName.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                    edProName.setTextColor(getResources().getColor(R.color.main_clr));
                }else{
                    edProName.setBackground(getResources().getDrawable(R.drawable.bg_grey));
                    edProName.setTextColor(getResources().getColor(R.color.title_clr));
                }

            }
        });

        edProNmbr.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edProNmbr.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                edProNmbr.setTextColor(getResources().getColor(R.color.main_clr));
            }
        });

        edProEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                edProEmail.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                edProEmail.setTextColor(getResources().getColor(R.color.main_clr));
            }
        });

*/

        if(user_profilee.equals("") || user_profilee.equals("null")){
            image_profile.setImageResource(R.mipmap.ic_launcher);
        }else{
          /*  Glide.with(getContext())
                    .load(BaseURL.profile_url+user_profilee)
                    .into(image_profile);
*/
            Global.loadGlideImage(getContext(),user_profilee,BaseURL.profile_url+user_profilee,image_profile);


        }


        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);*/

                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                try {
                    i.putExtra("return-data", true);
                    startActivityForResult(
                            Intent.createChooser(i, "Select Picture"), RESULT_LOAD_IMAGE);
                }catch (ActivityNotFoundException ex){
                    ex.printStackTrace();
                }
            }
        });

        userName.setText(name);
        if (mobile.equals("null")){
            userNmbr.setText(email);
            edProNmbr.setText("");
        }else{
            userNmbr.setText(mobile);
            edProNmbr.setText(mobile);
        }


        edProName.setText(name);
        edProEmail.setText(email);

        ibProSave.setVisibility(View.GONE);
        ibProEdit.setVisibility(View.VISIBLE);
        edProEmail.setEnabled(false);
        edProNmbr.setEnabled(false);
        edProName.setEnabled(false);



        ibProEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ibProSave.setVisibility(View.VISIBLE);
                ibProEdit.setVisibility(View.GONE);
                edProEmail.setEnabled(true);
                edProNmbr.setEnabled(true);
                edProName.setEnabled(true);

            }
        });

        ibProSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                profName=edProName.getText().toString();
                profEmail=edProEmail.getText().toString();
                profNmbr=edProNmbr.getText().toString();

                if(ValidateDetail(profName,profEmail,profNmbr)){
                    if (ConnectivityReceiver.isConnected()) {
                        showDialog("");
                        EditProfile(u_id,profName,profNmbr,profEmail);
                    } else {
                        Global.showInternetConnectionDialog(getContext());

                    }

                }else{

                    ibProSave.setVisibility(View.VISIBLE);
                    ibProEdit.setVisibility(View.GONE);

                }

            }
        });

        rlProAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), Addresslist.class);
                startActivity(intent);
            }
        });

        ivAddressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rlProAddress.callOnClick();
            }
        });

        tvChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custome_forget_password, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));
                //  alertDialog.setCancelable(false);


                EditText edOldPass=dialogView.findViewById(R.id.edOldPass);
                EditText edNewPass=dialogView.findViewById(R.id.edNewPass);
                EditText edReEnPass=dialogView.findViewById(R.id.edReEnPass);
                Button btnSavePass=dialogView.findViewById(R.id.btnSavePass);

                edOldPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        edOldPass.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));
                    }
                });


                edNewPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        edNewPass.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));
                    }
                });

                edReEnPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        edReEnPass.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));
                    }
                });


                btnSavePass.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String txtOldPass,txtNewPass,txtRePass;
                        txtOldPass=edOldPass.getText().toString();
                        txtNewPass=edNewPass.getText().toString();
                        txtRePass=edReEnPass.getText().toString();


                        if (txtOldPass.length()==0){
                            edOldPass.setError("Old password feild required !");
                            Global.showKeyBoard(getContext(),edOldPass);
                        }else if (txtNewPass.length()==0){
                            edNewPass.setError("New password feild required !");
                            Global.showKeyBoard(getContext(),edNewPass);
                        }else if (txtNewPass.length()<6){
                            edNewPass.setError("Please enter minimum 6 digit password !");
                            Global.showKeyBoard(getContext(),edNewPass);
                        }else if (txtRePass.length()==0){
                            edReEnPass.setError("Re-enter password feild required !");
                            Global.showKeyBoard(getContext(),edReEnPass);
                        }else if (txtRePass.length()<6){
                            edReEnPass.setError("Please enter minimum 6 digit password !");
                            Global.showKeyBoard(getContext(),edReEnPass);
                        }else if (txtOldPass.equals(txtNewPass)){
                            edNewPass.setError("Both Feilds are must be different!");
                            Global.showKeyBoard(getContext(),edNewPass);
                        }else if (!txtRePass.equals(txtNewPass)){
                            edReEnPass.setError("Both Feilds are must be same!");
                            Global.showKeyBoard(getContext(),edReEnPass);
                        }else{
                            //api call
                            if (ConnectivityReceiver.isConnected()) {
                                showDialog("");
                                changePass( u_id,txtOldPass,txtNewPass,txtRePass,alertDialog);
                            } else {
                                Global.showInternetConnectionDialog(getContext());

                            }
                        }

                    }
                });
                alertDialog.show();

            }
        });

    }



    private void EditProfile(String u_id, String profName, String profNmbr, String profEmail) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("user_name", profName);
        params.put("user_phone", profNmbr);
        params.put("user_email", profEmail);


        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.my_profile, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("TagOtp", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONObject jsonObject = response.getJSONObject("data");

                        String userid = jsonObject.getString("user_id");
                        String user_email = jsonObject.getString("user_email");
                        String user_name = jsonObject.getString("user_name");
                        String user_phone = jsonObject.getString("user_phone");
                        String user_image = jsonObject.getString("user_image");


                        userName.setText(user_name);
                        userNmbr.setText(user_phone);

                        edProName.setText(user_name);
                        edProNmbr.setText(user_phone);
                        edProEmail.setText(user_email);



                        Session_management session_management = new Session_management(getContext());
                        session_management.createLoginSession(userid, user_email, user_name, user_image, user_phone);

                        ibProSave.setVisibility(View.GONE);
                        ibProEdit.setVisibility(View.VISIBLE);

                        edProEmail.setEnabled(false);
                        edProNmbr.setEnabled(false);
                        edProName.setEnabled(false);


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
                Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void changePass(String u_id, String txtOldPass, String txtNewPass, String txtRePass, AlertDialog alertDialog) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("old_password", txtOldPass);
        params.put("new_password", txtNewPass);
        params.put("confirm_password", txtRePass);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.change_password, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("TagOtp", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){

                        alertDialog.dismiss();

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
              //  Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

                Uri path = data.getData();
                try {
                     photo = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                     Log.e ("photo", String.valueOf(photo));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                image_profile.setImageBitmap(photo);
            if (ConnectivityReceiver.isConnected()) {
                showDialog("");
                uploadImage(photo);
            } else {
                Global.showInternetConnectionDialog(getContext());
            }
        }

    }

    private void uploadImage(Bitmap photo) {
        CustomVolleyMultipartRequest multipartRequest = new CustomVolleyMultipartRequest(Request.Method.POST,BaseURL.upload_profile, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                Log.e("uploadProfile123",resultResponse);
                dismissDialog();
                Log.e("uploadProfile", response.toString());

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
                        String profile_url = jsonObject1.getString("profile_url");
                      //  String pincode = jsonObject1.getString("pincode");


                        Session_management session_management = new Session_management(getContext());
                        session_management.createLoginSession(user_id, user_email, user_name, user_image, user_phone);

                        if (getActivity() instanceof Home){
                            ((Home) getActivity()).getNavViewData();
                        }

                        Toast.makeText(getContext(),""+message, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", u_id);

                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                params.put("user_profile", new CustomVolleyMultipartRequest.DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(photo)));

                return params;
            }
        };
        multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(multipartRequest);
    }

    private boolean ValidateDetail(String txtName, String txtEmail, String txtNmbr) {

        if (txtName.length() == 0) {
            edProName.setError("please enter name");
            Global.showKeyBoard(getContext(),edProName);
            return false;
        } else if (!Global.isValidEmail(txtEmail)) {
            edProEmail.setError("please enter valid email address");
            Global.showKeyBoard(getContext(),edProEmail);
            return false;
        }else if (txtNmbr.length() != 10 ) {
            edProNmbr.setError("please enter valid mobile number");
            Global.showKeyBoard(getContext(),edProNmbr);
            return false;
        /*}else if (txtPincode.length()== 0 ) {
            Log.e("pincode",txtPincode);
            edProPin.setError("please enter Pincode");
            return false;
        }else if(!Global.isValidPinCode(txtPincode)){
            edProPin.setError("please enter valid pincode");
            return false;
        }*/}else{
            return true;
        }

    }


}
