package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Offer_Model;
import com.milkdelightuser.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_Offer extends RecyclerView.Adapter<Adapter_Offer.MyViewHolder> {
    Context context;
    List<Offer_Model> offerModelList;

    public Adapter_Offer(Context context, List<Offer_Model> offerModelList) {
        this.context = context;
        this.offerModelList = offerModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.listitem_offer,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      //  holder.ivDisc.setImageResource(offerModelList.get(position).getOffer_icon());
        holder.offerName.setText(offerModelList.get(position).getOffer_desc());
        holder.offerDesc.setText(offerModelList.get(position).getOffer_detaildesc());
        holder.offerExpire.setText("Expires on : "+offerModelList.get(position).getOffer_expireTime());
    }

    @Override
    public int getItemCount() {
        return offerModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDisc;
        TextView offerName,offerDesc,offerExpire;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ivDisc=itemView.findViewById(R.id.ivDisc);
            offerName=itemView.findViewById(R.id.offerName);
            offerDesc=itemView.findViewById(R.id.offerDesc);
            offerExpire=itemView.findViewById(R.id.offerExpire);
        }
    }
}
