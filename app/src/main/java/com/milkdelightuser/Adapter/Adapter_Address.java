package com.milkdelightuser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.milkdelightuser.Model.Address_Model;
import com.milkdelightuser.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Address extends RecyclerView.Adapter<Adapter_Address.holder> {

    Context context;
    List<Address_Model> addressModelList;
    int row_index=-1;

    private EventListener mEventListener;

    public Adapter_Address(Context context, List<Address_Model> addressModelList) {
        this.context=context;
        this.addressModelList=addressModelList;
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
    public holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_address, null, false);
        context = viewGroup.getContext();
        return new holder(view);   }

    @Override
    public void onBindViewHolder(@NonNull final holder holder, int i) {

        holder.adrName.setText(addressModelList.get(i).getUser_name());
        holder.adrNmbr.setText(addressModelList.get(i).getPhone_nmbr());
        holder.adr.setText(addressModelList.get(i).getArea_id());

       String status=addressModelList.get(i).getStatus();
       Log.e("status",status);
       if (status.equals("1")){
           Log.e("iff",status);
           row_index=i;
           holder.ChkSelect.setChecked(true);
       }else{
           Log.e("elseee",status);
           holder.ChkSelect.setChecked(false);
       }

        holder.llEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEventListener != null)
                    mEventListener.onItemEditClicked(i,addressModelList.get(i).getAddress_id());

               /* Intent intent=new Intent(context, Edit_Address.class);
                intent.putExtra("action","edit");
                intent.putExtra("id",addressModelList.get(i).getAddress_id());
                context.startActivity(intent);*/

                /*Intent intent=new Intent(context, Edit_Address.class);
                intent.putExtra("action","edit");
                intent.putExtra("id",addressModelList.get(i).getAddress_id());
                context.startActivity(intent);
                ((Activity)context).finish();*/

            }
        });

        holder.llDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mEventListener != null)
                    mEventListener.onItemDeleteClicked(i,addressModelList.get(i).getAddress_id());



            }
        });

        holder.ivAdrEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llEdit.callOnClick();
            }
        });

        holder.ivAdrDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llDel.callOnClick();
            }
        });

        holder.ChkSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.llSelect.callOnClick();
            }
        });


        holder.llSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=i;
                notifyDataSetChanged();
                if (mEventListener != null)
                    mEventListener.onItemViewClicked(i,addressModelList.get(i).getAddress_id());

            }
        });


       /* holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index=i;
                notifyDataSetChanged();
                if (mEventListener != null)
                    mEventListener.onItemViewClicked(i,addressModelList.get(i).getAddress_id());

            }
        });*/
        if(row_index==i){
            /* selected*/
            status="1";
            holder.ChkSelect.setChecked(true);
        }
        else
        {
            /*not selected*/
            status="0";
            holder.ChkSelect.setChecked(false);
        }


    }



    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class holder extends RecyclerView.ViewHolder {

        LinearLayout llEdit,llDel,llMain;
        ImageView ivAdrEdit,ivAdrDel;
        TextView adrName,adr,adrNmbr;
        RelativeLayout llSelect;
        CheckBox ChkSelect;


        public holder(@NonNull View itemView) {
            super(itemView);

            llMain =itemView.findViewById(R.id.llMain);
            llEdit =itemView.findViewById(R.id.llEdit);
            llDel = itemView.findViewById(R.id.llDel);
            ivAdrEdit = itemView.findViewById(R.id.ivAdrEdit);
            ivAdrDel = itemView.findViewById(R.id.ivAdrDel);
            adrName = itemView.findViewById(R.id.adrName);
            adr = itemView.findViewById(R.id.adr);
            adrNmbr = itemView.findViewById(R.id.adrNmbr);
            llSelect = itemView.findViewById(R.id.llSelect);
            ChkSelect = itemView.findViewById(R.id.ChkSElect);


        }
    }

    public void removeAt(int position) {
        addressModelList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, addressModelList.size());
    }

    public void AddData(List<Address_Model> viewModels) {
        addressModelList = new ArrayList<>();
        addressModelList.addAll(viewModels);
        notifyDataSetChanged();
    }

    public interface EventListener {
       // void onItemViewClick(int position);

        void onItemDeleteClicked(int i, String address_id);
        void onItemViewClicked(int i, String address_id);
        void onItemEditClicked(int i, String address_id);

    }

    public void setEventListener(EventListener eventlistener) {
        this.mEventListener = eventlistener;
    }

}
