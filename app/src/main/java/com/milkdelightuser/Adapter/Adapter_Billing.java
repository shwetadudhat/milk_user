package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Billing_Model;
import com.milkdelightuser.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Billing extends RecyclerView.Adapter<Adapter_Billing.holder> {

    List<Billing_Model> billing_models;
    Context context;

    public Adapter_Billing(List<Billing_Model> billing_models) {
        this.billing_models = billing_models;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_billing, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Billing_Model billing_model = billing_models.get(i);

        holder.delivery.setText(billing_model.getDelivery_date());
      //  holder.ordreid.setText(billing_model.getOrder_id());
        holder.price.setText(billing_model.getPrice());
    }

    @Override
    public int getItemCount() {
        return billing_models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView delivery , ordreid , price;
        ImageView city_image;

        public holder(@NonNull View itemView) {
            super(itemView);

            delivery = itemView.findViewById(R.id.date_billing);
            ordreid = itemView.findViewById(R.id.order);
            price = itemView.findViewById(R.id.price);
        }
    }
}
