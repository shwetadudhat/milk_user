package com.webzino.milkdelightuser.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Cart_Adapter1;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.ConnectivityReceiver;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.webzino.milkdelightuser.utils.Global.MY_PLAN_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.MY_STARTDATE_PREFS_NAME;


public class BuyOnce extends BaseActivity {

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

    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1,tvcgst1,tvsgst1;


    /*bottom view 2*/
    LinearLayout llPayNow;
    Button btnPaynow;
    RelativeLayout rlChk;
    CheckBox ChkWallet;
    TextView balance;

    Session_management sessionManagement;
    String u_id,user_name,user_nmbr,user_email;


    String promo_code;

    double total_gst,total_amount;
    double pro_gst,pro_sgst;

    double razorAmount=0,wallet,walletAmount;

    String Addrid,pincode;

    String pay_amount,discount_amount;

    JSONArray passArray;
    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor myEdit,myEdit1;

    AlertDialog alertDialog;
    ArrayList<HashMap<String, String>> map;

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_buyonce);
        setContent();

        db = new DatabaseHandler(BuyOnce.this);
        map = db.getCartAll();

        sessionManagement=new Session_management(BuyOnce.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        user_name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);

        sharedPreferences = getSharedPreferences(MY_STARTDATE_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(MY_PLAN_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        myEdit1 = sharedPreferences.edit();

        recycler_cartList.setLayoutManager(new LinearLayoutManager(BuyOnce.this));

        if (isInternetConnected()) {
            try {
                showDialog("");
                getAddressData(u_id);
                showTotalCredit(u_id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        pay_amount=getIntent().getStringExtra("total_amount");
        discount_amount=getIntent().getStringExtra("discount_amount");
        promo_code=getIntent().getStringExtra("promo_code");


        setPriceDetail();

        llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAdrEdit.callOnClick();
            }
        });

        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BuyOnce.this, Addresslist.class);
                startActivityForResult(intent,0);
            }
        });

        getOrderOnceList();

        btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (map.size()>0){
                    if (Addrid==null){
                        Toast.makeText(BuyOnce.this, "Choose Delivery Address First", Toast.LENGTH_SHORT).show();
                    }else{
                        if (isInternetConnected()){
                            showDialog("");
                            checkPincode();

                        }
//                    buyOrdrOnce();
                    }
                }else{
                    Toast.makeText(BuyOnce.this, "No data Found", Toast.LENGTH_SHORT).show();
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

    private void setContent() {
        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);
        title=findViewById(R.id.title);
        title.setText(getString(R.string.shoppingcart));

        llEdit=findViewById(R.id.llEdit);
        ll_addresstext=findViewById(R.id.ll_addresstext);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        cart_adrName=findViewById(R.id.cart_adrName);
        cart_adr=findViewById(R.id.cart_adr);
        cart_adrNmbr=findViewById(R.id.cart_adrNmbr);
        recycler_cartList=findViewById(R.id.recycler_cartList);

        tvBagTag1=findViewById(R.id.tvBagTag1);
        tvDelChargeTag1=findViewById(R.id.tvDelChargeTag1);
        tvCuponTa1=findViewById(R.id.tvCuponTa1);
        tvTotalTag1=findViewById(R.id.tvTotalTag1);
        tvcgst1=findViewById(R.id.tvcgst1);
        tvsgst1=findViewById(R.id.tvsgst1);

        llPayNow=findViewById(R.id.llPayNow);
        btnPaynow=findViewById(R.id.btnPaynow);
        rlChk=findViewById(R.id.rlChk);
        ChkWallet=findViewById(R.id.ChkWallet);
        balance=findViewById(R.id.balance);


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
                  Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void setPriceDetail() {
        total_amount= Double.parseDouble(db.getTotalAmount());
        Log.e("total_amount", String.valueOf(Integer.parseInt(db.getTotalAmountIncludeGSt())));
        tvBagTag1.setText(MainActivity.currency_sign+db.getTotalAmount());
        tvDelChargeTag1.setText("FREE");
        tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
        Double gst=Double.parseDouble(db.getTotalAmount())-Double.parseDouble(db.getTotalAmountIncludeGSt());
        Log.e("gst123",String.valueOf(gst));

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

                Log.e("total_gst==>123",String.valueOf(total_gst));
            }else{
                tvcgst1.setText(""+ MainActivity.currency_sign+"0.0");
                tvsgst1.setText(""+ MainActivity.currency_sign+"0.0");
            }
        }


        Log.e("total_gst==>123",String.valueOf(total_gst));


        if (pay_amount!=null){
            btnPaynow.setText(getString(R.string.pay_now)+" ("+ MainActivity.currency_sign+pay_amount+")");
            tvTotalTag1.setText(MainActivity.currency_sign+pay_amount);
            tvCuponTa1.setText("-"+ MainActivity.currency_sign+discount_amount);
            total_amount=Double.parseDouble(pay_amount);
        }else{
            tvDelChargeTag1.setText("FREE");
            tvCuponTa1.setText("-");
//            tvTotalTag1.setText(MainActivity.currency_sign+total_amount);
            tvTotalTag1.setText(MainActivity.currency_sign+db.getTotalAmount());
            btnPaynow.setText(getString(R.string.pay_now)+" ("+ MainActivity.currency_sign+db.getTotalAmount()+")");
        }
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
                                Intent intent=new Intent(BuyOnce.this, Addresslist.class);
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
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                            balance.setText("Your Balance : "+ MainActivity.currency_sign+wallet);
                            if (wallet>0){
                                ChkWallet.setChecked(true);
                                rlChk.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                            }else{
                                ChkWallet.setChecked(false);
                                ChkWallet.setEnabled(false);
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
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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

                pro_gst=Double.valueOf(map.get("price"))-Double.valueOf(map.get("gst_price"));
                Log.e("pro_gst",String.valueOf(pro_gst));

                if (pro_gst!=0.0){
                    pro_sgst=pro_gst/2;
                    Log.e("pro_sgst",String.valueOf(pro_sgst));
                }else{
                    pro_sgst=0.0;
                }


                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("order_qty", map.get("qty"));
                    jObjP.put("price", map.get("price"));
                    jObjP.put("amount", db.getTotalAmountById(map.get("product_id")));
                    jObjP.put("product_name", map.get("product_name"));
                    jObjP.put("pack_size", map.get("unit"));
                    jObjP.put("cgst_amount", String.valueOf(pro_sgst));
                    jObjP.put("sgst_amount",String.valueOf(pro_sgst));
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
                intent.putExtra("total_sgst",String.valueOf(total_gst));
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
        if  (walletAmount!=0.0){
            params.put("wallet_amount", String.valueOf(walletAmount));
        }else{
            params.put("wallet_amount", "");
        }
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
        if  (!discount_amount.equals("") || discount_amount.equals("null")){
            params.put("promocode_amount", String.valueOf(discount_amount));
        }else{
            params.put("promocode_amount", "");
        }
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_gst));

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
                            alertDialog = builder.create();
                            alertDialog.setCancelable(false);
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
                            tvTransDesc.setText(getString(R.string.payment_by_wallet));

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
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
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
                if (data == null){
                    Log.e("data",String.valueOf(data));
                }else {
                    String txMsg = data.getStringExtra("txMsg");
                    String referenceId = data.getStringExtra("referenceId");
                    String txStatus = data.getStringExtra("txStatus");
                    String orderAmount = data.getStringExtra("orderAmount");
                    String paymentMode = data.getStringExtra("paymentMode");

                    // TODO: Do something with your extra data

                    Log.e("txStatus",txStatus);

                    if (txStatus.equalsIgnoreCase("FAILED")|| txStatus.equalsIgnoreCase("CANCELLED")){
                        showSuccessDialog(txMsg,referenceId,txStatus);
                    }else{
                        if (ConnectivityReceiver.isConnected()) {
                            showDialog("");
                            oreder_once(passArray,orderAmount,referenceId,paymentMode,txMsg,txStatus);
                        } else {
                            Global.showInternetConnectionDialog(getApplicationContext());
                        }
                    }

                }

            }else{
                Log.e("resultcode",String.valueOf(resultCode));
            }
        }
    }//

    private void showSuccessDialog( String txMsg, String referenceId, String txStatus) {


        AlertDialog.Builder builder = new AlertDialog.Builder(BuyOnce.this);
        ViewGroup viewGroup = BuyOnce.this.getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(BuyOnce.this).inflate(R.layout.custom_success, viewGroup, false);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
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

        } else {
            tvStts.setText(R.string.payment_fail);
            tvStts.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            ivIcon.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (txStatus.equals("SUCCESS")) {
                    alertDialog.dismiss();
                    Intent intent = new Intent(BuyOnce.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void oreder_once(JSONArray passArray, String orderAmount,String transactionId,String paymentMode,String txMsg, String txStatus) {

        if (promo_code==null){
            promo_code="";
        }

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);

        params.put("pay_type", "CashFree");
        params.put("pay_mode", paymentMode);
        params.put("total_amount", String.valueOf(total_amount));
        params.put("promo_code", promo_code);
        params.put("transaction_id", transactionId);
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_gst));

        if  (walletAmount!=0.0){
            params.put("wallet_amount", String.valueOf(walletAmount));
        }else{
            params.put("wallet_amount", "");
        }
        if (razorAmount!=0.0){
            params.put("razor_pay_amount", String.valueOf(orderAmount));
        }else{
            params.put("razor_pay_amount", "");
        }
        if  (!discount_amount.equals("") || discount_amount.equals("null")){
            params.put("promocode_amount", String.valueOf(discount_amount));
        }else{
            params.put("promocode_amount", "");
        }

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

                        showSuccessDialog(txMsg, transactionId, txStatus);

                    }else {
                        Toast.makeText(BuyOnce.this, message, Toast.LENGTH_SHORT).show();
                        showSuccessDialog(txMsg, transactionId, txStatus);
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
                 Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        alertDialog.dismiss();
    }




}

