package com.milkdelightuser.Adapter;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.Model.PlanSelected_model;
import com.milkdelightuser.Model.Plan_model;
import com.milkdelightuser.Model.StartDate_Model;
import com.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;
import com.milkdelightuser.utils.Session_management;

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
import static com.milkdelightuser.utils.Global.MY_SUBSCRIPTION_PREFS_NAME;
import static com.milkdelightuser.utils.Global.SUB_DATA;


public class Adapter_SubProduct extends RecyclerView.Adapter<Adapter_SubProduct.holder> {

    Context context;
    ArrayList<HashMap<String, String>> list;
    List<Plan_model> planModelList;
    ArrayList<PlanSelected_model> planSelectedModelArrayList;
    ArrayList<StartDate_Model> stardateList;
    ArrayList<SubscriptioAddProduct_model> subProductList;
    ClickPosition clickPosition;

    RecyclerView recycler_frq;
    Adapter_FreqPlan adapterFreqPlan;

    private DatabaseHandler dbcart;

    String tardetDate;

   Session_management session_management;
   String user_id,planSkipDay;
    int plan_idd;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor myEdit;


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

        session_management=new Session_management(context);
        user_id = session_management.getUserDetails().get(BaseURL.KEY_ID);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart3, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final HashMap<String, String> map = list.get(i);
        dbcart = new DatabaseHandler(context);

        sharedPreferences = context.getSharedPreferences(MY_SUBSCRIPTION_PREFS_NAME, MODE_PRIVATE);
        myEdit = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString(SUB_DATA, "");

        Log.e("productimage",map.get("product_image"));
        Glide.with(context)
                .load(map.get("product_image"))

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.ivproImage);

        holder.tvproName.setText(map.get("product_name")+" ("+map.get("unit")+")");
//        holder.tvProPrice.setText("₹ "+map.get("price"));
        holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(map.get("price"))+ Math.round( Global.getTax(context, Double.valueOf(map.get("price"))))));

        holder.tvQty.setText(dbcart.getCartItemQty(map.get("product_id")));

        Log.e("product_iddd",map.get("product_id"));


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
                        map1.put("product_image", map.get("product_image"));
                        map1.put("unit", map.get("unit"));
                        map1.put("stock", map.get("stock"));

                        dbcart.setCart(map, Float.valueOf(holder.tvQty.getText().toString()));
                        dbcart.removeItemFromCart(map.get("product_id"));

                        list.remove(i);

                        if (mEventListener!=null){
                            mEventListener.onQtyClicked(i,map.get("product_id"));
                            //  mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);
                        }

                        notifyDataSetChanged();


                    }
                }
                return false;
            }
        });


        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String tardetDate12 = df.format(c);

        SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        tardetDate = df1.format(c);

        StartDate_Model startDate_model=new StartDate_Model();
        startDate_model.setProdcut_id(map.get("product_id"));

        startDate_model.setProduct_qty(map.get("qty"));
//        startDate_model.setProduct_price(dbcart.getTotalAmountById(map.get("product_id")));



        if (!json.isEmpty()) {
            Type type = new TypeToken<List<SubscriptioAddProduct_model>>() {}.getType();
            subProductList= gson.fromJson(json, type);
            Log.e("subProductList_frompref",subProductList.toString());
            for (int k=0;k<subProductList.size();k++){
                if (map.get("product_name").equals(subProductList.get(k).getProduct_name())){
                    Log.e("startdateeee",subProductList.get(k).getStart_date());
                    try {
                        holder.freqDate.setText(Global.getDateConvert(subProductList.get(k).getStart_date(),"yyyy-MM-dd","dd MMM yyyy"));
                        startDate_model.setStart_date(Global.getDateConvert(subProductList.get(k).getStart_date(),"yyyy-MM-dd","dd-MM-yyyy"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }else{
            holder.freqDate.setText(tardetDate12);
            startDate_model.setStart_date(tardetDate);
        }

        stardateList.add(startDate_model);
        Log.e("stardateList123",stardateList.toString());

        holder.freqDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                holder.freqDate.setText(dayOfMonth + " " + MONTHS[monthOfYear] + " " + year);
                                int month=monthOfYear+1;
                                String sMonth = "";
                                if (month < 10) {
                                    sMonth = "0"+month;
                                } else {
                                    sMonth = String.valueOf(month);
                                }
                                tardetDate=dayOfMonth+"-"+sMonth+"-"+year;
                                Log.e("tardetDate",tardetDate);

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

                                Log.e("stardateList",stardateList.toString());

                                if (mEventListener!=null){
                                    mEventListener.onDateClicked(i,map.get("product_id"),stardateList);
                                    //  mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);
                                }


                            }
                        }, year, month, day);
                picker.show();
            }
        });



        adapterFreqPlan = new Adapter_FreqPlan(context, planModelList,i);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_frq.setLayoutManager(gridLayoutManager);
        recycler_frq.setAdapter(adapterFreqPlan);

        if (!json.isEmpty()) {
            Type type = new TypeToken<List<SubscriptioAddProduct_model>>() {}.getType();
            subProductList= gson.fromJson(json, type);
            for (int k=0;k<subProductList.size();k++){
                if (map.get("product_name").equals(subProductList.get(k).getProduct_name())){

                        Log.e("plannnnnnidddd", subProductList.get(k).getPlan_id());
                        //  Log.e("plannnnnnidddd11111", subProductList.get(i).getPlan_id());
                        Log.e("proname", subProductList.get(k).getProduct_name());
                        Log.e("proname11111", map.get("product_name"));

                        if (subProductList.get(k).getPlan_id().equals("1")){
                            adapterFreqPlan.setSelectedItem(0);
                        }else if (subProductList.get(k).getPlan_id().equals("2")){
                            adapterFreqPlan.setSelectedItem(1);
                        }if (subProductList.get(k).getPlan_id().equals("3")){
                            adapterFreqPlan.setSelectedItem(2);
                        }if (subProductList.get(k).getPlan_id().equals("4")){
                            adapterFreqPlan.setSelectedItem(3);
                        }

                }/*else{
                    adapterFreqPlan.setSelectedItem(-1);
                }*/
            }

        }else{
            adapterFreqPlan.setSelectedItem(-1);
        }


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
                planSelected_model.setStart_date(getDate(stardateList,map));
                planSelected_model.setProduct_name(map.get("product_name"));

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

                String modelClass = new Gson().toJson(planSelected_model);

                Log.e("planSelectList",planSelectedModelArrayList.toString());
                if (mEventListener != null)
                    mEventListener.onItemViewClicked(i,map.get("product_id"),plan_idd,getDate(stardateList,map),planSelectedModelArrayList,stardateList);

            }


        });

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

        TextView tvproName,tvProPrice,tvQtyDec,tvQty,tvQtyInc ,freqDate;


        public holder(@NonNull View itemView) {
            super(itemView);

            ivproImage =itemView.findViewById(R.id.ivproImage);
            tvproName =itemView.findViewById(R.id.tvproName);
            tvProPrice =itemView.findViewById(R.id.tvProPrice);
            tvQtyDec =itemView.findViewById(R.id.tvQtyDec);
            tvQty =itemView.findViewById(R.id.tvQty);
            tvQtyInc =itemView.findViewById(R.id.tvQtyInc);
            freqDate =itemView.findViewById(R.id.freqDate);
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

    public interface ClickPosition {
        public void getPosition(int position);
    }
}
