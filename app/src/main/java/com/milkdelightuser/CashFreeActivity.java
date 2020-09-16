package com.milkdelightuser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cashfree.pg.CFPaymentService;
import com.cashfree.pg.ui.gpay.GooglePayStatusListener;


import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.cashfree.pg.CFPaymentService.PARAM_APP_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_EMAIL;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_NAME;
import static com.cashfree.pg.CFPaymentService.PARAM_CUSTOMER_PHONE;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_AMOUNT;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_CURRENCY;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_ID;
import static com.cashfree.pg.CFPaymentService.PARAM_ORDER_NOTE;

public class CashFreeActivity extends AppCompatActivity {

    private static final String TAG = "CashFreeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashfree);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Same request code for all payment APIs.
//        Log.d(TAG, "ReqCode : " + CFPaymentService.REQ_CODE);
        Log.d(TAG, "API Response : ");
        //Prints all extras. Replace with app logic.
        if (data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null)
                for (String key : bundle.keySet()) {
                    if (bundle.getString(key) != null) {
                        Log.d(TAG, key + " : " + bundle.getString(key));
                    }
                }
        }
    }

    public void onClick(View view) {


        /*
         * stage allows you to switch between sandboxed and production servers
         * for CashFree Payment Gateway. The possible values are
         *
         * 1. TEST: Use the Test server. You can use this service while integrating
         *      and testing the CashFree PG. No real money will be deducted from the
         *      cards and bank accounts you use this stage. This mode is thus ideal
         *      for use during the development. You can use the cards provided here
         *      while in this stage: https://docs.cashfree.com/docs/resources/#test-data
         *
         * 2. PROD: Once you have completed the testing and integration and successfully
         *      integrated the CashFree PG, use this value for stage variable. This will
         *      enable live transactions
         */
        String stage = "TEST";

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

        /*
         * token can be generated from your backend by calling cashfree servers. Please
         * check the documentation for details on generating the token.
         * READ THIS TO GENERATE TOKEN: https://bit.ly/2RGV3Pp
         */
        String token = "TOKEN_DATA";


        /*
         * appId will be available to you at CashFree Dashboard. This is a unique
         * identifier for your app. Please replace this appId with your appId.
         * Also, as explained below you will need to change your appId to prod
         * credentials before publishing your app.
         */
        String appId = "2910406ca04d8e95645bd89d840192";
        String orderId = "Order0001";
        String orderAmount = "1";
        String orderNote = "Test Order";
        String customerName = "John Doe";
        String customerPhone = "9900012345";
        String customerEmail = "test@gmail.com";

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

            /***
             * This method handles the payment gateway invocation (web flow).
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             * @param color1 Background color of the toolbar
             * @param color2 text color and icon color of toolbar
             * @param hideOrderId If true hides order Id from the toolbar
             */
            case R.id.web: {
                cfPaymentService.doPayment(CashFreeActivity.this, params, token, stage, "#784BD2", "#FFFFFF", false);
//                 cfPaymentService.doPayment(CashFreeActivity.this, params, token, stage);
                break;
            }
            /***
             * Same for all payment modes below.
             *
             * @param context Android context of the calling activity
             * @param params HashMap containing all the parameters required for creating a payment order
             * @param token Provide the token for the transaction
             * @param stage Identifies if test or production service needs to be invoked. Possible values:
             *              PROD for production, TEST for testing.
             */
            case R.id.upi: {
//                                cfPaymentService.selectUpiClient("com.google.android.apps.nbu.paisa.user");
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
}
