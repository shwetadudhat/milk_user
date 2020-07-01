package com.vaapglkns.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vaapglkns.R;
import com.vaapglkns.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by om on 7/7/2017.
 */

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.MyViewHolder> {
    //    public List<ResponseData.Slider> sliderData = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public DummyAdapter(Context c) {
        this.context = c;
    }

    //    public void addAll(List<ResponseData.Slider> mData) {
//        sliderData.addAll(mData);
//        notifyDataSetChanged();
//    }
//    public ResponseData.Slider getItem(int position){
//
//        return  sliderData.get(position);
//    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.transaction_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

//        final ResponseData.Slider slider = sliderData.get(position);
//        holder.tvslidetitle.setText(Utils.nullSafe("" + slider.slidetitle));
//        holder.tvslidecaption.setText(Utils.nullSafe("" + slider.slidecaption));
//
//        if (!StringUtils.isEmpty(slider.slideimage)) {
//            imageLoader.displayImage(slider.slideimage, holder.imgView);
//        }
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mEventlistener != null) {
//
//                    mEventlistener.onItemviewClick(position);
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        //        @BindView(R.id.tvJobId)
//        TextView tvJobId;
//        @BindView(R.id.tvNeedRev)
//        TextView tvslidecaption;
//        @BindView(R.id.tvSupervisor)
//        TextView tvSupervisor;
//        @BindView(R.id.tvDate)
//        TextView tvDate;
//        @BindView(R.id.tvStatus)
//        TextView tvStatus;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {

        void onItemviewClick(int position);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}
