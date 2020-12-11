package com.webzino.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_FreqPlan;
import com.webzino.milkdelightuser.Model.Plan_model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class update_subscribe extends BaseActivity {

    /*tool bar*/
    Toolbar toolbar;
    TextView toolTitle;
    ImageView ivBack,ivNotify;
    String proname;

    /*view1*/
    int qty=0;
    RelativeLayout rlSubscription,rlTargetDate;
    ImageView ivproImage;
    TextView tvproName,tvProPrice,tvQtyDec,tvQty,tvQtyInc,tvqtyUnit,tvproUnit;


    String product_id,sub_plan,start_date,end_date,product_qty,subs_id;
    String Addrid,tardetDate1,enddate1;
    Session_management session_management;
    String user_id,user_name,user_nmbr,user_email,plan_id,product_price,product_unit;
    String cur_date;

    int planId,planIdIntent;
    String enddate;
    String wallet;

    /*view2*/
    TextView freqDate,tvSub;
    RecyclerView recycler_freqPlan;
    Adapter_FreqPlan adapterFreqPlan;
    List<Plan_model> planModelList;
    String txtPlan,planSkipDay,skip_day;


    /*view3*/
    Button plan1,plan2,customPlan;

    /*view4*/
    LinearLayout llEdit;
    ImageView ivAdrEdit;
    TextView adrName,adr,adrNmbr;

    /*view 5*/
    TextView balance;
    CheckBox ChkWallet;


    LinearLayout llBuySubscrption;

    SharedPreferences settings ;
    SharedPreferences.Editor editor;

    int dayyyyy;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subscription);

        toolbar=findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.main_clr));
        toolTitle=findViewById(R.id.title);
        toolTitle.setText(R.string.modify_sub);

        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llBuySubscrption=findViewById(R.id.llBuySubscrption);



        ivNotify=findViewById(R.id.ivNotify);
        ivNotify.setVisibility(View.VISIBLE);

        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(update_subscribe.this, Home.class);
                intent.putExtra("notification","subscription_page");
                startActivity(intent);
            }
        });

        planModelList=new ArrayList<>();

        rlSubscription=findViewById(R.id.subs_detail);
        rlTargetDate=findViewById(R.id.rlTargetDate);
        rlSubscription.setVisibility(View.VISIBLE);
        rlTargetDate.setVisibility(View.GONE);

        ivproImage=findViewById(R.id.ivproImage);
        tvproName=findViewById(R.id.tvproName);
        tvProPrice=findViewById(R.id.tvProPrice);
        tvQtyDec=findViewById(R.id.tvQtyDec);
        tvQty=findViewById(R.id.tvQty);
        tvQtyInc=findViewById(R.id.tvQtyInc);
        tvqtyUnit=findViewById(R.id.tvqtyUnit);
        tvproUnit=findViewById(R.id.tvproUnit);

        tvSub=findViewById(R.id.tvSub);
        tvSub.setText(R.string.update_sub);


        product_id=getIntent().getStringExtra("product_id");
        sub_plan=getIntent().getStringExtra("sub_plan");
        start_date=getIntent().getStringExtra("start_date");
        end_date=getIntent().getStringExtra("end_date");
        product_qty=getIntent().getStringExtra("product_qty");
        product_price=getIntent().getStringExtra("product_price");
        product_unit=getIntent().getStringExtra("product_unit");
        subs_id=getIntent().getStringExtra("subs_id");
        skip_day=getIntent().getStringExtra("skip_day");
        plan_id=getIntent().getStringExtra("plan_id");
        planIdIntent= Integer.parseInt(plan_id);
        planId=planIdIntent;

        Log.e("strat_dateee1",start_date);

        Log.e("end_date",end_date);

        recycler_freqPlan=findViewById(R.id.recycler_frq);

        plan1=findViewById(R.id.plan1);
        plan2=findViewById(R.id.plan2);
        customPlan=findViewById(R.id.customPlan);

        llEdit=findViewById(R.id.llEdit);
        ivAdrEdit=findViewById(R.id.ivAdrEdit);
        adrName=findViewById(R.id.adrName);
        adr=findViewById(R.id.adr);
        adrNmbr=findViewById(R.id.adrNmbr);


        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        user_name = session_management.getUserDetails().get(BaseURL.KEY_NAME);
        user_nmbr = session_management.getUserDetails().get(BaseURL.KEY_MOBILE);
        user_email = session_management.getUserDetails().get(BaseURL.KEY_EMAIL);


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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date =null;
        try {
            date = format.parse(start_date);
            Log.e("date123", String.valueOf(date));
            format.applyPattern("dd MMM yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        cur_date = df.format(c);

        Log.e("formatteddate",cur_date);

        freqDate=findViewById(R.id.freqDate);


        balance=findViewById(R.id.balance);
        ChkWallet=findViewById(R.id.ChkWallet);

        plan1.setEnabled(false);
        plan2.setEnabled(false);
        customPlan.setEnabled(false);
        ChkWallet.setEnabled(false);
        ChkWallet.setChecked(false);
        tvQtyInc.setEnabled(false);
        tvQtyDec.setEnabled(false);


        llBuySubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("planIdIntent",String.valueOf(planIdIntent));


                Log.e("planId",String.valueOf(planId));
                if (planIdIntent!=planId){
                    if (isInternetConnected()) {
                        try {
                            showDialog("");

                            updateSub();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    Toast.makeText(update_subscribe.this, "Selected Plan already exists", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    private void updateSub() {

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date curdate = format1.parse(cur_date);
            Date endddate = format1.parse(end_date);

            format1= new SimpleDateFormat("yyyy-MM-dd");
            tardetDate1 = format1.format(curdate);
            enddate1 = format1.format(endddate);

            Log.e("start_date122",tardetDate1);
            Log.e("end_date122",enddate1);

            long diff = endddate.getTime() - curdate.getTime();
            long seconds = diff / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days1 = hours / 24;

            Log.e("dayssss", String.valueOf(days1));

            dayyyyy=(int) days1/ Integer.parseInt(skip_day);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Log.e("start_date12333",start_date);
        Log.e("cur_date123333",cur_date);

        getEndDateData(dayyyyy);

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("plan_id", String.valueOf(planId));
        params.put("subs_id", subs_id);
        params.put("user_id", user_id);
        params.put("start_date",cur_date);
        params.put("end_date", enddate);

        Log.e("modifySubparams",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_modify, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("modifySub", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        Toast.makeText(update_subscribe.this, message, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(update_subscribe.this,Home.class);
                        intent.putExtra("subsription","subsription");
                        startActivity(intent);


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
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

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


                        tvQty.setText(product_qty);
                        tvproName.setText(product_name);
                        tvproUnit.setText("("+product_unit+")");
                        tvProPrice.setText(MainActivity.currency_sign+product_price);


                        /*Glide.with(update_subscribe.this)
                                .load(proImgae)
                                .into(ivproImage);*/

                        Global.loadGlideImage(update_subscribe.this,proImgae,proImgae,ivproImage);



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
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);


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
                            Toast.makeText(update_subscribe.this, "0 data found", Toast.LENGTH_SHORT).show();
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

                                if (skip_days.equals("") || skip_days.equals("null") || skip_days.equals("0")){
                                    Log.e("skippdayyy","skipdayyy__if");
                                    plan_model.setSkipDays("1");
                                }else{
                                    plan_model.setSkipDays(skip_days);
                                }
                                planModelList.add(plan_model);

                            }

                            adapterFreqPlan=new Adapter_FreqPlan(update_subscribe.this,planModelList);

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
                            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recycler_freqPlan.setLayoutManager(gridLayoutManager);
                            recycler_freqPlan.setAdapter(adapterFreqPlan);

                            if (planModelList.size()>0){
                                for (int k=0;k<planModelList.size();k++){
                                    if (planModelList.get(k).getPlans().equals(sub_plan)){
                                        Adapter_FreqPlan.row_index=k;
                                    }
                                }
                            }

                            adapterFreqPlan.setEventListener(new Adapter_FreqPlan.EventListener() {
                                @Override
                                public void onItemViewClicked(int i,int plan) {
                                    // txtPlan=plan;
                                    planId=plan;
                                    planSkipDay=planModelList.get(i).getSkipDays();

                                    Log.e("id123", String.valueOf(planId));
                                    Log.e("planSkipDay", String.valueOf(planSkipDay));

                                }

                                @Override
                                public void onItemViewClicked1(int position, int plan, int position1) {
                                    planId=plan;
                                    planSkipDay=planModelList.get(position).getSkipDays();

                                    Log.e("id123", String.valueOf(planId));
                                    Log.e("planSkipDay", String.valueOf(planSkipDay));
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
                Toast.makeText(update_subscribe.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
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
                        Addrid=jsonObject.getString("id");
                        String full_address=jsonObject.getString("full_address");
                        String user_number=jsonObject.getString("user_number");


                        adrName.setText(user_name);
                        adrNmbr.setText(user_number);
                        adr.setText(full_address);

                        llEdit.setVisibility(View.GONE);


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
                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

    public void getEndDateData(int targetDate){

        targetDate=targetDate* Integer.parseInt(planSkipDay);


        /*if (planId==1){
            targetDate=targetDate*1;
        }else if (planId==3){
            targetDate=targetDate*3;
        }else if (planId==4){
            targetDate=targetDate*2;
        }else if (planId==5){
            targetDate=targetDate*7;
        }*/

        Log.e("targetDate", String.valueOf(targetDate));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = null;
        try {
            date = dateFormat.parse(tardetDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, targetDate);
        enddate = dateFormat.format(calendar.getTime());
        Log.e("enddate123444",enddate);
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
                Log.e("errorrr",error.toString());
                dismissDialog();
                Toast.makeText(getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }
}
