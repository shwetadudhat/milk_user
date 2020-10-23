package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Activity.CatProductListing;
import com.webzino.milkdelightuser.Model.AppCategory_Model;
import com.webzino.milkdelightuser.utils.Global;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_HomeCat extends RecyclerView.Adapter<Adapter_HomeCat.holder>   {

    Context context;
    List<AppCategory_Model> appCategoryModelList;
    private List<AppCategory_Model> arraylist;


    public Adapter_HomeCat(Context context, List<AppCategory_Model> appCategoryModelList) {
        this.context = context;
        this.appCategoryModelList = appCategoryModelList;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(appCategoryModelList);
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_homecategory, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {
        /*Glide.with(context)
                .load(appCategoryModelList.get(i).getCategory_image())
                .into(holder.image_appcat);*/

        Global.loadGlideImage(context,appCategoryModelList.get(i).getCategory_image(),appCategoryModelList.get(i).getCategory_image(),holder.image_appcat);

        holder.text_appcat.setText(appCategoryModelList.get(i).getCategory_name());

        holder.llCat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, CatProductListing.class);
                intent.putExtra("category_id",appCategoryModelList.get(i).getCategory_id());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return appCategoryModelList.size();
    }



    public class holder extends RecyclerView.ViewHolder {

        TextView text_appcat;
        ImageView image_appcat;
        LinearLayout llCat;

        public holder(@NonNull View itemView) {
            super(itemView);

            text_appcat =itemView.findViewById(R.id.text_appcat);
            image_appcat = itemView.findViewById(R.id.image_appcat);
            llCat = itemView.findViewById(R.id.llCat);


        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        appCategoryModelList.clear();
        if (charText.length() == 0) {
            appCategoryModelList.addAll(arraylist);
        } else {
            for (AppCategory_Model wp : arraylist) {
                if (wp.getCategory_name().toLowerCase(Locale.getDefault()).contains(charText) ) {
                    appCategoryModelList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
