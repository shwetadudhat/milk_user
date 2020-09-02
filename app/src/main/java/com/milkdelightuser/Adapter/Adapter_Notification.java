package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Notification_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.Global;

import java.text.ParseException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Notification extends RecyclerView.Adapter<Adapter_Notification.MyViewHolder> {
    Context context;
    List<Notification_Model> notificationModelList;

    public Adapter_Notification(Context context, List<Notification_Model> notificationModelList) {
        this.context = context;
        this.notificationModelList=notificationModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.listitem_notification,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (notificationModelList.get(position).getOrder_type().contains("Subscribe")){
            holder.ivNotify.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_noun_subscribe));
        }else{
            holder.ivNotify.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_coupon));
        }

       // holder.offer_name.setText(notificationModelList.get(position).getNotification_desc());
//        holder.offer_time.setText(notificationModelList.get(position).getTime());

        String enddateeee=notificationModelList.get(position).getEnd_date();
        try {
            String enddate = Global.getDateConvert(enddateeee, "yyyy-MM-dd", " d MMM yyyy");
            holder.offer_name.setText("Your Subscription plan no. "+ notificationModelList.get(position).getNotification_id()+" is end on "+enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            holder.offer_time.setText(Global.getDateConvert(notificationModelList.get(position).getTime(),"yyyy-MM-dd","hh:mm a"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*String enddateeee=notificationModelList.get(position).getEnd_date();
        if (!enddateeee.equals("null")) {
            try {
                String enddate = Global.getDateConvert(enddateeee, "yyyy-MM-dd", " d MMM yyyy");
                Log.e("enddate123", enddate);
                holder.offer_name.setText("Your Subscription is end on "+enddate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            holder.offer_name.setText("Your Subscription is end soon ");
        }

        try {
            holder.offer_time.setText(Global.getDateConvert(notificationModelList.get(position).getTime(),"yyyy-MM-dd HH:mm:ss","hh:mm a"));*//*notificationModelList.get(position).getTime()*//*
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivNotify;
        TextView offer_name,offer_time;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivNotify=itemView.findViewById(R.id.ivNotify);
            offer_name=itemView.findViewById(R.id.offer_name);
            offer_time=itemView.findViewById(R.id.offer_time);
        }
    }
}
