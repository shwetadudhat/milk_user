package com.webzino.milkdelightuser.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Model.PlanSelected_model;
import com.webzino.milkdelightuser.Model.Plan_model;
import com.webzino.milkdelightuser.Model.StartDate_Model;
import com.webzino.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.webzino.milkdelightuser.utils.Global.MY_PLAN_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.MY_STARTDATE_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.PLAN_DATA;
import static com.webzino.milkdelightuser.utils.Global.STARTDATE_DATA;


public class Adapter_SubProduct extends RecyclerView.Adapter<Adapter_SubProduct.holder> {

    Context context;
    ArrayList<HashMap<String, String>> list;
    List<Plan_model> planModelList;
    ArrayList<PlanSelected_model> planSelectedModelArrayList,planSelectedModelArrayList1;
    ArrayList<StartDate_Model> stardateList,stardateList1;
    ArrayList<SubscriptioAddProduct_model> subProductList;

    RecyclerView recycler_frq;
    Adapter_FreqPlan adapterFreqPlan;

    private DatabaseHandler dbcart;

    String tardetDate;

    Session_management session_management;
    String user_id,planSkipDay;
    int plan_idd;

    SharedPreferences sharedPreferences,sharedPreferences1;
    SharedPreferences.Editor myEdit1,myEdit2;

    Gson gson;
    String json,json1;


