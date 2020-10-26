package com.webzino.milkdelightuser.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_Prosize;
import com.webzino.milkdelightuser.Model.App_Product_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseActivity;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.Global.MY_FILTER_PREFS_NAME;

public class FilterCatProduct extends BaseActivity implements AdapterView.OnItemClickListener{

    RecyclerView recycler_prosize;
    ImageView ivClose;
    BetterSpinner edSortType;

    LinearLayout ll_clear,ll_apply;
    Adapter_Prosize adapterProsize;

    // ArrayList<String> stringArrayList;
    ArrayList<App_Product_Model> stringArrayList;
    String selestedItem;
    String itemtype1="",select_id;
    int index;
    int row_index=-1,positionn=-1;

    String category_id;

    SharedPreferences settings ;
    SharedPreferences.Editor editor;
    boolean isSelectedd;
    String filterData,sortData,seeAll;
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
        select_id=settings.getString("select_id","");
        category_id=getIntent().getStringExtra("category_id");



        stringArrayList=new ArrayList<>();
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, sort_type);



        if(edSortType != null) {

            edSortType.setAdapter(adapter);
            if (isSelectedd){
                if (!select_id.equals(-1)){
                    positionn=Integer.parseInt(select_id);
                }
                if (sortData!=null){
                    selestedItem=sortData;
                }
                if (filterData!=null){
                    itemtype1=filterData;
                }

                edSortType.setText(sortData);
            }else {
                edSortType.setSelection(0);
            }

            edSortType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    index=i;
                    Object item = adapterView.getItemAtPosition(+i);
                    selestedItem= String.valueOf(item);
                    Log.e("selestedItem",selestedItem);
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
                if (sortData==null){
                    sortData="";
                }
                if (itemtype1==null){
                    itemtype1="";
                }
                editor.putBoolean("locked", true);

                if (selestedItem==null){
                    selestedItem="";
                    editor.putString("sort_data", selestedItem);

                }else if(selestedItem!=null){
                    editor.putString("sort_data",selestedItem);
                }else if (isSelectedd){
                    sortData=settings.getString("sort_data","");
                    if (sortData!=null){
                        editor.putString("sort_data",sortData);
                        selestedItem=sortData;
                    }else{
                        editor.putString("sort_data",selestedItem);
                    }
                }
                editor.putString("select_id",String.valueOf(positionn));
                editor.putString("select",itemtype1);
                editor.apply();

                if (isSelectedd){
                    sortData=settings.getString("sort_data","");
                    select_id=settings.getString("select_id","");
                    positionn=Integer.parseInt(select_id);
                }
                /*select_id*/


                Intent returnIntent = new Intent();
                returnIntent.putExtra("filter_type",selestedItem);
                returnIntent.putExtra("seeAll",seeAll);

                if (itemtype1.equals("")){
                    returnIntent.putExtra("items_size","");
                    returnIntent.putExtra("items_unit","");
                }else{
                    String[] splited = itemtype1.split("\\s+");
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
                edSortType.setText(getString(R.string.select_sort_type));
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
                Intent backIntent = new Intent();
                backIntent.putExtra("category_id",category_id);
                setResult(RESULT_CANCELED, backIntent);
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

                            recycler_prosize.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));

                            Log.e("stringArrayList123", String.valueOf(stringArrayList));
                            adapterProsize=new Adapter_Prosize(getApplicationContext(),stringArrayList);
                            recycler_prosize.setAdapter(adapterProsize);

                            adapterProsize.setEventListener(new Adapter_Prosize.EventListener() {
                                @Override
                                public void onItemViewClicked(int position, String itemtype) {
                                    positionn=position;
                                    itemtype1=itemtype;
                                    Log.e("itemtypee",itemtype1);
                                }
                            });

                            if (isSelectedd){
                                select_id=settings.getString("select_id","");
                                Log.e("select_id",""+select_id);

                                if (select_id.equals("")){
                                    adapterProsize.setSelectedItem(-1);
                                }else{
                                    adapterProsize.setSelectedItem(Integer.parseInt(select_id));
                                }
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
                // Toast.makeText(FilterCatProduct.this, "" + error, Toast.LENGTH_SHORT).show();
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
}
