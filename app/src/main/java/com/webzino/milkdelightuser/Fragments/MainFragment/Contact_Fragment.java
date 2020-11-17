package com.webzino.milkdelightuser.Fragments.MainFragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.webzino.milkdelightuser.utils.BaseFragment;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.ConnectivityReceiver;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


/**
 * A simple {@link Fragment} subclass.
 */
public class Contact_Fragment extends BaseFragment {

    Context context;
    Button btnConSend;
    TextView tvConNmbr, tvConEmail;
    EditText edConMsg;
    String msg;
    CardView cardNmbr, cardEmail;

    Session_management session_management;
    String u_id;

    public Contact_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edConMsg = view.findViewById(R.id.edConMsg);
        tvConNmbr = view.findViewById(R.id.tvConNmbr);
        tvConEmail = view.findViewById(R.id.tvConEmail);
        btnConSend = view.findViewById(R.id.btnConSend);
        cardNmbr = view.findViewById(R.id.cardNmbr);
        cardEmail = view.findViewById(R.id.cardEmail);

        session_management=new Session_management(getContext());
        u_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            getContact();
        } else {
            Global.showInternetConnectionDialog(getContext());
        }



        cardNmbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("cardNmbr","cardNmbr");

                callPhoneNumber();

            }
        });

        cardEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendEmail();

            }
        });

        btnConSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edConMsg.getText().toString().equals("")){
                    msg=edConMsg.getText().toString();
                }else{
                    edConMsg.setError("Please enter message here!");
                    Global.showKeyBoard(getContext(),edConMsg);
                }

                if (ConnectivityReceiver.isConnected()) {
                    showDialog("");
                    Contact(u_id,msg);
                } else {
                    Global.showInternetConnectionDialog(getContext());

                }


            }
        });


    }

    private void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {tvConEmail.getText().toString()};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
          //  getActivity().finish();
            Log.e("Finished sending email.", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void callPhoneNumber() {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvConNmbr.getText().toString()));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tvConNmbr.getText().toString()));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void getContact() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.contacts_app_details, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.contains("1")){
                        JSONArray jsonArray=response.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            String mobile_number=jsonObject.getString("mobile_number");
                            String email=jsonObject.getString("email");

                            tvConEmail.setText(email);
                            tvConNmbr.setText(mobile_number);

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
                Toast.makeText(getContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }



    private void Contact(String u_id, String msg) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("message", msg);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.contacts, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("contact123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        edConMsg.setText("");

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
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
            else
            {
                Log.e("contact permission", "Permission not Granted");
            }
        }
    }



}
