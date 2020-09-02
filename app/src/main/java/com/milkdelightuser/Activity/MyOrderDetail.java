package com.milkdelightuser.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class MyOrderDetail extends BaseActivity {

    ImageView ivBack,ivNotify;
    TextView title;

    ImageView image_ordr;
    TextView text_ordr,price_ordr,qty_ordr,ordr_date,ordr_deldate;

    TextView ordr_custName,ordr_address,ordr_addNmbr;

    TextView tvBagTag1,tvDelChargeTag1,tvCuponTa1,tvTotalTag1;
    RelativeLayout rv_main,container_null1;

    String order_id;
    Session_management sessionManagement;
    String u_id,product_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_my_order_detail);

        title=findViewById(R.id.title);
        title.setText("Order Details");

        image_ordr=findViewById(R.id.image_ordr);
        text_ordr=findViewById(R.id.text_ordr);
        price_ordr=findViewById(R.id.price_ordr);
        qty_ordr=findViewById(R.id.qty_ordr);
        ordr_date=findViewById(R.id.ordr_date);
        ordr_deldate=findViewById(R.id.ordr_deldate);

        ordr_custName=findViewById(R.id.ordr_custName);
        ordr_address=findViewById(R.id.ordr_address);
        ordr_addNmbr=findViewById(R.id.ordr_addNmbr);

        tvBagTag1=findViewById(R.id.tvBagTag1);
        tvDelChargeTag1=findViewById(R.id.tvDelChargeTag1);
        tvCuponTa1=findViewById(R.id.tvCuponTa1);
        tvTotalTag1=findViewById(R.id.tvTotalTag1);


        rv_main=findViewById(R.id.rv_main);
        container_null1=findViewById(R.id.container_null1);


        order_id=getIntent().getStringExtra("order_id");
        product_image=getIntent().getStringExtra("product_image");
        sessionManagement=new Session_management(MyOrderDetail.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        if (isInternetConnected()) {
            showDialog("");
            getMyOrderDetail(u_id,order_id);
            //getAddressData(u_id);

        }


        if (tvDelChargeTag1.getText().toString().equals("Free")){
          //    tvTotalTag1.setText(Integer.parseInt(tvBagTag1.getTextColors().toString())+Integer.parseInt(tvCuponTa1.getTextColors().toString()));
        }else{
//              tvTotalTag1.setText(Integer.parseInt(tvBagTag1.getTextColors().toString())+Integer.parseInt(tvDelChargeTag1.getTextColors().toString())+Integer.parseInt(tvCuponTa1.getTextColors().toString()));
        }





        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);

        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyOrderDetail.this, drawer.class);
                intent.putExtra("notification","product_page");
                startActivity(intent);
                finish();
            }
        });
    }

    private void getMyOrderDetail(String u_id, String order_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("order_id", order_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.my_order_details, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("orderDetail_1234", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        container_null1.setVisibility(View.GONE);
                        rv_main.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");

                        JSONObject jsonObject1=jsonObject.getJSONObject("order_details");
                            String product_url=jsonObject.getString("product_url");
                            String product_name=jsonObject1.getString("product_name");
                            String product_image=jsonObject1.getString("product_image");
                            String delivery_date=jsonObject1.getString("delivery_date");
                            String start_date=jsonObject1.getString("start_date");
                            String order_qty=jsonObject1.getString("order_qty");
                            int  product_id=jsonObject1.getInt("product_id");
                            int subs_id=jsonObject1.getInt("subs_id");


                            String price=jsonObject1.getString("price");

                            String description=jsonObject1.getString("description");
                            String subscription_price=jsonObject1.getString("subscription_price");
                            String unit=jsonObject1.getString("unit");
                            String qty=jsonObject1.getString("qty");
                            String sub_status=jsonObject1.getString("sub_status");
                            String full_address=jsonObject1.getString("full_address");
                            String user_number=jsonObject1.getString("user_number");
                            String promocode_amount=jsonObject1.getString("promocode_amount");
                            String promo_code=jsonObject1.getString("promo_code");


                            text_ordr.setText(product_name+" ("+qty+" "+unit+")");
                            price_ordr.setText(MainActivity.currency_sign+price);
                            qty_ordr.setText("Qty : "+order_qty);


                            Log.e("delivery_date",delivery_date+"\nstartdate "+ start_date);
                            /**/

                            String formattedDate= Global.getDateConvert(start_date,"yyyy-MM-dd","EEE dd, MMM yyyy");/*Global.getDateConvert(start_date);*/
                            String DEldate= Global.getDateConvert(delivery_date,"yyyy-MM-dd","EEE dd, MMM yyyy");/*getDateConvert(delivery_date);*/


                            Log.e("formattedDate",formattedDate);
                            Log.e("DEldate",DEldate);

                            ordr_date.setText(formattedDate);
                            ordr_deldate.setText("Successfully order delivered on "+DEldate);

                            Log.e("imagggg",product_url+product_image);

                            Glide.with(MyOrderDetail.this)
                                    .load(product_url+product_image)
                                    .into(image_ordr);

                            tvBagTag1.setText("");
                            tvDelChargeTag1.setText("FREE");
                            tvCuponTa1.setText("-");
                           // tvTotalTag1.setText("");


                     //   ordr_custName.setText(user_name);
                        ordr_address.setText(full_address);
                        ordr_addNmbr.setText(user_number);



                    }else if (status.contains("0")){
                        Toast.makeText(MyOrderDetail.this, ""+message, Toast.LENGTH_SHORT).show();
                        container_null1.setVisibility(View.VISIBLE);
                        rv_main.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
             //   Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

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
                        String id=jsonObject.getString("id");
                        String user_number=jsonObject.getString("user_number");
                        String address=jsonObject.getString("full_address");

                        ordr_custName.setText(user_name);
                        ordr_address.setText(address);
                        ordr_addNmbr.setText(user_number);

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }


}
