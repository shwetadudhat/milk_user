package com.webzino.milkdelightuser.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.webzino.milkdelightuser.R;
import com.webzino.milkdelightuser.utils.Global;
import com.webzino.milkdelightuser.utils.Spinner1;

import org.json.JSONArray;

import java.util.ArrayList;

public class SpinnerAdapter extends BaseAdapter implements Filterable {
    // private Context mContext;
    private LayoutInflater infalter;
    private ArrayList<Spinner1> data = new ArrayList<Spinner1>();
    private ArrayList<Spinner1> dataSource = new ArrayList<Spinner1>();

    public SpinnerAdapter(Context c) {
        infalter = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // mContext = c;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Spinner1 getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    boolean isFilterable = false;

    public void setFilterable(boolean isFilterable) {
        this.isFilterable = isFilterable;
    }

    public void addAll(ArrayList<Spinner1> files) {

        try {
            this.data.clear();
            this.data.addAll(files);

            if (isFilterable) {
                this.dataSource.clear();
                this.dataSource.addAll(files);
            }

        } catch (Exception e) {
            Global.sendExceptionReport(e);
        }

        notifyDataSetChanged();
    }

    public ArrayList<Spinner1> getSelectedAll() {
        ArrayList<Spinner1> data = new ArrayList<Spinner1>();

        for (Spinner1 spinner1 : this.data) {
            if (spinner1.isSelected) {
                data.add(spinner1);
            }
        }

        Log.e("dataaaa",data.toString());

        return data;
    }

    public String getSelectedIds() {
        String str = "";

        for (Spinner1 spinner1 : data) {
            if (spinner1.isSelected) {
                str = str + spinner1.ID + ",";
            }
        }

        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public ArrayList<String> getSelectedIdList() {
        ArrayList<String> data = new ArrayList<String>();

        for (Spinner1 spinner1 : this.data) {
            if (spinner1.isSelected) {
                data.add(spinner1.ID);
            }
        }

        return data;
    }

    public JSONArray getSelectedIdArray() {
        JSONArray data = new JSONArray();

        try {
            for (Spinner1 spinner1 : this.data) {
                if (spinner1.isSelected) {
                    data.put(spinner1.ID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    public String getSelectedTitle() {
        String str = "";

        for (Spinner1 spinner1 : data) {
            if (spinner1.isSelected) {
                str = str + spinner1.title + ", ";
            }
        }

        str = str.trim();
        if (str.length() > 0) {
            str = str.substring(0, str.length() - 1);
        }

        return str;
    }

    public ArrayList<Spinner1> getAll() {
        return data;
    }

    public boolean isSelectedAll() {

        for (Spinner1 spinner1 : this.data) {
            if (!spinner1.isSelected) {
                return false;
            }
        }

        return true;
    }

    public int getSelectedCount() {

        int cnt = 0;

        for (Spinner1 spinner1 : this.data) {
            if (spinner1.isSelected) {
                cnt = cnt + 1;
            }
        }

        return cnt;
    }

    public boolean isSelectedAtleastOne() {

        for (Spinner1 spinner1 : this.data) {
            if (spinner1.isSelected) {
                return true;
            }
        }

        return false;
    }

    public boolean isSelected(int position) {
        return data.get(position).isSelected;
    }

    public void changeSelection(int position, boolean isMultiSel) {

        for (int i = 0; i < data.size(); i++) {
            if (position == i) {
                if (data.get(i).isSelected==false){
                    data.get(i).isSelected = !data.get(i).isSelected;
                }
                Log.e("data==>1",String.valueOf(data.get(i).isSelected));
                Log.e("data==>2",String.valueOf(!data.get(i).isSelected));
            } else if (!isMultiSel) {
                data.get(i).isSelected = false;
                Log.e("falseee","falsee");

            }
        }

        Log.e("dattalist",data.toString());

        notifyDataSetChanged();
    }

    public void setSelection(int position) {
        for (int i = 0; i < data.size(); i++) {
            if (position == i) {
                data.get(i).isSelected = true;
            } else {
                data.get(i).isSelected = false;
            }
        }

        notifyDataSetChanged();
    }

    public void selectAll(boolean selectall) {
        for (int i = 0; i < data.size(); i++) {
            data.get(i).isSelected = selectall;
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();

            convertView = infalter.inflate(R.layout.spinner_item, null);
            holder.tvMenuTitle = (TextView) convertView.findViewById(R.id.tvSpinnerTitle);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        try {
            holder.tvMenuTitle.setText(data.get(position).title);
        } catch (Exception e) {
            Global.sendExceptionReport(e);
        }

        return convertView;
    }

    public class ViewHolder {
        TextView tvMenuTitle;
    }

    @Override
    public Filter getFilter() {

        if (isFilterable) {
            return new PTypeFilter();
        }

        return null;
    }

    private class PTypeFilter extends Filter {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.

            data = (ArrayList<Spinner1>) results.values;
            if (data != null) {
                notifyDataSetChanged();
            } else {
                data = dataSource;
                notifyDataSetChanged();
            }
        }

        protected FilterResults performFiltering(CharSequence prefix) {
            // NOTE: this function is *always* called from a background thread,
            // and
            // not the UI thread.

            FilterResults results = new FilterResults();
            ArrayList<Spinner1> new_res = new ArrayList<Spinner1>();
            if (prefix != null && prefix.toString().length() > 0) {
                for (int index = 0; index < dataSource.size(); index++) {

                    try {
                        Spinner1 si = dataSource.get(index);

                        if (si.title.toLowerCase().contains(
                                prefix.toString().toLowerCase())) {
                            new_res.add(si);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                results.values = new_res;
                results.count = new_res.size();

            } else {
                Log.e("", "Called synchronized view");

                results.values = dataSource;
                results.count = dataSource.size();

            }
            return results;
        }
    }
}

