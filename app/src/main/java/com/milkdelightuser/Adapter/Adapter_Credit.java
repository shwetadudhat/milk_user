package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Credit_Model;
import com.milkdelightuser.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Credit extends RecyclerView.Adapter<Adapter_Credit.holder> {

    List<Credit_Model> credit_models;
    Context context;

    public Adapter_Credit(List<Credit_Model> credit_models) {
        this.credit_models = credit_models;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_credit, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {
        final Credit_Model credit_model = credit_models.get(i);
        holder.delivery.setText(credit_model.getDate_of_recharge());
        holder.amount.setText(credit_model.getAmount());
        holder.status.setText(credit_model.getRecharge_status());
    }

    @Override
    public int getItemCount() {
        return credit_models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView delivery , amount , status;
        ImageView city_image;

        public holder(@NonNull View itemView) {
            super(itemView);
            delivery = itemView.findViewById(R.id.date_credit);
            amount = itemView.findViewById(R.id.amount);
            status = itemView.findViewById(R.id.status_credit);
        }
    }
}
