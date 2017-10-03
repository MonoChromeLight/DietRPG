package edu.odu.cs.zomp.dietapp.ui.quests.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Quest;


public class QuestsAdapter extends RecyclerView.Adapter<QuestsAdapter.ViewHolder> {

    public interface IQuestsAdapter {
        void questClicked(Quest quest);
    }

    private Context context = null;
    private IQuestsAdapter callbacks;
    private List<Quest> quests = null;

    public QuestsAdapter(Context context, IQuestsAdapter callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.quests = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_card_quest, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Quest quest = quests.get(position);
        holder.questTitle.setText(quest.title);

        String progressString = quest.completedSegments + " / " + quest.totalSegments;
        holder.questProgress.setText(progressString);
    }

    @Override public int getItemCount() {
        return quests.size();
    }

    public void addQuest(Quest quest) {
        this.quests.add(quest);
        this.notifyDataSetChanged();
    }

    public void addAllQuests(List<Quest> quests) {
        this.quests = quests;
        this.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_card_quest_root) CardView viewRoot;
        @BindView(R.id.view_card_quest_title) TextView questTitle;
        @BindView(R.id.view_card_quest_progress) TextView questProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewRoot.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    callbacks.questClicked( quests.get(getAdapterPosition()) );
                }
            });
        }
    }
}
