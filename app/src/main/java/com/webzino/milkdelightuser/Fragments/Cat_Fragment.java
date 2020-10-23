package com.webzino.milkdelightuser.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_HomeCat;
import com.webzino.milkdelightuser.Model.AppCategory_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseFragment;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.ConnectivityReceiver;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;

public class Cat_Fragment extends BaseFragment {

    RecyclerView Recycler_cat;
    List<AppCategory_Model> appCategoryModelList;
    Adapter_HomeCat adapterHomeCat;

    RelativeLayout container_null1;

    public Cat_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        container_null1=view.findViewById(R.id.container_null1);
        Recycler_cat=view.findViewById(R.id.Recycler_cat);
        appCategoryModelList=new ArrayList<>();

        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            cateGoryData();
        } else {
            Global.showInternetConnectionDialog(getContext());

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cat, container, false);

        return view;

    }

    private void cateGoryData() {

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.category, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("category");
                        String cat_url=jsonObject.getString("category_url");
                        if (jsonArray.length()==0){
                            container_null1.setVisibility(View.VISIBLE);
                            Recycler_cat.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            container_null1.setVisibility(View.GONE);
                            Recycler_cat.setVisibility(View.VISIBLE);
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                String category_id=jsonObject1.getString("id");
                                String category_name=jsonObject1.getString("category_name");
                                String category_image=jsonObject1.getString("category_image");

                                AppCategory_Model appCategoryModel=new AppCategory_Model();
                                appCategoryModel.setCategory_id(category_id);
                                appCategoryModel.setCategory_name(category_name);
                                appCategoryModel.setCategory_image(cat_url+category_image);


                                appCategoryModelList.add(appCategoryModel);

                            }
                            adapterHomeCat=new Adapter_HomeCat(getContext(),appCategoryModelList);

                            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),3);
                            gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            Recycler_cat.setLayoutManager(gridLayoutManager);
                          //  Recycler_cat.addItemDecoration(new MemberItemDecoration());
                            Recycler_cat.setAdapter(adapterHomeCat);

                        }
                    }else if (status.contains("0")){
                        container_null1.setVisibility(View.VISIBLE);
                        Recycler_cat.setVisibility(View.GONE);
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
            //    Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


}










