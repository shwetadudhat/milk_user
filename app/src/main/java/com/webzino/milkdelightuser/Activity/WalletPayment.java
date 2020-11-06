package com.webzino.milkdelightuser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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


public class WalletPayment extends BaseActivity {

    private static final String TAG = "CashFreeActivity";
    DatabaseHandler db;
    Session_management session_management;
    String user_id,user_name,user_phn,user_email,activity;
    String amount;

    int randomDigit ;
    String appId,orderId ,orderAmount,orderNote,customerName,customerPhone,customerEmail;
    String token;
    String stage = "TEST";

    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor myEdit,myEdit1;

    String transactionId="";
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

        randomDigit = random(0, 1000);
        appId = getString(R.string.cashfree_api_key);
        orderId = "order_"+randomDigit;
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
                Toast.makeText(WalletPayment.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                    WalletPayment.this,
                    CFPaymentService.getCFPaymentServiceInstance().doesPhonePeExist(WalletPayment.this, stage)+"",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (view.getId() == R.id.gpay_ready) {
            CFPaymentService.getCFPaymentServiceInstance().isGPayReadyForPayment(WalletPayment.this, new GooglePayStatusListener() {
                @Override
                public void isReady() {
                    Toast.makeText(WalletPayment.this, "Ready", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void isNotReady() {
                    Toast.makeText(WalletPayment.this, "Not Ready", Toast.LENGTH_SHORT).show();
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
                cfPaymentService.doPayment(WalletPayment.this, params, token, stage, "#784BD2", "#FFFFFF", false);
//                 cfPaymentService.doPayment(CashFreeActivity.this, params, token, stage);
                break;
            }

            case R.id.upi: {
                params.put(PARAM_PAYMENT_OPTION, "upi");
                params.put(PARAM_UPI_VPA, "testsuccess@gocash");
//                cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
                cfPaymentService.upiPayment(WalletPayment.this, params, token, stage);
                break;
            }
            case R.id.amazon: {
                cfPaymentService.doAmazonPayment(WalletPayment.this, params, token, stage);
                break;
            }
            case R.id.gpay: {
                cfPaymentService.gPayPayment(WalletPayment.this, params, token, stage);
                break;
            }
            case R.id.phonePe: {
                cfPaymentService.phonePePayment(WalletPayment.this, params, token, stage);
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
        if (data != null) {
            Bundle bundle = data.getExtras();
            Intent intent=new Intent();
            for (String key : bundle.keySet()) {
                if (bundle.getString(key) != null) {
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

                    if (key.equals("paymentMode")){
                        String paymentMode= bundle.getString(key);
                        intent.putExtra("paymentMode",paymentMode);
                    }
                }
            }

            intent.putExtra("orderAmount",orderAmount);

            setResult(Activity.RESULT_OK,intent);
            finish();

        }
        /*if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                showDiaog(WalletPayment.this,bundle);

        }*/
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
