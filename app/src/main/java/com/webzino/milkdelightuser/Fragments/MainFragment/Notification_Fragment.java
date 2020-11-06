package com.webzino.milkdelightuser.Fragments.MainFragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_Notification;
import com.webzino.milkdelightuser.Model.Notification_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseFragment;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Session_management;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;
import static com.webzino.milkdelightuser.utils.Global.MY_NOTIFICATION_PREFS_NAME;
import static com.webzino.milkdelightuser.utils.Global.NOTIFICATION_DATA;


/**
 * A simple {@link Fragment} subclass.
 */
public class Notification_Fragment extends BaseFragment {

    RecyclerView recycle_notification;
    LinearLayout ll_no_notification;
    Adapter_Notification notificationAdapter;
    List<Notification_Model> notificationModelList;

    Session_management sessionManagement;
    String u_id;

    SharedPreferences pref;
    String json_array;

    public Notification_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycle_notification=view.findViewById(R.id.recycle_notification);/*visibility*/
        ll_no_notification=view.findViewById(R.id.ll_no_notification);/*visibility*/
        recycle_notification.setLayoutManager(new GridLayoutManager(getContext(),1));
        notificationModelList=new ArrayList<>();

       /* sessionManagement=new Session_management(getContext());
        u_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);*/

        pref = this.getActivity().getSharedPreferences(MY_NOTIFICATION_PREFS_NAME, MODE_PRIVATE);
        json_array = pref.getString(NOTIFICATION_DATA, null);
        if (json_array == null) {
            // the key does not exist
            Log.e("iffff","ifff");
            ll_no_notification.setVisibility(View.VISIBLE);
            recycle_notification.setVisibility(View.GONE);
        } else {
            // handle the value
            try {
                JSONArray jsoArray=new JSONArray(json_array);
                Log.e("jsoArray",jsoArray.toString());
                ll_no_notification.setVisibility(View.GONE);
                recycle_notification.setVisibility(View.VISIBLE);


                for (int i=0;i<jsoArray.length();i++){
                    JSONObject jsonObject=jsoArray.getJSONObject(i);
                    String start_date=jsonObject.getString("start_date");
                    String end_date=jsonObject.getString("end_date");
                    String subs_id=jsonObject.getString("subs_id");

                    Notification_Model notificationModel=new Notification_Model();
                    notificationModel.setNotification_id(subs_id);
                    notificationModel.setOrder_type("Subscribe");
                    notificationModel.setTime(start_date);
                    notificationModel.setEnd_date(end_date);


                    notificationModelList.add(notificationModel);
                }

                notificationAdapter=new Adapter_Notification(getContext(),notificationModelList);
                recycle_notification.setLayoutManager(new LinearLayoutManager(getContext()));
                recycle_notification.setAdapter(notificationAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }






/*
        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            notification(u_id);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        return view;
    }

    private void notification(String u_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.notification_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("notificationlist123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        ll_no_notification.setVisibility(View.GONE);
                        recycle_notification.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();


                        JSONArray jsonArray=response.getJSONArray("data");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String id=jsonObject1.getString("id");
                            String order_type=jsonObject1.getString("order_type");
                            String order_create_time=jsonObject1.getString("order_create_time");
                            String created_at=jsonObject1.getString("created_at");
                            String end_date=jsonObject1.getString("end_date");


                            Notification_Model notificationModel=new Notification_Model();
                            notificationModel.setNotification_id(id);
                            notificationModel.setOrder_type(order_type);
                            notificationModel.setTime(created_at);
                            notificationModel.setEnd_date(end_date);


                            notificationModelList.add(notificationModel);


                        }


                        notificationAdapter=new Adapter_Notification(getContext(),notificationModelList);
                        recycle_notification.setLayoutManager(new LinearLayoutManager(getContext()));
                        recycle_notification.setAdapter(notificationAdapter);



                    }else if (status.equals("0")){

                        ll_no_notification.setVisibility(View.VISIBLE);
                        recycle_notification.setVisibility(View.GONE);

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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
      /*  notificationModelList=new ArrayList<>();
        notificationModelList.add(new Notification_Model("1","12:00","this is test notification",R.drawable.discount));
        notificationModelList.add(new Notification_Model("2","11:00","this is test notification",R.drawable.discount));
        notificationAdapter=new Adapter_Notification(getContext(),notificationModelList);
        recycle_notification.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle_notification.setAdapter(notificationAdapter);*/


    }


}
