package com.webzino.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        View view = inflater.inflate(R.layout.listitem_cart, null, false);

        return new holder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int i) {

        final HashMap<String, String> map = list.get(i);
        dbcart = new DatabaseHandler(activity);

//        Glide.with(activity)
//                .load(map.get("product_image"))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .dontAnimate()
//                .into(holder.image_product);

        Global.loadGlideImage(activity,map.get("product_image"), map.get("product_image"),holder.image_product);
        Log.e("productprce",map.get("price"));

        holder.text_product.setText(map.get("product_name"));
        holder.text_cart_unit.setText("("+map.get("unit")+")");
        holder.price_product.setText(MainActivity.currency_sign+ map.get("price"));
        holder.number.setText(dbcart.getCartItemQty(map.get("product_id")));


        holder.ivDeleteCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setTitle(R.string.delete_title)
                        .setMessage(R.string.delete_msg)
                        .setPositiveButton(R.string.btn_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                count=1;
                                holder.number.setText(String.valueOf(count));
                                if (holder.number.getText().toString().contains("1")) {
                                    HashMap<String, String> map1 = new HashMap<>();
                                    map1.put("product_id", map.get("product_id"));
                                    map1.put("product_name", map.get("product_name"));
                                    map1.put("category_id", map.get("category_id"));
                                    map1.put("product_description", map.get("product_description"));
                                    map1.put("price", map.get("price"));
                                    map1.put("subscription_price", map.get("subscription_price"));
                                    map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                                    map1.put("gst_price", map.get("gst_price"));
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
                        })
                        .setNegativeButton(R.string.btn_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

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
                    map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                    map1.put("gst_price", map.get("gst_price"));
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
                    map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                    map1.put("gst_price", map.get("gst_price"));
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
                        map1.put("gst_subscription_price", map.get("gst_subscription_price") );
                        map1.put("gst_price", map.get("gst_price"));
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

        TextView text_product,litre_product,text_cart_unit, price_product, number,plus,minus;
        ImageView image_product,ivDeleteCart;


        public holder(@NonNull View itemView) {
            super(itemView);
            text_product = itemView.findViewById(R.id.text_cart);
            text_cart_unit = itemView.findViewById(R.id.text_cart_unit);
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
