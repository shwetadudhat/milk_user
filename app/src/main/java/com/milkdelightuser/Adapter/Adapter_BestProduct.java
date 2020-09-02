package com.milkdelightuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.Activity.Product;
import com.milkdelightuser.Model.App_Product_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.DatabaseHandler;
import com.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_BestProduct extends RecyclerView.Adapter<Adapter_BestProduct.holder>  {

    Context context;
    List<App_Product_Model> productModelList;
    private List<App_Product_Model> arraylist;

    int count = 1;
    private DatabaseHandler dbcart;

    Double product_price;

    public Adapter_BestProduct(Context context, List<App_Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(productModelList);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_sellingproduct, null, false);
        context = viewGroup.getContext();
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {
        App_Product_Model allProductsModel=productModelList.get(i);
        dbcart = new DatabaseHandler(context);

      //  product_price= Double.valueOf(productModelList.get(i).getPrice())+ Global.getTax(context, Double.valueOf(productModelList.get(i).getPrice()));
        //Log.e("Stringproduct_price",String.valueOf(Math.round(product_price)));
        holder.tvProName.setText(productModelList.get(i).getProduct_name()+" ("+productModelList.get(i).getQty()+" "+productModelList.get(i).getUnit()+")");
        holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getPrice())+ Math.round(Global.getTax(context, Double.valueOf(productModelList.get(i).getPrice())))));
       // holder.tvProPrice.setText(MainActivity.currency_sign+ allProductsModel.getPrice());

        Glide.with(context)
                .load(/*BaseURL.IMG_CATEGORY_URL + */productModelList.get(i).getProduct_image())
                .into(holder.ivProd);

        if (!productModelList.get(i).getMrp().equals("0")){
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getMrp())+ Math.round( Global.getTax(context, Double.valueOf(productModelList.get(i).getMrp())))));

         //   holder.tvOldPrice.setText( MainActivity.currency_sign +productModelList.get(i).getMrp());
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }


        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                holder.btnAdd.callOnClick();
               /* Intent intent=new Intent(context, Product.class);
                intent.putExtra("proname",productModelList.get(i).getProduct_name());
                intent.putExtra("proId",productModelList.get(i).getProduct_id());
                context.startActivity(intent);*/
            }
        });



        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HashMap<String, String> map = new HashMap<>();
                map.put("product_id", allProductsModel.getProduct_id());
                map.put("product_name", allProductsModel.getProduct_name());
                map.put("category_id", allProductsModel.getCategory_id());
                map.put("product_description", allProductsModel.getDescription());
                map.put("price", allProductsModel.getPrice());
                map.put("subscription_price", allProductsModel.getSubscription_price());
//                map.put("price", String.valueOf(Math.round(Double.valueOf(allProductsModel.getPrice())+ Global.getTax(context, Double.valueOf(allProductsModel.getPrice())))));
//                map.put("subscription_price", String.valueOf(Math.round(Double.valueOf(allProductsModel.getSubscription_price())+ Global.getTax(context, Double.valueOf(allProductsModel.getSubscription_price())))));
                map.put("product_image", allProductsModel.getProduct_image());
                map.put("unit",allProductsModel.getQty()+" "+ allProductsModel.getUnit());
                map.put("stock", allProductsModel.getStock());

                if (dbcart.isInCart(map.get("product_id"))) {
                    dbcart.setCart(map, Float.valueOf(dbcart.getCartItemQty(map.get("product_id")))+1);
                } else {
                    dbcart.setCart(map, Float.valueOf(count));
                }

                Intent intent=new Intent(context, Product.class);
                intent.putExtra("proname",productModelList.get(i).getProduct_name());
                intent.putExtra("proId",productModelList.get(i).getProduct_id());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView tvProName , tvProPrice,tvOldPrice;
        ImageView ivProd;
        Button btnAdd;
        LinearLayout llMain;

        public holder(@NonNull View itemView) {
            super(itemView);

            tvProName =itemView.findViewById(R.id.tvProName);
            tvProPrice =itemView.findViewById(R.id.tvProPrice);
            tvOldPrice =itemView.findViewById(R.id.tvOldPrice);
            ivProd =itemView.findViewById(R.id.ivProd);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            llMain = itemView.findViewById(R.id.llMain);

        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        productModelList.clear();
        if (charText.length() == 0) {
            productModelList.addAll(arraylist);
        } else {
            for (App_Product_Model wp : arraylist) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(charText) || wp.getUnit().toLowerCase(Locale.getDefault()).contains(charText)) {
                    productModelList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
