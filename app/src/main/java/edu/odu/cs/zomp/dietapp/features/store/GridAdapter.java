package edu.odu.cs.zomp.dietapp.features.store;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;


public class GridAdapter extends BaseAdapter {

    String[] categories = new String[]{"Weapons", "Armor", "Sheilds", "Items"};

    private Context context;

    public GridAdapter(Context context) {
        this.context = context;
    }

    @Override public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(context);
            tv.setLayoutParams(new GridView.LayoutParams(85, 85));
        }
        else {
            tv = (TextView) convertView;
        }

        tv.setText(categories[position]);
        return tv;
    }
}
