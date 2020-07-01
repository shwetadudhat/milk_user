package com.vaapglkns.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vaapglkns.R;
import com.vaapglkns.objects.Test;
import com.vaapglkns.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by om on 7/7/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder> {
    public List<Test.Array> data = new ArrayList<>();
    Eventlistener mEventlistener;
    Context context;

    public CommentsAdapter(Context c) {
        this.context = c;
    }

    public void addAll(List<Test.Array> mData) {
        data.addAll(mData);
        notifyDataSetChanged();
    }

    public Test.Array getItem(int position) {

        return data.get(position);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View v = inflater.inflate(R.layout.comments_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Test.Array item = data.get(position);
        holder.tvFirstName.setText(Utils.nullSafe("" + item.userId));
        holder.tvFirstContent.setText(Utils.nullSafe("" + item.content));
//        holder.tvslidecaption.setText(Utils.nullSafe("" + item.slidecaption));
//
//        if (!StringUtils.isEmpty(item.slideimage)) {
//            imageLoader.displayImage(item.slideimage, holder.imgView);
//        }
        holder.tvFirstReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEventlistener != null) {
                    mEventlistener.onFirstCommentviewClick(holder.llComments);
                }
            }
        });

//        LinearLayout TimeLinearLayout = new LinearLayout(context);
//
//        TimeLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//        TimeLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
//        holder.llSecondItem.setBackgroundColor(Color.BLUE);
//
//        holder.llSecondItem.removeAllViews();
//        holder.llSecondItem.addView(TimeLinearLayout);
//
        LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        if (item.parent.equalsIgnoreCase("1")) {

            holder.llSecondItem.removeAllViews();
//            for (int j = 0; j < 1; j++) {
            View convertView = layoutinflater.inflate(R.layout.comments_item_second, null, false);
//                convertView.setId(j);

            TextView tvSecondName = (TextView) convertView.findViewById(R.id.tvSecondName);
            TextView tvSecondContent = (TextView) convertView.findViewById(R.id.tvSecondContent);
            TextView tvSecondReply = (TextView) convertView.findViewById(R.id.tvSecondReply);
            final LinearLayout llSecondComments = (LinearLayout) convertView.findViewById(R.id.llSecondComments);

            tvSecondName.setText(Utils.nullSafe(item.userId));
            tvSecondContent.setText(Utils.nullSafe(item.content));
            //set image here
            holder.llSecondItem.addView(convertView);

            tvSecondReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEventlistener != null) {
                        mEventlistener.onSecondCommentviewClick(llSecondComments);
                    }
                }
            });


//            }
        }

        if (item.parent2.equalsIgnoreCase("3")) {
//            LayoutInflater layoutinflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            holder.llThirdItem.removeAllViews();
//            for (int j = 0; j < 1; j++) {
            View convertView = layoutinflater.inflate(R.layout.comments_item_third, null, false);
//                convertView.setId(j);

            TextView tvSecondName = (TextView) convertView.findViewById(R.id.tvSecondName);
            TextView tvSecondContent = (TextView) convertView.findViewById(R.id.tvSecondContent);
            TextView tvSecondReply = (TextView) convertView.findViewById(R.id.tvSecondReply);
            final LinearLayout llSecondComments = (LinearLayout) convertView.findViewById(R.id.llSecondComments);

            tvSecondName.setText(Utils.nullSafe(item.userId));
            tvSecondContent.setText(Utils.nullSafe(item.content));
            //set image here
            holder.llThirdItem.addView(convertView);

            tvSecondReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mEventlistener != null) {
                        mEventlistener.onThirdCommentviewClick(llSecondComments);
                    }
                }
            });


//            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llSecondItem)
        LinearLayout llSecondItem;
        @BindView(R.id.llThirdItem)
        LinearLayout llThirdItem;
        @BindView(R.id.tvFirstContent)
        TextView tvFirstContent;
        @BindView(R.id.tvFirstName)
        TextView tvFirstName;
        @BindView(R.id.llComments)
        LinearLayout llComments;
        @BindView(R.id.tvFirstReply)
        TextView tvFirstReply;
        @BindView(R.id.container)
        View container;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    public interface Eventlistener {

        void onItemviewClick(int position);

        void onFirstCommentviewClick(LinearLayout linearLayout);

        void onSecondCommentviewClick(LinearLayout linearLayout);

        void onThirdCommentviewClick(LinearLayout linearLayout);
    }

    public void setEventlistener(Eventlistener eventlistener) {

        this.mEventlistener = eventlistener;
    }

}
