package com.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Cart_Adapter2 extends RecyclerView.Adapter<Cart_Adapter2.holder> {

    ArrayList<HashMap<String, String>> list;

    Activity activity;
    String o= "0";
    private DatabaseHandler dbcart;

    int count=1;

    public Cart_Adapter2(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart2, null, false);

        return new holder(view);    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int i) {

        final HashMap<String, String> map = list.get(i);
        dbcart = new DatabaseHandler(activity);

        Log.e("productimage",map.get("product_image"));
        Glide.with(activity)
                .load(map.get("product_image"))

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image_product);

        holder.text_product.setText(map.get("product_name")+" ("+map.get("unit")+")");
        holder.price_product.setText("₹ "+map.get("price"));
        holder.number.setText(dbcart.getCartItemQty(map.get("product_id")));


        holder.plus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int count=0;
//                int i = Integer.parseInt(o);
//                i= Integer.parseInt(i+allProductsModel.getPrice());
                if (!holder.number.getText().toString().equalsIgnoreCase("0")) {
                    count = Integer.valueOf(holder.number.getText().toString());
                    count = count + 1;
                    holder.number.setText(String.valueOf(count));
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("product_id", map.get("product_id"));
                    map1.put("product_name", map.get("product_name"));
                    map1.put("category_id", map.get("category_id"));
                    map1.put("product_description", map.get("product_description"));
                    map1.put("price", map.get("price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                }
                    return false;
            }
        });

        holder.minus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int count;
                count= Integer.valueOf(holder.number.getText().toString());
                if (count > 1) {

                    count = count - 1;
                    holder.number.setText(String.valueOf(count));

                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("product_id", map.get("product_id"));
                    map1.put("product_name", map.get("product_name"));
                    map1.put("category_id", map.get("category_id"));
                    map1.put("product_description", map.get("product_description"));
                    map1.put("price", map.get("price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));

                }
                else if (count==1) {

                    holder.number.setText(String.valueOf(count));
                    if (holder.number.getText().toString().contains("1")){
                        HashMap<String, String> map1 = new HashMap<>();
                        map1.put("product_id", map.get("product_id"));
                        map1.put("product_name", map.get("product_name"));
                        map1.put("category_id", map.get("category_id"));
                        map1.put("product_description", map.get("product_description"));
                        map1.put("price", map.get("price"));
                        map1.put("product_image", map.get("product_image"));
                        map1.put("unit", map.get("unit"));
                        map1.put("stock", map.get("stock"));

                        dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        dbcart.removeItemFromCart(map.get("product_id"));

                        list.remove(i);

                        notifyDataSetChanged();

                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView text_product,litre_product, price_product, number,plus,minus;
        ImageView image_product,ivDeleteCart;


        public holder(@NonNull View itemView) {
            super(itemView);
            text_product = itemView.findViewById(R.id.tvproName);
            price_product = itemView.findViewById(R.id.tvProPrice);
            image_product = itemView.findViewById(R.id.ivproImage);
            plus = itemView.findViewById(R.id.tvQtyInc);
            minus= itemView.findViewById(R.id.tvQtyDec);
            number= itemView.findViewById(R.id.tvQty);


        }
    }
}
