package edu.odu.cs.zomp.dietapp.features.battle.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.odu.cs.zomp.dietapp.R;


public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ViewHolder> {

    public static final int ACTION_ATTACK = 0;
    public static final int ACTION_MAGIC = 1;
    public static final int ACTION_ITEMS = 2;
    public static final int ACTION_RUN = 3;

    public interface IActionAdapter {
        void actionClicked(int action);
    }

    private Context context = null;
    private IActionAdapter adapterInterface;

    public ActionAdapter(Context context, IActionAdapter adapterInterface) {
        this.context = context;
        this.adapterInterface = adapterInterface;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card_battle_action, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


}
