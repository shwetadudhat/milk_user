package com.milkdelightuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Adapter.Adapter_Recharge;
import com.milkdelightuser.Model.Recharge_Model;
import com.milkdelightuser.Model.TransactionDetail_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Recharge_history extends BaseActivity {

    List<Recharge_Model> recharge_models = new ArrayList<>();
    RecyclerView recharge_list;


    Session_management session_management;
    RelativeLayout relative_recharge,container_null1;

    String user_id;

    ImageView ivNotify,ivBack;
    TextView title;

    TextView wallet_price;

    String txtwallet_price;
    List<TransactionDetail_Model> transactionDetailModelList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recharge_history);

        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText("All Transactions");
        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Recharge_history.this, drawer.class);
                intent.putExtra("notification","product_page");
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wallet_price=findViewById(R.id.wallet_price);
        SharedPreferences settings = getSharedPreferences("wallet", Context.MODE_PRIVATE);
        txtwallet_price=settings.getString("wallet",null);

        Log.e("txtwallet_price",txtwallet_price);
        wallet_price.setText(txtwallet_price);

        relative_recharge = findViewById(R.id.relative_recharge);
        container_null1 = findViewById(R.id.container_null1);
        recharge_list = (RecyclerView) findViewById(R.id.Recycler_recharge);
        recharge_list.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));

        transactionDetailModelList=new ArrayList<>();

        recharge_models=new ArrayList<>();

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);


        if (isInternetConnected()) {
            showDialog("");
            getTransactionHistory(user_id);


          // Show_Recharge();
        }
    }

    private void getTransactionHistory(String user_id) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.wallet_history, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.d("rechargeHistory",response.toString());
                try {
                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")){

                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length()>0){
                            container_null1.setVisibility(View.GONE);
                            relative_recharge.setVisibility(View.VISIBLE);
                        }else {
                            container_null1.setVisibility(View.VISIBLE);
                            relative_recharge.setVisibility(View.GONE);
                            //  Toast.makeText(Recharge_history.this, "data not found", Toast.LENGTH_SHORT).show();
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id = jsonObject.getString("id");
                            String user_id = jsonObject.getString("user_id");
                            String amount = jsonObject.getString("amount");
                            String transaction_id = jsonObject.getString("transaction_id");
                            String amount_status = jsonObject.getString("amount_status");
                            String pay_type = jsonObject.getString("pay_type");
                            String recharge_status = jsonObject.getString("recharge_status");
                            String date_of_recharge=jsonObject.getString("date_of_recharge");
                            String created_at=jsonObject.getString("created_at");

                            if (pay_type.equals("-")){
                                pay_type="wallet";
                            }

                            Recharge_Model recharge_model = new Recharge_Model();
                            recharge_model.setWallet_recharge_history_id(id);
                            recharge_model.setUser_id(user_id);
                            recharge_model.setAmount(amount);
                            recharge_model.setTransaction_id(transaction_id);
                            recharge_model.setAmount_status(amount_status);
                            recharge_model.setPay_type(pay_type);
                            recharge_model.setRecharge_status(recharge_status);
                            recharge_model.setDate_of_recharge(date_of_recharge);
                            recharge_model.setCreated_at(created_at);

                            recharge_models.add(recharge_model);

                        }

                        Adapter_Recharge adapter_recharge = new Adapter_Recharge(recharge_models);
                        recharge_list.setAdapter(adapter_recharge);
                    }
                } catch (JSONException e) {
                    dismissDialog();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //    Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }

}
