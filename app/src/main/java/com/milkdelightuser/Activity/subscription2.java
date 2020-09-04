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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.milkdelightuser.Adapter.Adapter_SubProduct;
import com.milkdelightuser.Model.EndDate_Model;
import com.milkdelightuser.Model.PlanSelected_model;
import com.milkdelightuser.Model.Plan_model;
import com.milkdelightuser.Model.StartDate_Model;
import com.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.milkdelightuser.utils.Global.MY_FREQ_PREFS_NAME;
import static com.milkdelightuser.utils.Global.MY_SUBSCRIPTION_PREFS_NAME;

import static com.milkdelightuser.utils.Global.SUB_DATA;

public class subscription2 extends BaseActivity implements  PaymentResultListener {

    /*tool bar*/
    Toolbar toolbar;
    TextView toolTitle;
    ImageView ivBack,ivNotify;



    LinearLayout adrsss;

    /*view1*/
    RecyclerView recycler_cartitem;
    Adapter_SubProduct adapter;
    /*view2*/
    DatePickerDialog picker;
    List<Plan_model> planModelList;
    String planSkipDay;

    /*view3*/
    Button plan1,plan2,customPlan;
    SharedPreferences planPref;

    SharedPreferences.Editor planprefEdit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;

    /*view4*/
    LinearLayout llEdit;
    ImageView ivAdrEdit;
    TextView adrName,adr,adrNmbr;

    /*view 5*/
    TextView balance;
    CheckBox ChkWallet;


    LinearLayout llBuySubscrption,ll_addmore;
    String product_id;
    private DatabaseHandler db;
    Session_management session_management;
    String user_id,user_name,user_nmbr,user_email;
    String subs_id,pay_type;


    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    boolean isLocked;
    String planData;


    double razorAmount,totalProductAmout,totalProductPrice,wallet,walletAmount,total_price=0;

    String razorpayPaymentID="";
    String Addrid,tardetDate,enddate;

    int planId=-1;
    int dayyyyy;
    JSONArray passArray;
    RelativeLayout rlChk,  rlCoupon;
    double discount_amount;
    String promo_code="";
    double pro_gst,pro_sgst,total_gst=0,total_sgst=0;

