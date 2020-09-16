package com.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Cart_Adapter1 extends RecyclerView.Adapter<Cart_Adapter1.holder> {

    ArrayList<HashMap<String, String>> list;

    Activity activity;
    String o= "0";
    private DatabaseHandler dbcart;

    int count=1;

    public Cart_Adapter1(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart1, null, false);

        return new holder(view);    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int i) {

        final HashMap<String, String> map = list.get(i);
        dbcart = new DatabaseHandler(activity);
        Glide.with(activity)
                .load(map.get("product_image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image_product);

        holder.text_product.setText(map.get("product_name")+" ("+map.get("unit")+")");
//        holder.price_product.setText("₹ "+map.get("price"));
        holder.qty_product.setText("Qty : "+map.get("qty"));
        holder.price_product.setText(MainActivity.currency_sign+ String.valueOf(Math.round(Double.valueOf(map.get("price"))+ Math.round( Global.getTax(activity, Double.valueOf(map.get("price")))))));



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView text_product,qty_product, price_product;
        ImageView image_product,ivDeleteCart;


        public holder(@NonNull View itemView) {
            super(itemView);
            text_product = itemView.findViewById(R.id.text_cart);
            price_product = itemView.findViewById(R.id.price_cart);
            image_product = itemView.findViewById(R.id.image_cart);
            qty_product = itemView.findViewById(R.id.qty_cart);


        }
    }
}