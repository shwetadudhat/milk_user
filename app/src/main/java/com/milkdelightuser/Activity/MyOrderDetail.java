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

        Log.e("order_id",order_id);
        Log.e("uiddd",u_id);

        if (isInternetConnected()) {
            showDialog("");
            getMyOrderDetail(u_id,order_id);
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
                Intent intent=new Intent(MyOrderDetail.this, Home.class);
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
                        String product_name=jsonObject1.getString("product_name");
                        String product_image=jsonObject1.getString("product_image");
                        String delivery_date=jsonObject1.getString("delivery_date");
                        String order_qty=jsonObject1.getString("order_qty");
                        String price=jsonObject1.getString("price");
                        String unit=jsonObject1.getString("unit");
                        String qty=jsonObject1.getString("qty");

                        JSONObject orders=jsonObject1.getJSONObject("orders");
                        String promocode_amount=orders.getString("promocode_amount");
                        String total_amount=orders.getString("total_amount");
                        String total_cgst=orders.getString("total_cgst");
                        String total_sgst=orders.getString("total_sgst");
                        String created_at=orders.getString("created_at");


                        Double bagtag=Double.parseDouble(String.valueOf(Double.parseDouble(price)+ Math.round( Global.getTax(MyOrderDetail.this, Double.parseDouble(price)))))* Double.parseDouble(order_qty);
                        tvBagTag1.setText(MainActivity.currency_sign+Math.round(bagtag));
                        tvDelChargeTag1.setText("FREE");
                        if (promocode_amount.equals("0")){
                            tvCuponTa1.setText("-");
                        }else{
                            tvCuponTa1.setText("-"+MainActivity.currency_sign+Math.round(Float.parseFloat(promocode_amount)));
                        }
                        tvTotalTag1.setText(MainActivity.currency_sign+Math.round(Float.parseFloat(total_amount)));



                        JSONObject jsonObject2=jsonObject.getJSONObject("address");
                        String full_address=jsonObject2.getString("full_address");
                        String user_name=jsonObject2.getString("user_name");
                        String user_number=jsonObject2.getString("user_number");

                        String product_url=jsonObject.getString("product_url");

                        Log.e("imagggg",product_url+product_image);

                       /* Glide.with(MyOrderDetail.this)
                                .load(product_url+product_image)
                                .into(image_ordr);*/
                        Global.loadGlideImage(MyOrderDetail.this,product_image,product_url+product_image,image_ordr);



                        text_ordr.setText(product_name+" ("+qty+" "+unit+")");
                        price_ordr.setText(MainActivity.currency_sign+ Math.round(Double.parseDouble(String.valueOf(Double.parseDouble(price)+ Math.round( Global.getTax(MyOrderDetail.this, Double.parseDouble(price)))))));
                        qty_ordr.setText(getString(R.string.qty)+order_qty);

                        try {
                            String formattedDate= Global.getDateConvert(created_at,"yyyy-MM-dd hh:mm:ss","EEE dd, MMM yyyy");
                            String DEldate= Global.getDateConvert(delivery_date,"yyyy-MM-dd","EEE dd, MMM yyyy");

                            ordr_date.setText(formattedDate);
                            ordr_deldate.setText(getString(R.string.success_order)+" "+DEldate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        ordr_address.setText(full_address);
                        ordr_custName.setText(user_name);
                        ordr_addNmbr.setText(user_number);



                    }else if (status.contains("0")){
                        Toast.makeText(MyOrderDetail.this, ""+message, Toast.LENGTH_SHORT).show();
                        container_null1.setVisibility(View.VISIBLE);
                        rv_main.setVisibility(View.GONE);
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
             //   Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }




}
