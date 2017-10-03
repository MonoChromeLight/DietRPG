package edu.odu.cs.zomp.dietapp.ui.battle.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.BattleActionItem;


public class MagicAdapter extends RecyclerView.Adapter<MagicAdapter.ViewHolder> {

    public interface IMenuAdapter {
        void itemClicked(BattleActionItem actionItem);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_battle_action_root) CardView viewRoot;
        @BindView(R.id.view_battle_action_icon) ImageView icon;
        @BindView(R.id.view_battle_action_text) TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    adapterInterface.itemClicked( listItems.get(getAdapterPosition()) );
                }
            });
        }
    }

    private Context context = null;
    private IMenuAdapter adapterInterface = null;
    List<BattleActionItem> listItems = new ArrayList<>();

    public MagicAdapter(Context context, IMenuAdapter adapterInterface) {
        this.context = context;
        this.adapterInterface = adapterInterface;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card_battle_action, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        BattleActionItem menuItem = listItems.get(position);

        holder.text.setText(menuItem.title);

        // Set icon
    }

    @Override public int getItemCount() {
        return listItems.size();
    }
}
