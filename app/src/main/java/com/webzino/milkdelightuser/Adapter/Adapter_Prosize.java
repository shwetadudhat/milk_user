package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.Model.App_Product_Model;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import static com.webzino.milkdelightuser.utils.Global.setTypeFaceBold;
import static com.webzino.milkdelightuser.utils.Global.setTypeFaceRegular;

public class Adapter_Prosize extends RecyclerView.Adapter<Adapter_Prosize.holder> {

    Context context;
    ArrayList<App_Product_Model> stringArrayList;
    private EventListener mEventListener;
    int row_index=-1;
    String itemtype;

    public Adapter_Prosize(Context context, ArrayList<App_Product_Model> stringArrayList) {
        this.context = context;
        this.stringArrayList = stringArrayList;



    }

    public int getSelectedItem() {
        return row_index;
    }
    public void setSelectedItem(int selectedItem) {
        row_index = selectedItem;
        Log.e("clearrr11","clear"+row_index);
    }

    @NonNull
    @Override
    public Adapter_Prosize.holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_productsize, null, false);
        context = viewGroup.getContext();
        return new holder(view);    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {


        holder.tvProsize.setText(stringArrayList.get(i).getQty()+" "+stringArrayList.get(i).getUnit());
        holder.tvProsize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=i;
                notifyDataSetChanged();
                itemtype=holder.tvProsize.getText().toString();
                if (mEventListener != null)
                    mEventListener.onItemViewClicked(i,itemtype);


            }
        });
        if(row_index==i){
            holder.tvProsize.setBackground(context.getDrawable(R.drawable.home_add));
            holder.tvProsize.setTextColor(context.getColor(R.color.main_clr));
            setTypeFaceBold(holder.tvProsize);
            // editor.putBoolean("locked", true).apply();
        }
        else
        {
            holder.tvProsize.setBackground(context.getDrawable(R.drawable.bg_grey));
            holder.tvProsize.setTextColor(context.getColor(R.color.grey));
            setTypeFaceRegular(holder.tvProsize);
            // editor.putBoolean("locked", false).apply();
        }

        if (row_index==-1){
            Log.e("clearrr","clear");
            holder.tvProsize.setBackground(context.getDrawable(R.drawable.bg_grey));
            holder.tvProsize.setTextColor(context.getColor(R.color.grey));
            setTypeFaceRegular(holder.tvProsize);

        }
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();

    }



    public class holder extends RecyclerView.ViewHolder {

        TextView tvProsize;

        public holder(@NonNull View itemView) {
            super(itemView);

            tvProsize =itemView.findViewById(R.id.tvProsize);

        }
    }

    public interface EventListener {
        void onItemViewClicked(int position, String itemtype);
    }

    public EventListener getEventListener() {
        return mEventListener;
    }

    public  void setEventListener(EventListener eventListener) {
        mEventListener = eventListener;
    }
}
