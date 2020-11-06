package com.webzino.milkdelightuser.Fragments.MainFragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_FAQ;
import com.webzino.milkdelightuser.Model.Faq_Model;
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
public class Faq_Fragment extends BaseFragment {

    RecyclerView Faq_List;
    TextView tvPolicy;
    Context context;
    Adapter_FAQ adapter_faq;
    List<Faq_Model> faq_modelList;

    public Faq_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq_, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Faq_List = (RecyclerView)view.findViewById(R.id.Recycler_faq);
        tvPolicy = view.findViewById(R.id.tvPolicy);
        Faq_List.setLayoutManager(new GridLayoutManager(getContext(),1));

        faq_modelList=new ArrayList<>();


        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            faq();
            privacyPolicy();
        } else {
            Global.showInternetConnectionDialog(getContext());

        }

    }




    private void privacyPolicy() {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.privacy_policy, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(getContext(), "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String description=jsonObject.getString("description");


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    tvPolicy.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
                                } else {
                                    tvPolicy.setText(Html.fromHtml(description));
                                }


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
                 Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);


    }

    private void faq(){

        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest( BaseURL.faq, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("faq123", response.toString());
                try {
                    String status=response.getString("status");
                    String message=response.getString("message");


                    if (status.equals("1")){
                        JSONArray jsonArray = response.getJSONArray("data");
                        if (jsonArray.length()==0){
                            Toast.makeText(getContext(), "0 data found", Toast.LENGTH_SHORT).show();
                        }else{
                            for (int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject=jsonArray.getJSONObject(i);
                                String faq_id=jsonObject.getString("id");
                                String question=jsonObject.getString("question");
                                String answer=jsonObject.getString("answer");

                                faq_modelList.add(new Faq_Model(faq_id,question,answer));



                            }

                            adapter_faq=new Adapter_FAQ(context,faq_modelList);
                            Faq_List.setAdapter(adapter_faq);
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
                Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);



    }

}
