package com.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.milkdelightuser.Adapter.Adapter_FreqPlan;
import com.milkdelightuser.Adapter.Adapter_SubProduct;
import com.milkdelightuser.Adapter.Cart_Adapter2;
import com.milkdelightuser.Model.Plan_model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

public class subscription extends BaseActivity implements  PaymentResultListener {

    /*tool bar*/
    Toolbar toolbar;
    TextView toolTitle;
    ImageView ivBack,ivNotify;
    String proname;

    /*view1*/
    int qty=0;
    RelativeLayout rlSubscription;
    ImageView ivproImage;
    TextView tvproName,tvProPrice,tvQtyDec,tvQty,tvQtyInc,tvqtyUnit;
    RecyclerView recycler_cartitem;
    /*view2*/
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    DatePickerDialog picker;
    Button FreqEverDay,freqAltrDay,freqEver3day,FreqEver7Day;
    TextView freqDate;
    RecyclerView recycler_freqPlan;
    Adapter_FreqPlan adapterFreqPlan;
    List<Plan_model> planModelList;
    String txtPlan,planSkipDay;

    /*view3*/
    Button plan1,plan2,customPlan;
    SharedPreferences planPref;
    SharedPreferences.Editor planprefEdit;

    /*view4*/
    LinearLayout llEdit;
    ImageView ivAdrEdit;
    TextView adrName,adr,adrNmbr;

    /*view 5*/
    TextView balance;
    CheckBox ChkWallet;


    LinearLayout llBuySubscrption;
    String product_id,schedule;
    private DatabaseHandler db;
    Session_management session_management;
    String user_id,user_name,user_nmbr,user_email;
    String subs_id;


    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    boolean isLocked;
    String planData;

    String wallet,walletAmount;
    int razorAmount;
    int totalAmout,totalPrice;

    String razorpayPaymentID="";
    String Addrid,tardetDate,tardetDate1,enddate1;

    int planId=0;
    String enddate;
    int dayyyyy;
    JSONArray passArray;
    LinearLayout adrsss;

