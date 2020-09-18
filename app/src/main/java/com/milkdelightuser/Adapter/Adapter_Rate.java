package com.milkdelightuser.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.milkdelightuser.Model.Rate_Model;
import com.milkdelightuser.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.milkdelightuser.utils.Global.covertTimeToText;


public class Adapter_Rate extends RecyclerView.Adapter<Adapter_Rate.holder> {

    Context context;
    List<Rate_Model> rateModelList;

    public Adapter_Rate(Context context, List<Rate_Model> rateModelList) {
        this.context = context;
        this.rateModelList = rateModelList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_review, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateToStr = format.format(today);
     //   System.out.println(dateToStr);
        Log.e("dateToStr",dateToStr);

        Date ratedate = null;
        try {
             ratedate = format.parse(rateModelList.get(i).getCreated_at());
            String dateToStr1 = format.format(ratedate);
            //   System.out.println(dateToStr);
            Log.e("dateToStr11",dateToStr1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long diff = today.getTime() - ratedate.getTime();
        /*long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;*/

        Log.e("diff", String.valueOf(diff));


//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
//            long time = sdf.parse("2016-01-24T16:00:00.000Z").getTime();
            long time =  format.parse(rateModelList.get(i).getCreated_at()).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            Log.e("agoooo", (String) ago);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long seconds=TimeUnit.MILLISECONDS.toSeconds(today.getTime() - ratedate.getTime());
        long minutes= TimeUnit.MILLISECONDS.toMinutes(today.getTime() - ratedate.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(today.getTime() - ratedate.getTime());
        long days=TimeUnit.MILLISECONDS.toDays(today.getTime() - ratedate.getTime());


        String txt;

        if(seconds<60){
            txt=seconds+" seconds ago";
        }else if (minutes<60){
            txt=minutes +"mins ago";
        }else if (hours<24){
            txt=hours+" hours ago";
        } else {
            txt=days+" days ago";
        }

        holder.tvRateDesc.setText(rateModelList.get(i).getRate_desc());
        holder.tvRateTime.setText(txt);
//        holder.tvRateTime.setText(covertTimeToText(rateModelList.get(i).getCreated_at()));

      //  holder.user_dp.setImageResource(rateModelList.get(i).getUser_image());
//        Glide.with(context)
//                .load(/*BaseURL.IMG_CATEGORY_URL + */rateModelList.get(i).getUser_image())
//                .into(holder.user_dp);
        holder.user_name.setText(rateModelList.get(i).getUsername());
        holder.user_rate.setRating(Float.parseFloat(rateModelList.get(i).getRating_star()));

    }

    @Override
    public int getItemCount() {
        return rateModelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        CircleImageView user_dp;
        RatingBar user_rate;
        TextView user_name , tvRateTime,tvRateDesc;

        public holder(@NonNull View itemView) {
            super(itemView);

            user_dp =itemView.findViewById(R.id.user_dp);
            user_rate = itemView.findViewById(R.id.user_rate);
            user_name = itemView.findViewById(R.id.user_name);
            tvRateTime = itemView.findViewById(R.id.tvRateTime);
            tvRateDesc = itemView.findViewById(R.id.tvRateDesc);


        }
    }

    public void AddData(List<Rate_Model> viewModels) {
        rateModelList = new ArrayList<>();
        rateModelList.addAll(viewModels);
        notifyDataSetChanged();
    }
}
