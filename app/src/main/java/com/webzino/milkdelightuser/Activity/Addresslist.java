package com.webzino.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_Address;
import com.webzino.milkdelightuser.Model.Address_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Addresslist extends BaseActivity {

    ImageView ivBack;
    Toolbar toolbar;
    Button btnAddNew;
    RecyclerView recycler_address;
    Adapter_Address adapter_address;
    TextView toolTitle;
    List<Address_Model> addressModelList;

    Session_management sessionManagement;
    String u_id;

    RelativeLayout container_null1;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_addresslist);

        toolTitle=findViewById(R.id.title);
        toolTitle.setText(R.string.myaddress);

        sessionManagement=new Session_management(Addresslist.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        Log.e("u_id",u_id);

        addressModelList=new ArrayList<>();


        recycler_address=findViewById(R.id.recycler_address);
        container_null1=findViewById(R.id.container_null1);

        if (isInternetConnected()) {
            showDialog("");
            AddressList(u_id);
        }


        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAddNew=findViewById(R.id.btnAddNew);

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getApplicationContext(), Address_add.class);
                intent.putExtra("action","add");
                //startActivity(intent);
              //  finish();
                startActivityForResult(intent,1);

            }
        });

    }

    private void AddressList(String u_id) {
        addressModelList.clear();
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.address_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("AddressList", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
//                            Toast.makeText(Addresslist.this, "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            container_null1.setVisibility(View.GONE);
                            recycler_address.setVisibility(View.VISIBLE);
                            for (int i=0;i<jsonArray.length();i++){


                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String id=jsonObject.getString("id");
                                String user_id=jsonObject.getString("user_id");
                                String user_name=jsonObject.getString("user_name");
                                String user_number=jsonObject.getString("user_number");
                                String city_id=jsonObject.getString("city_id");
                                String state_id=jsonObject.getString("state_id");
                                String address=jsonObject.getString("address");
                                String full_address=jsonObject.getString("full_address");
                                String flagStatus=jsonObject.getString("status");

                                Address_Model addressModel=new Address_Model();

                                addressModel.setAddress_id(id);
                                addressModel.setUser_id(user_id);
                                addressModel.setUser_name(user_name);
                                addressModel.setPhone_nmbr(user_number);
                                addressModel.setCity_id(city_id);
                                addressModel.setState_id(state_id);
                                addressModel.setArea_id(full_address);
                                addressModel.setStatus(flagStatus);

                                addressModelList.add(addressModel);

                            }

                            adapter_address=new Adapter_Address(getApplicationContext(),addressModelList);
                            recycler_address.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
                            recycler_address.setAdapter(adapter_address);

                            adapter_address.setEventListener(new Adapter_Address.EventListener() {
                                @Override
                                public void onItemDeleteClicked(int i, String address_id) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Addresslist.this)
                                            .setTitle(R.string.delete_title)
                                            .setMessage(R.string.delete_msg)
                                            .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (isInternetConnected()) {
                                                        showDialog("");
                                                        AddressDelete( i,address_id);
                                                    }


                                                }
                                            })
                                            .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();


                                }

                                @Override
                                public void onItemViewClicked(int i, String address_id) {
                                    if (isInternetConnected()) {
                                        showDialog("");
                                        updateAddressStatus( i,address_id);
                                    }


                                }

                                @Override
                                public void onItemEditClicked(int i, String address_id) {
                                    Intent intent=new Intent(Addresslist.this, Edit_Address.class);
                                    intent.putExtra("action","edit");
                                    intent.putExtra("id",address_id);
                                    startActivityForResult(intent,1);
//                                    finish();
                                  //  context.startActivity(intent);
                                }
                            });
                        }

                    }else{
                        container_null1.setVisibility(View.VISIBLE);
                        recycler_address.setVisibility(View.GONE);
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
                container_null1.setVisibility(View.VISIBLE);
                recycler_address.setVisibility(View.GONE);
                Toast.makeText(Addresslist.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }

    private void updateAddressStatus(int i, String address_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("address_id", address_id);
        params.put("user_id", u_id);
        params.put("status", "1");


        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.delivery_status_update, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("AddressListStatus", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        intent.putExtra("user_id",u_id);
                        setResult(Activity.RESULT_OK,intent);
                        finish();

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
                // Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }


    public void AddressDelete(int i, String address_id){
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("address_id", address_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.address_delete, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                 dismissDialog();
                Log.e("AddressList", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                       adapter_address.removeAt(i);

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
               // Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

                String user_id=data.getStringExtra("user_id");
                showDialog("");
                AddressList(user_id);

            }
        }
    }//
}
