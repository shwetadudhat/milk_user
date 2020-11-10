package com.webzino.milkdelightuser.Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.Activity.Product;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Activity.ProductListing;
import com.webzino.milkdelightuser.Activity.Schedule;
import com.webzino.milkdelightuser.Activity.update_subscribe;
import com.webzino.milkdelightuser.Model.Showsubscrip_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseFragment;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.ConnectivityReceiver;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;
import static com.webzino.milkdelightuser.utils.Global.MY_NOTIFICATION_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.NOTIFICATION_DATA;


/**
 * A simple {@link Fragment} subclass.
 */
public class Subscription_Fragment extends BaseFragment {


    List<Showsubscrip_Model> showsubscripModels = new ArrayList<>();
    RecyclerView showsubsccriplist;
    LinearLayout llAddSubscrption;
    RelativeLayout container_null1;

    String user_id;

    Session_management session_management;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;


    public Subscription_Fragment() {
        // Required empty public constructor
    }

    @Override

    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences(MY_NOTIFICATION_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        llAddSubscrption = (LinearLayout) view.findViewById(R.id.llAddSubscrption);
        container_null1 =  view.findViewById(R.id.container_null1);
        showsubsccriplist = (RecyclerView) view.findViewById(R.id.Recycler_showSubscrip);
        showsubsccriplist.setLayoutManager(new GridLayoutManager(getContext(), 1));



        showsubscripModels=new ArrayList<>();

        session_management=new Session_management(getContext());
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
        Log.e("userId",user_id);

        llAddSubscrption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ProductListing.class);
                intent.putExtra("seeAll","bestProduct");
                startActivity(intent);
            }
        });


        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            showsubscrip(user_id);
        } else {
            Global.showInternetConnectionDialog(getContext());

        }

    }


    private void showsubscrip(String u_id) {

        String tag_json_obj = "json store req";
        final Map<String, String> params = new HashMap<String, String>();
        params.put("user_id",u_id);


        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_show_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("showsubscrip", response.toString());
                dismissDialog();

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {

                        Log.e("status",status);

                        JSONObject jsonObject=response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("subscription_data");
                        myEdit.putString(NOTIFICATION_DATA, jsonArray.toString());
                        myEdit.commit();

                        if (jsonArray.length()>0){
                            container_null1.setVisibility(View.GONE);
                            showsubsccriplist.setVisibility(View.VISIBLE);
                        }else{

                            container_null1.setVisibility(View.VISIBLE);
                            showsubsccriplist.setVisibility(View.GONE);
                        }
                        String product_url = jsonObject.getString("product_url");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String product_name = jsonObject1.getString("product_name");

                            Log.e("productname",product_name);

                            String subsid = jsonObject1.getString("subs_id");
                            String plan_id = jsonObject1.getString("plan_id");
                            String delivery_date =jsonObject1.getString("delivery_date");
                            String end_date =jsonObject1.getString("end_date");
                            String quantity = jsonObject1.getString("order_qty");
                            String price = jsonObject1.getString("price");
                            String start_date = jsonObject1.getString("start_date");
                            String discription = jsonObject1.getString("description");
                            String product_id=jsonObject1.getString("product_id");
                            String subprice=jsonObject1.getString("subscription_price");
                            String unit = jsonObject1.getString("unit");
                            String qty =jsonObject1.getString("qty");
                            String substatus = jsonObject1.getString("sub_status");
                            String skip_days= jsonObject1.getString("skip_days");
                            String plans = jsonObject1.getString("plans");
                            String order_id = jsonObject1.getString("order_id");

                            if (!product_name.equals("null")){

                                Log.e("producttt==>1","iffff");

                                JSONObject jsonObject2=jsonObject1.getJSONObject("product");

//                            String price = jsonObject2.getString("price");
                                JSONObject jsonObject3=jsonObject2.getJSONObject("product_image");
                                String product_image = jsonObject3.getString("product_image");

                                Log.e("product_image123",product_image);

                                Showsubscrip_Model showsubscrip_model = new Showsubscrip_Model();
                                showsubscrip_model.setProduct_name(product_name);
                                showsubscrip_model.setProduct_image(product_url+product_image);
                                showsubscrip_model.setUnit(unit);
                                showsubscrip_model.setProductid(product_id);
                                showsubscrip_model.setPlan_id(plan_id);
                                showsubscrip_model.setSkip_days(skip_days);
                                showsubscrip_model.setPrice(price);
                                showsubscrip_model.setOrder_qty(quantity);
                                showsubscrip_model.setQty(qty);
                                showsubscrip_model.setSubprice(subprice);
                                showsubscrip_model.setDescription(discription);
                                showsubscrip_model.setSub_status(substatus);
                                showsubscrip_model.setSubs_id(subsid);
                                showsubscrip_model.setPlans(plans);
                                showsubscrip_model.setDelivery_date(delivery_date);
                                showsubscrip_model.setStart_date(start_date);
                                showsubscrip_model.setEnd_date(end_date);
                                showsubscrip_model.setOrderId(order_id);

                                showsubscripModels.add(showsubscrip_model);
                            }

                        }

                        Adapter_Showsubscrip adapter_showsubscrip = new Adapter_Showsubscrip(showsubscripModels);
                        showsubsccriplist.setAdapter(adapter_showsubscrip);
                        Log.e("showsubscripModels",showsubscripModels.toString());
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

                 Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }
    public class Adapter_Showsubscrip extends RecyclerView.Adapter<Adapter_Showsubscrip.holder> {

        List<Showsubscrip_Model> showsubscripModels;
        Context context;
        private int mYear, mMonth, mDay;
        int end=0;
        String start_date;
        String end_date;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        String subs_id;

        public Adapter_Showsubscrip(List<Showsubscrip_Model> showsubscripModels) {
            this.showsubscripModels = showsubscripModels;
        }

        @NonNull
        @Override
        public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.listitem_showsubscrip, viewGroup, false);
            context = viewGroup.getContext();
            return new holder(view);    }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull final holder holder, final int i) {


            final Showsubscrip_Model showsubscrip_model = showsubscripModels.get(i);

            sharedPreferences = context.getSharedPreferences("subsid", MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.putString("subs_id",showsubscrip_model.getSubs_id());
            editor.commit();

          /*  Glide.with(context)
                    .load( showsubscrip_model. getProduct_image())
                    .into(holder.image_plan);*/

            Global.loadGlideImage(context,showsubscrip_model. getProduct_image(),showsubscrip_model. getProduct_image(),holder.image_plan);



            holder.text_plan.setText(showsubscrip_model.getProduct_name()+" ("+showsubscrip_model.getQty()+" "+showsubscrip_model.getUnit()+")");
            holder.unit1.setText(showsubscrip_model.getQty()+" "+showsubscrip_model.getUnit());

            holder.price1.setText(MainActivity.currency_sign+ Math.round(Double.parseDouble(String.valueOf(Double.parseDouble(showsubscrip_model.getPrice())/*+ Math.round( Global.getTax(context, Double.parseDouble(showsubscrip_model.getPrice())))*/))));
            holder.qty.setText(getString(R.string.qty) + " " + showsubscrip_model.getOrder_qty());
            holder.substatus.setText(showsubscrip_model.getPlans());

            if (showsubscrip_model.getSub_status().contains(getString(R.string.pause))){
                holder.pause.setText(R.string.resume);
             //   holder.pause.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp),null,null,null);
            }
            else {
                holder.pause.setText(R.string.pause);
             //   holder.pause.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp),null,null,null);

            }

            holder.pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (showsubscrip_model.getSub_status().contains(getString(R.string.pause))){
//
                        start_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());;

                        subs_id = showsubscrip_model.getSubs_id();
                        String Skip_day= showsubscrip_model.getSkip_days();

                        if (ConnectivityReceiver.isConnected()) {
                            showDialog("");
                            resume_orders(subs_id,start_date,Skip_day);
                        } else {
                            Global.showInternetConnectionDialog(getContext());

                        }


                    }
                    else {

                        final Calendar c1 = Calendar.getInstance();
                        c1.add(Calendar.DAY_OF_MONTH, 1);

                        int  mYear2 = c1.get(Calendar.YEAR);
                        int mMonth2 = c1.get(Calendar.MONTH)+1;
                        int  mDay2 = c1.get(Calendar.DAY_OF_MONTH)+1;


                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        ViewGroup viewGroup = v.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(context).inflate(R.layout.custom, viewGroup, false);
                        builder.setView(dialogView);


                        final Dialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));

                        final TextView startdate = dialogView.findViewById(R.id.start_date);
                        final TextView endstart = dialogView.findViewById(R.id.end_date);

                        startdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                startdate.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));

                            }
                        });

                        endstart.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                endstart.setBackground(getContext().getResources().getDrawable(R.drawable.bg_edit));

                            }
                        });

                        c1.add(Calendar.DAY_OF_MONTH, -1);

                        int  mYear1 = c1.get(Calendar.YEAR);
                        int mMonth1 = c1.get(Calendar.MONTH)+1;
                        int  mDay1 = c1.get(Calendar.DAY_OF_MONTH)+1;

                        start_date= mDay1+"-"+mMonth1+"-"+mYear1;

                        end_date=mDay2+"-"+mMonth2+"-"+mYear2;





                        startdate.setText(start_date);

                        endstart.setText(end_date);

                        startdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                mDay = c.get(Calendar.DAY_OF_MONTH)+1;
                                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                String _year = String.valueOf(year);
                                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                                String _pickedDate =  _date+ "-" + _month +"-"+year ;
                                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12

                                                start_date = _pickedDate;
                                                startdate.setText(start_date);
                                                 end= Integer.parseInt(_date)+1;
                                                endstart.setText(end+"-"+_month+"-"+year);
                                                Log.d("dfasd",end+"-"+_month+"-"+year);

                                            }
                                        }, mYear, mMonth,mDay);
                                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                dialog.show();
                            }
                        });


                        // if button is clicked, close the custom dialog

                        end_date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                        endstart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final Calendar c = Calendar.getInstance();
                                mYear = c.get(Calendar.YEAR);
                                mMonth = c.get(Calendar.MONTH);
                                if (end==0){
                                    mDay = c.get(Calendar.DAY_OF_MONTH)+2;

                                }
                                else {
                                    mDay = end;
                                }


                                DatePickerDialog dialog = new DatePickerDialog(getContext(),
                                        new DatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                String _year = String.valueOf(year);
                                                String _month = (month + 1) < 10 ? "0" + (month + 1) : String.valueOf(month + 1);
                                                String _date = dayOfMonth < 10 ? "0" + dayOfMonth : String.valueOf(dayOfMonth);
                                                String _pickedDate =  _date+ "-" + _month +"-"+year ;
                                                Log.e("PickedDate: ", "Date: " + _pickedDate); //2019-02-12


                                                end_date = _pickedDate;
                                                endstart.setText(end_date);

                                            }
                                        }, mYear, mMonth,mDay);
                                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                                dialog.show();

                            }
                        });

                        Button btnSave = dialogView.findViewById(R.id.btnSave);
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                subs_id = showsubscrip_model.getSubs_id();
                                String delivery_date= showsubscrip_model.getDelivery_date();
                                if (ConnectivityReceiver.isConnected()) {
                                    showDialog("");
                                    Log.e("start_date",start_date);
                                    Log.e("end_date",end_date);
                                    pause(subs_id,start_date,end_date,delivery_date, dialog);
                                } else {
                                    Global.showInternetConnectionDialog(getContext());
                                }

                            }
                        });

                        dialog.show();

                    }

                }

            });

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    ViewGroup viewGroup = v.findViewById(android.R.id.content);
                    View dialogView = LayoutInflater.from(context).inflate(R.layout.custom_delete_reason, viewGroup, false);
                    builder.setView(dialogView);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.setCancelable(true);
                    alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.color.transparent));


                    EditText edMsg=dialogView.findViewById(R.id.edMsg);
                    Button btnApply=dialogView.findViewById(R.id.btnApply);
                    edMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View view, boolean b) {
                            edMsg.setBackground(getResources().getDrawable(R.drawable.bg_edit));
                        }
                    });

                    btnApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String msg=edMsg.getText().toString();

                            if (msg.equals("")){
                                edMsg.setError("This Feild is Require!!");
                            }else{
                                /*api call*/
                                if (ConnectivityReceiver.isConnected()) {
                                    showDialog("");
                                    delete(i,showsubscrip_model.getSubs_id(),msg,alertDialog);
                                } else {
                                    Global.showInternetConnectionDialog(getContext());

                                }

                            }
                        }
                    });
                    alertDialog.show();

                }
            });

            holder.modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getContext(), update_subscribe.class);
                    intent.putExtra("product_id",showsubscrip_model.getProductid());
                    intent.putExtra("subs_id",showsubscrip_model.getSubs_id());
                    intent.putExtra("plan_id",showsubscrip_model.getPlan_id());
                    intent.putExtra("product_qty",showsubscrip_model.getOrder_qty());
                    intent.putExtra("product_unit",showsubscrip_model.getQty()+" "+showsubscrip_model.getUnit());
                    intent.putExtra("product_price",showsubscrip_model.getPrice());
                    intent.putExtra("sub_plan",showsubscrip_model.getPlans());
                    intent.putExtra("start_date",showsubscrip_model.getStart_date());
                    intent.putExtra("end_date",showsubscrip_model.getEnd_date());
                    intent.putExtra("skip_day",showsubscrip_model.getSkip_days());

                    startActivity(intent);


                }
            });

            holder.subDelStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent=new Intent(getContext(), Schedule.class);
                    intent.putExtra("stts",showsubscrip_model.getSub_status());
                    intent.putExtra("subs_id",showsubscrip_model.getSubs_id());
                    intent.putExtra("start_date",showsubscrip_model.getStart_date());
                    intent.putExtra("end_date",showsubscrip_model.getEnd_date());
                    intent.putExtra("sub_plan",showsubscrip_model.getPlan_id());
                    intent.putExtra("order_id",showsubscrip_model.getOrderId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return showsubscripModels.size();
        }

        public class holder extends RecyclerView.ViewHolder {

            TextView unit1 , price1  , quantity , text_plan , substatus , qty;
            ImageView image_plan;
            TextView pause  , modify;
            ImageView delete;
            Button subDelStatus;


            public holder(@NonNull View itemView) {
                super(itemView);

                image_plan =itemView.findViewById(R.id.image_plan);
                text_plan = itemView.findViewById(R.id.text_plan);
                price1 = itemView.findViewById(R.id.price_plan);
                unit1 = itemView.findViewById(R.id.unit_plan);
                pause = itemView.findViewById(R.id.pause);
                qty = itemView.findViewById(R.id.qty_plan);
                delete = itemView.findViewById(R.id.delete);
                modify = itemView.findViewById(R.id.modify);
                substatus = itemView.findViewById(R.id.substatus_show);
                subDelStatus = itemView.findViewById(R.id.subDelStatus);

            }
        }

        private void pause(String subs_id, String pause_start, String pause_end, String delivery_date, Dialog dialog){

            String tag_json_obj = "json store req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("subs_id", subs_id);
            params.put("start_date", pause_start);
            params.put("end_date",pause_end);

            Log.e("paramssss",params.toString());

            CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_pause_orders, params
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e("pauseorder",response.toString());
                    dismissDialog();
                    dialog.dismiss();
                    try {

                        String status = response.getString("status");
                        String message = response.getString("message");

                        if (status.contains("1")){


                            Toast.makeText(context.getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();


                        }else {

                            Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog.dismiss();
                        dismissDialog();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dialog.dismiss();
                    dismissDialog();
                    Log.e("errrorr",error.toString());
                    Toast.makeText(context.getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
        }


        private void resume_orders(String subs_id, String start_date, String skip_days){

            String tag_json_obj = "json store req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("subs_id", subs_id);
            params.put("start_date", start_date);
           // params.put("skip_days",skip_days);
            params.put("user_id",user_id);

            CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_resume_orders, params
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Tag",response.toString());

                    dismissDialog();

                    try {

                        String status = response.getString("status");
                        String message = response.getString("message");

                        if (status.contains("1")){

                            Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();

                        }else {

                            Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(context.getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
        }

        public void removeAt(int position) {
            showsubscripModels.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, showsubscripModels.size());
        }

        private void delete(int i, String id, String msg, AlertDialog alertDialog){

            String tag_json_obj = "json store req";
            Map<String, String> params = new HashMap<String, String>();
            params.put("subs_id", id);
            params.put("reason", msg);

            CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.subscription_order_delete, params
                    , new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    dismissDialog();
                    alertDialog.dismiss();
                    Log.e("deleteTag",response.toString());

                    try {

                        String status = response.getString("status");
                        String message = response.getString("message");

                        if (status.contains("1")){

                            removeAt(i);

                        }else {

                            Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error",error.toString());
                    dismissDialog();
                    alertDialog.dismiss();
                    Toast.makeText(context.getApplicationContext(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
        }


    }

}
