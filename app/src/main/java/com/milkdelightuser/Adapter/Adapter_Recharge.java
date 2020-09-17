package com.milkdelightuser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.milkdelightuser.Activity.MainActivity;
import com.milkdelightuser.Model.Recharge_Model;
import com.milkdelightuser.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Recharge extends RecyclerView.Adapter<Adapter_Recharge.holder> {

    List<Recharge_Model> recharge_models;
    Context context;

  /*  Date enddate=null ;*/
  Date enddate = null, CurrentDate = null;
    String currentdate = "", olddate;



    public Adapter_Recharge(List<Recharge_Model> recharge_models) {
        this.recharge_models = recharge_models;

        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        Calendar c = Calendar.getInstance();

        enddate = c.getTime();

        Recharge_Model recharge_model;


        for (int i = 0; i < recharge_models.size(); i++) {
            recharge_model = recharge_models.get(i);
            Date startDte = null;
            try {
                startDte = originalFormat.parse(recharge_model.getCreated_at());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            olddate = targetFormat.format(startDte);

            if (i==0){
                recharge_model.setIsshow(true);
            }

            if (daysBetween(startDte, enddate) > 0 && !currentdate.equals(olddate)) {
                currentdate = olddate;
                recharge_model.setIsshow(true);
            }
        }
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_showrecharge, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Recharge_Model rechargeModel = recharge_models.get(i);


        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        DateFormat targetFormat2 = new SimpleDateFormat("HH:mm a");
        Calendar c = Calendar.getInstance();

        Date startDte = null;
        String formattedDate = null;
        String formattedDate1 = null;
        String olddate;

        try {
            startDte = originalFormat.parse(rechargeModel.getCreated_at());
            olddate = targetFormat.format(startDte);
            enddate = c.getTime();
           /* if (enddate != null) {
                if (daysBetween(startDte, enddate) > 0 && !currentdate.equals(olddate)) {
                    currentdate = olddate;
                    holder.tvDate.setVisibility(View.VISIBLE);
                } else {
                    holder.tvDate.setVisibility(View.GONE);
                }

            } else {
                holder.tvDate.setVisibility(View.VISIBLE);

            }*/

            if (rechargeModel.isIsshow()) {
                holder.tvDate.setVisibility(View.VISIBLE);
            } else {
                holder.tvDate.setVisibility(View.GONE);
            }
//            enddate = originalFormat.parse(rechargeModel.getCreated_at());

            formattedDate = targetFormat.format(startDte);
            formattedDate1 = targetFormat2.format(startDte);

            Log.e("formattedDate", formattedDate);
            Log.e("formattedDate123", formattedDate1);

        } catch (ParseException e) {
            e.printStackTrace();
        }


       /* try {
            startDte = originalFormat.parse(rechargeModel.getCreated_at());
            if (enddate!=null){
                if (daysBetween(startDte,enddate)>0){
                    holder.tvDate.setVisibility(View.VISIBLE);
                }else{
                    holder.tvDate.setVisibility(View.GONE);
                }

            }else{
                holder.tvDate.setVisibility(View.VISIBLE);

            }

             enddate = originalFormat.parse(rechargeModel.getCreated_at());

             formattedDate = targetFormat.format(startDte);
             formattedDate1 = targetFormat2.format(startDte);

            Log.e("formattedDate",formattedDate);
            Log.e("formattedDate123",formattedDate1);

        } catch (ParseException e) {
            e.printStackTrace();
        }*/


        holder.tvDate.setText(formattedDate);
        holder.tvGetTrans.setText("add to wallet From : "+rechargeModel.getPay_type());
        holder.tvTransId.setText("Transaction Id : "+rechargeModel.getTransaction_id());
        holder.tvTime.setText(formattedDate1);
        if (rechargeModel.getAmount_status().equals("Credit")){
            holder.tvPrice.setText("+"+ MainActivity.currency_sign +rechargeModel.getAmount());
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.tvPrice.setText("-"+ MainActivity.currency_sign +rechargeModel.getAmount());
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.red));
        }

        holder.setIsRecyclable(false);


    }

    @Override
    public int getItemCount() {
        return recharge_models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView tvDate;
        TextView tvGetTrans,tvTransId,tvTime,tvPrice;


        public holder(@NonNull View itemView) {
            super(itemView);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvGetTrans=itemView.findViewById(R.id.tvGetTrans);
            tvTransId=itemView.findViewById(R.id.tvTransId);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvPrice=itemView.findViewById(R.id.tvPrice);

        }
    }


    public static long daysBetween(Date startDate, Date endDate) {
        Calendar sDate = getDatePart(startDate);
        Calendar eDate = getDatePart(endDate);

        long daysBetween = 0;
        while (sDate.before(eDate)) {
            sDate.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        Log.e("daysBetween", String.valueOf(daysBetween));
        return daysBetween;
    }

    public static Calendar getDatePart(Date date) {
        Calendar cal = Calendar.getInstance();       // get calendar instance
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);            // set hour to midnight
        cal.set(Calendar.MINUTE, 0);                 // set minute in hour
        cal.set(Calendar.SECOND, 0);                 // set second in minute
        cal.set(Calendar.MILLISECOND, 0);            // set millisecond in second


        Log.e("cal",cal.toString());
        return cal;                                  // return the date part
    }

}
