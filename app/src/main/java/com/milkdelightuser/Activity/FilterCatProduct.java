package com.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Model.App_Product_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.milkdelightuser.utils.Global.MY_FILTER_PREFS_NAME;

public class FilterCatProduct extends BaseActivity implements AdapterView.OnItemClickListener{

    RecyclerView recycler_prosize;
    ImageView ivClose;
    BetterSpinner edSortType;

    LinearLayout ll_clear,ll_apply;
    Adapter_Prosize adapterProsize;

    // ArrayList<String> stringArrayList;
    ArrayList<App_Product_Model> stringArrayList;
    String selestedItem;
    String itemtype="";
    int index,select_id;
    int row_index=-1;
    String category_id;


    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    boolean isSelectedd;
    String filterData,sortData;
    ArrayAdapter<String> adapter;


    private static final String[] sort_type = new String[] {
            "ASC", "DESC"
    };


    @SuppressLint("CommitPrefEdits")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_filter_product);

        category_id=getIntent().getStringExtra("category_id");

        ll_apply=findViewById(R.id.ll_apply);
        ll_clear=findViewById(R.id.ll_clear);
        edSortType=findViewById(R.id.edSortType);
        ivClose=findViewById(R.id.ivClose);
        recycler_prosize=findViewById(R.id.recycler_prosize);


        settings = getApplicationContext().getSharedPreferences(MY_FILTER_PREFS_NAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        isSelectedd = settings.getBoolean("locked", false);
        filterData=settings.getString("select","");
        sortData=settings.getString("sort_data","");
        select_id=settings.getInt("select_id",0);



        Log.e("select",filterData);
        Log.e("sortData123",sortData);
        Log.e("select_id",""+select_id);



        stringArrayList=new ArrayList<>();

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sort_type);

        if (isSelectedd){
            edSortType.setText(sortData);
        }

        if(edSortType != null) {

            edSortType.setAdapter(adapter);

            edSortType.setSelection(0);

            edSortType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    index=i;
                    Log.e("index",""+i);
                    Object item = adapterView.getItemAtPosition(+i);
                    Log.e   ("index123", String.valueOf(item));
                    selestedItem= String.valueOf(item);
                    Log.e("selestedItem",selestedItem);
                    editor.putBoolean("locked", true);
                    editor.putString("sort_data",selestedItem);
                    editor.apply();

                    if (isSelectedd){
                        sortData=settings.getString("sort_data","");
                        Log.e(" sortData11111",sortData);
                    }
                    // Toast.makeText(getBaseContext(), sort_type[index], Toast.LENGTH_SHORT).show();
                }
            });


        }


        if (isInternetConnected()) {
            try {
                showDialog("");

                getProductSizeData();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        ll_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //   Log.e("selestedItem",selestedItem);
                Log.e("sortData123",sortData);
                Log.e("sortData","sortData");

                Intent returnIntent = new Intent();


                if (filterData.equals("")){
                    returnIntent.putExtra("filter_type",sortData);

                    returnIntent.putExtra("items_size","");
                    returnIntent.putExtra("items_unit","");
                }else{

                    Log.e("itemtype",filterData);

                    String[] splited = filterData.split("\\s+");
                    Log.e("splited",splited[0]);
                    Log.e("splited2",splited[1]);

                    returnIntent.putExtra("filter_type",sortData);
                    returnIntent.putExtra("items_size",splited[0]);
                    returnIntent.putExtra("items_unit",splited[1]);
                }

                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        ll_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                /*product size listing data  */
                row_index=-1;
                adapterProsize.setSelectedItem(row_index);
                adapterProsize.notifyDataSetChanged();

                /*   spinner data*/
                edSortType.setText("Select Sort Type");
                adapter = new ArrayAdapter<String>(FilterCatProduct.this,
                        android.R.layout.simple_dropdown_item_1line, sort_type);
                edSortType.setAdapter(adapter);

                editor.clear().apply();



                Intent backIntent = new Intent();
                backIntent.putExtra("category_id",category_id);
                setResult(RESULT_CANCELED, backIntent);
                finish();


            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void getProductSizeData(){
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.product_size, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("state", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    Toast.makeText(FilterCatProduct.this, message, Toast.LENGTH_SHORT).show();

                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(FilterCatProduct.this, "0 data found", Toast.LENGTH_SHORT).show();

                        }else{
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String qty=jsonObject1.getString("qty");
                                String unit=jsonObject1.getString("unit");

                                App_Product_Model appProductModel=new App_Product_Model();
                                appProductModel.setQty(qty);
                                appProductModel.setUnit(unit);


                                stringArrayList.add(appProductModel);

                            }

                            Log.e("stringArrayList", String.valueOf(stringArrayList));
                            recycler_prosize.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));

                            Log.e("stringArrayList123", String.valueOf(stringArrayList));
                            adapterProsize=new Adapter_Prosize(getApplicationContext(),stringArrayList);
                            recycler_prosize.setAdapter(adapterProsize);


                            if (isSelectedd){
                                filterData=settings.getString("select","");
                                Log.e("filterData123",filterData);
                                Log.e("select_id",""+select_id);

                                adapterProsize.setSelectedItem(select_id);
                                adapterProsize.notifyDataSetChanged();

                            }

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
                // Toast.makeText(FilterProduct.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        index = adapterView.getSelectedItemPosition();
        Log.e   ("index32332", String.valueOf(index));
        Toast.makeText(getBaseContext(), sort_type[index], Toast.LENGTH_SHORT).show();

    }

    public class Adapter_Prosize extends RecyclerView.Adapter<Adapter_Prosize.holder> {

        Context context;
        ArrayList<App_Product_Model> stringArrayList;


        public Adapter_Prosize(Context context, ArrayList<App_Product_Model> stringArrayList) {
            this.context = context;
            this.stringArrayList = stringArrayList;



        }

        public int getSelectedItem() {
            return row_index;
        }
        public void setSelectedItem(int selectedItem) {
            row_index = selectedItem;
            Log.e("clearrr11","clear"+row_index);
        }

        @NonNull
        @Override
        public Adapter_Prosize.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            View view = inflater.inflate(R.layout.listitem_productsize, null, false);
            context = viewGroup.getContext();
            return new Adapter_Prosize.holder(view);    }

        @Override
        public void onBindViewHolder(@NonNull final Adapter_Prosize.holder holder, int i) {


            isSelectedd = settings.getBoolean("locked", false);
            if (isSelectedd){
                filterData=settings.getString("select","");
                Log.e("filterData",filterData);
                if (stringArrayList.get(i).equals(filterData)){
                    row_index=i;
                    Log.e("row_index",""+row_index);
                }
            }

            holder.tvProsize.setText(stringArrayList.get(i).getQty()+" "+stringArrayList.get(i).getUnit());
            holder.tvProsize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    row_index=i;
                    notifyDataSetChanged();
                    itemtype=holder.tvProsize.getText().toString();
                    editor.putBoolean("locked", true);
                    editor.putInt("select_id", i);
                    editor.putString("select",itemtype);
                    editor.apply();

                }
            });
            if(row_index==i){
                holder.tvProsize.setBackground(getResources().getDrawable(R.drawable.home_add));
                holder.tvProsize.setTextColor(getResources().getColor(R.color.main_clr));
                setTypeFaceBold(holder.tvProsize);
                // editor.putBoolean("locked", true).apply();
            }
            else
            {
                holder.tvProsize.setBackground(getResources().getDrawable(R.drawable.bg_grey));
                holder.tvProsize.setTextColor(getResources().getColor(R.color.grey));
                setTypeFaceRegular(holder.tvProsize);
                // editor.putBoolean("locked", false).apply();
            }

            if (row_index==-1){
                Log.e("clearrr","clear");
                holder.tvProsize.setBackground(getResources().getDrawable(R.drawable.bg_grey));
                holder.tvProsize.setTextColor(getResources().getColor(R.color.grey));
                setTypeFaceRegular(holder.tvProsize);
                editor.clear();
                editor.apply();
            }
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();

        }

        public class holder extends RecyclerView.ViewHolder {

            TextView tvProsize;

            public holder(@NonNull View itemView) {
                super(itemView);

                tvProsize =itemView.findViewById(R.id.tvProsize);

            }
        }
    }


}