    RelativeLayout rlChk,  rlCoupon;
    String discount_amount="",promo_code="";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subscription);

        //razor pay
        Checkout.preload(getActivity());

        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main_clr));
        toolTitle=findViewById(R.id.title);
        toolTitle.setText("Add Subscriptions"/*proname*/);
        rlSubscription=findViewById(R.id.subs_detail);

        ivproImage=findViewById(R.id.ivproImage);
        tvproName=findViewById(R.id.tvproName);
        tvProPrice=findViewById(R.id.tvProPrice);
        tvQtyDec=findViewById(R.id.tvQtyDec);
        tvQty=findViewById(R.id.tvQty);
        tvQtyInc=findViewById(R.id.tvQtyInc);
        tvqtyUnit=findViewById(R.id.tvqtyUnit);

        db = new DatabaseHandler(getActivity());
        planModelList=new ArrayList<>();

        Date c = Calendar.getInstance().getTime();


        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String tardetDate12 = df.format(c);

        Log.e("tardetDate",tardetDate12);

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        tardetDate = df1.format(c);

        balance=findViewById(R.id.balance);
        ChkWallet=findViewById(R.id.ChkWallet);


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
                Intent intent=new Intent(subscription.this, drawer.class);
                intent.putExtra("notification","subscription_page");
                startActivity(intent);
            }
        });

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);

        recycler_cartitem=findViewById(R.id.recycler_cartitem);
        adrsss=findViewById(R.id.adrsss);
        recycler_cartitem.setLayoutManager(new LinearLayoutManager(subscription.this));

        product_id=getIntent().getStringExtra("product_id");
        schedule=getIntent().getStringExtra("schedule");
        if (schedule!=null){
            rlSubscription.setVisibility(View.VISIBLE);
            recycler_cartitem.setVisibility(View.GONE);
        }else{
            rlSubscription.setVisibility(View.GONE);
            recycler_cartitem.setVisibility(View.VISIBLE);
        }


        if (isInternetConnected()) {
            try {
                showDialog("");
                getProductDetail(product_id);
                getAddressData(user_id);
                showTotalCredit(user_id);
                getPlanadata();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayList<HashMap<String, String>> map = db.getCartAll();
        Cart_Adapter2 adapter = new Cart_Adapter2(subscription.this, map);
        recycler_cartitem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        totalAmout= Integer.parseInt(db.getTotalAmount());

        freqDate=findViewById(R.id.freqDate);
        freqDate.setText(tardetDate12);
        freqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(subscription.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                freqDate.setText(dayOfMonth + " " + MONTHS[monthOfYear] + " " + year);

                                tardetDate=dayOfMonth+"-"+monthOfYear+"-"+year;
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        recycler_freqPlan=findViewById(R.id.recycler_frq);

        plan1=findViewById(R.id.plan1);
        plan2=findViewById(R.id.plan2);
        customPlan=findViewById(R.id.customPlan);



        llEdit=findViewById(R.id.llEdit);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        adrName=findViewById(R.id.adrName);
        adr=findViewById(R.id.adr);
        adrNmbr=findViewById(R.id.adrNmbr);

        llBuySubscrption=findViewById(R.id.llBuySubscrption);
      //  llBuySubscrption.setEnabled(false);


        planPref = getSharedPreferences("plannn", 0);
        isLocked=  planPref.getBoolean("locked"+subs_id,false);
        planData=planPref.getString("plan","");
        Log.e("planData",planData);



        llBuySubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (planId!=0 && !planData.equals("")){
                    Log.e("iff","ifff");

                    if (isInternetConnected()) {
                        try {
                            if (Addrid==null){
                                Toast.makeText(subscription.this, "plase select address first", Toast.LENGTH_SHORT).show();
                            }else{
                                showDialog("");
                                AddSubscription();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //  llBuySubscrption.setEnabled(true);
                }else{
                    Log.e("else","elseee");
                    Toast.makeText(subscription.this, "Please select Frequency and plan data", Toast.LENGTH_SHORT).show();
                }


            }
        });

       // freqencySelector();
        selectPlan();


    }


    private void getPlanadata() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.subscription_plans_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length()==0){
                            Toast.makeText(subscription.this, "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                int id=jsonObject1.getInt("id");
                                String plans=jsonObject1.getString("plans");
                                String days=jsonObject1.getString("days");
                                String description=jsonObject1.getString("description");
                                String skip_days=jsonObject1.getString("skip_days");

                               Plan_model plan_model=new Plan_model();
                               plan_model.setId(id);
                               plan_model.setPlans(plans);
                               plan_model.setDays(days);
                               plan_model.setDescription(description);
                               plan_model.setSkipDays(skip_days);

                               planModelList.add(plan_model);

                            }

                            Log.e("planModelList", String.valueOf(planModelList));
                            Adapter_SubProduct.ClickPosition clickPosition = null;
                            adapterFreqPlan=new Adapter_FreqPlan(subscription.this,planModelList);

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recycler_freqPlan.setLayoutManager(gridLayoutManager);
                            recycler_freqPlan.setAdapter(adapterFreqPlan);

                            adapterFreqPlan.setEventListener(new Adapter_FreqPlan.EventListener() {
                                @Override
                                public void onItemViewClicked(int i,int  plan) {
                                  //  txtPlan=plan;
                                    planId=planModelList.get(i).getId();
                                    planSkipDay=planModelList.get(i).getSkipDays();
                                    Log.e("id123", String.valueOf(planId));


                                }

                                @Override
                                public void onItemViewClicked1(int position, int planId, int position1) {

                                }
                            });

                        }
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
              //  Toast.makeText(subscription.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
       jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    public void getDateData(int targetDate){

       SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
       Date date = null;
       try {
           date = dateFormat.parse(tardetDate);
       } catch (ParseException e) {
           e.printStackTrace();
       }
       Calendar calendar = Calendar.getInstance();
       calendar.setTime(date);
       calendar.add(Calendar.DATE, targetDate);
       enddate = dateFormat.format(calendar.getTime());
       Log.e("enddate",enddate);
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
                        String price = product_details.getString("price");
                        String subscription_price = product_details.getString("subscription_price");
                        String qty1 = product_details.getString("qty");
                        String product_image1 = product_details.getString("product_image");
                        String description = product_details.getString("description");
                        String stock = product_details.getString("stock");
                        String unit = product_details.getString("unit");
                        String mrp = product_details.getString("mrp");
                        String product_review_count = product_details.getString("product_review_count");
                        String product_url = product_details.getString("product_url");

                        String proImgae=product_url+product_image1;

                        // float ratting1 = 0;
                        String ratting123=null;
                        JSONArray jsonArray1=product_details.getJSONArray("product_ratting");
                        for (int j =0;j<jsonArray1.length();j++){
                            JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                            ratting123=jsonObject2.getString("rating");
                            Log.e("ratting123",ratting123);
                            // ratting1= Float.parseFloat(jsonObject2.getString("rating"));
                        }

                        if (db.isInCart(product_id)) {
                            tvQty.setText(db.getCartItemQty(product_id));
                            tvproName.setText(db.getCartItemName(product_id)+ " ("+db.getCartItemUnit(product_id)+")");
                            Log.e("dbgetCart",db.getCartItemUnit(product_id));
                            tvProPrice.setText(MainActivity.currency_sign+db.getCartItemPrice(product_id));

                            totalAmout= Integer.parseInt(db.getCartItemQty(product_id))* Integer.parseInt(db.getCartItemPrice(product_id));
                            Log.e("total1234", String.valueOf(totalAmout));


                            Glide.with(subscription.this)
                                    .load(db.getCartItemImage(product_id))
                                    .into(ivproImage);
                            // ivproImage.setImageResource(db.getCartItemImage(product_id));

                        } else {
                            tvQty.setText("0");
                            tvproName.setText(proname+ " ("+qty1+unit+")");
                            tvProPrice.setText(price);

                            Glide.with(subscription.this)
                                    .load(product_url+product_image1)
                                    .into(ivproImage);
                        }

                        qty= Integer.parseInt(tvQty.getText().toString());

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
                                    map.put("price", price);
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
                                        map.put("price", price);
                                        map.put("product_image", proImgae);
                                        map.put("unit", qty1+" "+unit);
                                        map.put("stock", stock);
                                        db.setCart(map, Float.valueOf(qty));
                                        db.removeItemFromCart(map.get("product_id"));


                                    }
                                }

                                totalAmout= Integer.parseInt(db.getCartItemQty(product_id))* Integer.parseInt(db.getCartItemPrice(product_id));
                                Log.e("total1234", String.valueOf(totalAmout));


                            }
                        });
                        tvQtyInc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int count = 0;
                              //  if (!tvQty.getText().toString().equalsIgnoreCase("0")) {
                                    count = Integer.valueOf(tvQty.getText().toString());
                                    count = count + 1;
                                    tvQty.setText(String.valueOf(count));
                                    HashMap<String, String> map = new HashMap<>();

                                    map.put("product_id", product_id1);
                                    map.put("product_name", product_name);
                                    map.put("category_id", category_id1);
                                    map.put("product_description", description);
                                    map.put("price", price);
                                    map.put("product_image", proImgae);
                                    map.put("unit", qty1+" "+unit);
                                    map.put("stock", stock);
                                    db.setCart(map, Float.valueOf(tvQty.getText().toString()));

                             //   }
                                totalAmout= Integer.parseInt(db.getCartItemQty(product_id))* Integer.parseInt(db.getCartItemPrice(product_id));
                                Log.e("total1234", String.valueOf(totalAmout));

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
               // Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
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

                             wallet = jsonObject.getString("wallet_credits");
                             balance.setText("Your Balance : "+ MainActivity.currency_sign+wallet);
                             if (Integer.parseInt(wallet)>0){
                                 ChkWallet.setChecked(true);
                             }else{
                                 ChkWallet.setChecked(false);
                             }

                            settings = getApplicationContext().getSharedPreferences("wallet", Context.MODE_PRIVATE);
                            editor = settings.edit();
                            editor.putString("wallet", wallet);
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
                Log.e("error",error.toString());
              //  Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
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
                        adrsss.setVisibility(View.VISIBLE);
                        //Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");
                        String user_name=jsonObject.getString("user_name");
                        Addrid=jsonObject.getString("id");
                        String full_address=jsonObject.getString("full_address");
                        String user_number=jsonObject.getString("user_number");


                        adrName.setText(user_name);
                        adrNmbr.setText(user_number);
                        adr.setText(full_address);

                        llEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent=new Intent(subscription.this,Addresslist.class);
                               /* intent.putExtra("action","edit");
                                intent.putExtra("id",Addrid);*/
                                startActivityForResult(intent,0);
                            }
                        });

                        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llEdit.callOnClick();
                            }
                        });

                    }else if (status.contains("0")){
                        adrsss.setVisibility(View.GONE);
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

    private void selectPlan(){

        planPref = getSharedPreferences("plannn", 0);
        planprefEdit=planPref.edit();

        plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customPlan.setTag("unselected"+subs_id);
                plan2.setTag("unselected"+subs_id);
                plan1.setTag("selected"+subs_id);


                if (plan1.getTag().equals("selected"+subs_id)){
                    planprefEdit.putBoolean("locked", true).commit();
                }else if (plan1.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }

                planprefEdit.putString("plan","plan1");
                planprefEdit.apply();


                getDateData(15);

                plan2.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                plan2.setTextColor(getResources().getColor(R.color.main_clr));
                customPlan.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                customPlan.setTextColor(getResources().getColor(R.color.main_clr));
                plan1.setBackground(getResources().getDrawable(R.drawable.bg_fb));
                plan1.setTextColor(getResources().getColor(R.color.white));

            }
        });

        plan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plan2.setTag("selected"+subs_id);
                plan1.setTag("unselected"+subs_id);
                customPlan.setTag("unselected"+subs_id);


                if (plan2.getTag().equals("selected"+subs_id)){
                    planprefEdit.putBoolean("locked", true).commit();
                }else if (plan2.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }

                planprefEdit.putString("plan","plan2");
                planprefEdit.apply();


                getDateData(30);

                customPlan.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                customPlan.setTextColor(getResources().getColor(R.color.main_clr));
                plan1.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                plan1.setTextColor(getResources().getColor(R.color.main_clr));
                plan2.setBackground(getResources().getDrawable(R.drawable.bg_fb));
                plan2.setTextColor(getResources().getColor(R.color.white));

            }
        });
        customPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plan2.setTag("unselected"+subs_id);
                customPlan.setTag("selected"+subs_id);
                plan2.setTag("unselected"+subs_id);


                if (customPlan.getTag().equals("selected"+subs_id)){
                    planprefEdit.putBoolean("locked", true).commit();
                }else if (customPlan.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }

                planprefEdit.putString("plan","customplan");
                planprefEdit.apply();


                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(subscription.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                int month=monthOfYear+1;
                                String sMonth = "";
                                if (month < 10) {
                                    sMonth = "0"+ String.valueOf(month);
                                } else {
                                    sMonth = String.valueOf(month);
                                }
                                enddate=dayOfMonth+"-"+sMonth+"-"+year;
                                Log.e("enddate",enddate);

                            }
                        }, year, month, day);
                picker.show();

                plan1.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                plan1.setTextColor(getResources().getColor(R.color.main_clr));
                plan2.setBackgroundColor(getResources().getColor(R.color.bg_clr));
                plan2.setTextColor(getResources().getColor(R.color.main_clr));
                customPlan.setBackground(getResources().getDrawable(R.drawable.bg_fb));
                customPlan.setTextColor(getResources().getColor(R.color.white));
            }
        });

    }

    private void AddSubscription() {

        Log.e("start_date1",tardetDate);
        Log.e("end_date1",enddate);

        totalAmout= Integer.parseInt(db.getTotalAmount());

        Log.e("totalAmount", String.valueOf(totalAmout));


        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date startdate = format.parse(tardetDate);
            Date endddate = format.parse(enddate);

            format= new SimpleDateFormat("yyyy-MM-dd");
            tardetDate1 = format.format(startdate);
            enddate1 = format.format(endddate);

            Log.e("start_date122",tardetDate1);
            Log.e("end_date122",enddate1);

            long diff = endddate.getTime() - startdate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days1 = hours / 24;

            Log.e("dayssss", String.valueOf(days1)+"\nstartdate:"+startdate);

             dayyyyy=(int) days1/ Integer.parseInt(planSkipDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e("dayyyyy", String.valueOf(dayyyyy));
        totalPrice=totalAmout*dayyyyy;

        Log.e("total_amount12333", String.valueOf(totalPrice));


        if (ChkWallet.isChecked()){
            if (Integer.parseInt(wallet)<totalPrice){

                Log.e("ifff", String.valueOf(totalPrice));
                razorAmount=totalPrice- Integer.parseInt(wallet);
                walletAmount=wallet;

                Log.e("razoramount123", String.valueOf(razorAmount));
                Log.e("totalAmout123", String.valueOf(totalPrice));
                Log.e("walletAmount123", String.valueOf(walletAmount));
                Log.e("wallet123", String.valueOf(wallet));

            }else {
                /*wallet 2100  totalprice 400*/

                walletAmount= String.valueOf(totalPrice);
                razorAmount=0;
                Log.e("totalAmout444", String.valueOf(totalPrice));
                Log.e("walletAmount444", String.valueOf(walletAmount));

            }

        }else{
            razorAmount=totalPrice;
            walletAmount="0";
            Log.e("razoramount777", String.valueOf(razorAmount));
            Log.e("totalAmout777", String.valueOf(totalPrice));

        }

        if (razorAmount!=0){
            Log.e("razorrrrrr", String.valueOf(razorAmount));
            startPayment(razorAmount);
        }else{
             addSubPlan();
        }



    }

    private void startPayment(int razorAmount) {
        Checkout checkout = new Checkout();

        final Activity activity = subscription.this;
        int price_rs = razorAmount;
        try {
            JSONObject options = new JSONObject();

            /**
             * Merchant Name
             * eg: ACME Corp || HasGeek etc.
             */
            options.put("name",user_name);
            options.put("description","Add Amount in Wallet");
            //YOU CAN OMIT THE IMAGE OPTION TO FETCH THE IMAGE FROM DASHBOARD
            options.put("image","https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency","INR");
            /**
             * Amount is always passed in currency subunits
             * Eg: "500" = INR 5.00
             */
            options.put("amount",price_rs*100);

            JSONObject prefill = new JSONObject();
            prefill.put("email", user_email);
            prefill.put("contact",user_nmbr);

            options.put("prefill",prefill);

            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e("cds", "Error in starting Razorpay Checkout", e);
        }

    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        this.razorpayPaymentID=razorpayPaymentID;
        ArrayList<HashMap<String, String>> items = db.getCartAll();
        Log.e("onSuccess","onsuccess");

        try {

            addSubPlan();


            AlertDialog.Builder builder = new AlertDialog.Builder(subscription.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(subscription.this).inflate(R.layout.custom_success, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
             alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
            TextView tvStts=dialogView.findViewById(R.id.tvStts);
            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);


            //  tvStts.setText("");
            tvTransId.setText("Transaction id : "+razorpayPaymentID);
             getTransactionId();
            //  tvTransDesc.setText("");
            tvTransDesc.setText("Payment done through your Wallet amount");

            tvStts.setText("Payment Success");
            tvStts.setTextColor(getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

            db.clearCart();

            llDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(subscription.this, drawer.class);
                    startActivity(intent);
                    finish();
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }

    }

    private void getTransactionId() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("subs_id", subs_id);
        params.put("transaction_id", razorpayPaymentID);

        Log.e("paramssubadad",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.transaction_id, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAdd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
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

    private void addSubPlan() {

        ArrayList<HashMap<String, String>> items = db.getCartAll();
        if (items.size() > 0) {
            passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("order_qty", map.get("qty"));
                    jObjP.put("subscription_price", map.get("price"));
                    jObjP.put("plans_id", String.valueOf(planId));
                    jObjP.put("amount", db.getTotalAmountById(map.get("product_id")));
                    passArray.put(jObjP);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (isInternetConnected()) {
                but_sub_plan(passArray);
                Log.e("parseArray",passArray.toString());
            }
        }

    }

    private void but_sub_plan(JSONArray passArray) {

        Log.e("walletAmount",walletAmount);

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("start_date", tardetDate1);
        params.put("end_date", enddate1);
        params.put("wallet_amount", walletAmount);
        params.put("razor_pay_amount", String.valueOf(razorAmount));
        params.put("pay_type", "");
        params.put("pay_mode", "");
        params.put("total_amount", String.valueOf(totalPrice));
        params.put("promo_code", "");
        params.put("promocode_amount", "");

        Log.e("paramssubadad123",params.toString());



        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscribe_plans_add, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAdd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Log.e("razorAmount", String.valueOf(razorAmount));

                        showTotalCredit(user_id);

                        if (razorAmount==0){

                            /*api call payment gateway*/
                            AlertDialog.Builder builder = new AlertDialog.Builder(subscription.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(subscription.this).inflate(R.layout.custom_success, viewGroup, false);
                            builder.setView(dialogView);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCancelable(true);
                            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



                            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
                            TextView tvStts=dialogView.findViewById(R.id.tvStts);
                            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
                            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
                            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

                            tvTransId.setVisibility(View.GONE);

                            tvStts.setText("Payment Success");
                            tvStts.setTextColor(getResources().getColor(R.color.green));
                            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
                            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
                            ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                            tvTransDesc.setText("Payment done through your Wallet amount");

                           // db.removeItemFromCart(product_id);
                            /*if (passArray.length()>1){
                                db.clearCart();
                            }else{
                                db.removeItemFromCart(product_id);
                            }*/
                            ArrayList<HashMap<String, String>> items = db.getCartAll();
                            if (items.size() > 1) {

                                for (int i = 0; i < items.size(); i++) {
                                    HashMap<String, String> map = items.get(i);

                                    db.removeItemFromCart(map.get("product_id"));
                                }
                            }else{
                                db.removeItemFromCart(product_id);
                            }



                            llDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(subscription.this, drawer.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });


                                alertDialog.show();


                        }

                    }else if (status.equals("3")){
                        Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
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

    private void Checkplan() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("product_id", product_id);
        params.put("plan_id",  String.valueOf(planId));

        Log.e("paramschkPlan",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_plan_check, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAdd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("3")) {
                        Toast.makeText(subscription.this, message, Toast.LENGTH_SHORT).show();
                        llBuySubscrption.setEnabled(false);
                    }else{
                        llBuySubscrption.setEnabled(true);
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
    public void onPaymentError(int i, String s) {

        Log.e("payerror",s);
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(subscription.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(subscription.this).inflate(R.layout.custom_success, viewGroup, false);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(true);
               alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


            LinearLayout llDialog=dialogView.findViewById(R.id.llDialog);
            TextView tvStts=dialogView.findViewById(R.id.tvStts);
            TextView tvTransId=dialogView.findViewById(R.id.tvTransId);
            TextView tvTransDesc=dialogView.findViewById(R.id.tvTransDesc);
            ImageView ivIcon=dialogView.findViewById(R.id.ivIcon);

            tvTransId.setVisibility(View.GONE);

            tvStts.setText("Payment Failed");
            tvStts.setTextColor(getResources().getColor(R.color.red));
            ivIcon.setImageResource(R.drawable.ic_noun_close_1);
            //  ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);


            llDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            alertDialog.show();

    } catch (Exception e) {
        Log.e("TAG", "Exception in onPaymentSuccess", e);
    }

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
                getAddressData(user_id);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("user_id11","user_id11");
                //Write your code if there's no result
            }
        }
    }//


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("resume","resume");
        getAddressData(user_id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(subscription.this, drawer.class);
        startActivity(intent);
        finish();
    }
}
