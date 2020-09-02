package com.milkdelightuser.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Plan_List_Model;
import com.milkdelightuser.R;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Plan_List_Adapter extends RecyclerView.Adapter<Plan_List_Adapter.holder> {

    List<Plan_List_Model> plan_list_models;
    Context context;
    String date;

    public Plan_List_Adapter(List<Plan_List_Model> plan_list_models) {
        this.plan_list_models = plan_list_models;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_planlist, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Plan_List_Model planListModel = plan_list_models.get(i);



        holder.textView12.setText(planListModel.getPlans());

        holder.textView12.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {

                holder.textView12.setBackgroundColor(context.getResources().getColor(R.color.black));
            }
        });
    }

    @Override
    public int getItemCount() {
        return plan_list_models.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView textView12 ,calenderview;
        ImageView city_image;
        DatePickerDialog datePickerDialog;
        Calendar calendar;
        int year;
        int month;
        int dayofmonth;

        public holder(@NonNull View itemView) {
            super(itemView);

            textView12 = itemView.findViewById(R.id.planlist);

         //   calenderview= itemView.findViewById(R.id.calender_text);

        }
    }
}
