package com.milkdelightuser.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Adapter.Adapter_BestProductList;
import com.milkdelightuser.Model.App_Product_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseActivity;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

@RequiresApi(api = Build.VERSION_CODES.M)
public class CatProductListing extends BaseActivity implements View.OnScrollChangeListener {

    Adapter_BestProductList adapterBestProductList;
    List<App_Product_Model> productModelList;

    RecyclerView recycler_product;
    TextView toolTitle;
    ImageView ivBack;

    RelativeLayout container_null1,rlMain;

    ImageView ivNotify,ivFilter;

    String seeAll,category_id;
    int curPage=1;

    ProgressBar progressBar;
    boolean isLast=false;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_product_listing);

        toolTitle=findViewById(R.id.title);
        toolTitle.setText("Product Listing");

        
        seeAll=getIntent().getStringExtra("seeAll");
        category_id=getIntent().getStringExtra("category_id");
       /* if(!category_id.equals("null")) {
            Log.e("category_id", category_id);
        }*/


        ivBack=findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        ivNotify=findViewById(R.id.ivNotify);
        ivFilter=findViewById(R.id.ivFilter);


        rlMain=findViewById(R.id.rlMain);
        container_null1=findViewById(R.id.container_null1);

        ivNotify.setVisibility(View.VISIBLE);
        ivFilter.setVisibility(View.VISIBLE);

        recycler_product = (RecyclerView) findViewById(R.id.recycler_product);
        recycler_product.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_product.setLayoutManager(gridLayoutManager);

        //Initializing our superheroes list
        productModelList = new ArrayList<>();

        if (isInternetConnected()) {
            try {
                showDialog("");

                getData();

                //Adding an scroll change listener to recyclerview
                recycler_product.setOnScrollChangeListener(CatProductListing.this);
                //initializing our adapter
                adapterBestProductList = new Adapter_BestProductList( this,productModelList);
                //Adding adapter to recyclerview
                recycler_product.setAdapter(adapterBestProductList);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ivNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CatProductListing.this,drawer.class);
                intent.putExtra("notification","product_page");
                startActivity(intent);
            }
        });

        ivFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getApplicationContext(), FilterCatProduct.class);
                intent.putExtra("category_id",category_id);
                startActivityForResult(intent,0);

            }
        });


    }

    private CustomVolleyJsonRequest getDataFromServer(int requestCount) {

         progressBar = (ProgressBar) findViewById(R.id.ProgressBarBottom);

        //Displaying Progressbar
        if (isLast || requestCount==1){
            progressBar.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }

        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley

        Map<String, String> params = new HashMap<String, String>();

        Map<String, String> params2 = new HashMap<String, String>();
        params2.put("category_id", category_id);
        params2.put("page", String.valueOf(requestCount));


        Log.e("params",params.toString());
        String url = null;

        if (category_id!=null){
            url=BaseURL.cat_product_list;
            params=params2;
        }


        Log.e("params",params.toString());
        Log.e("url",url);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();

                Log.e("cat_product_list", response.toString());


                if (category_id!=null){
                    parseData1(response);
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
     //

        //Returning the request
        return jsonObjectRequest;
    }


    private void parseData1(JSONObject response) {

        try {
            String status=response.getString("status");
            String message=response.getString("message");

            if (status.equals("1")){
                JSONObject jsonObject=response.getJSONObject("data");

                JSONObject jsonObject1;
                if (category_id!=null){
                     jsonObject1=jsonObject.getJSONObject("product_list");
                }else{
                     jsonObject1=jsonObject.getJSONObject("base_product");
                }

                String product_url=jsonObject.getString("product_url");
                // int total= Integer.parseInt(jsonObject1.getString("total"));


                JSONArray jsonArray=jsonObject1.getJSONArray("data");

                Log.e("jsonArray", String.valueOf(jsonArray.length()));
                if (jsonArray.length()<10){
                    isLast=true;
                }

                for (int j=0;j<jsonArray.length();j++){
                    JSONObject jsonObject2=jsonArray.getJSONObject(j);

                    String product_id = jsonObject2.getString("product_id");
                    String category_id = jsonObject2.getString("category_id");
                    String product_name = jsonObject2.getString("product_name");
                    String price = jsonObject2.getString("price");
                    String subscription_price = jsonObject2.getString("subscription_price");
                    String qty = jsonObject2.getString("qty");
                    String product_image = jsonObject2.getString("product_image");
                    String description = jsonObject2.getString("description");
                    String stock = jsonObject2.getString("stock");
                    String unit = jsonObject2.getString("unit");
                    String total1 = jsonObject2.getString("total");
                    String mrp = jsonObject2.getString("mrp");

                    String product_review_count = jsonObject2.getString("product_review_count");

                    String ratting = null;
                    JSONArray jsonArray1=jsonObject2.getJSONArray("product_ratting");
                    if (jsonArray1.length()!=0){
                        for (int k =0;k<jsonArray1.length();k++){
                            JSONObject jsonObject3=jsonArray1.getJSONObject(k);
                            ratting=jsonObject3.getString("rating");
                        }
                    }else{
                        ratting="0";
                    }

                    App_Product_Model appProductModel=new App_Product_Model();
                    appProductModel.setProduct_id(product_id);
                    appProductModel.setProduct_name(product_name);
                    appProductModel.setPrice(price);
                    appProductModel.setSubscription_price(subscription_price);
                    appProductModel.setQty(qty);
                    appProductModel.setProduct_image(product_url+product_image);
                    appProductModel.setDescription(description);
                    appProductModel.setStock(stock);
                    appProductModel.setUnit(unit);
                    appProductModel.setTotal(total1);
                    appProductModel.setRate(ratting);
                    appProductModel.setReview_count(product_review_count);
                    appProductModel.setMrp(mrp);

                    productModelList.add(appProductModel);
                    Log.e("productModelList0", String.valueOf(productModelList.size()));

                }

                //Notifying the adapter that data has been added or changed
                adapterBestProductList.notifyDataSetChanged();

            }else if (status.contains("0")) {
                container_null1.setVisibility(View.VISIBLE);
                rlMain.setVisibility(View.GONE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void getData() {

        String tag_json_obj = "json store req";

        AppController.getInstance().addToRequestQueue(getDataFromServer(curPage), tag_json_obj);
       // requestQueue.add(getDataFromServer(curPage));
        //Incrementing the request counter
        if (!isLast){
            curPage++;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if(resultCode == Activity.RESULT_OK){
                String filter_type=data.getStringExtra("filter_type");
                String items_size=data.getStringExtra("items_size");
                String items_unit=data.getStringExtra("items_unit");
                showDialog(" ");
                getData1(filter_type,items_size,items_unit);


            }
            if (resultCode == Activity.RESULT_CANCELED) {

                category_id=data.getStringExtra("category_id");
                Log.e("category_id",category_id);

                Intent intent=new Intent(CatProductListing.this, CatProductListing.class);
                intent.putExtra("category_id",category_id);
                startActivity(intent);
                finish();
            }
        }
    }//

    private void getData1(String filter_type, String items_size, String items_unit) {

//        productModelList.clear();
//        productModelList=new ArrayList<>();


        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("filter_type", filter_type.toLowerCase());
        params.put("items_size", items_size);
        params.put("items_unit", items_unit);
        params.put("category_id", category_id);

        Log.e("getFilterData",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.category_filter, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("Tfilterdata", response.toString());
                productModelList = new ArrayList();

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){
                        container_null1.setVisibility(View.GONE);
                        rlMain.setVisibility(View.VISIBLE);

                        JSONObject jsonObject1=response.getJSONObject("data");
                        String product_url=jsonObject1.getString("product_url");
                        JSONArray jsonArray=jsonObject1.getJSONArray("product_list");

                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            String product_id = jsonObject.getString("product_id");
                            String category_id = jsonObject.getString("category_id");
                            String product_name = jsonObject.getString("product_name");
                            String price = jsonObject.getString("price");
                            String subscription_price = jsonObject.getString("subscription_price");
                            String qty = jsonObject.getString("qty");
                            String product_image = jsonObject.getString("product_image");
                            String description = jsonObject.getString("description");
                            String stock = jsonObject.getString("stock");
                            String unit = jsonObject.getString("unit");
                            String total1 = jsonObject.getString("total");
                            String mrp = jsonObject.getString("mrp");
                            String product_review_count = jsonObject.getString("product_review_count");

                            String ratting = null;
                            JSONArray jsonArray1=jsonObject.getJSONArray("product_ratting");
                            if (jsonArray1.length()>0){
                                for (int j =0;j<jsonArray1.length();j++){
                                    JSONObject jsonObject2=jsonArray1.getJSONObject(j);
                                    ratting=jsonObject2.getString("rating");
                                }
                            }else{
                                ratting="0.0";
                            }

                            App_Product_Model appProductModel=new App_Product_Model();
                            appProductModel.setProduct_id(product_id);
                            appProductModel.setProduct_name(product_name);
                            appProductModel.setPrice(price);
                            appProductModel.setSubscription_price(subscription_price);
                            appProductModel.setQty(qty);
                            if (product_image.equals("null")){

                            }else{
                                appProductModel.setProduct_image(product_url+product_image);
                            }

                            appProductModel.setDescription(description);
                            appProductModel.setStock(stock);
                            appProductModel.setUnit(unit);
                            appProductModel.setTotal(total1);
                            appProductModel.setMrp(mrp);
                            appProductModel.setRate(ratting);
                            appProductModel.setReview_count(product_review_count);
                            appProductModel.setMrp(mrp);

                            productModelList.add(appProductModel);


                        }

                        Log.e("productModelList1234", String.valueOf(productModelList.size()));

                      //  notifyDataSetChanged();

                        adapterBestProductList.updateData(productModelList);
                        adapterBestProductList.notifyDataSetChanged();


                    }else if (status.contains("0")){
                        container_null1.setVisibility(View.VISIBLE);
                        rlMain.setVisibility(View.GONE);

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);

    }

    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }


    @Override
    public void onScrollChange(View view, int i, int i1, int i2, int i3) {
        if (isLastItemDisplaying(recycler_product)) {
            getData();
        }

    }


}
