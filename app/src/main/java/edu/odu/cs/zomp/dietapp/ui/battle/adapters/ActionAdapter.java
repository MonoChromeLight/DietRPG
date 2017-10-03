package edu.odu.cs.zomp.dietapp.ui.battle.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private IActionAdapter callbacks;

    public ActionAdapter(Context context, IActionAdapter callbacks) {
        this.context = context;
        this.callbacks = callbacks;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card_battle_action, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        switch (position) {
            case ACTION_ATTACK:
                holder.tag = ACTION_ATTACK;
                holder.actionTitle.setText("Attack");
                try {
                    InputStream inputStream = context.getAssets().open("icon_sword.png");
                    Drawable iconDrawable = Drawable.createFromStream(inputStream, null);
                    holder.actionIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case ACTION_MAGIC:
                holder.tag = ACTION_MAGIC;
                holder.actionTitle.setText("Magic");
                try {
                    InputStream inputStream = context.getAssets().open("icon_crystal.png");
                    Drawable iconDrawable = Drawable.createFromStream(inputStream, null);
                    holder.actionIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case ACTION_ITEMS:
                holder.tag = ACTION_ITEMS;
                holder.actionTitle.setText("Items");
                try {
                    InputStream inputStream = context.getAssets().open("icon_potion.png");
                    Drawable iconDrawable = Drawable.createFromStream(inputStream, null);
                    holder.actionIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            case ACTION_RUN:
                holder.tag = ACTION_RUN;
                holder.actionTitle.setText("Flee");
                try {
                    InputStream inputStream = context.getAssets().open("icon_shield.png");
                    Drawable iconDrawable = Drawable.createFromStream(inputStream, null);
                    holder.actionIcon.setImageDrawable(iconDrawable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
        }
    }

    @Override public int getItemCount() {
        return 4;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_battle_action_root) CardView viewRoot;
        @BindView(R.id.view_battle_action_icon) ImageView actionIcon;
        @BindView(R.id.view_battle_action_text) TextView actionTitle;

        int tag = -1;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                if (tag != -1)
                    callbacks.actionClicked(tag);
                }
            });
        }
    }
}
