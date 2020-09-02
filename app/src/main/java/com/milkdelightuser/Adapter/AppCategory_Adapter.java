package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.milkdelightuser.Model.AppCategory_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseURL;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class AppCategory_Adapter extends RecyclerView.Adapter<AppCategory_Adapter.holder> {

    List<AppCategory_Model> appCategoryModels;
    Context context;

    public AppCategory_Adapter(List<AppCategory_Model> appCategoryModels) {
        this.appCategoryModels = appCategoryModels;
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
        final AppCategory_Model appCategory_model = appCategoryModels.get(i);
        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + appCategory_model. getCategory_image())

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.city_image);
        holder.textView12.setText(appCategory_model.getCategory_name());
    }

    @Override
    public int getItemCount() {
        return appCategoryModels.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        TextView textView12;
        ImageView city_image;

        public holder(@NonNull View itemView) {
            super(itemView);

            city_image =itemView.findViewById(R.id.image_appcat);
            textView12 = itemView.findViewById(R.id.text_appcat);
        }
    }
}