    private EventListener mEventListener;
    DatePickerDialog picker;
    public static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};


    public Adapter_SubProduct(Context context, ArrayList<HashMap<String, String>> list, List<Plan_model> planModelList) {
        this.context = context;
        this.list = list;
        this.planModelList=planModelList;
        planSelectedModelArrayList=new ArrayList<>();
        stardateList=new ArrayList<>();
        subProductList=new ArrayList<>();
        planSelectedModelArrayList1=new ArrayList<>();
        stardateList1=new ArrayList<>();

        session_management=new Session_management(context);
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_subcart, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final HashMap<String, String> map = list.get(i);
        dbcart = new DatabaseHandler(context);

        sharedPreferences = context.getSharedPreferences(MY_PLAN_PREFS_NAME, MODE_PRIVATE);
        sharedPreferences1 = context.getSharedPreferences(MY_STARTDATE_PREFS_NAME, MODE_PRIVATE);
        myEdit1 = sharedPreferences.edit();
        myEdit2 = sharedPreferences1.edit();



        gson = new Gson();
        json = sharedPreferences.getString(PLAN_DATA, "");
        json1 = sharedPreferences1.getString(STARTDATE_DATA, "");

        Log.e("productimage",map.get("product_image"));


        Global.loadGlideImage(context,map.get("product_image"), map.get("product_image"),holder.ivproImage);



        holder.tvproName.setText(map.get("product_name"));
        holder.tvproUnit.setText("("+map.get("unit")+")");
        holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(map.get("subscription_price"))));
        holder.tvQty.setText(dbcart.getCartItemQty(map.get("product_id")));


        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);

        Date c = calendar.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String tardetDate12 = df.format(c);

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        tardetDate = df1.format(c);


        StartDate_Model startDate_model=new StartDate_Model();
        startDate_model.setProdcut_id(map.get("product_id"));
        startDate_model.setProduct_qty(map.get("qty"));


        if (!json1.isEmpty()) {
            Type type = new TypeToken<List<StartDate_Model>>() {}.getType();
            stardateList1= gson.fromJson(json1, type);
            for (int k=0;k<stardateList1.size();k++){
                if (map.get("product_id").equals(stardateList1.get(k).getProdcut_id())){
                    try {
                        holder.freqDate.setText(Global.getDateConvert(stardateList1.get(k).getStart_date(),"dd-MM-yyyy","dd MMM yyyy"));
                        startDate_model.setStart_date(stardateList1.get(k).getStart_date());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                }else{
                    holder.freqDate.setText(tardetDate12);
                    startDate_model.setStart_date(tardetDate);
                }
            }

        }else{

            holder.freqDate.setText(tardetDate12);
            startDate_model.setStart_date(tardetDate);
        }

        if (stardateList.size()>0){
            for (int j=0;j<stardateList.size();j++){
                if (stardateList.get(j).getProdcut_id().equals(map.get("product_id"))){
                    stardateList.remove(j);
                }
            }
            stardateList.add(startDate_model);
        }else{
            stardateList.add(startDate_model);
        }

        Log.e("stardateList==>123333",String.valueOf(stardateList));

        if (mEventListener!=null){
            mEventListener.onDateClicked(i,map.get("product_id"),stardateList);
        }


        holder.freqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH)+1;
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                Log.e("day==>1",String.valueOf(day));
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                int month=monthOfYear+1;
                                String sMonth = "",sDate = "";
                                if (month < 10) {
                                    sMonth = "0"+month;
                                } else {
                                    sMonth = String.valueOf(month);
                                }

                                if (dayOfMonth < 10) {
                                    sDate = "0"+dayOfMonth;
                                } else {
                                    sDate = String.valueOf(dayOfMonth);
                                }
                                tardetDate=sDate+"-"+sMonth+"-"+year;
                                holder.freqDate.setText(sDate + " " + MONTHS[monthOfYear] + " " + year);

                                StartDate_Model startDate_model=new StartDate_Model();
                                startDate_model.setProdcut_id(map.get("product_id"));
                                startDate_model.setStart_date(tardetDate);
                                startDate_model.setProduct_qty(map.get("qty"));

                                if (stardateList.size()>0){
                                    for (int j=0;j<stardateList.size();j++){
                                        if (stardateList.get(j).getProdcut_id().equals(map.get("product_id"))){
                                            stardateList.remove(j);
                                        }
                                    }
                                    stardateList.add(startDate_model);
                                }else{
                                    stardateList.add(startDate_model);
                                }


                                AddDateToPref(stardateList1);

                                if (mEventListener!=null){
                                    mEventListener.onDateClicked(i,map.get("product_id"),stardateList);
                                }


                            }
                        }, year, month, day);

                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);

                picker.getDatePicker().setMinDate(calendar.getTimeInMillis()/*System.currentTimeMillis() - 1000*/);
                picker.show();
            }
        });


        adapterFreqPlan = new Adapter_FreqPlan(context, planModelList,i);


        PlanSelected_model planSelected_model=new PlanSelected_model();
        planSelected_model.setId(String.valueOf(i));
        planSelected_model.setProduct_name(map.get("product_name"));
        planSelected_model.setProduct_id(map.get("product_id"));

        if (!json.isEmpty()) {
            Type type = new TypeToken<List<PlanSelected_model>>() {}.getType();
            planSelectedModelArrayList1= gson.fromJson(json, type);
            Log.e("planllist1==>frompref",planSelectedModelArrayList1.toString());
            for (int k=0;k<planSelectedModelArrayList1.size();k++){
                if (map.get("product_name").equals(planSelectedModelArrayList1.get(k).getProduct_name())){


                    if (planSelectedModelArrayList1.get(k).getPlan_id().equals("1")){
                        adapterFreqPlan.setSelectedItem(0);
                    }else if (planSelectedModelArrayList1.get(k).getPlan_id().equals("2")){
                        adapterFreqPlan.setSelectedItem(1);
                    }else if (planSelectedModelArrayList1.get(k).getPlan_id().equals("3")){
                        adapterFreqPlan.setSelectedItem(2);
                    }else if (planSelectedModelArrayList1.get(k).getPlan_id().equals("4")){
                        adapterFreqPlan.setSelectedItem(3);
                    }/*else{
                        adapterFreqPlan.setSelectedItem(-1);
                    }*/


                    Log.e("skipppadayyy",planSelectedModelArrayList1.get(k).getSkip_day());
                    planSelected_model.setPlan_id(planSelectedModelArrayList1.get(k).getPlan_id());
                    planSelected_model.setSkip_day(planSelectedModelArrayList1.get(k).getSkip_day());

                    break;


                }else{
                    adapterFreqPlan.setSelectedItem(-1);
                    planSelected_model.setPlan_id("-1");
                    planSelected_model.setSkip_day("0");


                }
            }

        }else{
            adapterFreqPlan.setSelectedItem(-1);
            planSelected_model.setPlan_id("-1");
            planSelected_model.setSkip_day("0");

        }

        if (planSelectedModelArrayList.size()>0){
            for (int j=0;j<planSelectedModelArrayList.size();j++){
                if (planSelectedModelArrayList.get(j).getProduct_id().equals(map.get("product_id"))){

                    planSelectedModelArrayList.remove(j);
                }
            }
            planSelectedModelArrayList.add(planSelected_model);
        }else{
            planSelectedModelArrayList.add(planSelected_model);
        }


        Log.e("planSelectList123",planSelectedModelArrayList.toString());
