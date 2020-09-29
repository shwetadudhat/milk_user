package com.milkdelightuser.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Activity.Home;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.Activity.Recharge_history;
import com.milkdelightuser.Activity.WalletPayment;
import com.milkdelightuser.CashFreeActivity;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseFragment;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet_Fragment extends BaseFragment {
    TextView wallet_price,tvViewTransaction,tvDesc,btnAddMoney;
    String amount , user_id,user_name,user_nmbr,user_email;
    int count = 0;
    EditText amount_edit;

    Button price100,price1000,price2000,price3000;
    Session_management session_management;
    public static String priceee;

    Double pay;

    SharedPreferences settings ;
    SharedPreferences.Editor editor;


    public Wallet_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet_, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        session_management=new Session_management(getContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);


        wallet_price = view.findViewById(R.id.wallet_price);
        btnAddMoney = view.findViewById(R.id.btnAddMoney);
        amount_edit= view.findViewById(R.id.amount_edit);
        tvDesc= view.findViewById(R.id.tvDesc);
        tvViewTransaction= view.findViewById(R.id.tvViewTransaction);

        amount_edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                amount_edit.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));
            }
        });

        tvViewTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),Recharge_history.class);
                //intent.putExtra("wallet_price",priceee);
                startActivity(intent);
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            showTotalCredit(user_id);
        } else {
            Global.showInternetConnectionDialog(getContext());
        }

        btnAddMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (amount_edit.getText().toString().length()==0){
                    Toast.makeText(getContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();
                } else if (Integer.valueOf(amount_edit.getText().toString())>0){

                    pay= Double.valueOf(amount_edit.getText().toString().trim());
                    Log.e("pay",String.valueOf(pay));

                    Intent intent=new Intent(getContext(), WalletPayment.class);
                    intent.putExtra("activity","wallet");
                    intent.putExtra("cashfree_amount",String.valueOf(pay));

//                    startActivity(intent);
                    startActivityForResult(intent,0);

                } else {
                    Toast.makeText(getContext(), "Please enter the amount", Toast.LENGTH_SHORT).show();

                }
            }
        });

        price100 = view.findViewById(R.id.price100);
        price1000 = view.findViewById(R.id.price1000);
        price2000 = view.findViewById(R.id.price2000);
        price3000 = view.findViewById(R.id.price3000);


        amount_edit.setText(String.valueOf(count));

        price3000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count= Integer.valueOf(amount_edit.getText().toString());
                count = count + 3000;
                amount_edit.setText(String.valueOf(count));

            }
        });
        price1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count= Integer.valueOf(amount_edit.getText().toString());
                count = count + 1000;
                amount_edit.setText(String.valueOf(count));

            }
        });
        price2000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count= Integer.valueOf(amount_edit.getText().toString());
                count = count + 2000;
                amount_edit.setText(String.valueOf(count));

            }
        });
        price100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count= Integer.valueOf(amount_edit.getText().toString());
                count = count + 100;
                amount_edit.setText(String.valueOf(count));

            }
        });


    }


    private void showTotalCredit(String user_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id",user_id);

        Log.e("user_id",user_id);

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.show_credit, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("wallet_Tag",response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {


                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String wallet = jsonObject.getString("wallet_credits");

                            wallet_price.setText(MainActivity.currency_sign + Math.round(Double.parseDouble(wallet)));
                            priceee=wallet_price.getText().toString();
                            Log.e("priceeee",priceee);

                             settings = getContext().getSharedPreferences("wallet", Context.MODE_PRIVATE);
                             editor = settings.edit();
                             editor.putString("wallet", priceee);
                             editor.apply();

                        }
                    }
                    else {

                        Toast.makeText(getContext(),""+message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissDialog();
                Log.e("error",error.toString());
               // Toast.makeText(getContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK) {
                String txMsg = data.getStringExtra("txMsg");
                String referenceId = data.getStringExtra("referenceId");
                String txStatus = data.getStringExtra("txStatus");
                String orderAmount = data.getStringExtra("orderAmount");
                // TODO: Do something with your extra data

                Log.e("msggggg",txMsg);

                showSuccessDialog(txMsg,referenceId,txStatus,orderAmount);

            }
        }
    }

    private void showSuccessDialog( String txMsg, String referenceId, String txStatus,String orderAmount) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        ViewGroup viewGroup = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.custom_success, viewGroup, false);
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

        amount_edit.setText("0");

        if (txStatus.equals("SUCCESS")){
            tvStts.setText(R.string.payment_success);
            tvStts.setTextColor(getContext().getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            ivIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

            if (ConnectivityReceiver.isConnected()) {
                showDialog("");
                wallet1(referenceId, orderAmount);
                Log.e("wallet", "wallet");
            } else {
                Global.showInternetConnectionDialog(getContext());
            }
        } else {
            tvStts.setText(R.string.payment_fail);
            tvStts.setTextColor(getContext().getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            ivIcon.setColorFilter(ContextCompat.getColor(getContext(), R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void wallet1(String referenceId, String orderAmount) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();

        params.put("user_id",user_id);
        params.put("amount",orderAmount);
        params.put("transaction_id",referenceId);
        params.put("pay_type","CashFree");

        Log.e("params_wallet",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.add_credit, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("addCredit123",response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
                         showTotalCredit(user_id);
                    }
                    else {

                        Toast.makeText(getContext(),""+message, Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",error.toString());

//                Toast.makeText(pay_razor.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }

    @Override
    public void onResume(){
        super.onResume();
        //OnResume Fragment
        Log.e("onresumewallet","onresumewallet");
       /* Wallet_Fragment wallet_fragment = new Wallet_Fragment();
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container_12, wallet_fragment);
        transaction.commit();*/
    }
}
