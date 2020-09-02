package com.milkdelightuser.Activity;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.milkdelightuser.Adapter.Adapter_Recharge;
import com.milkdelightuser.Model.Schedule_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class Schedule extends BaseActivity {

    private HorizontalCalendar horizontalCalendar;

    List<Schedule_Model> scheduleModels = new ArrayList<>();
    RecyclerView schedulelist;
    Context context;
    String todaydate;
    RelativeLayout visible_rl , schedule_relative;
    Session_management session_management;
    String user_id;
    Button back;

    String a;
    TextView orderstatus;

    ImageView ivBack;
    TextView title,tvLeftDay;

    TextView substatus_show,text_plan,price_plan,qty_plan,unit_plan;
    ImageView image_plan;

    CardView llOrdrStts;
    TextView tvOrderStatus;

    LinearLayout llRenewSubscrption;
    String stts,subs_id1,start_date,end_date,sub_plan;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule);


        //orderstatus = findViewById(R.id.substatus_schedule);


        ivBack=findViewById(R.id.ivBack);
        title=findViewById(R.id.title);
        tvLeftDay=findViewById(R.id.tvLeftDay);


        substatus_show=findViewById(R.id.substatus_show);
        text_plan=findViewById(R.id.text_plan);
        price_plan=findViewById(R.id.price_plan);
        qty_plan=findViewById(R.id.qty_plan);
        unit_plan=findViewById(R.id.unit_plan);
        image_plan=findViewById(R.id.image_plan);

        llOrdrStts=findViewById(R.id.llOrdrStts);
        tvOrderStatus=findViewById(R.id.tvOrderStatus);



        llRenewSubscrption=findViewById(R.id.llRenewSubscrption);
        llRenewSubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });





        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        subs_id1=getIntent().getStringExtra("subs_id");
        start_date=getIntent().getStringExtra("start_date");
        end_date=getIntent().getStringExtra("end_date");
        sub_plan=getIntent().getStringExtra("sub_plan");
      //  Adapter_Recharge.daysBetween(start_date, end_date);

     //   String dateStr = "2/3/2017";

        Log.e("start_date",start_date);
        Log.e("end_date",end_date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date date1 = simpleDateFormat.parse(start_date);
            Date date2 = simpleDateFormat.parse(end_date);

            long dayyy=  Adapter_Recharge.daysBetween(date1, date2);
            Log.e("dayyy", String.valueOf(dayyy));

            tvLeftDay.setText(dayyy+" day left");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar endDate = Calendar.getInstance(); // End date
        endDate.add(Calendar.DAY_OF_MONTH, 7);

        Calendar startDate = Calendar.getInstance(); // Start date
        todaydate= String.valueOf(DateFormat.format("yyyy-MM-dd",startDate));
        Log.e("todaydatee", String.valueOf(DateFormat.format("yyyy-MM-dd",startDate)));
        startDate.add(Calendar.DAY_OF_MONTH, -7);

        Calendar defaultDate = Calendar.getInstance();
       /*
        Calendar startDate = Calendar.getInstance();
        todaydate= String.valueOf(DateFormat.format("yyyy-MM-dd",startDate));
        Log.e("todaydatee",String.valueOf(DateFormat.format("yyyy-MM-dd",startDate)));
        startDate.add(Calendar.MONTH, -1);

        *//* end after 1 month from now *//*
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);*/

        Schedule(todaydate);
        if (isInternetConnected()) {
            try {
                showDialog("");
                getSUbsDetail();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("defaultSelectedDate",defaultDate.toString());


        horizontalCalendar = new HorizontalCalendar.Builder(Schedule.this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatTopText("MMM")
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .textSize(11f, 20f, 12f)
                .showTopText(true)
                .showBottomText(true)
                .selectedDateBackground(getResources().getDrawable(R.drawable.bg_fb))
                .end()
                .defaultSelectedDate(defaultDate)
                .build();


        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                a = String.valueOf(DateFormat.format("yyyy-MM-dd", date));

                Log.e("scheduleResponsedate",a);

                Schedule(a);

            }

        });

        horizontalCalendar.getDefaultStyle()
                .setBackground(getResources().getDrawable(R.drawable.home_add))
                .setColorTopText(getResources().getColor(R.color.small_title_clr))
                .setColorMiddleText(getResources().getColor(R.color.title_clr))
                .setColorBottomText(getResources().getColor(R.color.small_title_clr));

        horizontalCalendar.getSelectedItemStyle()
                .setBackground(getResources().getDrawable(R.drawable.bg_fb))
                .setColorTopText(getResources().getColor(R.color.white))
                .setColorMiddleText(getResources().getColor(R.color.white))
                .setColorBottomText(getResources().getColor(R.color.white));




    }

    private void getSUbsDetail() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", user_id);
        params.put("subs_id",subs_id1);

        Log.e("getSUbsDetail",params.toString());


        StringRequest strRequest = new StringRequest(Request.Method.POST, BaseURL.subscription_product_details,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        dismissDialog();
                        Log.e("response",response.toString());
                       // Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String msg=jsonObject.getString("message");
                            String status=jsonObject.getString("status");

                            if (status.equals("1")){
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                String product_name=jsonObject1.getString("product_name");
                                String product_image=jsonObject1.getString("product_image");
                                String subs_id=jsonObject1.getString("subs_id");
                                String order_qty=jsonObject1.getString("order_qty");
                                String price=jsonObject1.getString("price");
                                String start_date=jsonObject1.getString("start_date");
                                String end_date=jsonObject1.getString("end_date");
                                String description=jsonObject1.getString("description");
                                String unit=jsonObject1.getString("unit");
                                String qty=jsonObject1.getString("qty");
                                String sub_status=jsonObject1.getString("sub_status");
                                String product_url=jsonObject1.getString("product_url");


                                String skip_days=jsonObject1.getString("skip_days");
                                String plans=jsonObject1.getString("plans");

                                substatus_show.setText(plans);
                                text_plan.setText(product_name+"("+qty+" "+unit+")");
                                price_plan.setText(price);
                                qty_plan.setText("QTY :"+order_qty);
                                unit_plan.setText(unit);

                                Glide.with(Schedule.this)
                                        .load(product_url+product_image)
                                        .into(image_plan);




                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("errooror",error.toString());
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("subs_id",subs_id1);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strRequest);

    }

    private void Schedule(String aa) {

        StringRequest strRequest = new StringRequest(Request.Method.POST, BaseURL.scheduling,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        dismissDialog();
                        Log.e("scheduleResponse",response.toString());

                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String msg=jsonObject.getString("message");
                            String status=jsonObject.getString("status");

                            if (status.equals("1")){
                                JSONObject jsonObject1=jsonObject.getJSONObject("data");

                                String status1=jsonObject1.getString("status");

                              //  stts=getIntent().getStringExtra("stts");
                                if (status1.equals("pause")){
                                    llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.pause));
                                    tvOrderStatus.setText(R.string.txt_pause);
                                    tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle_filled_black_24dp, 0, 0, 0);
                                }else if (status1.equals("Pending")){
                                    llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.pending));
                                    tvOrderStatus.setText(R.string.txt_pending);
                                    tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_noun_pending, 0, 0, 0);
                                }else if (status1.equals("Completed")){
                                    llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.delivered));
                                    tvOrderStatus.setText(R.string.txt_delivered);
                                    tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                                }else{

                                    llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.main_clr));
                                    tvOrderStatus.setText("Not order Available");
                                    tvOrderStatus.setTextColor(getResources().getColor(R.color.white));
                                 //   tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_noun_close_1, 0, 0, 0);


                                }



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e("errooror",error.toString());
                      //  Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("subs_id", subs_id1);
                params.put("plans_id", sub_plan);
                params.put("date",aa);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strRequest);

      /*  CustomVolleyJsonArrayRequest jsonArrayRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, BaseURL.scheduling, params, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("ScheduleTag", response.toString());

                try {


                     scheduleModels.clear();


                    if (response.length()>0) {
                        visible_rl.setVisibility(View.VISIBLE);
                        schedule_relative.setVisibility(View.GONE);


                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);


                            String product_name = jsonObject.getString("product_name");
                            String product_image = jsonObject.getString("product_image");
                            String subsid=jsonObject.getString("subs_id");
                            String order_oty = jsonObject.getString("order_qty");
                            String Product_price = jsonObject.getString("price");
                            String start_date=jsonObject.getString("start_date");
                            String product_discription = jsonObject.getString("description");


                            String product_quintity = jsonObject.getString("unit");


                            String qty = jsonObject.getString("qty");
                            String substatus = jsonObject.getString("sub_status");

                            String ordertype = jsonObject.getString("order_type");
                            String skipdayys=jsonObject.getString("skip_days");
                            String plans=jsonObject.getString("plans");
                            Schedule_Model subsofday_model = new Schedule_Model();
                            subsofday_model.setProduct_name(product_name);
                            subsofday_model.setProduct_image(product_image);
                            subsofday_model.setDescription(product_discription);
                            subsofday_model.setPrice(Product_price);
                            subsofday_model.setUnit(product_quintity);

                            subsofday_model.setOrder_qty(order_oty);
                            subsofday_model.setOrder_type(ordertype);
                            subsofday_model.setStart_date(start_date);
                            subsofday_model.setSubs_id(subsid);
                            subsofday_model.setSub_status(substatus);
                            subsofday_model.setSkip_days(skipdayys);
                            subsofday_model.setPlans(plans);
                            subsofday_model.setQty(qty);

                            scheduleModels.add(subsofday_model);


                            //            Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
//                            }

                        }
                        Adapter_Schedule adapter_subsofday = new Adapter_Schedule(scheduleModels);
                        schedulelist.setAdapter(adapter_subsofday);
                        adapter_subsofday.notifyDataSetChanged();
                    }

              else {
                  scheduleModels.clear();
                  visible_rl.setVisibility(View.GONE);
                        schedule_relative.setVisibility(View.VISIBLE);
              }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("errorrrr",error.toString());

                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_obj);*/
    }

}
