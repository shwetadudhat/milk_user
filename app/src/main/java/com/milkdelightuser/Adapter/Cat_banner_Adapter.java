package com.milkdelightuser.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.milkdelightuser.Model.Cat_Banner_Model;
import com.milkdelightuser.R;
import com.milkdelightuser.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import static android.content.Context.MODE_PRIVATE;
import static com.milkdelightuser.utils.Global.IMGURL_PREFS_NAME;
import static com.milkdelightuser.utils.Global.URL_DATA;

public class Cat_banner_Adapter extends PagerAdapter {

    List<Cat_Banner_Model> cat_banner_models;
    Context context;

    public Cat_banner_Adapter(Context applicationContext, List<Cat_Banner_Model> cat_banner_models) {
        this.cat_banner_models = cat_banner_models;
        this.context = applicationContext;
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
        return cat_banner_models.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = LayoutInflater.from(view.getContext()).inflate(R.layout.listitem_catbanner,view,false);
        ImageView banner=imageLayout.findViewById(R.id.image_category);
        final Cat_Banner_Model cat_banner_model = cat_banner_models.get(position);
//        Glide.with(context)
//                .load(/*BaseURL.IMG_CATEGORY_URL + */cat_banner_model.getBanner_image())
//                .into(banner);
      // banner.setImageResource(cat_banner_model.getBanner_image());
        Global.loadGlideImage(context,cat_banner_model.getBanner_image(),cat_banner_model.getBanner_image(),banner);



        view.addView(imageLayout, 0);
        return imageLayout;
    }

}
