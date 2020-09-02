package com.milkdelightuser.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.milkdelightuser.Model.Menu_Model;
import com.milkdelightuser.R;

import java.util.ArrayList;

import androidx.core.content.ContextCompat;

public class Adapter_menu1 extends ArrayAdapter<Menu_Model> {
    private int mSelectedItem=-1;
    private final Context context;
    ArrayList<Menu_Model> nav_item;

    public Adapter_menu1(Context context, int resource, ArrayList<Menu_Model> nav_item) {
        super(context, resource, nav_item);
        this.context = context;
        this.nav_item = nav_item;
    }
    public int getSelectedItem() {
        return mSelectedItem;
    }
    public void setSelectedItem(int selectedItem) {
        mSelectedItem = selectedItem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Menu_Model user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_customrow, parent, false);
        }
        // Lookup view for data population
        ImageView imgHome=convertView.findViewById(R.id.img_home);
        View view1=convertView.findViewById(R.id.view1);
        TextView tvHome = (TextView) convertView.findViewById(R.id.tvHome1);
        // Populate the data into the template view using the data object
        imgHome.setImageResource(user.getImage());
        tvHome.setText(user.getTitle());
        // Return the completed view to render on screen

        if(position == mSelectedItem){
            tvHome.setTextColor(context.getResources().getColor(R.color.main_clr));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgHome.setColorFilter(ContextCompat.getColor(context, R.color.main_clr), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            view1.setBackgroundColor(context.getResources().getColor(R.color.main_clr));

        }
        else
        {
            tvHome.setTextColor(context.getResources().getColor(R.color.small_title_clr));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgHome.setColorFilter(ContextCompat.getColor(context, R.color.small_title_clr), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            view1.setBackgroundColor(context.getResources().getColor(R.color.white));

        }

        if (mSelectedItem==-1){
            tvHome.setTextColor(context.getResources().getColor(R.color.small_title_clr));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                imgHome.setColorFilter(ContextCompat.getColor(context, R.color.small_title_clr), android.graphics.PorterDuff.Mode.SRC_IN);
            }
            view1.setBackgroundColor(context.getResources().getColor(R.color.white));

        }



        return convertView;
        //Get item TextView
    }
}

