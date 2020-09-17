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


public class Cart_activity1 extends BaseActivity implements PaymentResultListener {

    private DatabaseHandler db;
    SharedPreferences settings ;
    SharedPreferences.Editor editor;

    ImageView ivBack,ivNotify;
    TextView title;

    LinearLayout scroll_view;
    RelativeLayout viewFirst,viewSecond;
    RelativeLayout container_null1;

    /*view1*/
    RelativeLayout rlCoupon;
    RecyclerView recycler_cartitem;
    Cart_Adapter cart_adapter;

    /*view2*/
    LinearLayout llEdit;
    ImageView ivAdrEdit;
    TextView cart_adrName,cart_adr,cart_adrNmbr;
    RecyclerView recycler_cartList;

    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1;

    /*bottom view 1*/
    LinearLayout llOrder,ll_ordrOnce,ll_buySubscription;

    /*bottom view 2*/
    LinearLayout llPayNow;
    Button btnPaynow;
    RelativeLayout rlChk;
    CheckBox ChkWallet;
    TextView balance;

    Session_management sessionManagement;
    String u_id,subs_id,user_name,user_nmbr,user_email;


    String promo_code;

    int total_bagtag,total_gst,total_sgst,total_amount;

    double razorAmount=0,totalAmout,wallet,walletAmount,discount_amount;

    String razorpayPaymentID="";
    String Addrid,tardetDate;

