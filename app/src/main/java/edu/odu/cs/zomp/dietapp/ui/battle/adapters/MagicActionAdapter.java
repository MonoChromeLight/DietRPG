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


public class MagicActionAdapter extends RecyclerView.Adapter<MagicActionAdapter.ViewHolder> {

    public interface MagicActionInterface {
        void castSpell(String action);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_battle_actionBtn) TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            text.setOnClickListener(v -> callbacks.castSpell( magicActions.get(getAdapterPosition()) ));
        }
    }

    private Context context = null;
    private MagicActionInterface callbacks = null;
    private List<String> magicActions;

    public MagicActionAdapter(Context context, List<String> magicActions, MagicActionInterface callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.magicActions = magicActions;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_battle_action, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(magicActions.get(position));
    }

    @Override public int getItemCount() {
        return magicActions.size();
    }
}
