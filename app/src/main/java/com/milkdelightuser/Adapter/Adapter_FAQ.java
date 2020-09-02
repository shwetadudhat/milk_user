package com.milkdelightuser.Adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Faq_Model;
import com.milkdelightuser.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_FAQ extends RecyclerView.Adapter<Adapter_FAQ.holder> {

    Context context;
    List<Faq_Model> faq_modelList;
    int time=500;

    public Adapter_FAQ(Context context, List<Faq_Model> faq_modelList) {
        this.context = context;
        this.faq_modelList = faq_modelList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_faq, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        holder.question.setText(faq_modelList.get(i).getQuestion());
        holder.answer.setText(faq_modelList.get(i).getAnswer());

        holder.ivDwn.setTag("");

        holder.ivDwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (holder.ivDwn.getTag()!=null && holder.ivDwn.getTag().toString().equals("180")){
                    ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation",180, 0);
                    anim.setDuration(time);
                    anim.start();
                    holder.ivDwn.setTag("");
                    holder.answer.setVisibility(View.GONE);


                }  else {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(view, "rotation",0,  180);
                    anim.setDuration(time);
                    anim.start();
                    holder.ivDwn.setTag(180+"");
                    holder.answer.setVisibility(View.VISIBLE);

                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return faq_modelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView question , answer;
        ImageView ivDwn;

        public holder(@NonNull View itemView) {
            super(itemView);

            question =itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            ivDwn = itemView.findViewById(R.id.ivDwn);

        }
    }
}