    SharedPreferences pref;
    String txtPlan;
    ArrayList<HashMap<String, String>> map,dbmap;
    ArrayList<PlanSelected_model> planSelectedModelArrayList;
    ArrayList<EndDate_Model> endDateModelArrayList;
    ArrayList<StartDate_Model> stardateList1;
    ArrayList<SubscriptioAddProduct_model> subProductList;
    long days1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subscription2);
        setContent();
        getUserData();
        setArrayList();

        //razor pay
        Checkout.preload(getActivity());

        toolbar.setBackgroundColor(getResources().getColor(R.color.main_clr));
        toolTitle.setText("Add Subscriptions"/*proname*/);
        db = new DatabaseHandler(getActivity());
        map = db.getCartAll();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String tardetDate12 = df.format(c);
        Log.e("tardetDate",tardetDate12);

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        tardetDate = df1.format(c);


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        ivNotify.setVisibility(View.VISIBLE);
        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(subscription2.this, drawer.class);
                intent.putExtra("notification","subscription_page");
                startActivity(intent);
            }
        });

        planPref = getSharedPreferences("plannn", 0);
        planprefEdit=planPref.edit();
        planprefEdit.clear().apply();
        product_id=getIntent().getStringExtra("product_id");


        if (ConnectivityReceiver.isConnected()) {
            try {
                showDialog("");
              //  getProductDetail(product_id);
                getAddressData(user_id);
                showTotalCredit(user_id);
                getPlanadata();
                selectPlan();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        pref = this.getActivity().getSharedPreferences(MY_FREQ_PREFS_NAME, MODE_PRIVATE);

        sharedPreferences = this.getActivity().getSharedPreferences(MY_SUBSCRIPTION_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(SUB_DATA, "");
        if (!json.isEmpty()) {
            Type type = new TypeToken<List<SubscriptioAddProduct_model>>() {}.getType();
            subProductList= gson.fromJson(json, type);
            Log.e("subProductList_frompref",subProductList.toString());

        }

        rlCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getTotalAmount();

                AlertDialog.Builder builder = new AlertDialog.Builder(subscription2.this);
                ViewGroup viewGroup = view.findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(subscription2.this).inflate(R.layout.custom_coupon, viewGroup, false);
                builder.setView(dialogView);
                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(true);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText tvCode=dialogView.findViewById(R.id.tvCode);
                Button btnApplyCoupon=dialogView.findViewById(R.id.btnApplyCoupon);

                tvCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View view, boolean b) {
                        tvCode.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                    }
                });

                btnApplyCoupon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String code;
                        code=tvCode.getText().toString();


                        if (code.length()==0){
                            tvCode.setError("please enter the Coupon Code");
                        }else{
                            //api call

                            if (isInternetConnected()) {
                                try {
                                    showDialog("");
                                    applyCoupon(alertDialog,code);


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                });
                alertDialog.show();


            }
        });

        ll_addmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(subProductList.size()>0){
                    Log.e("subproductList_addmore",subProductList.toString());
                    Gson gson = new Gson();
                    String json = gson.toJson(subProductList);
                    myEdit.putString(SUB_DATA, json);
                    myEdit.commit();
                }
                Intent intent=new Intent(subscription2.this, ProductListing.class);
                intent.putExtra("seeAll","bestProduct");
                startActivity(intent);
                finish();
            }
        });


        llBuySubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isLocked=  planPref.getBoolean("locked",false);
                planData=planPref.getString("plan","");

                Log.e("planId",planId+"\nplandata"+planData);

                if (planId!=0 && !planData.equals("")){
                    Log.e("iff","ifff");

                    if (isInternetConnected()) {
                        try {
                            if (Addrid==null){
                                llEdit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent=new Intent(subscription2.this,Addresslist.class);
                                        startActivityForResult(intent,0);
                                    }
                                });
                                Toast.makeText(subscription2.this, "plase select address first", Toast.LENGTH_SHORT).show();
                            }else{
                                showDialog("");
                                Log.e("totalAmount","totalAmount");
                                AddSubscription();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Log.e("else","elseee");
                    Toast.makeText(subscription2.this, "Please Select Plan or Frequency ", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }

    private void setArrayList() {
        planModelList=new ArrayList<>();
        planSelectedModelArrayList=new ArrayList<>();
        endDateModelArrayList=new ArrayList<>();
        subProductList=new ArrayList<>();
    }

    private void setContent() {
        toolbar=findViewById(R.id.toolbar);
        toolTitle=findViewById(R.id.title);
        ivBack=findViewById(R.id.ivBack);
        ivNotify=findViewById(R.id.ivNotify);

        recycler_cartitem=findViewById(R.id.recycler_cartitem);
        adrsss=findViewById(R.id.adrsss);

        rlChk=findViewById(R.id.rlChk);
        rlCoupon=findViewById(R.id.rlCoupon);

        balance=findViewById(R.id.balance);
        ChkWallet=findViewById(R.id.ChkWallet);

        plan1=findViewById(R.id.plan1);
        plan2=findViewById(R.id.plan2);
        customPlan=findViewById(R.id.customPlan);

        llEdit=findViewById(R.id.llEdit);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        adrName=findViewById(R.id.adrName);
        adr=findViewById(R.id.adr);
        adrNmbr=findViewById(R.id.adrNmbr);

        llBuySubscrption=findViewById(R.id.llBuySubscrption);
        ll_addmore=findViewById(R.id.ll_addmore);



    }

    private void getUserData(){
        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);

    }


    public void getDateData(int targetDate){

        for (int i = 0; i < stardateList1.size(); i++) {
            tardetDate=stardateList1.get(i).getStart_date();
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
            Log.e("enddate", enddate);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            try {
                Date startdate = format.parse(tardetDate);
                Date endddate = format.parse(enddate);

                long diff = endddate.getTime() - startdate.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                days1 = hours / 24;

                Log.e("dayss", String.valueOf(days1)+"\nstartdate:"+startdate);

                for (int j=0;j<planSelectedModelArrayList.size();j++){
                    if (stardateList1.get(i).getProdcut_id().equals(planSelectedModelArrayList.get(j).getProduct_id())){
                        dayyyyy=(int) days1/ Integer.parseInt(planSelectedModelArrayList.get(j).getSkip_day());
                        totalProductAmout= Double.parseDouble(db.getTotalSUbAmountById(stardateList1.get(i).getProdcut_id()));
                        Log.e("totalamounttt", String.valueOf(totalProductAmout));
                        totalProductPrice=totalProductAmout*dayyyyy;

                        Log.e("totalPricefromendlist", String.valueOf(totalProductPrice));

                    }


                }
                EndDate_Model endDate_model = new EndDate_Model();
                endDate_model.setProdcut_id(stardateList1.get(i).getProdcut_id());
                endDate_model.setEnd_date(enddate);
                endDate_model.setTotal_days(String.valueOf(dayyyyy));
                endDate_model.setProduct_totalPrice((int) totalProductPrice);


                if (endDateModelArrayList.size()>0){
                    for (int k=0;k<endDateModelArrayList.size();k++){
                        if (endDateModelArrayList.get(k).getProdcut_id().equals(stardateList1.get(i).getProdcut_id())){
                            Log.e("stardateList1get(k)",stardateList1.get(k).getProdcut_id());
                            Log.e("stardateLit(i)",stardateList1.get(i).getProdcut_id());
                            Log.e("endDateModelget(k)",endDateModelArrayList.get(k).getProdcut_id());
                            endDateModelArrayList.remove(k);
                        }
                    }
                    endDateModelArrayList.add(endDate_model);
                }else{
                    endDateModelArrayList.add(endDate_model);
                }
                Log.e("endDateModelArrayList", endDateModelArrayList.toString());

            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


    }

    private void getPlanadata() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.subscription_plans_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");

                        if (jsonArray.length()==0){
                            Toast.makeText(getApplicationContext(), "0 data found", Toast.LENGTH_SHORT).show();
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

                            Log.e("planModelList123", String.valueOf(planModelList));


                            recycler_cartitem.setLayoutManager(new LinearLayoutManager(subscription2.this));

                            adapter = new Adapter_SubProduct(subscription2.this, map,planModelList);
                            recycler_cartitem.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            adapter.setEventListener(new Adapter_SubProduct.EventListener() {
                                @Override
                                public void onQtyClicked(int position, String product_id) {
                                    isLocked=  planPref.getBoolean("locked",false);
                                    planData=planPref.getString("plan","");

                                    Log.e("planId12333",planId+"\nplandata12333"+planData);


                                    if (planData!=null){
                                        if (planData.equals("plan1")){
                                            //getDateData(15);
                                            plan1.callOnClick();
                                        }else if (planData.equals("plan2")){
                                            plan2.callOnClick();
                                            //  getDateData(30);
                                        }else if (planData.equals("customplan")){
                                            customPlan.callOnClick();
                                        }
                                    }

                                }

                                @Override
                                public void onDateClicked(int position, String product_id, ArrayList<StartDate_Model> stardateList) {
                                    stardateList1=stardateList;

                                    isLocked=  planPref.getBoolean("locked",false);
                                    planData=planPref.getString("plan","");

                                    Log.e("planId12333",planId+"\nplandata12333"+planData);


                                    if (planData!=null){
                                        if (planData.equals("plan1")){
                                            //getDateData(15);
                                            plan1.callOnClick();
                                        }else if (planData.equals("plan2")){
                                            plan2.callOnClick();
                                            //  getDateData(30);
                                        }else if (planData.equals("customplan")){
                                            customPlan.callOnClick();
                                        }
                                    }
                                }

                                @Override
                                public void onItemViewClicked(int position, String product_id, int plan_id, String start_date, ArrayList<PlanSelected_model> planSelectedArrayList, ArrayList<StartDate_Model> stardateList) {
                                    txtPlan = planModelList.get(position).getPlans();
                                    planId = planModelList.get(position).getId();
                                    planSkipDay = planModelList.get(position).getSkipDays();

                                    planSelectedModelArrayList=planSelectedArrayList;
                                    stardateList1=stardateList;

                                    Log.e("planSelectedList",planSelectedModelArrayList.toString());

                                    isLocked=  planPref.getBoolean("locked",false);
                                    planData=planPref.getString("plan","");

                                    Log.e("planId12else",planId+"\nplandata12333else"+planData);


                                    if (planData!=null){
                                        if (planData.equals("plan1")){
                                            //getDateData(15);
                                            plan1.callOnClick();
                                        }else if (planData.equals("plan2")){
                                            plan2.callOnClick();
                                            //  getDateData(30);
                                        }else if (planData.equals("customplan")){
                                            customPlan.callOnClick();
                                        }
                                    }

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
                //   dismissDialog();
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
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
                            }else{
                                ChkWallet.setChecked(false);
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
                Log.e("error",error.toString());
                //  Toast.makeText(getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

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
                Log.e("user_address_tag", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
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
                                Intent intent=new Intent(subscription2.this,Addresslist.class);
                                intent.putExtra("action","edit");
                                intent.putExtra("id",Addrid);
                                startActivityForResult(intent,0);
                            }
                        });

                        ivAdrEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                llEdit.callOnClick();
                            }
                        });

                    }else if (status.equals("0")){
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    private void selectPlan(){

        plan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customPlan.setTag("unselected"+subs_id);
                plan2.setTag("unselected"+subs_id);
                plan1.setTag("selected"+subs_id);

                if (plan1.getTag().equals("selected"+subs_id)){
                    planprefEdit.putBoolean("locked", true).commit();
                    planprefEdit.putString("plan","plan1");
                    planprefEdit.apply();
                }else if (plan1.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }


                getDateData(15);
                //  getTotalAmount();

                plan2.setBackground(getResources().getDrawable(R.drawable.bg_button));
                plan2.setTextColor(getResources().getColor(R.color.main_clr));
                customPlan.setBackground(getResources().getDrawable(R.drawable.bg_button));
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
                    planprefEdit.putString("plan","plan2");
                    planprefEdit.apply();

                }else if (plan2.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }

                 getDateData(30);
               // Global.getDateData(30,tardetDate,enddate);
                //  getTotalAmount();

                customPlan.setBackground(getResources().getDrawable(R.drawable.bg_button));
                customPlan.setTextColor(getResources().getColor(R.color.main_clr));
                plan1.setBackground(getResources().getDrawable(R.drawable.bg_button));
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
                    planprefEdit.putString("plan","customplan");
                    planprefEdit.apply();
                }else if (customPlan.getTag().equals("unselected"+subs_id)){
                    planprefEdit.putBoolean("locked", false).commit();
                }

                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(subscription2.this,
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

                                for (int i = 0; i < stardateList1.size(); i++) {
                                    tardetDate=stardateList1.get(i).getStart_date();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    Date date = null;
                                    try {
                                        date = dateFormat.parse(tardetDate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                    try {
                                        Date startdate = format.parse(tardetDate);
                                        Date endddate = format.parse(enddate);

                                        long diff = endddate.getTime() - startdate.getTime();
                                        long seconds = diff / 1000;
                                        long minutes = seconds / 60;
                                        long hours = minutes / 60;
                                        days1 = hours / 24;

                                        Log.e("dayss",days1+"\nstartdate:"+startdate);

                                        for (int j=0;j<planSelectedModelArrayList.size();j++){

                                            if (stardateList1.get(i).getProdcut_id().equals(planSelectedModelArrayList.get(j).getProduct_id())){
                                                dayyyyy=(int) days1/ Integer.parseInt(planSelectedModelArrayList.get(j).getSkip_day());

                                                totalProductAmout= Double.parseDouble(db.getTotalSUbAmountById(stardateList1.get(i).getProdcut_id()));
                                                totalProductPrice=totalProductAmout*dayyyyy;

                                            }


                                        }
                                        EndDate_Model endDate_model = new EndDate_Model();
                                        endDate_model.setProdcut_id(stardateList1.get(i).getProdcut_id());
                                        endDate_model.setEnd_date(enddate);
                                        endDate_model.setTotal_days(String.valueOf(dayyyyy));
                                        endDate_model.setProduct_totalPrice((int) totalProductPrice);


                                        if (endDateModelArrayList.size()>0){
                                            for (int k=0;k<endDateModelArrayList.size();k++){
                                                if (endDateModelArrayList.get(k).getProdcut_id().equals(stardateList1.get(i).getProdcut_id())){
                                                    Log.e("stardateList1get(k)",stardateList1.get(k).getProdcut_id());
                                                    Log.e("stardateLit(i)",stardateList1.get(i).getProdcut_id());
                                                    Log.e("endDateModelget(k)",endDateModelArrayList.get(k).getProdcut_id());
                                                    endDateModelArrayList.remove(k);
                                                }
                                            }
                                            endDateModelArrayList.add(endDate_model);
                                        }else{
                                            endDateModelArrayList.add(endDate_model);
                                        }
                                        Log.e("customEndList", endDateModelArrayList.toString());

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        }, year, month, day);
                picker.show();

                // getTotalAmount();
                plan1.setBackground(getResources().getDrawable(R.drawable.bg_button));
                plan1.setTextColor(getResources().getColor(R.color.main_clr));
                plan2.setBackground(getResources().getDrawable(R.drawable.bg_button));
                plan2.setTextColor(getResources().getColor(R.color.main_clr));
                customPlan.setBackground(getResources().getDrawable(R.drawable.bg_fb));
                customPlan.setTextColor(getResources().getColor(R.color.white));
            }
        });

    }

    public  void getTotalAmount()  {


        subProductList.clear();
        dbmap = db.getCartAll();


        if (dbmap.size() > 0) {
            passArray = new JSONArray();
            for (int i = 0; i < map.size(); i++) {
                HashMap<String, String> map1 = map.get(i);

                SubscriptioAddProduct_model subscriptioAddProductModel=new SubscriptioAddProduct_model();
                subscriptioAddProductModel.setProduct_id(map1.get("product_id"));
                subscriptioAddProductModel.setProduct_name(map1.get("product_name"));
                subscriptioAddProductModel.setProduct_qty(db.getCartItemQty(map1.get("product_id")));
                subscriptioAddProductModel.setProduct_price(Integer.parseInt(map1.get("subscription_price")));
                subscriptioAddProductModel.setProduct_unit(map1.get("unit"));

                for (int j=0;j<stardateList1.size();j++){
                    for (int v=0;v<stardateList1.size();v++){
                        if (map1.get("product_id").equals(stardateList1.get( v).getProdcut_id())){
                            Log.e("subscriptionProductOd",map1.get("product_id"));
                            Log.e("startProductOd",stardateList1.get( v).getProdcut_id());
                            Log.e("startdateeee",stardateList1.get( v).getStart_date());
                            String start_date = null;
                            try {
                                start_date= Global.getDateConvert(stardateList1.get( v).getStart_date(),"dd-MM-yyyy","yyyy-MM-dd");
                                Log.e("start_date",start_date);
                            }catch (ParseException e){
                                e.printStackTrace();
                            }
                          //  Log.e("priceeeee",stardateList1.get(v).getProduct_price());



                            subscriptioAddProductModel.setStart_date(start_date);
                        }
                    }

                }

                for (int j=0;j<endDateModelArrayList.size();j++){
                    for (int v=0;v<endDateModelArrayList.size();v++){
                        if (map1.get("product_id").equals(endDateModelArrayList.get(v).getProdcut_id())){
                            Log.e("pro_id",map1.get("product_id"));
                            String end_date = null;
                            try {
                                end_date= Global.getDateConvert(endDateModelArrayList.get( v).getEnd_date(),"dd-MM-yyyy","yyyy-MM-dd");
                            }catch (ParseException e){
                                e.printStackTrace();
                            }


                            pro_gst=endDateModelArrayList.get(v).getProduct_totalPrice()* Global.getGSTTax(subscription2.this, Double.valueOf(String.valueOf(endDateModelArrayList.get(v).getProduct_totalPrice())));
                            pro_sgst=endDateModelArrayList.get(v).getProduct_totalPrice()* Global.getSGSTTax(subscription2.this, Double.valueOf(String.valueOf(endDateModelArrayList.get(v).getProduct_totalPrice())));

                            subscriptioAddProductModel.setProduct_gst((int) Math.round(pro_gst));
                            subscriptioAddProductModel.setProduct_sgst((int) Math.round(pro_sgst));
                            subscriptioAddProductModel.setProduct_totalprice(endDateModelArrayList.get(v).getProduct_totalPrice());
                            subscriptioAddProductModel.setEnd_date(end_date);
                        }
                    }

                }


                for (int j=0;j<planSelectedModelArrayList.size();j++){
                    for (int v=0;v<planSelectedModelArrayList.size();v++){
                        if (map1.get("product_id").equals(planSelectedModelArrayList.get(v).getProduct_id())){
                            subscriptioAddProductModel.setPlan_id(planSelectedModelArrayList.get(v).getPlan_id());
                        }
                    }
                }

                subProductList.add(subscriptioAddProductModel);

            }

            Log.e("subProdutList",subProductList.toString());


            if (subProductList.size()>0){
                for (int i=0;i<subProductList.size();i++){
                    total_price=total_price+ subProductList.get(i).getProduct_totalprice()+subProductList.get(i).getProduct_gst()+subProductList.get(i).getProduct_sgst();
                    total_gst=total_gst+subProductList.get(i).getProduct_gst();
                    total_sgst=total_sgst+subProductList.get(i).getProduct_sgst();
                }
                Log.e("total_price",total_price+"\ntotalgsttt:\t"+total_gst+"\ntotalsgst:\t"+total_sgst);
            }

        }


    }

    /*public  void getTotalAmount(){
        Log.e("start_date1",tardetDate);
        Log.e("end_date1",enddate);

        // totalAmout=Integer.parseInt(db.getTotalAmount());
        totalAmout=Integer.parseInt(db.getCartItemQty(product_id))*Integer.parseInt(db.getCartItemPrice(product_id));
        Log.e("totalAmout", String.valueOf(totalAmout));

        Log.e("totalAmount",String.valueOf(totalAmout));


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

            Log.e("dayssss",String.valueOf(days1)+"\nstartdate:"+startdate);

            dayyyyy=(int) days1/Integer.parseInt(planSkipDay);

        } catch (ParseException e) {
            e.printStackTrace();
        }



        Log.e("dayyyyy",String.valueOf(dayyyyy));
        totalPrice=totalAmout*dayyyyy;
        Log.e("totalPrice123",String.valueOf(totalPrice));
        total_gst=totalPrice*gst;
        total_sgst=totalPrice*sgst;
        total_tax=total_gst+total_sgst;
        totalPrice=total_tax+totalPrice;

        Log.e("totalPrice",String.valueOf(totalPrice));
    }*/

    private void AddSubscription() {

        Log.e("total_price", String.valueOf(total_price));


        if (String.valueOf(total_price).equals("0.0")){
            getTotalAmount();
        }

        Log.e("total_amount33", String.valueOf(total_price));

        if (ChkWallet.isChecked()){
            if (wallet<total_price){

                razorAmount= total_price-wallet;
                walletAmount=wallet;

                Log.e("razoramount123", String.valueOf(razorAmount));
                Log.e("totalAmout123", String.valueOf(total_price));
                Log.e("walletAmount123", String.valueOf(walletAmount));
                Log.e("wallet123", String.valueOf(wallet));

            }else {
           /* wallet 2100  totalprice 400*/

                walletAmount=total_price;
                razorAmount=0;
                Log.e("totalAmout444", String.valueOf(total_price));
                Log.e("walletAmount444", String.valueOf(walletAmount));

            }

        }else{
            razorAmount= (int) total_price;
            walletAmount=0;
            Log.e("razoramount777", String.valueOf(razorAmount));
            Log.e("totalAmout777", String.valueOf(total_price));

        }

        if (razorAmount!=0){
            Log.e("razorrrrrr", String.valueOf(razorAmount));
            pay_type="Razor Pay";
            startPayment(razorAmount);
        }else{
            pay_type="Wallet Pay";
            addSubPlan();
        }

     //   addSubPlan();


    }

    private void startPayment(double razorAmount) {
        Checkout checkout = new Checkout();

        final Activity activity = subscription2.this;
        double price_rs = razorAmount;
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
        Log.e("onSuccess","onsuccess");

        try {
            pay_type="Razor Pay";

            addSubPlan();


            AlertDialog.Builder builder = new AlertDialog.Builder(subscription2.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(subscription2.this).inflate(R.layout.custom_success, viewGroup, false);
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

            tvStts.setText("Payment Success");
            tvStts.setTextColor(getResources().getColor(R.color.green));
            ivIcon.setImageResource(R.drawable.ic_noun_check_1);
            tvTransDesc.setText("Payment done through your Razor amount");
            // ivIcon.setColorFilter(ContextCompat.getColor(subscription.this, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
            ivIcon.setColorFilter(ContextCompat.getColor(subscription2.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

           // db.removeItemFromCart(product_id);

            db.clearCart();



            llDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(subscription2.this, drawer.class);
                    startActivity(intent);
                    finish();
                }
            });

            alertDialog.show();

        } catch (Exception e) {
            Log.e("TAG", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int i, String s) {

        Log.e("payerror",s);
        try{
            AlertDialog.Builder builder = new AlertDialog.Builder(subscription2.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(subscription2.this).inflate(R.layout.custom_success, viewGroup, false);
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
            ivIcon.setColorFilter(ContextCompat.getColor(subscription2.this, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);


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
                        Toast.makeText(subscription2.this, message, Toast.LENGTH_SHORT).show();
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


    }

    private void addSubPlan() {

      if (subProductList.size()>0){
          passArray = new JSONArray();
          for (int i=0;i<subProductList.size();i++){
              JSONObject jObjP = new JSONObject();
              try {
                  jObjP.put("product_id", subProductList.get(i).getProduct_id());
                  jObjP.put("order_qty",subProductList.get(i).getProduct_qty());
                  jObjP.put("subscription_price", subProductList.get(i).getProduct_price());
                  jObjP.put("plans_id", subProductList.get(i).getPlan_id());
                  jObjP.put("amount", subProductList.get(i).getProduct_totalprice()+subProductList.get(i).getProduct_gst()+subProductList.get(i).getProduct_sgst());
                  jObjP.put("cgst_amount",subProductList.get(i).getProduct_gst());
                  jObjP.put("sgst_amount",subProductList.get(i).getProduct_sgst());
                  jObjP.put("start_date", subProductList.get(i).getStart_date());
                  jObjP.put("end_date", subProductList.get(i).getEnd_date());
                  passArray.put(jObjP);
              } catch (JSONException e) {
                  e.printStackTrace();
              }

          }


         // Log.e("parseArray",passArray.toString());

          if (isInternetConnected()) {
              but_sub_plan(passArray);
              Log.e("parseArray",passArray.toString());
          }
      }


    }

   /* private void addSubPlan() {

        ArrayList<HashMap<String, String>> items = db.getCartAll();
        if (items.size() > 0) {
            passArray = new JSONArray();
            for (int i = 0; i < items.size(); i++) {
                HashMap<String, String> map = items.get(i);
                JSONObject jObjP = new JSONObject();


                Log.e("priceeee",String.valueOf((Double.parseDouble(map.get("qty"))*Double.parseDouble(map.get("subscription_price")))));
                pro_gst= (Double.parseDouble(map.get("qty"))*Double.parseDouble(map.get("subscription_price")))*gst;
                pro_sgst= (Double.parseDouble(map.get("qty"))*Double.parseDouble(map.get("subscription_price")))*sgst;
                Log.e("pro_gst123",String.valueOf(pro_gst));
                try {
                    jObjP.put("product_id", map.get("product_id"));
                    jObjP.put("order_qty", map.get("qty"));
                    jObjP.put("subscription_price", map.get("subscription_price"));
                    Log.e("gstttttt",String.valueOf(gst));
                    if (map.get("product_id").equals(planSelectedModelArrayList.get(i).getProduct_id())){
                        Log.e("plaidddddd",planSelectedModelArrayList.get(i).getPlan_id());
                        planId=Integer.parseInt(planSelectedModelArrayList.get(i).getPlan_id());
                        jObjP.put("plans_id", String.valueOf(planId));
                    }
                    jObjP.put("amount", (Double.parseDouble(db.getTotalSUbAmountById(map.get("product_id"))))+pro_gst+pro_sgst);
                    jObjP.put("cgst_amount",String.valueOf(pro_gst));
                    jObjP.put("sgst_amount",String.valueOf( pro_sgst));
                    jObjP.put("start_date", tardetDate1);
                    jObjP.put("end_date", enddate1);
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

    }*/

    private void but_sub_plan(JSONArray passArray) {

        Log.e("walletAmount", String.valueOf(walletAmount));


        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("product_object", passArray.toString());
        params.put("address_id",  Addrid);
        params.put("wallet_amount", String.valueOf(walletAmount));
        params.put("razor_pay_amount", String.valueOf(razorAmount));
        params.put("pay_type", pay_type);
        params.put("total_amount", String.valueOf(total_price));
        params.put("promo_code", promo_code);
        params.put("promocode_amount", String.valueOf(discount_amount));
        params.put("total_cgst", String.valueOf(total_gst));
        params.put("total_sgst", String.valueOf(total_sgst));

        Log.e("paramssubadd11111",params.toString());



        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscribe_plans_add, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("subAddedd123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Log.e("razorAmount", String.valueOf(razorAmount));



                        if (razorAmount==0){

                            /*api call payment gateway*/
                            AlertDialog.Builder builder = new AlertDialog.Builder(subscription2.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(subscription2.this).inflate(R.layout.custom_success, viewGroup, false);
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
                            ivIcon.setColorFilter(ContextCompat.getColor(subscription2.this, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);
                            tvTransDesc.setText("Payment done through your Wallet amount");

                            db.removeItemFromCart(product_id);


                            showTotalCredit(user_id);


                            llDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent=new Intent(subscription2.this, drawer.class);
                                    startActivity(intent);
                                    finish();
                                    alertDialog.dismiss();
                                }
                            });


                            alertDialog.show();


                        }

                    }else if (status.equals("3")){
                        Toast.makeText(subscription2.this, message, Toast.LENGTH_SHORT).show();
                    }else if (status.equals("0")){
                        Toast.makeText(subscription2.this, message, Toast.LENGTH_SHORT).show();
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
                getAddressData(user_id);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.e("user_id11","user_id11");
                //Write your code if there's no result
            }
        }
    }//

    private void applyCoupon(AlertDialog alertDialog, String code) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("promo_code_id", "");
        params.put("total_amount", String.valueOf(total_price));
        params.put("promo_code", code);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.apply_coupon, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tag123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        alertDialog.dismiss();

                        JSONObject jsonObject=response.getJSONObject("data");

                        discount_amount= Double.parseDouble(jsonObject.getString("discount"));
                        String pay_amount=jsonObject.getString("pay_amount");
                        promo_code=jsonObject.getString("promo_code");


                        total_price= Float.parseFloat(pay_amount);


                    }else{
                        alertDialog.dismiss();
                        Toast.makeText(subscription2.this, ""+message, Toast.LENGTH_SHORT).show();

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(subscription2.this, drawer.class);
        startActivity(intent);
        finish();
    }


}
