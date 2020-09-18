package com.milkdelightuser.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.milkdelightuser.Adapter.Cart_Adapter;
import com.milkdelightuser.Adapter.Cart_Adapter1;
import com.milkdelightuser.CashFreeActivity;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


public class Cart_activity extends BaseActivity  {

    private DatabaseHandler db;


    ImageView ivBack,ivNotify;
    TextView title;

    LinearLayout scroll_view;
    RelativeLayout container_null1;

    /*view1*/
    RelativeLayout rlCoupon;
    RecyclerView recycler_cartitem;


    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1;

    /*bottom view 1*/
    LinearLayout llOrder,ll_ordrOnce,ll_buySubscription;


    Session_management sessionManagement;
    String u_id,subs_id,user_name,user_nmbr,user_email;


    String promo_code;

    int total_bagtag,total_gst,total_sgst,total_amount;

    double razorAmount=0,/*totalAmout,*/wallet,walletAmount,discount_amount;

    String Addrid,tardetDate;

    String enddate;
    String pay_amount;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart2);

        db = new DatabaseHandler(Cart_activity.this);


        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText("Shopping Cart");

        sessionManagement=new Session_management(Cart_activity.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        user_name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

        scroll_view=findViewById(R.id.scroll_view);
        container_null1=findViewById(R.id.container_null1);

        rlCoupon=findViewById(R.id.rlCoupon);
        recycler_cartitem=findViewById(R.id.recycler_cartitem);
        recycler_cartitem.setLayoutManager(new LinearLayoutManager(Cart_activity.this));

        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        //  freqDate.setText(day + " " + MONTHS[month] + " " + year);
        tardetDate=day+"-"+month+"-"+year;

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date startdate = format.parse(tardetDate);
            Date endddate = format.parse(tardetDate);

            format = new SimpleDateFormat("yyyy-MM-dd");
            tardetDate = format.format(startdate);
            enddate = format.format(endddate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("tardetDate",tardetDate);



        tvBagTag1=findViewById(R.id.tvBagTag1);
        tvDelChargeTag1=findViewById(R.id.tvDelChargeTag1);
        tvCuponTa1=findViewById(R.id.tvCuponTa1);
        tvTotalTag1=findViewById(R.id.tvTotalTag1);


        llOrder=findViewById(R.id.llOrder);
        ll_ordrOnce=findViewById(R.id.ll_ordrOnce);
        ll_buySubscription=findViewById(R.id.ll_buySubscription);



        total_bagtag= (int) (Integer.parseInt(db.getTotalAmount())+ Math.round(Global.getTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount()))) ));
        total_gst= (int) Math.round((Integer.parseInt(db.getTotalAmount())* Global.getGSTTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
        total_sgst=(int) Math.round( (Integer.parseInt(db.getTotalAmount())* Global.getSGSTTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));

        total_amount= Integer.parseInt(db.getTotalAmount())+total_gst+total_sgst;
        Log.e("total_amount", String.valueOf(total_amount));
        tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount()+" (GST: "+total_gst+" SGST: "+total_sgst+")");
        tvDelChargeTag1.setText("FREE");
        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
       // totalAmout=Math.round(total_amount);

        getCartList();




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
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                            tvCode.setError("please enter the Coupon Code");
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

              Intent intent=new Intent(Cart_activity.this,BuyOnce.class);
              intent.putExtra("total_amount",String.valueOf(total_amount));
              intent.putExtra("discount_amount",String.valueOf(Math.round(discount_amount)));
              intent.putExtra("promo_code",promo_code);
              startActivity(intent);


            }
        });



        ll_buySubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity.this,subscription2.class);
               // intent.putExtra("product_id",product_id);
                startActivity(intent);
            }
        });


        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity.this,drawer.class);
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
        Cart_Adapter adapter = new Cart_Adapter(Cart_activity.this, map);
        recycler_cartitem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setEventListener(new Cart_Adapter.EventListener() {
            @Override
            public void onItemViewClicked(int i) {

                total_bagtag= (int) (Integer.parseInt(db.getTotalAmount())+ Math.round( Global.getTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
                total_gst= (int) Math.round((Integer.parseInt(db.getTotalAmount())* Global.getGSTTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
                total_sgst=(int) Math.round( (Integer.parseInt(db.getTotalAmount())* Global.getSGSTTax(Cart_activity.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));

                total_amount= Integer.parseInt(db.getTotalAmount())+total_gst+total_sgst;
                Log.e("total_amount", String.valueOf(total_amount));
                tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount()+" (GST: "+total_gst+" SGST: "+total_sgst+")");
                tvDelChargeTag1.setText("FREE");
                tvTotalTag1.setText(MainActivity.currency_sign+total_amount);



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

                        total_amount= jsonObject.getInt("total_amount");
                        discount_amount= Double.parseDouble(jsonObject.getString("discount"));
                        pay_amount=jsonObject.getString("pay_amount");
                        promo_code=jsonObject.getString("promo_code");
                        total_amount= Integer.parseInt(String.valueOf(Math.round(Double.parseDouble(pay_amount))));

                        tvCuponTa1.setText("-"+MainActivity.currency_sign+Math.round(discount_amount));
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
        Intent intent=new Intent(this,drawer.class);
        startActivity(intent);
    }


    private void buyOnce(JSONArray passArray) {

        if (promo_code==null){
            promo_code="";
            Log.e("promo_if","promo_if");
        }else{
            Log.e("promo_else","promo_else");
        }

        Log.e("walletAmount", String.valueOf(walletAmount));

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("wallet_amount", String.valueOf(walletAmount));
        params.put("razor_pay_amount", String.valueOf(razorAmount));
        params.put("pay_type", "Wallet Pay");
        params.put("pay_mode", "");
        params.put("transaction_id", "");
        params.put("total_amount", String.valueOf(total_amount));
        params.put("promo_code", promo_code);
        params.put("promocode_amount", String.valueOf(discount_amount));
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_sgst));

        Log.e("paramssubadad",params.toString());


        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.order_buy_once, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAdd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        //Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();

                        if (razorAmount==0){

                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(Cart_activity.this).inflate(R.layout.custom_success, viewGroup, false);
                            builder.setView(dialogView);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                               alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
                            TextView tvStts=dialogView.findViewById(R.id.tvStts);
                            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
                            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
                            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

                            tvTransId.setVisibility(View.GONE);

                            tvStts.setText("Payment Success");
                            tvStts.setTextColor(getResources().getColor(R.color.green));
                            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
                            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                            ivIcon.setColorFilter(ContextCompat.getColor(Cart_activity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                            tvTransDesc.setText("Payment done through your Wallet amount");

                            db.clearCart();

                            llDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(Cart_activity.this,drawer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                            alertDialog.show();


                        }

                    }else if (status.equals("3")){
                        Toast.makeText(Cart_activity.this, message, Toast.LENGTH_SHORT).show();
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }



}

