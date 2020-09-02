package com.milkdelightuser.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.milkdelightuser.Model.Select_organistionmodel;
import com.milkdelightuser.R;

import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Select_organisationadapter extends ArrayAdapter<String> {

private final LayoutInflater mInflater;
private final Context mContext;
private final List<Select_organistionmodel> items;
private final int mResource;

public Select_organisationadapter(@NonNull Context context, @LayoutRes int resource,
                                  @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        items = objects;

}

        @Override
public View getDropDownView(int position, @Nullable View convertView,
                            @NonNull ViewGroup parent) {

        return createItemView(position, convertView, parent);



}

@Override
public @NonNull
View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
        }

            private View createItemView(int position, View convertView, ViewGroup parent){

             final View view = mInflater.inflate(mResource, parent, false);
                Select_organistionmodel listdatapos = items.get(position);

                TextView list_data = (TextView) view.findViewById(R.id.list_data);

                list_data.setText(listdatapos.getPosition_name());

                      return view;
        }
        }