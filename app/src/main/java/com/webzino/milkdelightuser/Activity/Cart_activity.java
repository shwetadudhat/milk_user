package com.webzino.milkdelightuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Cart_Adapter;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


public class Cart_activity extends BaseActivity {

    private DatabaseHandler db;


    ImageView ivBack,ivNotify;
    TextView title;

    LinearLayout scroll_view;
    RelativeLayout container_null1;

    /*view1*/
    RelativeLayout rlCoupon;
    RecyclerView recycler_cartitem;
    Cart_Adapter adapter;

    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1,tvcgst1,tvsgst1;

    /*bottom view 1*/
    LinearLayout llOrder,ll_ordrOnce,ll_buySubscription;

    Session_management sessionManagement;
    String u_id,user_name,user_nmbr,user_email;

    String promo_code;
    Double total_gst,total_amount, pro_gst ;
    Double discount_amount=0.0;
    String pay_amount;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart2);
        setContent();

        db = new DatabaseHandler(Cart_activity.this);

        sessionManagement=new Session_management(Cart_activity.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        user_name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);


        recycler_cartitem.setLayoutManager(new LinearLayoutManager(Cart_activity.this));

        // totalAmout=Math.round(total_amount);

        getCartList();
        setPriceDetail();


        rlCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("totroooollll", String.valueOf(total_amount));
                AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity.this);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(Cart_activity.this).inflate(R.layout.custom_coupon, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                EditText tvCode=dialogView.findViewById(R.id.tvCode);
                Button btnApplyCoupon=dialogView.findViewById(R.id.btnApplyCoupon);

                tvCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        tvCode.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                    }
                });


                btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code;
                        code=tvCode.getText().toString();

                        Log.e("total_amount123", String.valueOf(total_amount));
//                        Log.e("totalAmout", String.valueOf(totalAmout));

                        if (code.length()==0){
                            tvCode.setError(getString(R.string.enter_coupon));
                            Global.showKeyBoard(getApplicationContext(),tvCode);
                        }else{
                            //api call

                            if (isInternetConnected()) {
                                try {
                                    showDialog("");
                                    applyCoupon(alertDialog,code);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                    }
                });
                alertDialog.show();


            }
        });

        ll_ordrOnce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("total_amount1234555",String.valueOf(total_amount));

              Intent intent=new Intent(Cart_activity.this, BuyOnce.class);
              intent.putExtra("total_amount",String.valueOf(total_amount));
              intent.putExtra("discount_amount",String.valueOf(Math.round(discount_amount)));
              intent.putExtra("promo_code",promo_code);
              startActivity(intent);
            }
        });


        ll_buySubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity.this, subscription2.class);
               // intent.putExtra("product_id",product_id);
                startActivity(intent);
            }
        });


        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity.this, Home.class);
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
    }

    private void setPriceDetail() {
        total_amount= Double.parseDouble(db.getTotalAmount());
        Log.e("total_gstamount", String.valueOf(Integer.parseInt(db.getTotalAmountIncludeGSt())));
        Log.e("total_amount", String.valueOf(Integer.parseInt(db.getTotalAmount())));
        tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount());
        tvDelChargeTag1.setText("FREE");
        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);

        ArrayList<HashMap<String, String>> items = db.getCartAll();
        if (items.size() > 0) {

            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                pro_gst = (Double.valueOf(map.get("price")) - Double.valueOf(map.get("gst_price")))*Double.valueOf(map.get("qty"));
                Log.e("pro_gst_123",String.valueOf(pro_gst));


            }
            Log.e("pro_gst_123_1",String.valueOf(pro_gst));

            if (pro_gst!=0.0){
                total_gst= pro_gst/2;
                tvcgst1.setText(""+ MainActivity.currency_sign+String.valueOf(total_gst));
                tvsgst1.setText(""+ MainActivity.currency_sign+String.valueOf(total_gst));

                Log.e("total_gst==>123",total_gst.toString());
            }else{
                tvcgst1.setText(""+ MainActivity.currency_sign+"0.0");
                tvsgst1.setText(""+ MainActivity.currency_sign+"0.0");
            }
        }


    }

    private void setContent() {
        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText(R.string.shoppingcart);

        tvBagTag1=findViewById(R.id.tvBagTag1);
        tvDelChargeTag1=findViewById(R.id.tvDelChargeTag1);
        tvCuponTa1=findViewById(R.id.tvCuponTa1);
        tvTotalTag1=findViewById(R.id.tvTotalTag1);
        tvcgst1=findViewById(R.id.tvcgst1);
        tvsgst1=findViewById(R.id.tvsgst1);

        llOrder=findViewById(R.id.llOrder);
        ll_ordrOnce=findViewById(R.id.ll_ordrOnce);
        ll_buySubscription=findViewById(R.id.ll_buySubscription);

        scroll_view=findViewById(R.id.scroll_view);
        container_null1=findViewById(R.id.container_null1);

        rlCoupon=findViewById(R.id.rlCoupon);
        recycler_cartitem=findViewById(R.id.recycler_cartitem);

    }

    private void getCartList() {
        ArrayList<HashMap<String, String>> map = db.getCartAll();
        if (map.size()==0){
            Log.e("ifff12",map.toString());
            scroll_view.setVisibility(View.GONE);
            container_null1.setVisibility(View.VISIBLE);
        }else{
            Log.e("else12",map.toString());
            container_null1.setVisibility(View.GONE);
            scroll_view.setVisibility(View.VISIBLE);
        }
        adapter = new Cart_Adapter(Cart_activity.this, map);
        recycler_cartitem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setEventListener(new Cart_Adapter.EventListener() {
            @Override
            public void onItemViewClicked(int i) {

                setPriceDetail();


            }
        });

    }


    private void applyCoupon(AlertDialog alertDialog, String code) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("promo_code_id", "");
        params.put("total_amount", ""+total_amount);
        params.put("promo_code", code);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.apply_coupon, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("CouponTag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        alertDialog.dismiss();

                        JSONObject jsonObject=response.getJSONObject("data");

                        total_amount= Double.parseDouble(String.valueOf(jsonObject.getInt("total_amount")));
                        discount_amount= Double.parseDouble(jsonObject.getString("discount"));
                        pay_amount=jsonObject.getString("pay_amount");
                        promo_code=jsonObject.getString("promo_code");
                        total_amount= Double.parseDouble(String.valueOf(Math.round(Double.parseDouble(pay_amount))));

                        tvCuponTa1.setText("-"+ MainActivity.currency_sign+Math.round(discount_amount));
                        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);

                        Log.e("total_amount123333", String.valueOf(total_amount));
                        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);



                    }else{
                        alertDialog.dismiss();
                        tvCuponTa1.setText("-");
                        Toast.makeText(Cart_activity.this, ""+message, Toast.LENGTH_SHORT).show();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(this, Home.class);
        startActivity(intent);
    }


}

