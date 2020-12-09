package com.webzino.milkdelightuser;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;
import com.google.gson.Gson;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.Home;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;
import static com.cashfree.pg.CFPaymentService.PARAM_PAYMENT_OPTION;
import static com.cashfree.pg.CFPaymentService.PARAM_UPI_VPA;
import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.webzino.milkdelightuser.utils.Global.MY_PLAN_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.MY_STARTDATE_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.random;


public class CashFreeActivity extends BaseActivity {

    private static final String TAG = "CashFreeActivity";
    DatabaseHandler db;
    Session_management session_management;
    String user_id,user_name,user_phn,user_email,Addrid="",activity;
    String amount;

    int randomDigit ;
    String appId,orderId ,orderAmount,orderNote,customerName,customerPhone,customerEmail;
    String token;
//    String stage = "TEST";
    String stage =/* "TEST"*/"PROD";

    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor myEdit,myEdit1;
    Gson gson;
    String json;
    String discount_amount;
    String promo_code="";
    double total_price=0,total_gst=0,total_sgst=0;
    String passArrayString;
    JSONArray passArray;
    String walletAmount="",razorAmount="",paymentMode="",transactionId="",total_amount="";
    ArrayList<SubscriptioAddProduct_model> subProductList1;
    TextView textView3;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cashfree);
        setContent();

        db = new DatabaseHandler(getActivity());

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_phn = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);

        sharedPreferences = getSharedPreferences(MY_STARTDATE_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences1 = getSharedPreferences(MY_PLAN_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();
        myEdit1 = sharedPreferences.edit();



        Intent intent = getIntent();
        activity=intent.getStringExtra("activity");

        Log.e("Activity==>", "onCreate: "+activity );

        amount=getIntent().getStringExtra("cashfree_amount");
        textView3.setText(getString(R.string.pay_txt)+" "+ MainActivity.currency_sign+amount+" "+getString(R.string.pay_txt1));
        if (activity.equals("subscription")){
            Addrid=getIntent().getStringExtra("address_id");
            walletAmount=getIntent().getStringExtra("wallet_amount");
            promo_code=getIntent().getStringExtra("promo_code");
            discount_amount= getIntent().getStringExtra("discount_amount");
            total_amount=intent.getStringExtra("total_price");
            subProductList1= (ArrayList<SubscriptioAddProduct_model>) intent.getSerializableExtra("subList");
            Log.e("total_amount_if",total_amount);
            Log.e("subListtttt",subProductList1.toString());

            if (subProductList1.size()>0) {
                for (int i = 0; i < subProductList1.size(); i++) {
                    total_price = total_price + subProductList1.get(i).getProduct_totalprice();
                    total_gst = total_gst + subProductList1.get(i).getProduct_gst();
                    total_sgst = total_sgst + subProductList1.get(i).getProduct_sgst();
                }
                Log.e("total_price", total_price + "\ntotalgsttt:\t" + total_gst + "\ntotalsgst:\t" + total_sgst);

                passArray = new JSONArray();
                for (int i = 0; i < subProductList1.size(); i++) {
                    JSONObject jObjP = new JSONObject();
                    try {
                        jObjP.put("product_id", subProductList1.get(i).getProduct_id());
                        jObjP.put("order_qty", subProductList1.get(i).getProduct_qty());
                        jObjP.put("subscription_price", subProductList1.get(i).getProduct_price());
                        jObjP.put("plans_id", subProductList1.get(i).getPlan_id());
                        jObjP.put("amount", subProductList1.get(i).getProduct_totalprice() + subProductList1.get(i).getProduct_gst() + subProductList1.get(i).getProduct_sgst());
                        jObjP.put("cgst_amount", subProductList1.get(i).getProduct_gst());
                        jObjP.put("sgst_amount", subProductList1.get(i).getProduct_sgst());
                        jObjP.put("start_date", subProductList1.get(i).getStart_date());
                        jObjP.put("end_date", subProductList1.get(i).getEnd_date());
                        passArray.put(jObjP);

                        Log.e("passarrayyya",passArray.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

           /* }*/
        }

        Calendar rightNow = Calendar.getInstance();


        randomDigit = random(0, 1000);
        appId = getString(R.string.cashfree_api_key);
        orderId = "order_"+rightNow.getTimeInMillis()+randomDigit;
        orderAmount = amount/*"10"*/;
        orderNote = "Test Order";
        customerName =user_name;
        customerPhone =user_phn;
        customerEmail = user_email;


        if (isInternetConnected()) {
            showDialog("");
            getToken(orderId,orderAmount);
        }
    }

    private void setContent() {
        textView3=findViewById(R.id.textView3);
    }

    private void getToken(String orderId, String orderAmount) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("order_id", orderId);
        params.put("order_amount", orderAmount);

        Log.e("tokenParam",params.toString());
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( Request.Method.POST, BaseURL.generate_cashfree_token, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("getTokenResponsre", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    //  Toast.makeText(Address_add_edit.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        token=response.getString("data");
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
                Toast.makeText(CashFreeActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void onClick(View view) {


        //Show the UI for doGPayPayment and phonePePayment only after checking if the apps are ready for payment
        if (view.getId() == R.id.phonePe_exists) {
            Toast.makeText(
                    CashFreeActivity.this,
                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(CashFreeActivity.this, stage)+"",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (view.getId() == R.id.gpay_ready) {
            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(CashFreeActivity.this, new GooglePayStatusListener() {
                @Override
                public void isReady() {
                    Toast.makeText(CashFreeActivity.this, "Ready", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void isNotReady() {
                    Toast.makeText(CashFreeActivity.this, "Not Ready", Toast.LENGTH_SHORT).show();
                }
            });
            return;
        }


        Map<String, String> params = new HashMap<>();

        params.put(PARAM_APP_ID, appId);
        params.put(PARAM_ORDER_ID, orderId);
        params.put(PARAM_ORDER_AMOUNT, orderAmount);
        params.put(PARAM_ORDER_NOTE, orderNote);
        params.put(PARAM_CUSTOMER_NAME, customerName);
        params.put(PARAM_CUSTOMER_PHONE, customerPhone);
        params.put(PARAM_CUSTOMER_EMAIL, customerEmail);
        params.put(PARAM_ORDER_CURRENCY, "INR");


        for(Map.Entry entry : params.entrySet()) {
            Log.d("CFSKDSample", entry.getKey() + " " + entry.getValue());
        }

        CFPaymentService cfPaymentService = CFPaymentService.getCFPaymentServiceInstance();
        cfPaymentService.setOrientation(0);
        switch (view.getId()) {

            case R.id.web: {
                cfPaymentService.doPayment(CashFreeActivity.this, params, token, stage, "#0092FF", "#FFFFFF", false);
//                 cfPaymentService.doPayment(CashFreeActivity.this, params, token, stage);
                break;
            }

            case R.id.upi: {
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "testsuccess@gocash");
//                cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
                cfPaymentService.upiPayment(CashFreeActivity.this, params, token, stage);
                break;
            }
            case R.id.amazon: {
                cfPaymentService.doAmazonPayment(CashFreeActivity.this, params, token, stage);
                break;
            }
            case R.id.gpay: {
                cfPaymentService.gPayPayment(CashFreeActivity.this, params, token, stage);
                break;
            }
            case R.id.phonePe: {
                cfPaymentService.phonePePayment(CashFreeActivity.this, params, token, stage);
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : "+resultCode);
        //Prints all extras. Replace with app logic.
        /*if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                showDiaog(CashFreeActivity.this,bundle);

        }*/
        Log.e("data",String.valueOf(data));
        if (data != null) {
            Bundle bundle = data.getExtras();
            Intent intent=new Intent();
            for (String key : bundle.keySet()) {
                if (bundle.getString(key) != null) {


//                    showDiaog(CashFreeActivity.this,bundle);

                    if (key.equals("txMsg")){
                        intent.putExtra("txMsg",bundle.getString(key));
                    }
                    if (key.equals("referenceId")){
                        transactionId=bundle.getString(key);
                        intent.putExtra("referenceId",transactionId);
                    }

                    if (key.equals("txStatus")){
                        String status= bundle.getString(key);
                        intent.putExtra("txStatus",status);
                    }
                    /*paymentMode*/
                    if (key.equals("paymentMode")){
                        String paymentMode= bundle.getString(key);
                        intent.putExtra("paymentMode",paymentMode);
                    }
                }
            }

            intent.putExtra("orderAmount",orderAmount);
            intent.putExtra("subList",subProductList1);

            setResult(Activity.RESULT_OK,intent);
            finish();

        }
    }

    public  void showDiaog(Context context, Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ViewGroup viewGroup = ((Activity)context).getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_success, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));


        LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
        TextView tvStts=dialogView.findViewById(R.id.tvStts);
        TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
        TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
        ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

        for (String key : bundle.keySet()) {
            if (bundle.getString(key) != null) {
                if (key.equals("txMsg")){
                    tvTransDesc.setText(bundle.getString(key));
                }
                if (key.equals("referenceId")){
                    transactionId=bundle.getString(key);
                    tvTransId.setText("Transaction id:"+transactionId);
                }

                if (key.equals("txStatus")){
                    String status= bundle.getString(key);
                    if (status.equals("SUCCESS")){
                        tvStts.setText("Payment Success");
                        tvStts.setTextColor(context.getResources().getColor(R.color.green));
                        ivIcon.setImageResource(R.drawable.ic_noun_check_1);
                        ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                       /* tvTransDesc.setText("Payment done through your Wallet amount");*/
                        if (key.equals("paymentMode")){
                            paymentMode=bundle.getString(key);
                        }


                     //   db.clearCart();
                        if (activity.equals("subscription")) {
                            if (isInternetConnected()) {
                                but_sub_plan(passArray, orderAmount);
                                Log.e("parseArray", passArray.toString());
                            }
                        }


                    }else{
                        tvStts.setText("Payment Failed");
                        tvStts.setTextColor(context.getResources().getColor(R.color.red));
                        ivIcon.setImageResource(R.drawable.ic_noun_close_1);
                        //  ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
                        ivIcon.setColorFilter(ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                        /* tvTransDesc.setText("Payment done through your Wallet amount");*/

                    }
                }

            }
        }



        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent=new Intent(context, Home.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                    alertDialog.dismiss();

            }
        });


        alertDialog.show();

    }


    public  void but_sub_plan(JSONArray passArray, String orderAmount) {


        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("wallet_amount", String.valueOf(walletAmount));
        params.put("razor_pay_amount", String.valueOf(orderAmount));
        params.put("pay_type", "CashFree");
        params.put("pay_mode", "");
        params.put("total_amount", String.valueOf(total_price));
        params.put("promo_code", promo_code);
        params.put("transaction_id", transactionId);
        params.put("promocode_amount", String.valueOf(discount_amount));
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_sgst));

        Log.e("subadd123",params.toString());



        /*CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscribe_plans_add, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAddedd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Log.e("razorAmount", String.valueOf(razorAmount));

                        db.clearCart();
                        myEdit.clear().apply();
                        myEdit1.clear().apply();


                    }else if (status.equals("3")){
                        Toast.makeText(CashFreeActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else if (status.equals("0")){
                        Toast.makeText(CashFreeActivity.this, message, Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);*/

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
