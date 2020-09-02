package com.milkdelightuser.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.milkdelightuser.Model.Home_Banner2_model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.BaseURL;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;

public class Home_Banner2_Adapter extends PagerAdapter {

    List<Home_Banner2_model> homeBanner2Models;
    Activity context;


    public Home_Banner2_Adapter(FragmentActivity activity, List<Home_Banner2_model> homeModelModels2) {

        this.homeBanner2Models = homeModelModels2;
        this.context = activity;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public int getCount() {
        return homeBanner2Models.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.listitem_homebanner2,view,false);

        ImageView banner=imageLayout.findViewById(R.id.image_homebannner);

        final Home_Banner2_model home_model_model = homeBanner2Models.get(position);

       /* Picasso.with(context)
                .load(BaseURL.IMG_CATEGORY_URL + home_model_model.getBanner_image()).placeholder(R.drawable.vege)
                .into(banner);*/

        view.addView(imageLayout, 0);

        return imageLayout;
    }





}