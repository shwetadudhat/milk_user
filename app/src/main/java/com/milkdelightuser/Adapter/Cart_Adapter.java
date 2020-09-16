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
import android.widget.Toast;

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


public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.holder> {

    ArrayList<HashMap<String, String>> list;

    Activity activity;
    String o= "0";
    private DatabaseHandler dbcart;
    private EventListener mEventListener;

    int count=1;

    public Cart_Adapter(Activity activity, ArrayList<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.cart, null, false);

        return new holder(view);
    }

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
        holder.price_product.setText(MainActivity.currency_sign+ String.valueOf(Math.round(Double.valueOf(map.get("price"))+ Math.round( Global.getTax(activity, Double.valueOf(map.get("price")))))));
     //   holder.price_product.setText("₹ "+map.get("price"));
        holder.number.setText(dbcart.getCartItemQty(map.get("product_id")));


        holder.ivDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(activity, "delete clickeddd", Toast.LENGTH_SHORT).show();
                count=1;
                  //  dbcart.removeItemFromCart(map.get("product_id"));
              //  notifyItemRemoved(i);
                holder.number.setText(String.valueOf(count));
                if (holder.number.getText().toString().contains("1")){
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("product_id", map.get("product_id"));
                    map1.put("product_name", map.get("product_name"));
                    map1.put("category_id", map.get("category_id"));
                    map1.put("product_description", map.get("product_description"));
                    map1.put("price", map.get("price"));
                    map1.put("subscription_price", map.get("subscription_price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));

                    dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                    dbcart.removeItemFromCart(map.get("product_id"));

                    list.remove(i);

                    notifyDataSetChanged();
                    if (mEventListener != null)
                        mEventListener.onItemViewClicked(i);

                }
            }
        });


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
                    map1.put("subscription_price", map.get("subscription_price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));

                    if (mEventListener != null)
                        mEventListener.onItemViewClicked(i);


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
                    map1.put("subscription_price", map.get("subscription_price"));
                    map1.put("product_image", map.get("product_image"));
                    map1.put("unit", map.get("unit"));
                    map1.put("stock", map.get("stock"));
                    dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));

                    if (mEventListener != null)
                        mEventListener.onItemViewClicked(i);

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
                        map1.put("subscription_price", map.get("subscription_price"));
                        map1.put("product_image", map.get("product_image"));
                        map1.put("unit", map.get("unit"));
                        map1.put("stock", map.get("stock"));

                        dbcart.setCart(map, Float.valueOf(holder.number.getText().toString()));
                        dbcart.removeItemFromCart(map.get("product_id"));

                        list.remove(i);

                        notifyDataSetChanged();

                        if (mEventListener != null)
                            mEventListener.onItemViewClicked(i);

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
            text_product = itemView.findViewById(R.id.text_cart);
            price_product = itemView.findViewById(R.id.price_cart);
            image_product = itemView.findViewById(R.id.image_cart);
            plus = itemView.findViewById(R.id.plus1);
            minus= itemView.findViewById(R.id.minus);
            number= itemView.findViewById(R.id.one);
            ivDeleteCart= itemView.findViewById(R.id.ivDeleteCart);

        }
    }

    public interface EventListener {
        void onItemViewClicked(int position);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
