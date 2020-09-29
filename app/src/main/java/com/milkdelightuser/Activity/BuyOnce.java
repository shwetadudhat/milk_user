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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Adapter.Cart_Adapter1;
import com.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

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
import static com.milkdelightuser.utils.Global.MY_PLAN_PREFS_NAME;
import static com.milkdelightuser.utils.Global.MY_STARTDATE_PREFS_NAME;


public class BuyOnce extends BaseActivity  {

    private DatabaseHandler db;
    SharedPreferences settings ;
    SharedPreferences.Editor editor;

    ImageView ivBack,ivNotify;
    TextView title;

    /*view2*/
    LinearLayout llEdit,ll_addresstext;
    ImageView ivAdrEdit;
    TextView cart_adrName,cart_adr,cart_adrNmbr;
    RecyclerView recycler_cartList;

    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1;


    /*bottom view 2*/
    LinearLayout llPayNow;
    Button btnPaynow;
    RelativeLayout rlChk;
    CheckBox ChkWallet;
    TextView balance;

    Session_management sessionManagement;
    String u_id,user_name,user_nmbr,user_email;


    String promo_code;

    int total_bagtag,total_gst,total_sgst,total_amount;
    double gst,sgst,pro_gst,pro_sgst,total_tax;

    double razorAmount=0,/*totalAmout,*/wallet,walletAmount;

    String Addrid,tardetDate,pincode;

    String enddate;
    String pay_amount,discount_amount;

