package com.webzino.milkdelightuser.Activity;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_RenewPlan;
import com.webzino.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.webzino.milkdelightuser.utils.Global.IMGURL_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.URL_DATA;

public class RenewPlanActivity extends BaseActivity {
    TextView tvLeftDay,title;
    ImageView ivBack;
    LinearLayout llRenewSubscrption;
    RecyclerView recycler_renewplan;
    RelativeLayout container_null1;
    String order_id;
    Session_management session_management;
    String user_id;

    ArrayList<SubscriptioAddProduct_model> renewProductModelList=new ArrayList<>();
    ArrayList<SubscriptioAddProduct_model> renewProductModelList1=new ArrayList<>();
    Adapter_RenewPlan adapter_renewPlan;
    Double total_price;
    JSONArray passArray;
    String address_id;
    long days1;
    Double total_gst,total_sgst;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_renew_plan);

        ivBack=findViewById(R.id.ivBack);
        tvLeftDay=findViewById(R.id.tvLeftDay);
        title=findViewById(R.id.title);
        container_null1=findViewById(R.id.container_null1);
        llRenewSubscrption=findViewById(R.id.llRenewSubscrption);
        recycler_renewplan=findViewById(R.id.recycler_renewplan);
        recycler_renewplan.setLayoutManager(new GridLayoutManager(RenewPlanActivity.this,1));

        tvLeftDay.setVisibility(View.GONE);
        title.setText(R.string.renew_plan);

        order_id=getIntent().getStringExtra("order_id");
        Log.e("order_id",order_id);

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);


        if (isInternetConnected()){
            showDialog("");
            SetRenewPlanData();

        }

        llRenewSubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RenewPlanActivity.this, WalletPayment.class);
                intent.putExtra("activity","wallet");
                intent.putExtra("cashfree_amount",String.valueOf(total_price));
                startActivityForResult(intent,0);

            }
        });



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void SetRenewPlanData() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",user_id);
        params.put("order_id",order_id);
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.get_subscription_order_details, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("get_subscription123", response.toString());
                dismissDialog();
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    JSONObject jsonObject=response.getJSONObject("data");
                    if (status.equals("1")){
                        JSONArray jsonArray = jsonObject.getJSONArray("subscriptions");

                        if (jsonArray.length()==0){
                            Toast.makeText(RenewPlanActivity.this, "no data found", Toast.LENGTH_SHORT).show();

                        }else{
                            container_null1.setVisibility(View.GONE);
                            recycler_renewplan.setVisibility(View.VISIBLE);
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                String price=jsonObject1.getString("price");
                                address_id=jsonObject1.getString("address_id");
                                String total_amount=jsonObject1.getString("total_amount");
                                String order_qty=jsonObject1.getString("order_qty");
                                String cgst_amount=jsonObject1.getString("cgst_amount");
                                String sgst_amount=jsonObject1.getString("sgst_amount");
                                String plan_id=jsonObject1.getString("plan_id");
                                String start_date=jsonObject1.getString("start_date");
                                String end_date=jsonObject1.getString("end_date");

                                JSONObject product=jsonObject1.getJSONObject("product");
                                String product_id=product.getString("product_id");
                                String product_name=product.getString("product_name");
                                String qty=product.getString("qty");
                                String unit=product.getString("unit");

                                JSONObject product_imageobj=product.getJSONObject("product_image");
                                String product_image=product_imageobj.getString("product_image");

                                SharedPreferences GstPref1 = getSharedPreferences(IMGURL_PREFS_NAME, MODE_PRIVATE);
                                String urlData = GstPref1.getString(URL_DATA, null);
                                String product_url = null;
                                if (urlData != null) {
                                    Log.e("gstData",urlData);
                                    try {
                                        JSONObject jsonObject12=new JSONObject(urlData);
                                         product_url=jsonObject12.getString("product_url");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                SubscriptioAddProduct_model subscriptioAddProductModel=new SubscriptioAddProduct_model();
                                subscriptioAddProductModel.setProduct_id(product_id);
                                subscriptioAddProductModel.setProduct_name(product_name);
                                subscriptioAddProductModel.setProduct_qty(order_qty);
                                subscriptioAddProductModel.setProduct_unit(qty+" "+unit);
                                subscriptioAddProductModel.setStart_date(start_date);
                                subscriptioAddProductModel.setEnd_date(end_date);
                                subscriptioAddProductModel.setProduct_price(Integer.parseInt(price));
                                subscriptioAddProductModel.setAddress_id(address_id);
                                subscriptioAddProductModel.setProduct_totalprice(Integer.parseInt(total_amount));
                                subscriptioAddProductModel.setPlan_id(plan_id);
                                subscriptioAddProductModel.setProduct_gst(Integer.parseInt(cgst_amount));
                                subscriptioAddProductModel.setImage(product_url+product_image);


                                renewProductModelList.add(subscriptioAddProductModel);


                            }

                            adapter_renewPlan=new Adapter_RenewPlan(RenewPlanActivity.this,renewProductModelList);
                            recycler_renewplan.setAdapter(adapter_renewPlan);
                            adapter_renewPlan.setEventListener(new Adapter_RenewPlan.EventListener() {
                                @Override
                                public void onItemViewClicked(int position, ArrayList<SubscriptioAddProduct_model> renewPlanlist) {
                                    Log.e("SubscriptioAddProduct_model",String.valueOf(renewPlanlist));
                                    renewProductModelList1=renewPlanlist;
                                    RenewPlanList(renewProductModelList1);
                                }
                            });


                        }
                    }else if (status.equals("0")){

                        container_null1.setVisibility(View.VISIBLE);
                        recycler_renewplan.setVisibility(View.GONE);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                //   dismissDialog();
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void RenewPlanList(ArrayList<SubscriptioAddProduct_model> renewProductModellist) {
        total_price=0.0;
        total_gst=0.0;
        total_sgst=0.0;
        for (int i=0;i<renewProductModellist.size();i++){
            total_price=total_price+Double.parseDouble(String.valueOf(renewProductModellist.get(i).getProduct_totalprice()));
            total_gst=total_gst+Double.parseDouble(String.valueOf(renewProductModellist.get(i).getProduct_gst()));

        }
        Log.e("totalprice",String.valueOf(total_price));
        Log.e("total_gst",String.valueOf(total_gst));
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
                String paymentMode = data.getStringExtra("paymentMode");
                // TODO: Do something with your extra data

                Log.e("msggggg",txMsg);

                showSuccessDialog(txMsg,referenceId,txStatus,orderAmount,paymentMode);

            }else{
                Log.e("resultcode",String.valueOf(resultCode));
            }
        }
    }

    private void showSuccessDialog(String txMsg, String referenceId, String txStatus, String orderAmount,String paymentMode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RenewPlanActivity.this);
        ViewGroup viewGroup = RenewPlanActivity.this.getWindow().getDecorView().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(RenewPlanActivity.this).inflate(R.layout.custom_success, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
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
            tvStts.setTextColor(getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            ivIcon.setColorFilter(ContextCompat.getColor(RenewPlanActivity.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

            if (isInternetConnected()) {
                showDialog("");
                renewSubscription(referenceId,paymentMode);
                Log.e("wallet", "wallet");
            }
        } else {
            tvStts.setText(R.string.payment_fail);
            tvStts.setTextColor(getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            ivIcon.setColorFilter(ContextCompat.getColor(RenewPlanActivity.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
        }

        llDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txStatus.equals("SUCCESS")) {
                    alertDialog.dismiss();
                    Intent intent = new Intent(RenewPlanActivity.this, Home.class);
                    startActivity(intent);
                    finish();
                }else{
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.show();
    }

    private void renewSubscription(String referenceId,String paymentMode) {
        Log.e("SubscriptioAddProduct==>1", String.valueOf(renewProductModelList1));
        if (renewProductModelList1.size() > 0) {
            passArray = new JSONArray();
            for (int i = 0; i < renewProductModelList1.size(); i++) {
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", renewProductModelList1.get(i).getProduct_id());
                    jObjP.put("order_qty", renewProductModelList1.get(i).getProduct_qty());
                    jObjP.put("subscription_price", renewProductModelList1.get(i).getProduct_price());
                    jObjP.put("plans_id", renewProductModelList1.get(i).getPlan_id());
                    jObjP.put("amount", renewProductModelList1.get(i).getProduct_totalprice());
                    jObjP.put("cgst_amount", renewProductModelList1.get(i).getProduct_gst());
                    jObjP.put("sgst_amount", renewProductModelList1.get(i).getProduct_gst());
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date startdate = format.parse(renewProductModelList1.get(i).getStart_date());
                        Date endddate = format.parse(renewProductModelList1.get(i).getEnd_date());

                        long diff = endddate.getTime() - startdate.getTime();
                        long seconds = diff / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        days1 = hours / 24;

                        Log.e("dayss==>1", days1 + "\nstartdate:" + startdate);


                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(endddate);
                        calendar.add(Calendar.DAY_OF_YEAR, +1);
                        Date newDate = calendar.getTime();
                        Log.e("newDate", String.valueOf(newDate));


                        calendar.setTime(newDate);
                        calendar.add(Calendar.DATE, (int) days1);
                        Date newEndDate = calendar.getTime();
                        Log.e("newEndDate", String.valueOf(newEndDate));

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String newDate11 = dateFormat.format(newDate);
                        String newEndDate11 = dateFormat.format(newEndDate);

                        Log.e("newEndDate11",newEndDate11+"\n"+newDate11);

                        jObjP.put("start_date",newDate11);
                        jObjP.put("end_date", newEndDate11);


                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    jObjP.put("product_name", renewProductModelList1.get(i).getProduct_name());
                    jObjP.put("pack_size", renewProductModelList1.get(i).getProduct_unit());
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (!referenceId.equals("")){
                    but_renew_plan1(passArray,String.valueOf(total_price),referenceId,paymentMode);
                }

            }
        }
    }

    private void but_renew_plan1(JSONArray passArray, String total_price, String referenceId,String paymentMode) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  address_id);
        params.put("wallet_amount", "");
        params.put("razor_pay_amount", String.valueOf(total_price));
        params.put("pay_type", "CashFree");
        params.put("pay_mode", paymentMode);
        params.put("total_amount", String.valueOf(total_price));
        params.put("promo_code", "");
        params.put("transaction_id", referenceId);
        params.put("promocode_amount", "");
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_gst));

        Log.e("renewdataparam",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscribe_plans_add, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("renew123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                    Log.e("success","success");

                    }else if (status.equals("3")){
                        Toast.makeText(RenewPlanActivity.this, message, Toast.LENGTH_SHORT).show();
                    }else if (status.equals("0")){
                        Toast.makeText(RenewPlanActivity.this, message, Toast.LENGTH_SHORT).show();
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