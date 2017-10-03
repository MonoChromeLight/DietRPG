package edu.odu.cs.zomp.dietapp.ui.store;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.StoreItem;


public class StoreItemsAdapter extends RecyclerView.Adapter<StoreItemsAdapter.ViewHolder> {

    Context context = null;
    List<StoreItem> items = null;

    public StoreItemsAdapter(Context context) {
        this.context = context;
        this.items = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_store_card, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        StoreItem item = items.get(position);

        holder.title.setText(item.itemTitle);
        holder.price.setText(String.format("$%.2f", item.price));


        try {
            InputStream imgStream;
            Drawable iconAsset;

            switch (item.category) {
                case "weapon":
                    imgStream = context.getAssets().open("icon_sword.png");
                    iconAsset = Drawable.createFromStream(imgStream, null);
                    holder.icon.setImageDrawable(iconAsset);
                    break;
                case "shield":
                    imgStream = context.getAssets().open("icon_shield.png");
                    iconAsset = Drawable.createFromStream(imgStream, null);
                    holder.icon.setImageDrawable(iconAsset);
                    break;
                case "potion":
                    imgStream = context.getAssets().open("icon_potion.png");
                    iconAsset = Drawable.createFromStream(imgStream, null);
                    holder.icon.setImageDrawable(iconAsset);
                    break;
                case "crystal":
                    imgStream = context.getAssets().open("icon_crystal.png");
                    iconAsset = Drawable.createFromStream(imgStream, null);
                    holder.icon.setImageDrawable(iconAsset);
                    break;
                case "armor":
                    imgStream = context.getAssets().open("icon_armor.png");
                    iconAsset = Drawable.createFromStream(imgStream, null);
                    holder.icon.setImageDrawable(iconAsset);
                    break;
            }
        } catch (IOException e) {
            Log.e("BattleActivity", e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public void setData(List<StoreItem> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_item_store_card_icon) ImageView icon;
        @BindView(R.id.view_item_store_card_title) TextView title;
        @BindView(R.id.view_item_store_card_price) TextView price;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
