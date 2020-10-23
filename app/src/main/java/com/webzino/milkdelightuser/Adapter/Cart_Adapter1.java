package com.webzino.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Cart_Adapter1 extends RecyclerView.Adapter<Cart_Adapter1.holder> {

    ArrayList<HashMap<String, String>> list;

    Activity activity;


    public Cart_Adapter1(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_cart1, null, false);

        return new holder(view);    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int i) {

        final HashMap<String, String> map = list.get(i);

     /*   Glide.with(activity)
                .load(map.get("product_image"))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image_product);*/

        Global.loadGlideImage(activity,map.get("product_image"), map.get("product_image"),holder.image_product);

        holder.text_product.setText(map.get("product_name")+" ("+map.get("unit")+")");
//        holder.price_product.setText("₹ "+map.get("price"));
        holder.qty_product.setText(activity.getString(R.string.qty)+map.get("qty"));
        holder.price_product.setText(MainActivity.currency_sign+map.get("price"));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView text_product,qty_product, price_product;
        ImageView image_product;


        public holder(@NonNull View itemView) {
            super(itemView);
            text_product = itemView.findViewById(R.id.text_cart);
            price_product = itemView.findViewById(R.id.price_cart);
            image_product = itemView.findViewById(R.id.image_cart);
            qty_product = itemView.findViewById(R.id.qty_cart);


        }
    }
}
