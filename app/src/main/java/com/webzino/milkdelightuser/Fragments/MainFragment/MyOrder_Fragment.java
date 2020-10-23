package com.webzino.milkdelightuser.Fragments.MainFragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Adapter.Adapter_MyOrder;
import com.webzino.milkdelightuser.Model.Order_Model;
import com.webzino.milkdelightuser.utils.AppController;
import com.webzino.milkdelightuser.utils.BaseFragment;
import com.webzino.milkdelightuser.utils.BaseURL;
import com.webzino.milkdelightuser.utils.ConnectivityReceiver;
import com.webzino.milkdelightuser.utils.CustomVolleyJsonRequest;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Session_management;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrder_Fragment extends BaseFragment {

    RecyclerView recycler_myordr;
    Adapter_MyOrder adapterMyOrder;
    List<Order_Model> orderModelList;

    RelativeLayout container_null1;

    Session_management sessionManagement;
    String u_id;

    public MyOrder_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_myorder, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        orderModelList=new ArrayList<>();

        container_null1 = view.findViewById(R.id.container_null1);
        recycler_myordr = (RecyclerView)view.findViewById(R.id.recycler_myordr);
        recycler_myordr.setLayoutManager(new GridLayoutManager(getContext(),1));

        sessionManagement=new Session_management(getContext());
        u_id = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);


        if (ConnectivityReceiver.isConnected()) {
            showDialog("");
            getMyOrderDetail(u_id);
        } else {
            Global.showInternetConnectionDialog(getContext());

        }

    }

    private void getMyOrderDetail(String u_id) {
        String tag_json_obj = "json store req";
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_id", u_id);

        Log.e("params",params.toString());

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.get_my_order_list, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                dismissDialog();
                Log.e("myOrder123", response.toString());

                try {
                    String status=response.getString("status");
                    String message=response.getString("message");

                    if (status.equals("1")){

                        container_null1.setVisibility(View.GONE);
                        recycler_myordr.setVisibility(View.VISIBLE);
                        //  Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject=response.getJSONObject("data");

                        JSONArray jsonArray=jsonObject.getJSONArray("order_list");
                        String product_url=jsonObject.getString("product_url");

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            String product_name=jsonObject1.getString("product_name");
                            int subs_id=jsonObject1.getInt("subs_id");
                            String delivery_date= Global.getDateConvert(jsonObject1.getString("delivery_date"),"yyyy-MM-dd","EEE dd, MMM yyyy");
                            String order_qty=jsonObject1.getString("order_qty");
                            String price=jsonObject1.getString("price");
                            String unit=jsonObject1.getString("unit");
                            String qty=jsonObject1.getString("qty");
                            String sub_status=jsonObject1.getString("sub_status");

                            JSONObject jsonObject2=jsonObject1.getJSONObject("product");
                            String gst=jsonObject2.getString("gst");
                            JSONObject jsonObject3=jsonObject2.getJSONObject("product_image");
                            String product_image=jsonObject3.getString("product_image");


                            Order_Model orderModel=new Order_Model();
                            orderModel.setOrder_id(String.valueOf(subs_id));
                            orderModel.setOffer_product(product_name);
                            orderModel.setOffer_qty(order_qty);
                            orderModel.setGst(gst);

//                            orderModel.setOffer_pricee(String.valueOf(Math.round(Double.parseDouble(price)+ Math.round( Global.getTax(getContext(), Double.parseDouble(price))))));
                           /* if (!gst.equals("null")){
                                Log.e("priceee123", String.valueOf(Math.round(Double.parseDouble(price))+ Math.round(Global.getTax1(getContext(), Double.parseDouble(price),Double.parseDouble(gst)))));
                                orderModel.setOffer_pricee(String.valueOf(Math.round(Double.parseDouble(price))+ Math.round(Global.getTax1(getContext(), Double.parseDouble(price),Double.parseDouble(gst)))));
                            }else{
                                orderModel.setOffer_pricee(price);
                            }*/
                            orderModel.setOffer_pricee(price);
                            orderModel.setOffer_deliveryText(delivery_date);
                            orderModel.setOrder_unit(qty+" "+unit);
                            orderModel.setOrder_icon(product_url+product_image);
                            orderModel.setSubStatus(sub_status);

                            orderModelList.add(orderModel);

                        }

                        adapterMyOrder=new Adapter_MyOrder(getContext(),orderModelList);
                        recycler_myordr.setAdapter(adapterMyOrder);



                    }else if (status.equals("0")){

                        container_null1.setVisibility(View.VISIBLE);
                        recycler_myordr.setVisibility(View.GONE);

                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error1234",error.toString());
                dismissDialog();
              //  Toast.makeText(getContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, tag_json_obj);
    }

   /* private void about(){
        orderModelList=new ArrayList<>();
        orderModelList.add(new Order_Model("1","milk","2","100","delivered on this day",R.drawable.milk));
        orderModelList.add(new Order_Model("2","milk123","6","70","delivered on this day",R.drawable.milk12));
        adapterMyOrder=new Adapter_MyOrder(getContext(),orderModelList);
        recycler_myordr.setAdapter(adapterMyOrder);
    }*/

}
