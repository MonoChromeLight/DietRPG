package edu.odu.cs.zomp.dietapp.ui.character.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;

public class AttributeAdapter extends BaseAdapter {

    private final Context context;
    private final Map<String, Integer> baseAttributes;
    private final Map<String, Integer> augmentedAttributes;

    public AttributeAdapter(Context context, Map<String, Integer> baseAttributes, Map<String, Integer> augmentedAttributes) {
        this.context = context;
        this.baseAttributes = baseAttributes;
        this.augmentedAttributes = augmentedAttributes;
    }

    @Override public int getCount() { return augmentedAttributes.size(); }

    // Returns a stat name, based on the switch mapping. This is the order the attributes will be displayed in
    @Override public Object getItem(int position) {
        return augmentedAttributes.get(Character.statNames[position]);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_stat, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String attr = Character.attributeNames[position];
        holder.attributeName.setText(attr);
        holder.attributeValue.setText(String.format(Locale.US, "%d", augmentedAttributes.get(attr)));
        if (augmentedAttributes.get(attr) > baseAttributes.get(attr))
            holder.attributeValue.setTextColor(ContextCompat.getColor(context, R.color.googleGreen));
        else if (augmentedAttributes.get(attr) < baseAttributes.get(attr))
            holder.attributeValue.setTextColor(ContextCompat.getColor(context, R.color.googleRed));

        return convertView;
    }

    public class ViewHolder {
        @BindView(R.id.item_attribute_statName) TextView attributeName;
        @BindView(R.id.item_attribute_value) TextView attributeValue;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
