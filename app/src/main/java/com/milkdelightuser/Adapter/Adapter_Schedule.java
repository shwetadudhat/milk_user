package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.Model.Schedule_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseURL;
import com.milkdelightuser.utils.Global;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Schedule extends RecyclerView.Adapter<Adapter_Schedule.holder> {

    List<Schedule_Model> schedule_models;
    Context context;

    public Adapter_Schedule(List<Schedule_Model> schedule_models) {
        this.schedule_models = schedule_models;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_schedule, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Schedule_Model scheduleModel = schedule_models.get(i);

      /*  Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + scheduleModel.getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.image_day);
*/
        Global.loadGlideImage(context,scheduleModel.getProduct_image(),BaseURL.IMG_CATEGORY_URL + scheduleModel.getProduct_image(),holder.image_day);


        String sign= MainActivity.currency_sign;

        holder.price_product.setText("Price:" + " "+sign+scheduleModel.getPrice());

        holder.product.setText(scheduleModel.getProduct_name());
        holder.description_product.setText(scheduleModel.getDescription());
        // holder.unit_product.setText(subsofday_model.get());

        holder.quantity.setText("Quntity:" + " " +scheduleModel.getOrder_qty());
        holder.unit_product.setText(scheduleModel.getUnit());
     //   holder.substatus.setText(scheduleModel.getSub_status());
        holder.qty.setText(scheduleModel.getQty());
        holder.ordertype.setText(scheduleModel.getOrder_type());
        holder.order_sttus.setText(scheduleModel.getOrder_status());


    }

    @Override
    public int getItemCount() {
        return schedule_models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView product , description_product ,unit_product, price_product , substatus , quantity , qty , ordertype,order_sttus;
        ImageView image_day;
        Button sub_button;

        public holder(@NonNull View itemView) {
            super(itemView);

            //sub_button= itemView.findViewById(R.id.subscribe_day);
            image_day =itemView.findViewById(R.id.imge_schdule);

            product = itemView.findViewById(R.id.text_schedule);
            description_product = itemView.findViewById(R.id.discription_schedule);
            unit_product = itemView.findViewById(R.id.unit_schedule);
            ordertype = itemView.findViewById(R.id.order_type);
            price_product = itemView.findViewById(R.id.price_schedule);
            substatus = itemView.findViewById(R.id.substatus_schedule);
            qty = itemView.findViewById(R.id.qty_schdule);
            quantity = itemView.findViewById(R.id.quantity_schedule);
            order_sttus = itemView.findViewById(R.id.substatus_schedule);
        }
    }
}
