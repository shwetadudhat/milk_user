package com.webzino.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.MainActivity;
import com.webzino.milkdelightuser.Model.SubscriptioAddProduct_model;
import com.webzino.milkdelightuser.utils.Global;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_RenewPlan extends RecyclerView.Adapter<Adapter_RenewPlan.holder> {

    ArrayList<SubscriptioAddProduct_model> productModelArrayList;
    ArrayList<SubscriptioAddProduct_model> productModelArrayList1;

    Activity activity;
    String o= "0";
    private EventListener mEventListener;
    String select_product="false";

    public Adapter_RenewPlan(Activity activity,  ArrayList<SubscriptioAddProduct_model> productModelArrayList) {
        this.productModelArrayList = productModelArrayList;
        this.activity = activity;
        this.productModelArrayList1=new ArrayList<>();

    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_renewplan, null, false);

        return new holder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final holder holder, final int i) {

        if (productModelArrayList.size()== productModelArrayList.size() - 1) {
            holder.llborder.setVisibility(View.GONE);
        }


        Global.loadGlideImage(activity,productModelArrayList.get(i).getProduct_name(), productModelArrayList.get(i).getImage(),holder.image_cart);


        holder.product_name.setText(productModelArrayList.get(i).getProduct_name());
        holder.product_unit.setText("("+productModelArrayList.get(i).getProduct_unit()+")");
        holder.product_price.setText(MainActivity.currency_sign+productModelArrayList.get(i).getProduct_price());
        holder.product_qty.setText(activity.getString(R.string.qty)+" "+productModelArrayList.get(i).getProduct_qty());


        holder.chk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                select_product="true";
                if (holder.chk.isChecked()) {
                    productModelArrayList1.add(productModelArrayList.get(i));


                    if (mEventListener != null)
                        mEventListener.onItemViewClicked(i,productModelArrayList1);

                }else {
                    select_product="false";
                    productModelArrayList1.remove(productModelArrayList.get(i));

                    if (mEventListener != null)
                        mEventListener.onItemViewClicked(i,productModelArrayList1);

                }

            }
        });



    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView product_name,product_price, product_qty,product_unit;
        ImageView image_cart;
        CheckBox chk;
        LinearLayout   llborder;



        public holder(@NonNull View itemView) {
            super(itemView);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_qty = itemView.findViewById(R.id.product_qty);
            product_unit = itemView.findViewById(R.id.product_unit);
            image_cart = itemView.findViewById(R.id.image_cart);
            chk= itemView.findViewById(R.id.chk);


        }
    }


    public interface EventListener {
        void onItemViewClicked(int position, ArrayList<SubscriptioAddProduct_model> sublist);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