    JSONArray passArray;
    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor myEdit,myEdit1;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buyonce);

        db = new DatabaseHandler(BuyOnce.this);


        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText(getString(R.string.shoppingcart));



        sessionManagement=new Session_management(BuyOnce.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        user_name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

        sharedPreferences = getSharedPreferences(MY_STARTDATE_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(MY_PLAN_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        myEdit1 = sharedPreferences.edit();



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
        ll_addresstext=findViewById(R.id.ll_addresstext);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        cart_adrName=findViewById(R.id.cart_adrName);
        cart_adr=findViewById(R.id.cart_adr);
        cart_adrNmbr=findViewById(R.id.cart_adrNmbr);
        recycler_cartList=findViewById(R.id.recycler_cartList);
        recycler_cartList.setLayoutManager(new LinearLayoutManager(BuyOnce.this));

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



        llPayNow=findViewById(R.id.llPayNow);
        btnPaynow=findViewById(R.id.btnPaynow);
        rlChk=findViewById(R.id.rlChk);
        ChkWallet=findViewById(R.id.ChkWallet);
        balance=findViewById(R.id.balance);

        pay_amount=getIntent().getStringExtra("total_amount");
        discount_amount=getIntent().getStringExtra("discount_amount");
        promo_code=getIntent().getStringExtra("promo_code");


        total_bagtag= (int) (Integer.parseInt(db.getTotalAmount())+ Math.round(Global.getTax(BuyOnce.this, Double.parseDouble(String.valueOf(db.getTotalAmount()))) ));
        total_gst= (int) Math.round((Integer.parseInt(db.getTotalAmount())* Global.getGSTTax(BuyOnce.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));
        total_sgst=(int) Math.round( (Integer.parseInt(db.getTotalAmount())* Global.getSGSTTax(BuyOnce.this, Double.parseDouble(String.valueOf(db.getTotalAmount())))));

        total_amount= Integer.parseInt(db.getTotalAmount())+total_gst+total_sgst;
        Log.e("total_amount", String.valueOf(total_amount));
        tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount()+" (GST: "+total_gst+" SGST: "+total_sgst+")");


        if (pay_amount!=null){
            btnPaynow.setText(getString(R.string.pay_now)+" ("+MainActivity.currency_sign+pay_amount+")");
            tvTotalTag1.setText(MainActivity.currency_sign+pay_amount);
            tvCuponTa1.setText("-"+MainActivity.currency_sign+discount_amount);
            total_amount=Integer.parseInt(pay_amount);
        }else{
            tvDelChargeTag1.setText("FREE");
            tvCuponTa1.setText("-");
            tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
            btnPaynow.setText(getString(R.string.pay_now)+" ("+MainActivity.currency_sign+total_amount+")");
        }


        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAdrEdit.callOnClick();
            }
        });

        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyOnce.this,Addresslist.class);
                startActivityForResult(intent,0);
                /*Intent intent=new Intent(BuyOnce.this,Addresslist.class);
                startActivity(intent);*/
            }
        });




        getOrderOnceList();


        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Addrid==null){
                    Toast.makeText(BuyOnce.this, "Choose Delivery Address First", Toast.LENGTH_SHORT).show();
                }else{
                    if (isInternetConnected()){
                        showDialog("");
                        checkPincode();
                    }
//                    buyOrdrOnce();
                }
            }
        });



        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyOnce.this, Home.class);
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

    private void checkPincode() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("pincode", pincode);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.pincode_check, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("pincheck123rsponse", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                         buyOrdrOnce();
                    }
                    else if (status.equals("0")){
                        Toast.makeText(BuyOnce.this, ""+message, Toast.LENGTH_SHORT).show();
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


    private void getOrderOnceList(){

        ArrayList<HashMap<String, String>> map1 = db.getCartAll();
        Cart_Adapter1 adapter1 = new Cart_Adapter1(BuyOnce.this, map1);
        recycler_cartList.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                Log.e("addresstag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        ll_addresstext.setVisibility(View.VISIBLE);
                        //Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String user_name=jsonObject.getString("user_name");
                        Addrid=jsonObject.getString("id");
                        String user_number=jsonObject.getString("user_number");
                        String address=jsonObject.getString("full_address");
                        pincode=jsonObject.getString("pincode");

                        cart_adrName.setText(user_name);
                        cart_adr.setText(address);
                        cart_adrNmbr.setText(user_number);

                        llEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(BuyOnce.this,Addresslist.class);
                                startActivityForResult(intent,0);

                            }
                        });

                        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llEdit.callOnClick();
                            }
                        });

                    }
                    else if (status.equals("0")){
                        ll_addresstext.setVisibility(View.GONE);
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

                razorAmount=total_amount-wallet;
                walletAmount=wallet;

            }else {
                /*wallet 2100  totalprice 400*/
                walletAmount= total_amount;
                razorAmount=0;
            }
        }else{
            razorAmount=total_amount;
            walletAmount=0;
        }

        Log.e("razoramount123", String.valueOf(razorAmount));
        Log.e("totalAmout123", String.valueOf(total_amount));
        Log.e("walletAmount123", String.valueOf(walletAmount));
        Log.e("wallet123", String.valueOf(wallet));

        buyProduct();


    }

    private void buyProduct() {

        ArrayList<HashMap<String, String>> items = db.getCartAll();
        if (items.size() > 0) {
            passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);

                pro_gst=Double.valueOf(map.get("price"))* Global.getGSTTax(BuyOnce.this, Double.valueOf(map.get("price")));
                pro_sgst=Double.valueOf(map.get("price"))* Global.getSGSTTax(BuyOnce.this, Double.valueOf(map.get("price")));

                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("order_qty", map.get("qty"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("amount", db.getTotalAmountById(map.get("product_id")));
                    jObjP.put("cgst_amount", Math.round(pro_gst));
                    jObjP.put("sgst_amount",Math.round(pro_sgst));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.e("total_amount_cart",String.valueOf(total_amount));

            if (razorAmount!=0){
                Log.e("jsonArray123",passArray.toString());
                Intent intent=new Intent(BuyOnce.this, BuyOncePayment.class);

                intent.putExtra("activity","listitem_cart");
                intent.putExtra("address_id",Addrid);
                intent.putExtra("jsonArray",passArray.toString());
                intent.putExtra("wallet_amount", String.valueOf(walletAmount));
                intent.putExtra("cashfree_amount", String.valueOf(razorAmount));
                intent.putExtra("total_price", String.valueOf(total_amount));
                intent.putExtra("promo_code",promo_code);
                intent.putExtra("discount_amount",String.valueOf(discount_amount));
                intent.putExtra("total_cgst",String.valueOf(total_gst));
                intent.putExtra("total_sgst",String.valueOf(total_sgst));
//                startActivity(intent);
                startActivityForResult(intent,5);

            }else{
                if (isInternetConnected()) {
                    buyOnce(passArray);
                }
            }

        }

    }

    private void buyOnce(JSONArray passArray) {

        if (promo_code==null){
            promo_code="";
        }


        Log.e("walletAmount", String.valueOf(walletAmount));

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("wallet_amount", String.valueOf(walletAmount));
        if (razorAmount!=0.0){
            params.put("razor_pay_amount", String.valueOf(razorAmount));
        }else{
            params.put("razor_pay_amount", "");
        }

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

                            AlertDialog.Builder builder = new AlertDialog.Builder(BuyOnce.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(BuyOnce.this).inflate(R.layout.custom_success, viewGroup, false);
                            builder.setView(dialogView);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                               alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
                            TextView tvStts=dialogView.findViewById(R.id.tvStts);
                            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
                            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
                            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

                            tvTransId.setVisibility(View.GONE);

                            tvStts.setText(R.string.payment_success);
                            tvStts.setTextColor(getResources().getColor(R.color.green));
                            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
                            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                            ivIcon.setColorFilter(ContextCompat.getColor(BuyOnce.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                            tvTransDesc.setText("Payment done through your Wallet amount");

                            db.clearCart();

                            llDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(BuyOnce.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                            alertDialog.show();


                        }

                    }else if (status.equals("3")){
                        Toast.makeText(BuyOnce.this, message, Toast.LENGTH_SHORT).show();
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

        }else if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK) {
                String txMsg = data.getStringExtra("txMsg");
                String referenceId = data.getStringExtra("referenceId");
                String txStatus = data.getStringExtra("txStatus");
                String orderAmount = data.getStringExtra("orderAmount");

                // TODO: Do something with your extra data

                Log.e("msggggg",txMsg);
                showDialog("");
                showSuccessDialog(txMsg,referenceId,txStatus,orderAmount);

                //  but_sub_plan(passArray,orderAmount);

            }
        }
    }//

    private void showSuccessDialog( String txMsg, String referenceId, String txStatus,String orderAmount) {


        AlertDialog.Builder builder = new AlertDialog.Builder(BuyOnce.this);
        ViewGroup viewGroup =BuyOnce.this.getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BuyOnce.this).inflate(R.layout.custom_success, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));


        LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
        TextView tvStts=dialogView.findViewById(R.id.tvStts);
        TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
        TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
        ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);


        tvTransDesc.setText(txMsg);
        tvTransId.setText("Transaction id:"+referenceId);



        if (txStatus.equals("SUCCESS")){
            tvStts.setText(R.string.payment_success);
            tvStts.setTextColor(getApplicationContext().getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            ivIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

            if (ConnectivityReceiver.isConnected()) {
                showDialog("");

                oreder_once(passArray,orderAmount,referenceId);
                /* but_sub_plan1(passArray, orderAmount,referenceId);*/
                Log.e("parseArray", passArray.toString());
            } else {
                Global.showInternetConnectionDialog(getApplicationContext());
            }
        } else {
            tvStts.setText(R.string.payment_fail);
            tvStts.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            ivIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void oreder_once(JSONArray passArray, String orderAmount,String transactionId) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("wallet_amount", String.valueOf(walletAmount));
        params.put("razor_pay_amount", orderAmount);
        params.put("pay_type", "CashFree");
        params.put("total_amount", String.valueOf(total_amount));
        params.put("promo_code", promo_code);
        params.put("transaction_id", transactionId);
        params.put("promocode_amount", String.valueOf(discount_amount));
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_sgst));

        Log.e("parambuyonce",params.toString());



        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.order_buy_once, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("buyonce123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Log.e("razorAmount", String.valueOf(razorAmount));

                        db.clearCart();
                        myEdit1.clear().apply();
                        myEdit.clear().apply();

                    }else if (status.equals("3")){
                        Toast.makeText(BuyOnce.this, message, Toast.LENGTH_SHORT).show();
                    }else if (status.equals("0")){
                        Toast.makeText(BuyOnce.this, message, Toast.LENGTH_SHORT).show();
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


}

