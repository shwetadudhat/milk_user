package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.model.Model;
import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Activity.Product;
import com.webzino.milkdelightuser.Model.App_Product_Model;
import com.webzino.milkdelightuser.utils.DatabaseHandler;
import com.webzino.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_BestProductList extends RecyclerView.Adapter<Adapter_BestProductList.holder> {

    Context context;
    List<App_Product_Model> productModelList;
    private List<App_Product_Model> arraylist;

    int count = 1;
    private DatabaseHandler dbcart;

    Double product_price;
    Double gst,cgst,sgst;

    public Adapter_BestProductList(Context context, List<App_Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(productModelList);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_product, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        App_Product_Model allProductsModel=productModelList.get(i);
        dbcart = new DatabaseHandler(context);
      //  notifyItemChanged(i);

     //   Log.e("productRate",productModelList.get(i).getRate());
        if (!productModelList.get(i).getRate().equals("null")){
            holder.ratingBar.setRating(Float.parseFloat(productModelList.get(i).getRate()));
        }else{
            holder.ratingBar.setRating(Float.parseFloat("0.0"));
        }

        holder.tvRate.setText(productModelList.get(i).getReview_count()+" Ratings");

        holder.tvProName.setText(productModelList.get(i).getProduct_name()+" ("+productModelList.get(i).getQty()+" "+productModelList.get(i).getUnit()+")");

        Global.loadGlideImage(context,productModelList.get(i).getProduct_image(),productModelList.get(i).getProduct_image(),holder.ivProd);


        holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.parseDouble(productModelList.get(i).getPrice())));
        holder.tvOldPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getMrp())));

        if (!productModelList.get(i).getMrp().equals("0")){
            holder.tvOldPrice.setVisibility(View.VISIBLE);
            holder.tvOldPrice.setPaintFlags(holder.tvOldPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.tvOldPrice.setVisibility(View.GONE);
        }

        holder.llitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.btnAdd.callOnClick();
                Intent intent=new Intent(context, Product.class);
                intent.putExtra("proname",productModelList.get(i).getProduct_name());
                intent.putExtra("proId",productModelList.get(i).getProduct_id());
                context.startActivity(intent);
            }
        });

        Log.e("allProcatid",allProductsModel.getCategory_id());

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
                map.put("product_image", allProductsModel.getProduct_image());
                map.put("unit",allProductsModel.getQty()+" "+ allProductsModel.getUnit());
                map.put("stock", allProductsModel.getStock());
                if (!productModelList.get(i).getGst().equals("null")){
                    gst= Double.valueOf(productModelList.get(i).getGst());
                    map.put("gst_price",String.valueOf(Math.round(Double.parseDouble(productModelList.get(i).getPrice()))- Math.round(Global.getTax1(context, Double.parseDouble(productModelList.get(i).getPrice()),gst))));
                    Log.e("priceee123", String.valueOf(Math.round(Double.parseDouble(productModelList.get(i).getPrice()))- Math.round(Global.getTax1(context, Double.parseDouble(productModelList.get(i).getPrice()),gst))));
                    map.put("gst_subscription_price",  String.valueOf(Math.round(Double.parseDouble(productModelList.get(i).getSubscription_price()))- Math.round(Global.getTax1(context, Double.parseDouble(productModelList.get(i).getSubscription_price()),gst))));

                }else{
                    map.put("gst_price", allProductsModel.getPrice());
                    map.put("gst_subscription_price", allProductsModel.getSubscription_price());
                }
                Log.e("producttttprice", allProductsModel.getPrice());

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

        TextView tvProName , tvProPrice,tvRate,tvOldPrice;
        ImageView ivProd;
        Button btnAdd;
        RatingBar ratingBar;
        LinearLayout llitem;

        public holder(@NonNull View itemView) {
            super(itemView);
            ratingBar=itemView.findViewById(R.id.rating);
            tvRate=itemView.findViewById(R.id.tvRate);
            tvProName =itemView.findViewById(R.id.tvProName);
            tvProPrice =itemView.findViewById(R.id.tvProPrice);
            ivProd =itemView.findViewById(R.id.ivProd);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            llitem = itemView.findViewById(R.id.llitem);
            tvOldPrice = itemView.findViewById(R.id.tvOldPrice);

        }
    }

    public void updateData(List<App_Product_Model> viewModels) {
        productModelList = new ArrayList<>();
        productModelList.addAll(viewModels);
        notifyDataSetChanged();
    }

    // Filter Class
//    public void filter(String charText) {
    public void filter(String filter_type,String items_size,String items_unit) {
        String data=items_size+items_unit;
        data = data.toLowerCase(Locale.getDefault());

       /* Collections.sort(productModelList, new Comparator<App_Product_Model>() {
            @Override
            public int compare(App_Product_Model lhs, App_Product_Model rhs) {
                return lhs.getProduct_id().compareTo(rhs.getProduct_id());
            }
        });*/
        productModelList.clear();
        if (data.length() == 0) {
            productModelList.addAll(arraylist);
        } else {
            for (App_Product_Model wp : arraylist) {
                if (wp.getProduct_name().toLowerCase(Locale.getDefault()).contains(data) || wp.getUnit().toLowerCase(Locale.getDefault()).contains(data)) {
                    productModelList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
