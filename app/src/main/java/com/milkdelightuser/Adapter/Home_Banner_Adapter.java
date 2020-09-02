package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.milkdelightuser.Model.Home_Model_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseURL;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


public class Home_Banner_Adapter extends RecyclerView.Adapter<Home_Banner_Adapter.holder> {

    List<Home_Model_Model> home_model_models;
    Context context;

    public Home_Banner_Adapter(FragmentActivity activity, List<Home_Model_Model> home_model_models) {
        this.home_model_models = home_model_models;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_homebanner, viewGroup, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        final Home_Model_Model selectCityModel = home_model_models.get(i);

        Glide.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + selectCityModel. getBanner_image())

                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .into(holder.city_image);

       // holder.textView12.setText(selectCityModel.getCity_name());



    }

    @Override
    public int getItemCount() {
        return home_model_models.size();
    }

    public static class holder extends RecyclerView.ViewHolder {

        public ImageView city_image;
        TextView textView12;
       // ImageView city_image;

        public holder(@NonNull View itemView) {
            super(itemView);

            city_image =itemView.findViewById(R.id.image_banner2);
          //  textView12 = itemView.findViewById(R.id.city);
        }
    }
}
