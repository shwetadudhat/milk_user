package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.milkdelightuser.Model.ImagesData;
import com.milkdelightuser.R;

import java.util.ArrayList;

import androidx.viewpager.widget.PagerAdapter;


public class Adapter_Banner extends PagerAdapter {
    Context context;
    ArrayList<ImagesData> imagesData=new ArrayList<>();;
    private EventListener mEventListener;

    public Adapter_Banner(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imagesData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setBackgroundResource(R.drawable.ic_referafriend/*imagesData.get(position).getUrl()*/);
        container.addView(view, 0);
        return container;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }


    public void  addAll(ArrayList<ImagesData> data) {
        imagesData = new ArrayList();
        imagesData.addAll(data);
        notifyDataSetChanged();
    }

    public interface EventListener {
        void onItemViewClicked(int position);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

   public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
