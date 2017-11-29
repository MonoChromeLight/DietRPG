package edu.odu.cs.zomp.dietapp.ui.battle.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Item;


public class ItemActionAdapter extends RecyclerView.Adapter<ItemActionAdapter.ViewHolder> {

    public interface ItemActionInterface {
        void useItem(Item item);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_battle_actionBtn) TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            text.setOnClickListener(v -> callbacks.useItem( items.get(getAdapterPosition()) ));
        }
    }

    private Context context;
    private ItemActionInterface callbacks;
    private List<Item> items;

    public ItemActionAdapter(Context context, List<Item> items, ItemActionInterface callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.items = items;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_battle_action, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.text.setText(item.name);
    }

    @Override public int getItemCount() {
        return items.size();
    }
}
