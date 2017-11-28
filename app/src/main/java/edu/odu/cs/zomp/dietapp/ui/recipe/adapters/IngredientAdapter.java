package edu.odu.cs.zomp.dietapp.ui.recipe.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;

public class IngredientAdapter extends BaseAdapter {

    private final Context context;
    private List<String> ingredients;

    public IngredientAdapter(Context context) {
        this.context = context;
    }

    @Override public int getCount() { return ingredients.size(); }

    // Returns a stat name, based on the switch mapping. This is the order the attributes will be displayed in
    @Override public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_ingredient, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(ingredients.get(position));
        return convertView;
    }

    public void addIngredient(List<String> ingredients) {
        if (this.ingredients == null)
            this.ingredients = new ArrayList<>();

        this.ingredients.addAll(ingredients);
    }

    public void addIngredient(String ingredient) {
        if (this.ingredients == null)
            this.ingredients = new ArrayList<>();

        this.ingredients.add(ingredient);
    }

    public class ViewHolder {
        @BindView(R.id.item_ingredient_name) TextView name;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
