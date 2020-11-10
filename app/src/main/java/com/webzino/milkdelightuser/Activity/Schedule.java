package com.webzino.milkdelightuser.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_Recharge;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.cardview.widget.CardView;
import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static com.webzino.milkdelightuser.utils.Global.IMGURL_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.URL_DATA;


public class Schedule extends BaseActivity {

    private HorizontalCalendar horizontalCalendar;


    String todaydate;
    Session_management session_management;
    String user_id;
    RelativeLayout container_null1;

    String a;

    ImageView ivBack;
    TextView title,tvLeftDay;

    TextView substatus_show,text_plan,price_plan,qty_plan,unit_plan;
    ImageView image_plan;

    CardView llOrdrStts;
    TextView tvOrderStatus;

    LinearLayout llRenewSubscrption,ll_product;
    String stts,subs_id1,start_date,end_date,sub_plan,order_id;
    String product_name, product_id,product_image, order_qty,price, description, unit, qty, sub_status,product_url,skip_days,plans,plan_id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_schedule);


        ivBack=findViewById(R.id.ivBack);
        title=findViewById(R.id.title);
        tvLeftDay=findViewById(R.id.tvLeftDay);
        ll_product=findViewById(R.id.ll_product);
        container_null1=findViewById(R.id.container_null1);

        substatus_show=findViewById(R.id.substatus_show);
        text_plan=findViewById(R.id.text_plan);
        price_plan=findViewById(R.id.price_plan);
        qty_plan=findViewById(R.id.qty_plan);
        unit_plan=findViewById(R.id.unit_plan);
        image_plan=findViewById(R.id.image_plan);

        llOrdrStts=findViewById(R.id.llOrdrStts);
        tvOrderStatus=findViewById(R.id.tvOrderStatus);

        llRenewSubscrption=findViewById(R.id.llRenewSubscrption);

        session_management=new Session_management(getApplicationContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);

        subs_id1=getIntent().getStringExtra("subs_id");
        start_date=getIntent().getStringExtra("start_date");
        end_date=getIntent().getStringExtra("end_date");
        sub_plan=getIntent().getStringExtra("sub_plan");
        order_id=getIntent().getStringExtra("order_id");

        Log.e("start_date-->1",start_date);
        Log.e("end_date-->1",end_date);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(Global.getDateConvert(start_date,"yyyy-MM-dd","EEE MMM dd HH:mm:ss z yyyy")));// all done
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar endDate = Calendar.getInstance(); // End date
        endDate.add(Calendar.DAY_OF_MONTH, 7);

        Calendar startDate = Calendar.getInstance(); // Start date
        todaydate= String.valueOf(DateFormat.format("yyyy-MM-dd",startDate));
        Log.e("todaydatee", String.valueOf(DateFormat.format("yyyy-MM-dd",startDate)));
        startDate.add(Calendar.DAY_OF_MONTH, -7);


        Log.e("start_date",start_date);
        Log.e("end_date",end_date);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null,date2 = null;
        try {
             date1 = simpleDateFormat.parse(start_date);
             Date curdate = simpleDateFormat.parse(todaydate);
             date2 = simpleDateFormat.parse(end_date);

            long dayyy=  Adapter_Recharge.daysBetween(curdate, date2);
            Log.e("dayyy", String.valueOf(dayyy));

            tvLeftDay.setText(dayyy+" day left");

        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar defaultDate = Calendar.getInstance();



        if (isInternetConnected()) {
            try {
                showDialog("");
                Schedule(todaydate);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Log.e("defaultSelectedDate",defaultDate.toString());

        Calendar cal_start = Calendar.getInstance();
        Calendar cal_end = Calendar.getInstance();
        cal_start.setTime(date1);
        cal_end.setTime(date2);


        horizontalCalendar = new HorizontalCalendar.Builder(Schedule.this, R.id.calendarView)
                .range(cal_start, cal_end)
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


        llRenewSubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RenewPlanActivity.class);
                intent.putExtra("order_id",order_id);
                startActivity(intent);

            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
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
//                                String jsonObjectData=jsonObject1.getString("data");

                                if (status1.equals("false")){
                                    ll_product.setVisibility(View.GONE);
                                    container_null1.setVisibility(View.VISIBLE);
                                    llRenewSubscrption.setVisibility(View.GONE);
                                }else{
                                    ll_product.setVisibility(View.VISIBLE);
                                    container_null1.setVisibility(View.GONE);
                                    llRenewSubscrption.setVisibility(View.VISIBLE);
                                    JSONObject jsonObjectData=jsonObject1.getJSONObject("data");
                                    Log.e("dataResponse",String.valueOf(jsonObjectData));

//                                    JSONObject jsonObjectData=new JSONObject("data");
//                                    if (jsonObjectData.has("PROJECT_NUMBER")) {

                                    Log.e("jsonObjectData",String.valueOf(jsonObjectData));

                                    JSONObject product=jsonObjectData.getJSONObject("product");
                                    JSONObject plan_details=jsonObjectData.getJSONObject("plan_details");
                                    JSONObject productImage=product.getJSONObject("product_image");
                                    String product_image=productImage.getString("product_image");


                                    String status12=jsonObjectData.getString("status");


                                    String subscription_price=product.getString("subscription_price");
                                    String product_name=product.getString("product_name");
                                    String qty=product.getString("qty");
                                    String unit=product.getString("unit");
                                    String order_qty=jsonObjectData.getString("order_qty");
                                    String plans=plan_details.getString("plans");

                                    SharedPreferences GstPref1 = getSharedPreferences(IMGURL_PREFS_NAME, MODE_PRIVATE);
                                    String urlData = GstPref1.getString(URL_DATA, null);
                                    String product_url = null;
                                    if (urlData != null) {
                                        Log.e("urlData",urlData);
                                        try {
                                            JSONObject jsonObject12=new JSONObject(urlData);
                                            product_url=jsonObject12.getString("product_url");

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    Log.e("imgurl",product_url+product_image);
                                    Global.loadGlideImage(Schedule.this,product_image,product_url+product_image,image_plan);

                                    substatus_show.setText(plans);
                                    text_plan.setText(product_name+"("+qty+" "+unit+")");
                                    price_plan.setText(MainActivity.currency_sign+subscription_price);
                                    qty_plan.setText(getString(R.string.qty)+order_qty);
                                    unit_plan.setText(qty+" "+unit);


                                    //  stts=getIntent().getStringExtra("stts");
                                    if (status12.equals("cancel")){
                                        llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.pause));
                                        tvOrderStatus.setText(R.string.txt_pause);
                                        tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pause_circle_filled_black_24dp, 0, 0, 0);
                                    }else if (status12.equals("Pending")){
                                        llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.pending));
                                        tvOrderStatus.setText(R.string.txt_pending);
                                        tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_noun_pending, 0, 0, 0);
                                    }else if (status12.equals("Completed")){
                                        llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.delivered));
                                        tvOrderStatus.setText(R.string.txt_delivered);
                                        tvOrderStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_circle_black_24dp, 0, 0, 0);
                                    }else{
                                        llOrdrStts.setCardBackgroundColor(getResources().getColor(R.color.main_clr));
                                        tvOrderStatus.setText(status12);
                                        tvOrderStatus.setTextColor(getResources().getColor(R.color.white));
                                    }

                                }

                                Log.e("statusss",status1);
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

                Log.e("params",params.toString());
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(strRequest);

    }

}
