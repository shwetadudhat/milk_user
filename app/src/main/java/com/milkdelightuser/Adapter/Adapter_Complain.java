package com.milkdelightuser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.milkdelightuser.Model.Complain_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.AppController;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.CustomVolleyJsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.milkdelightuser.utils.AppController.MY_SOCKET_TIMEOUT_MS;


public class Adapter_Complain extends RecyclerView.Adapter<Adapter_Complain.holder> {

    List<Complain_Model> complainModels;
    Context context;

    public Adapter_Complain(List<Complain_Model> complainModels) {
        this.complainModels = complainModels;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_complain, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Complain_Model complainModel = complainModels.get(i);


        holder.textcomplain.setText(complainModel.getComplain_name());

holder.textcomplain.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        report();
    }
});

    }

    @Override
    public int getItemCount() {
        return complainModels.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView textcomplain;

        public holder(@NonNull View itemView) {
            super(itemView);

            textcomplain = itemView.findViewById(R.id.text_complain);

        }
    }

    private void report(){

        String tag_json_obj = "json store req";
    Map<String, String> params = new HashMap<String, String>();
        params.put("cityadmin_id", "1");
        params.put("completed_id", "19");
        params.put("delivery_boy_id", "22");
        params.put("complain_id", "1");

        CustomVolleyJsonRequest jsonObjectRequest= new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.report_issue, params
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Tag",response.toString());

                    try {

                        String status = response.getString("status");
                        String message = response.getString("message");

                    if (status.contains("1")){


                        //    Toast.makeText(context.getApplicationContext(),""+message, Toast.LENGTH_SHORT).show();

                    }else {

                       // Toast.makeText(context.getApplicationContext(), ""+message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

             //   Toast.makeText(context.getApplicationContext(), ""+error, Toast.LENGTH_SHORT).show();
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(jsonObjectRequest,tag_json_obj);
    }

}
