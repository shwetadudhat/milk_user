package com.milkdelightuser.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.milkdelightuser.Model.AppCategory_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.ConnectivityReceiver;
import com.milkdelightuser.utils.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Shop_by_category extends AppCompatActivity {

    List<AppCategory_Model> appCategoryModels = new ArrayList<>();
    RecyclerView app_cat_list;
    Context context;

    String cityadmin_id, category_id;

    Button back;
    SharedPreferences sharedPreferences,cityAdminprefrence;
    SharedPreferences.Editor editor,editorprefrences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_by_category);


        sharedPreferences = getSharedPreferences("product_category", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        cityAdminprefrence = getSharedPreferences("cityAdmin", MODE_PRIVATE);
        editorprefrences = cityAdminprefrence.edit();

        cityadmin_id = cityAdminprefrence.getString("cityadmin_id", "");

        category_id = sharedPreferences.getString("category_id", "");


        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        app_cat_list = (RecyclerView) findViewById(R.id.recycler_shop);
        app_cat_list.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));

        app_cat_list.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), app_cat_list, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AppCategory_Model catagoryModel = appCategoryModels.get(position);
//
                String s = catagoryModel.getCategory_id();

                Intent intent = new Intent(getApplicationContext(), Product.class);

                editor=cityAdminprefrence.edit();

                editor.putString("category_id",s);
                Log.d("sdf",s);
                editor.commit();
                startActivity(intent);
            }
            //
            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        if (ConnectivityReceiver.isConnected()) {

          // appcategory();
        } else{


        }
    }

   /* private void appcategory() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("cityadmin_id", cityadmin_id);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.appcategory, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag", response.toString());

                try {

                    String status = response.getString("status");
                    String message = response.getString("message");

                    if (status.contains("1")) {
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String city_name = jsonObject.getString("category_name");
                            String city_image = jsonObject.getString("category_image");
                            String city_id = jsonObject.getString("category_id");


                            AppCategory_Model categoryModel = new AppCategory_Model();
                            categoryModel.setCategory_name(city_name);
                           // categoryModel.setCategory_image(Integer.parseInt(city_image));
                            categoryModel.setCategory_image(city_image);
                            categoryModel.setCategory_id(city_id);

                            appCategoryModels.add(categoryModel);

                            AppCategory_Adapter appCategory_adapter = new AppCategory_Adapter(appCategoryModels);
                            app_cat_list.setAdapter(appCategory_adapter);

                         //   Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                        }
                    } else {

//                        Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //         Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }*/
}
