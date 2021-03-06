package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.Plan_model;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_FreqPlan extends RecyclerView.Adapter<Adapter_FreqPlan.holder> {

    Context context;
    private List<Plan_model> planModelList;
   public static int  row_index=-1;
    private EventListener mEventListener;
    /*Adapter_SubProduct.ClickPosition clickPosition;*/
    int position;


    public Adapter_FreqPlan(Context context, List<Plan_model> planModelList, int position /*Adapter_SubProduct.ClickPosition clickPosition*/) {
        this.context = context;
        this.planModelList = planModelList;
       // this.clickPosition = clickPosition;
        this.row_index = position;
    }

    public Adapter_FreqPlan(Context context, List<Plan_model> planModelList) {
        this.context = context;
        this.planModelList = planModelList;
    }

    public int getSelectedItem() {
        return row_index;
    }
    public void setSelectedItem(int selectedItem) {
        row_index = selectedItem;
    }

    public void setSelectedItem1(int position,int selectedItem) {
        row_index = selectedItem;
        this.position=position;
    }


    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_freqplan, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {


        holder.Freq.setText(planModelList.get(i).getPlans());

        if(planModelList.get(i).isSelected()){
            holder.Freq.setBackground(context.getResources().getDrawable(R.drawable.bg_fb));
            holder.Freq.setTextColor(context.getResources().getColor(R.color.white));
        }else{
            holder.Freq.setBackgroundColor(context.getResources().getColor(R.color.bg_clr));
            holder.Freq.setTextColor(context.getResources().getColor(R.color.main_clr));
        }


        holder.Freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=i;
                holder.Freq.setSelected(true);
        //        clickPosition.getPosition(i);

                if (mEventListener != null) {
                    mEventListener.onItemViewClicked1(i, planModelList.get(i).getId(),position);
                }
                notifyDataSetChanged();

            }
        });

       /* if (mEventListener != null) {
            mEventListener.onItemViewClicked1(i, planModelList.get(i).getId(),position);
        }*/


        if(row_index==i){
            holder.Freq.setSelected(true);
            holder.Freq.setBackground(context.getResources().getDrawable(R.drawable.bg_fb));
            holder.Freq.setTextColor(context.getResources().getColor(R.color.white));
        }
        else
        {
            holder.Freq.setSelected(false);
            holder.Freq.setBackgroundColor(context.getResources().getColor(R.color.bg_clr));
            holder.Freq.setTextColor(context.getResources().getColor(R.color.main_clr));

        }

    }

    @Override
    public int getItemCount() {
        return planModelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView Freq ;

        public holder(@NonNull View itemView) {
            super(itemView);

            Freq =itemView.findViewById(R.id.Freq);

        }
    }

    public interface EventListener {
        void onItemViewClicked(int position, int planId);
        void onItemViewClicked1(int position, int planId, int position1);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
