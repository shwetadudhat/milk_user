package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.Notification_Model;
import com.webzino.milkdelightuser.utils.Global;

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

            holder.ivNotify.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_coupon));

        holder.offer_name.setText(notificationModelList.get(position).getNotification_id());
        holder.offer_time.setText(notificationModelList.get(position).getNotification_desc());

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
