package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.About_Model;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_About extends RecyclerView.Adapter<Adapter_About.holder> {

    Context context;
    List<About_Model> aboutModelList;

    public Adapter_About(Context context, List<About_Model> aboutModelList) {
        this.context = context;
        this.aboutModelList = aboutModelList;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_about, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        holder.AboutQuestion.setText(aboutModelList.get(i).getQuestion());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.AboutAnswer.setText(Html.fromHtml(aboutModelList.get(i).getAnswer(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.AboutAnswer.setText(Html.fromHtml(aboutModelList.get(i).getAnswer()));
        }



    }

    @Override
    public int getItemCount() {
        return aboutModelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView AboutQuestion , AboutAnswer;

        public holder(@NonNull View itemView) {
            super(itemView);

            AboutQuestion =itemView.findViewById(R.id.AboutQuestion);
            AboutAnswer = itemView.findViewById(R.id.AboutAnswer);


        }
    }
}
