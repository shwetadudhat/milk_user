package com.webzino.milkdelightuser.Fragments.MainFragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_Offer;
import com.webzino.milkdelightuser.Model.Offer_Model;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Offer_Fragment extends BaseFragment {

    RecyclerView recycle_offer;
    Context context;
    Adapter_Offer adapterOffer;
    List<Offer_Model> offerModelList;

    RelativeLayout container_null1;

    public Offer_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        container_null1 = view.findViewById(R.id.container_null1);
        recycle_offer = (RecyclerView)view.findViewById(R.id.recycle_offer);
        recycle_offer.setLayoutManager(new GridLayoutManager(getContext(),1));



        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            myOffer();
        } else {
            Global.showInternetConnectionDialog(getContext());

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_offer, container, false);



        return view;
    }

    private void myOffer() {
        offerModelList=new ArrayList<>();

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.offer, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("ofer123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            container_null1.setVisibility(View.VISIBLE);
                            recycle_offer.setVisibility(View.GONE);
                            Toast.makeText(getContext(), "0 data found", Toast.LENGTH_SHORT).show();
                        }else{

                            container_null1.setVisibility(View.GONE);
                            recycle_offer.setVisibility(View.VISIBLE);
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String id=jsonObject.getString("id");
                                String title=jsonObject.getString("title");
                                String description=jsonObject.getString("description");
                                String image=jsonObject.getString("image");
                                String expire_date=jsonObject.getString("expire_date");

                                offerModelList.add(new Offer_Model(id,title,description,expire_date,image));

                            }
                            adapterOffer=new Adapter_Offer(getContext(),offerModelList);
                            recycle_offer.setAdapter(adapterOffer);

                        }
                    }else if (status.contains("0")){
                        container_null1.setVisibility(View.VISIBLE);
                        recycle_offer.setVisibility(View.GONE);
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
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);

    }



}