    int planId;
    String enddate;
    String pay_amount;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart2);

        db = new DatabaseHandler(Cart_activity1.this);


        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText("Shopping Cart");

        sessionManagement=new Session_management(Cart_activity1.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        user_name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

        viewFirst=findViewById(R.id.viewFirst);
        viewSecond=findViewById(R.id.viewSecond);
        scroll_view=findViewById(R.id.scroll_view);
        container_null1=findViewById(R.id.container_null1);

        rlCoupon=findViewById(R.id.rlCoupon);
        recycler_cartitem=findViewById(R.id.recycler_cartitem);
        recycler_cartitem.setLayoutManager(new LinearLayoutManager(Cart_activity1.this));

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

        llEdit=findViewById(R.id.llEdit);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        cart_adrName=findViewById(R.id.cart_adrName);
        cart_adr=findViewById(R.id.cart_adr);
        cart_adrNmbr=findViewById(R.id.cart_adrNmbr);
        recycler_cartList=findViewById(R.id.recycler_cartList);
        recycler_cartList.setLayoutManager(new LinearLayoutManager(Cart_activity1.this));

     //   tvBagTag1.setText("₹"+db.getTotalAmount());

        if (isInternetConnected()) {
            try {
                showDialog("");
                getAddressData(u_id);
                showTotalCredit(u_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        tvBagTag1=findViewById(R.id.tvBagTag1);
        tvDelChargeTag1=findViewById(R.id.tvDelChargeTag1);
        tvCuponTa1=findViewById(R.id.tvCuponTa1);
        tvTotalTag1=findViewById(R.id.tvTotalTag1);


        llOrder=findViewById(R.id.llOrder);
        ll_ordrOnce=findViewById(R.id.ll_ordrOnce);
        ll_buySubscription=findViewById(R.id.ll_buySubscription);

        llPayNow=findViewById(R.id.llPayNow);
        btnPaynow=findViewById(R.id.btnPaynow);
        rlChk=findViewById(R.id.rlChk);
        ChkWallet=findViewById(R.id.ChkWallet);
        balance=findViewById(R.id.balance);


        total_bagtag= (int) (Integer.parseInt(db.getTotalAmount())+ Math.round(Global.getTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount()))) ));
        total_gst= (int) Math.round((Integer.parseInt(db.getTotalAmount())* Global.getGSTTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
        total_sgst=(int) Math.round( (Integer.parseInt(db.getTotalAmount())* Global.getSGSTTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));

        total_amount= Integer.parseInt(db.getTotalAmount())+total_gst+total_sgst;
        Log.e("total_amount", String.valueOf(total_amount));
        tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount()+" (GST: "+total_gst+" SGST: "+total_sgst+")");
        tvDelChargeTag1.setText("FREE");
        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
       // totalAmout=Math.round(total_amount);

        getCartList();


        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAdrEdit.callOnClick();
            }
        });

        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity1.this,Addresslist.class);
                //intent.putExtra("action","edit");
               // intent.putExtra("id",addressModelList.get(i).getAddress_id());
                startActivity(intent);
            }
        });


        rlCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("totroooollll", String.valueOf(total_amount));
                AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity1.this);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(Cart_activity1.this).inflate(R.layout.custom_coupon, viewGroup, false);
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

                totalAmout=total_amount;

                btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code;
                        code=tvCode.getText().toString();

                        Log.e("total_amount123", String.valueOf(total_amount));
                        Log.e("totalAmout", String.valueOf(totalAmout));

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
                viewFirst.setVisibility(View.GONE);
                viewSecond.setVisibility(View.VISIBLE);
                llOrder.setVisibility(View.GONE);
                llPayNow.setVisibility(View.VISIBLE);
                rlChk.setVisibility(View.VISIBLE);

                getOrderOnceList();


            }
        });

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buyOrdrOnce();
            }
        });

        ll_buySubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity1.this,subscription2.class);
               // intent.putExtra("product_id",product_id);
                startActivity(intent);
            }
        });


        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Cart_activity1.this,drawer.class);
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
        Cart_Adapter adapter = new Cart_Adapter(Cart_activity1.this, map);
        recycler_cartitem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.setEventListener(new Cart_Adapter.EventListener() {
            @Override
            public void onItemViewClicked(int i) {

                total_bagtag= (int) (Integer.parseInt(db.getTotalAmount())+ Math.round( Global.getTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
                total_gst= (int) Math.round((Integer.parseInt(db.getTotalAmount())* Global.getGSTTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
                total_sgst=(int) Math.round( (Integer.parseInt(db.getTotalAmount())* Global.getSGSTTax(Cart_activity1.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));

                total_amount= Integer.parseInt(db.getTotalAmount())+total_gst+total_sgst;
                Log.e("total_amount", String.valueOf(total_amount));
                tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount()+" (GST: "+total_gst+" SGST: "+total_sgst+")");
                tvDelChargeTag1.setText("FREE");
                tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
//                totalAmout=Math.round(total_bagtag);

              //  btnPaynow.setText("PAY NOW ("+MainActivity.currency_sign+total_amount+")");


            }
        });

    }

    private void getOrderOnceList(){

        ArrayList<HashMap<String, String>> map1 = db.getCartAll();
        Cart_Adapter1 adapter1 = new Cart_Adapter1(Cart_activity1.this, map1);
        recycler_cartList.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

    }

    private void applyCoupon(AlertDialog alertDialog, String code) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("promo_code_id", "");
        params.put("total_amount", ""+totalAmout);
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

                        String user_id=jsonObject.getString("user_id");
                        String promocode_id=jsonObject.getString("promocode_id");
                        total_amount= jsonObject.getInt("total_amount");
                        discount_amount= Double.parseDouble(jsonObject.getString("discount"));
                        pay_amount=jsonObject.getString("pay_amount");
                        promo_code=jsonObject.getString("promo_code");

                        tvCuponTa1.setText(promo_code);
                        tvTotalTag1.setText(MainActivity.currency_sign+pay_amount);
                        total_amount= Integer.parseInt(pay_amount);
                        Log.e("total_amount123333", String.valueOf(total_amount));
                        //Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
                        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
                        btnPaynow.setText("PAY NOW ("+MainActivity.currency_sign+total_amount+")");


                    }else{
                        alertDialog.dismiss();
                        tvCuponTa1.setText(code);
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

    private void getAddressData(String user_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.user_address, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        //Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String user_name=jsonObject.getString("user_name");
                        Addrid=jsonObject.getString("id");
                        String user_number=jsonObject.getString("user_number");
                        String address=jsonObject.getString("full_address");

                        cart_adrName.setText(user_name);
                        cart_adr.setText(address);
                        cart_adrNmbr.setText(user_number);

                        llEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(Cart_activity1.this,Addresslist.class);
                                startActivityForResult(intent,0);
//                                Intent intent=new Intent(Cart_activity.this,Addresslist.class);
//                               /* intent.putExtra("action","edit");
//                                intent.putExtra("id",Addrid);*/
//                                startActivity(intent);
                            }
                        });

                        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llEdit.callOnClick();
                            }
                        });

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

    private void showTotalCredit(String user_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id",user_id);

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.show_credit, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.d("Tag",response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {


                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            wallet = Double.parseDouble(jsonObject.getString("wallet_credits"));
                            balance.setText("Your Balance : "+MainActivity.currency_sign+wallet);
                            if (wallet>0){
                                ChkWallet.setChecked(true);
                                rlChk.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                            }else{
                                ChkWallet.setChecked(false);
                                rlChk.setBackground(getResources().getDrawable(R.drawable.bg_grey));
                            }

                            settings = getApplicationContext().getSharedPreferences("wallet", Context.MODE_PRIVATE);
                            editor = settings.edit();
                            editor.putString("wallet", String.valueOf(wallet));
                            editor.apply();

                        }
                    }
                    else {

                        Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
               // Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }


    private void buyOrdrOnce() {

        if (ChkWallet.isChecked()){
            if (wallet<total_amount){

                Log.e("ifff", String.valueOf(total_amount));
                razorAmount=total_amount-wallet;
                walletAmount=wallet;

                Log.e("razoramount123", String.valueOf(razorAmount));
                Log.e("totalAmout123", String.valueOf(total_amount));
                Log.e("walletAmount123", String.valueOf(walletAmount));
                Log.e("wallet123", String.valueOf(wallet));


            }else {
                /*wallet 2100  totalprice 400*/

                walletAmount= total_amount;
                razorAmount=0;
                Log.e("totalAmout444", String.valueOf(total_amount));
                Log.e("walletAmount444", String.valueOf(walletAmount));

            }

        }else{
            razorAmount=total_amount;
            walletAmount=0;
            Log.e("razoramount777", String.valueOf(razorAmount));
            Log.e("totalAmout777", String.valueOf(total_amount));



        }

        if (razorAmount!=0){
            startPayment(razorAmount);
        }else{
            buyProduct();
        }



    }

    private void buyProduct() {

        ArrayList<HashMap<String, String>> items = db.getCartAll();
        if (items.size() > 0) {
            JSONArray passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("order_qty", map.get("qty"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("plans_id", String.valueOf(planId));
                    jObjP.put("amount", db.getTotalAmountById(map.get("product_id")));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (isInternetConnected()) {
                buyOnce(passArray);
            }

        }

    }

    private void buyOnce(JSONArray passArray) {

        Log.e("walletAmount", String.valueOf(walletAmount));

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("start_date", tardetDate);
        params.put("end_date", enddate);
        params.put("wallet_amount", String.valueOf(walletAmount));
        params.put("razor_pay_amount", String.valueOf(razorAmount));
        params.put("pay_type", "");
        params.put("pay_mode", "");
        params.put("total_amount", String.valueOf(totalAmout));
        params.put("promo_code", promo_code);
        params.put("promocode_amount", String.valueOf(discount_amount));

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

                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity1.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(Cart_activity1.this).inflate(R.layout.custom_success, viewGroup, false);
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
                            ivIcon.setColorFilter(ContextCompat.getColor(Cart_activity1.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                            tvTransDesc.setText("Payment done through your Wallet amount");

                            db.clearCart();

                            llDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(Cart_activity1.this,drawer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });



                            alertDialog.show();


                        }

                    }else if (status.equals("3")){
                        Toast.makeText(Cart_activity1.this, message, Toast.LENGTH_SHORT).show();
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

    private void startPayment(double razorAmount) {
        Checkout checkout = new Checkout();

        final Activity activity = Cart_activity1.this;
        double price_rs = razorAmount;
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name",user_name);
            options.put("description","Add Amount in Wallet");
            //YOU CAN OMIT THE IMAGE OPTION TO FETCH THE IMAGE FROM DASHBOARD
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency","INR");
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount",price_rs*100);

            JSONObject prefill = new JSONObject();
            prefill.put("email", user_email);
            prefill.put("contact",user_nmbr);

            options.put("prefill",prefill);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }

    }


    private void getTransactionId() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("subs_id", subs_id);
        params.put("transaction_id", razorpayPaymentID);

        Log.e("paramssubadad",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.transaction_id, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAdd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(Cart_activity1.this, message, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        this.razorpayPaymentID=razorpayPaymentID;

        try {

            buyProduct();

            AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity1.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(Cart_activity1.this).inflate(R.layout.custom_success, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
             alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
            TextView tvStts=dialogView.findViewById(R.id.tvStts);
            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);


            //  tvStts.setText("");
            tvTransId.setText("Transaction id : "+razorpayPaymentID);
            getTransactionId();
           // tvTransDesc.setText("");
            tvTransDesc.setText("Payment done through your Razor amount");

            tvStts.setText("Payment Success");
            tvStts.setTextColor(getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivIcon.setColorFilter(ContextCompat.getColor(Cart_activity1.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);


            db.clearCart();

            llDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Cart_activity1.this,drawer.class);
                    startActivity(intent);
                    finish();
                }
            });


            alertDialog.show();


        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }


    }
    @Override
    public void onPaymentError(int i, String s) {

        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(Cart_activity1.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(Cart_activity1.this).inflate(R.layout.custom_success, viewGroup, false);
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

            tvStts.setText("Payment Failed");
            tvStts.setTextColor(getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            //  ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivIcon.setColorFilter(ContextCompat.getColor(Cart_activity1.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);


            llDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();

                    showTotalCredit(u_id);
                    //  wallet();
                }
            });

            alertDialog.show();


        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.e("result","result");
        if (requestCode == 0) {

            Log.e("result_code", String.valueOf(requestCode));
            if(resultCode == Activity.RESULT_OK){

                String user_id11=data.getStringExtra("user_id");
                Log.e("user_id11",user_id11);
                showDialog("");
                getAddressData(user_id11);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("user_id11","user_id11");
                //Write your code if there's no result
            }
        }
    }//


}

