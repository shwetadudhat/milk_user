package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Activity.MyOrderDetail;
import com.webzino.milkdelightuser.Model.Order_Model;
import com.webzino.milkdelightuser.utils.Global;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter_MyOrder extends RecyclerView.Adapter<Adapter_MyOrder.MyViewHolder> {
    Context context;
    List<Order_Model> orderModelList;

    public Adapter_MyOrder(Context context, List<Order_Model> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.listitem_myorder,null,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     //   holder.image_ordr.setImageResource(orderModelList.get(position).getOrder_icon());
      /*  Glide.with(context)
                .load(orderModelList.get(position).getOrder_icon())
                .into(holder.image_ordr);*/

        Global.loadGlideImage(context,orderModelList.get(position).getOrder_icon(),orderModelList.get(position).getOrder_icon(),holder.image_ordr);


        holder.text_ordr.setText(orderModelList.get(position).getOffer_product());
        holder.text_orderunit.setText("("+orderModelList.get(position).getOrder_unit()+")");
        holder.price_ordr.setText(MainActivity.currency_sign +orderModelList.get(position).getOffer_pricee());
        holder.qty_ordr.setText(context.getString(R.string.qty)+orderModelList.get(position).getOffer_qty());

       // String formattedDate = Global.convertDate(orderModelList.get(position).getOffer_deliveryText());
        if (orderModelList.get(position).getSubStatus().contains("Pending")){
            holder.delvr_txt.setText(context.getString(R.string.delivery_on)+" "+orderModelList.get(position).getSubStatus());
        }else{
            holder.delvr_txt.setText(context.getString(R.string.delivery_on)+orderModelList.get(position).getOffer_deliveryText());
        }


        /* if (!gst.equals("null")){
                                Log.e("priceee123", String.valueOf(Math.round(Double.parseDouble(price))+ Math.round(Global.getTax1(getContext(), Double.parseDouble(price),Double.parseDouble(gst)))));
                                orderModel.setOffer_pricee(String.valueOf(Math.round(Double.parseDouble(price))+ Math.round(Global.getTax1(getContext(), Double.parseDouble(price),Double.parseDouble(gst)))));
                            }else{
                                orderModel.setOffer_pricee(price);
                            }
*/


        holder.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, MyOrderDetail.class);
                intent.putExtra("product_image",orderModelList.get(position).getOrder_icon());
                intent.putExtra("order_id",orderModelList.get(position).getOrder_id());
                intent.putExtra("pricee",orderModelList.get(position).getOffer_pricee());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlMain;
        ImageView image_ordr;
        TextView text_ordr,price_ordr,qty_ordr,delvr_txt,text_orderunit;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rlMain=itemView.findViewById(R.id.rlMain);
            image_ordr=itemView.findViewById(R.id.image_ordr);
            text_ordr=itemView.findViewById(R.id.text_ordr);
            price_ordr=itemView.findViewById(R.id.price_ordr);
            qty_ordr=itemView.findViewById(R.id.qty_ordr);
            delvr_txt=itemView.findViewById(R.id.delvr_txt);
            text_orderunit=itemView.findViewById(R.id.text_orderunit);
        }
    }
}