//        AddPlanToPref(planSelectedModelArrayList);

        if (mEventListener != null)
            mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);

        //  adapterFreqPlan.setSelectedItem(-1);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_frq.setLayoutManager(gridLayoutManager);
        recycler_frq.setAdapter(adapterFreqPlan);


        adapterFreqPlan.setEventListener(new Adapter_FreqPlan.EventListener() {
            @Override
            public void onItemViewClicked(int position, int planId) {

            }

            @Override
            public void onItemViewClicked1(int position, int planId, int position1) {
                plan_idd=planId;
                planId = planModelList.get(position).getId();
                planSkipDay = planModelList.get(position).getSkipDays();
                Log.e("position", String.valueOf(position));
                Log.e("listposition", String.valueOf(i));
                Log.e("startdateee",tardetDate);


                PlanSelected_model planSelected_model=new PlanSelected_model();
                planSelected_model.setId(String.valueOf(i));
                planSelected_model.setProduct_name(map.get("product_name"));
                planSelected_model.setProduct_id(map.get("product_id"));
                planSelected_model.setPlan_id(String.valueOf(planId));
                planSelected_model.setSkip_day(planSkipDay);

                if (planSelectedModelArrayList.size()>0){
                    for (int j=0;j<planSelectedModelArrayList.size();j++){
                        if (planSelectedModelArrayList.get(j).getProduct_id().equals(map.get("product_id"))){

                            planSelectedModelArrayList.remove(j);
                        }
                    }
                    planSelectedModelArrayList.add(planSelected_model);
                }else{
                    planSelectedModelArrayList.add(planSelected_model);
                }

                AddPlanToPref(planSelectedModelArrayList);

                Log.e("planSelectList",planSelectedModelArrayList.toString());
                if (mEventListener != null)
                    mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);

            }


        });

        holder.tvQtyInc.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int count=0;
