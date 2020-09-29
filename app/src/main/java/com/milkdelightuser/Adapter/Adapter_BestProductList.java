package com.milkdelightuser.Adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_BestProductList extends RecyclerView.Adapter<Adapter_BestProductList.holder> {

    Context context;
    List<App_Product_Model> productModelList;

    int count = 1;
    private DatabaseHandler dbcart;

    Double product_price;
    Double gst,cgst,sgst;

    public Adapter_BestProductList(Context context, List<App_Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
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

        product_price= Double.valueOf(productModelList.get(i).getPrice())+ Math.round( Global.getTax(context, Double.valueOf(productModelList.get(i).getPrice())));

        holder.tvProName.setText(productModelList.get(i).getProduct_name()+" ("+productModelList.get(i).getQty()+" "+productModelList.get(i).getUnit()+")");
        //holder.tvProPrice.setText(MainActivity.currency_sign+Math.round(product_price));
//        holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getPrice())+ Math.round( Global.getTax(context, Double.valueOf(productModelList.get(i).getPrice())))));
        //  holder.tvProPrice.setText((MainActivity.currency_sign+Math.round(Float.parseFloat(productModelList.get(i).getPrice()))));

       /* Glide.with(context)
                .load(*//*BaseURL.IMG_CATEGORY_URL + *//*productModelList.get(i).getProduct_image())
                .into(holder.ivProd);
*/
        Global.loadGlideImage(context,productModelList.get(i).getProduct_image(),productModelList.get(i).getProduct_image(),holder.ivProd);

        if (!productModelList.get(i).getGst().equals("null")){
            gst= Double.valueOf(productModelList.get(i).getGst());
            holder.tvProPrice.setText(MainActivity.currency_sign+ String.valueOf(Math.round(Double.parseDouble(productModelList.get(i).getPrice()))+ Math.round(Global.getTax1(context, Double.parseDouble(productModelList.get(i).getPrice()),gst))));
            holder.tvOldPrice.setText(MainActivity.currency_sign+ String.valueOf(Math.round(Double.valueOf(productModelList.get(i).getMrp())+ Math.round( Global.getTax1(context, Double.valueOf(productModelList.get(i).getMrp()),gst)))));

        }else{
            holder.tvProPrice.setText(MainActivity.currency_sign+ Math.round(Double.parseDouble(productModelList.get(i).getPrice())));
            holder.tvOldPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getMrp())));

        }


        if (!productModelList.get(i).getMrp().equals("0")){
            holder.tvOldPrice.setVisibility(View.VISIBLE);
         //   holder.tvOldPrice.setText( MainActivity.currency_sign +productModelList.get(i).getMrp());
//            holder.tvOldPrice.setText(MainActivity.currency_sign+ Math.round(Double.valueOf(productModelList.get(i).getMrp())+ Math.round( Global.getTax(context, Double.valueOf(productModelList.get(i).getMrp())))));
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
}
