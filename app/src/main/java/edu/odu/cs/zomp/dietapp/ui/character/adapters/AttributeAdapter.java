package edu.odu.cs.zomp.dietapp.ui.character.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.ViewHolder> {

    private final Context context;
    private final Map<String, Integer> baseAttributes;
    private final Map<String, Integer> augmentedAttributes;

    public AttributeAdapter(Context context, Map<String, Integer> baseAttributes, Map<String, Integer> augmentedAttributes) {
        this.context = context;
        this.baseAttributes = baseAttributes;
        this.augmentedAttributes = augmentedAttributes;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_stat, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        String attr = Character.attributeNames[position];
        holder.attributeName.setText(attr);

        if (augmentedAttributes.get(attr) > baseAttributes.get(attr)) {
            int difference = augmentedAttributes.get(attr) - baseAttributes.get(attr);
            holder.attributeValue.setText(String.format(Locale.US, "%d (+%d)", baseAttributes.get(attr), difference));
            holder.attributeValue.setTextColor(ContextCompat.getColor(context, R.color.googleGreen));
        } else if (augmentedAttributes.get(attr) < baseAttributes.get(attr)) {
            int difference = baseAttributes.get(attr) - augmentedAttributes.get(attr);
            holder.attributeValue.setText(String.format(Locale.US, "%d (%d)", baseAttributes.get(attr), difference));
            holder.attributeValue.setTextColor(ContextCompat.getColor(context, R.color.googleRed));
        } else {
            holder.attributeValue.setText(String.format(Locale.US, "%d", augmentedAttributes.get(attr)));
        }
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public int getItemCount() {
        return baseAttributes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_attribute_statName) TextView attributeName;
        @BindView(R.id.item_attribute_value) TextView attributeValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