//                int i = Integer.parseInt(o);
//                i= Integer.parseInt(i+allProductsModel.getPrice());
                if (!holder.tvQty.getText().toString().equalsIgnoreCase("0")) {
                    count = Integer.valueOf(holder.tvQty.getText().toString());
                    count = count + 1;
                    holder.tvQty.setText(String.valueOf(count));
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("product_id", map.get("product_id"));
                    map1.put("product_name", map.get("product_name"));
                    map1.put("category_id", map.get("category_id"));
                    map1.put("product_description", map.get("product_description"));
                    map1.put("price", map.get("price"));
                    map1.put("subscription_price", map.get("subscription_price"));
                    map1.put("gst_subscription_price", map.get("gst_subscription_price") );

                    map1.put("gst_price", map.get("gst_price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    map1.put("qty",holder.tvQty.getText().toString());
                    dbcart.setCart(map, Float.valueOf(holder.tvQty.getText().toString()));

                    if (mEventListener!=null){
                        mEventListener.onQtyClicked(i,map.get("product_id"));
                        //  mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);
                    }


                }
                return false;
            }
        });

        holder.tvQtyDec.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int count;
                count= Integer.valueOf(holder.tvQty.getText().toString());
                if (count > 1) {

                    count = count - 1;
                    holder.tvQty.setText(String.valueOf(count));

                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("product_id", map.get("product_id"));
                    map1.put("product_name", map.get("product_name"));
                    map1.put("category_id", map.get("category_id"));
                    map1.put("product_description", map.get("product_description"));
                    map1.put("price", map.get("price"));
                    map1.put("subscription_price", map.get("subscription_price"));
                    map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                    map1.put("gst_price", map.get("gst_price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    map1.put("qty",holder.tvQty.getText().toString());
                    dbcart.setCart(map, Float.valueOf(holder.tvQty.getText().toString()));
                    if (mEventListener!=null){
                        mEventListener.onQtyClicked(i,map.get("product_id"));
                        //  mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);
                    }

                }
                else if (count==1) {

                    holder.tvQty.setText(String.valueOf(count));
                    if (holder.tvQty.getText().toString().contains("1")){
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("product_id", map.get("product_id"));
                        map1.put("product_name", map.get("product_name"));
                        map1.put("category_id", map.get("category_id"));
                        map1.put("product_description", map.get("product_description"));
                        map1.put("price", map.get("price"));
                        map1.put("subscription_price", map.get("subscription_price"));
                        map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                        map1.put("gst_price", map.get("gst_price"));
                        map1.put("product_image", map.get("product_image"));
                        map1.put("unit", map.get("unit"));
                        map1.put("stock", map.get("stock"));

                        dbcart.setCart(map, Float.valueOf(holder.tvQty.getText().toString()));
                        dbcart.removeItemFromCart(map.get("product_id"));

                        list.remove(i);
                        notifyItemRemoved(i);
                        notifyItemRangeChanged(i, list.size());



                        for (int j=0;j<stardateList.size();j++){
                            if (stardateList.get(j).getProdcut_id().equals(map.get("product_id"))){
                                stardateList.remove(j);
                                break;
                            }
                        }

                        for (int j=0;j<planSelectedModelArrayList.size();j++){
                            if (planSelectedModelArrayList.get(j).getProduct_id().equals(map.get("product_id"))){
                                planSelectedModelArrayList.remove(j);
                                break;
                            }
                        }


                       if (list.size()==0){
                           myEdit1.clear().apply();
                           myEdit2.clear().apply();
                       }

//                        notifyDataSetChanged();
//                        removeAt(i);

                        Log.e("startdate_remove===>1",stardateList.toString());
                        Log.e("planselectedList_remove",planSelectedModelArrayList.toString());

                        AddDateToPref(stardateList);
                        AddPlanToPref(planSelectedModelArrayList);


                        if (mEventListener!=null){
                            mEventListener.onQtyClicked(i,map.get("product_id"));
                            mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);
                        }


                    }
                }
                return false;
            }
        });

    }

    public void AddDateToPref(ArrayList<StartDate_Model> stardateList1){
        if (stardateList1.size()>0){
            myEdit2.clear().apply();
            gson = new Gson();

            Log.e("stardateList1==>pref",stardateList1.toString());
            json1 = gson.toJson(stardateList1);
            myEdit2.putString(STARTDATE_DATA, json1);
            myEdit2.commit();
        }

    }

    private void AddPlanToPref(ArrayList<PlanSelected_model> planSelectedModelArrayList) {
        if (planSelectedModelArrayList.size()>0){
            myEdit1.clear().apply();
            gson = new Gson();

            json = gson.toJson(planSelectedModelArrayList);
            Log.e("planList==>pref",planSelectedModelArrayList.toString());
            myEdit1.putString(STARTDATE_DATA, json);
            myEdit1.commit();
        }
    }

    public String getDate(ArrayList<StartDate_Model> stardateList, HashMap<String, String> map ){
        for (int j=0;j<stardateList.size();j++){
            if (stardateList.get(j).getProdcut_id().equals(map.get("product_id"))){
                tardetDate=stardateList.get(j).getStart_date();
            }
        }
        return tardetDate;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        ImageView ivproImage;

        TextView tvproName,tvProPrice,tvQtyDec,tvQty,tvQtyInc ,freqDate,tvproUnit;


        public holder(@NonNull View itemView) {
            super(itemView);

            ivproImage =itemView.findViewById(R.id.ivproImage);
            tvproName =itemView.findViewById(R.id.tvproName);
            tvProPrice =itemView.findViewById(R.id.tvProPrice);
            tvQtyDec =itemView.findViewById(R.id.tvQtyDec);
            tvQty =itemView.findViewById(R.id.tvQty);
            tvQtyInc =itemView.findViewById(R.id.tvQtyInc);
            freqDate =itemView.findViewById(R.id.freqDate);
            tvproUnit =itemView.findViewById(R.id.tvproUnit);
            recycler_frq =itemView.findViewById(R.id.recycler_frq);

        }
    }

    public interface EventListener {
        void onQtyClicked(int position, String product_id);
        void onDateClicked(int position, String product_id, ArrayList<StartDate_Model> stardateList);

        void onItemViewClicked(int position, String product_id, int plan_id, String date, ArrayList<PlanSelected_model> planSelectedArrayList, ArrayList<StartDate_Model> stardateList);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }





}
