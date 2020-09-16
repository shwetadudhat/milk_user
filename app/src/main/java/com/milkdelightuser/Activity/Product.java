package com.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Adapter.Adapter_Rate;
import com.milkdelightuser.Adapter.Cat_banner_Adapter;
import com.milkdelightuser.Model.Cat_Banner_Model;
import com.milkdelightuser.Model.Rate_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CirclePageIndicator;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

/*import com.viewpagerindicator.CirclePageIndicator;*/


public class Product extends BaseActivity {


    /*category banner*/
    ViewPager pager ;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CirclePageIndicator indicator;


    private DatabaseHandler db;



    /*tool bar*/
    Toolbar toolbar;
    TextView toolTitle;
    ImageView ivBack,ivNotify;
    String proname,proId;

    TextView itemName,itemPrice,tvRate;
    RatingBar rating;
    int qty=0;
    TextView tvQtyDec,tvQty,tvQtyInc,tvProDesc,tvItemType,tvItemFat,tvItemWeight,tvItemMilkType,allReview,tvMrp;
    LinearLayout addReview;

    LinearLayout ll_schedule,ll_cart;

    RecyclerView recycler_review;
    List<Rate_Model> rateModelList;
    Adapter_Rate adapterRate;

    Session_management sessionManagement;
    String u_id;
    float  txtrate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main_clr));
        toolTitle=findViewById(R.id.title);

        db = new DatabaseHandler(getActivity());

        sessionManagement=new Session_management(Product.this);
        u_id=sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


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
                Intent intent=new Intent(Product.this,drawer.class);
                intent.putExtra("notification","product_page");
                startActivity(intent);
                finish();
            }
        });


        pager = (ViewPager)findViewById(R.id.view_pager_product);
        indicator = findViewById(R.id.indicator_product);
        itemName = findViewById(R.id.itemName);
        itemPrice = findViewById(R.id.itemPrice);
        tvMrp = findViewById(R.id.tvMrp);

        tvRate = findViewById(R.id.tvRate);
        rating = findViewById(R.id.rating);
        tvQtyDec = findViewById(R.id.tvQtyDec);
        tvQty = findViewById(R.id.tvQty);
        tvQtyInc = findViewById(R.id.tvQtyInc);
        tvProDesc = findViewById(R.id.tvProDesc);
        tvItemType = findViewById(R.id.tvItemType);
        tvItemFat = findViewById(R.id.tvItemFat);
        tvItemWeight = findViewById(R.id.tvItemWeight);
        tvItemMilkType = findViewById(R.id.tvItemMilkType);
        allReview = findViewById(R.id.allReview);
        addReview = findViewById(R.id.addReview);
        recycler_review = findViewById(R.id.recycler_review);
        ll_schedule = findViewById(R.id.ll_schedule);
        ll_cart = findViewById(R.id.ll_cart);

        proname=getIntent().getStringExtra("proname");
        proId=getIntent().getStringExtra("proId");
        toolTitle.setText(proname);

        rateModelList=new ArrayList<>();

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Product.this);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(Product.this).inflate(R.layout.custome_review, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


                RatingBar rate=dialogView.findViewById(R.id.rate);
                EditText edMsg=dialogView.findViewById(R.id.edMsg);
                Button btnApply=dialogView.findViewById(R.id.btnApply);

                rate.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        txtrate=rate.getRating();
                        return rate.onTouchEvent(motionEvent);
                    }
                });
                btnApply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String msg;

                        msg=edMsg.getText().toString();

                        /*api call*/
                        showDialog(" ");
                        SendReviewList(u_id,proId, String.valueOf(txtrate),msg,alertDialog);

                    }
                });
                alertDialog.show();

            }
        });

        ll_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Product.this,subscription2.class);
                intent.putExtra("schedule","schedule");
                intent.putExtra("product_id",proId);
                startActivity(intent);
                finish();

            }
        });

        ll_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Product.this,Cart_activity.class);
                startActivity(intent);
                finish();

            }
        });

        rateModelList=new ArrayList<>();

        if (isInternetConnected()) {

            showDialog(" ");
           // cat_banner();
            getProductDetail(proId);

            getReviewList(proId);


        }


        Log.e("rateModelList123", String.valueOf(rateModelList));




    }

    public void getProductDetail(String proId) {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("product_id", proId);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.product_details, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("productDetail123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONObject product_details=jsonObject.getJSONObject("product_details");

                        String product_id1 = product_details.getString("product_id");
                        String category_id1 = product_details.getString("category_id");
                        String product_name = product_details.getString("product_name");
                        String category_name = product_details.getString("category_name");
                        String price = String.valueOf(Double.valueOf(product_details.getString("price"))+ Math.round( Global.getTax(Product.this, Double.valueOf(product_details.getString("price")))));
                        String subscription_price = String.valueOf(Double.valueOf(product_details.getString("subscription_price"))+ Math.round(  Global.getTax(Product.this, Double.valueOf(product_details.getString("price")))));

//                        String subscription_price = product_details.getString("subscription_price");
                        String qty1 = product_details.getString("qty");
                        String product_image1 = product_details.getString("product_image");
                        String description = product_details.getString("description");
                        String stock = product_details.getString("stock");
                        String unit = product_details.getString("unit");
                        String mrp = String.valueOf(Double.valueOf(product_details.getString("mrp"))+ Math.round( Global.getTax(Product.this, Double.valueOf(product_details.getString("mrp")))));

                    //    String mrp = product_details.getString("mrp");
                        String product_review_count = product_details.getString("product_review_count");
                        String product_url = product_details.getString("product_url");
                        String milk_type = product_details.getString("milk_type");
                        String fat = product_details.getString("fat");

                        String proImgae=product_url+product_image1;

                       // float ratting1 = 0;
                        String ratting123=null;
                        JSONArray jsonArray1=product_details.getJSONArray("product_ratting");

                        if (jsonArray1.length()>0){
                            for (int j =0;j<jsonArray1.length();j++){
                                JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                                ratting123=jsonObject2.getString("rating");
                                Log.e("ratting123",ratting123);
                                // ratting1= Float.parseFloat(jsonObject2.getString("rating"));
                            }
                        }else{
                            ratting123="0.0";
                        }


                        tvProDesc.setText(description);
                        tvItemType.setText(product_name);
                        if (!fat.equals("null")){
                            tvItemFat.setText(fat);
                        }else{
                            tvItemFat.setText("");
                        }

                        tvItemWeight.setText(qty1+ " "+unit);

                        if (!milk_type.equals("null")){
                            tvItemMilkType.setText(milk_type);
                        }else{
                            tvItemMilkType.setText("");
                        }

                        rating.setRating(Float.parseFloat(ratting123));

                        tvRate.setText(product_review_count+" Ratings");

                        JSONArray product_images=jsonObject.getJSONArray("product_images");
                        setUpProductSlider1(pager,indicator,product_images,product_url);

                        itemName.setText(product_name+" ("+qty1+" "+unit+")");


                        itemPrice.setText(MainActivity.currency_sign + Math.round(Float.parseFloat(price)));
                        if (!mrp.equals("0")){
                            tvMrp.setVisibility(View.VISIBLE);
                            tvMrp.setText( MainActivity.currency_sign + Math.round(Float.parseFloat(mrp)));
                            tvMrp.setPaintFlags(tvMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        }

                        if (db.isInCart(proId)) {
                            tvQty.setText(db.getCartItemQty(proId));
                        } else{
                            tvQty.setText("0");
                        }

                        tvQtyDec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                int count;
                                count = Integer.valueOf(tvQty.getText().toString());
                                if (count > 1) {
                                    count = count - 1;
                                    tvQty.setText(String.valueOf(count));
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("product_id", product_id1);
                                    map.put("product_name", product_name);
                                    map.put("category_id", category_id1);
                                    map.put("product_description", description);
                                    map.put("price", String.valueOf(Math.round(Float.parseFloat(price))));
                                    map.put("subscription_price", String.valueOf(Math.round(Float.parseFloat(subscription_price))));
                                    map.put("product_image", proImgae);
                                    map.put("unit", qty1+" "+unit);
                                    map.put("stock", stock);
                                    db.setCart(map, Float.valueOf(qty));

                                    db.setCart(map, Float.valueOf(tvQty.getText().toString()));

                                } else if (count == 1) {

                                    tvQty.setText(String.valueOf(count));
                                    if (tvQty.getText().toString().contains("1")) {
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("product_id", product_id1);
                                        map.put("product_name", product_name);
                                        map.put("category_id", category_id1);
                                        map.put("product_description", description);
                                        map.put("price", String.valueOf(Math.round(Float.parseFloat(price))));
                                        map.put("subscription_price", String.valueOf(Math.round(Float.parseFloat(subscription_price))));
                                        map.put("product_image", proImgae);
                                        map.put("unit", qty1+" "+unit);
                                        map.put("stock", stock);
                                        db.setCart(map, Float.valueOf(qty));
                                        db.removeItemFromCart(map.get("product_id"));


                                    }
                                }
                                //
                            }
                        });
                        tvQtyInc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int count = 0;
                               // if (!tvQty.getText().toString().equalsIgnoreCase("0")) {
                                    count = Integer.valueOf(tvQty.getText().toString());
                                    count = count + 1;
                                    tvQty.setText(String.valueOf(count));
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("product_id", product_id1);
                                    map.put("product_name", product_name);
                                    map.put("category_id", category_id1);
                                    map.put("product_description", description);
                                    map.put("price", String.valueOf(Math.round(Float.parseFloat(price))));
                                    map.put("subscription_price", String.valueOf(Math.round(Float.parseFloat(subscription_price))));
                                    map.put("product_image", proImgae);
                                    map.put("unit", qty1+" "+unit);
                                    map.put("stock", stock);
                                    db.setCart(map, Float.valueOf(tvQty.getText().toString()));



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

    private void setUpProductSlider1(ViewPager pager, CirclePageIndicator indicator, JSONArray product_images, String product_url) {
        List<Cat_Banner_Model> catBannerModelList =new ArrayList<>();
        Cat_Banner_Model catBannerModel;

        for (int i=0;i<product_images.length();i++){

            JSONObject jsonObject = null;
            try {
                jsonObject = product_images.getJSONObject(i);
                String product_image = jsonObject.getString("product_image");
                String id = jsonObject.getString("id");
                String product_id = jsonObject.getString("product_id");

                catBannerModel=new Cat_Banner_Model();
                catBannerModel.setBanner_id(product_id);
                catBannerModel.setBanner_image(product_url+product_image);

                catBannerModelList.add(catBannerModel);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        NUM_PAGES = catBannerModelList.size();

        Cat_banner_Adapter adapter_banner = new Cat_banner_Adapter( getApplicationContext(), catBannerModelList);
        pager.setAdapter(adapter_banner);

        if (getApplicationContext()!=null){
            final float density = getResources().getDisplayMetrics().density;

            indicator.setRadius(3 * density);
            indicator.setSpacing(12);

            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });


            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    pager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 3000, 3000);

            Cat_banner_Adapter adapter_banner1 = new Cat_banner_Adapter(Product.this,catBannerModelList);

            pager.setAdapter(adapter_banner1);

            indicator.setViewPager(pager);
            indicator.setSnap(true);
            indicator.setVisibility(View.VISIBLE);
        }
    }



    private void SendReviewList(String u_id, String proId, String rate, String comment, AlertDialog alertDialog) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);
        params.put("product_id", proId);
        params.put("rate", rate);
        params.put("comment", comment);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.send_review, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("send_review", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        alertDialog.dismiss();
                       // adapterRate.notifyDataSetChanged();
                        rateModelList.clear();
                        getReviewList(proId);

                    }else{
                        Toast.makeText(Product.this, message, Toast.LENGTH_SHORT).show();
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

    private void getReviewList(String proId) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("product_id", proId);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.review_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("review_list", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                     //   Toast.makeText(Product.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONArray jsonArray=jsonObject.getJSONArray("review_list");

                        allReview.setText("All Reviews("+jsonArray.length()+")");

                        String profile_url=jsonObject.getString("profile_url");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String user_id=jsonObject1.getString("user_id");
                            String rate=jsonObject1.getString("rate");
                            String comment=jsonObject1.getString("comment");
                            String user_name=jsonObject1.getString("user_name");
                            String user_image=jsonObject1.getString("user_image");
                            String created_at=jsonObject1.getString("created_at");

                            Rate_Model rateModel=new Rate_Model();
                            rateModel.setRate_id(user_id);
                            rateModel.setRating_star(rate);
                            rateModel.setRate_desc(comment);
                            rateModel.setUsername(user_name);
                            rateModel.setUser_image(profile_url+user_image);
                            rateModel.setCreated_at(created_at);

                            rateModelList.add(rateModel);

                        }

                        adapterRate=new Adapter_Rate(getApplicationContext(),rateModelList);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
                        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recycler_review.setLayoutManager(gridLayoutManager);
                        recycler_review.setAdapter(adapterRate);

                    }else if(status.equals("0")){
                        allReview.setText("All Reviews(0)");
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    @Override
    public void onBackPressed() {
        getActivity().finish();
        super.onBackPressed();
    }
}
