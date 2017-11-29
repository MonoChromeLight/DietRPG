package edu.odu.cs.zomp.dietapp.ui.archive.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;


public class QuestArchiveAdapter extends RecyclerView.Adapter<QuestArchiveAdapter.ViewHolder> {

    public interface QuestArchiveInterface {
        void questClicked(QuestProgress questProgressItem);
    }

    private Context context = null;
    private QuestArchiveInterface callbacks;
    private List<QuestProgress> archivedQuests = null;

    public QuestArchiveAdapter(Context context, QuestArchiveInterface callbacks) {
        this.context = context;
        this.callbacks = callbacks;
        this.archivedQuests = new ArrayList<>();
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card_quest, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        QuestProgress quest = archivedQuests.get(position);
        holder.questTitle.setText(quest.questName);
        holder.questProgress.setText(String.format(Locale.US, "%d / %d", quest.currentSegment, quest.totalSegments));
    }

    @Override public int getItemCount() {
        return archivedQuests.size();
    }

    public void add(QuestProgress activeQuest) {
        this.archivedQuests.add(activeQuest);
        this.notifyDataSetChanged();
    }

    public void addAllQuests(List<QuestProgress> activeQuests) {
        this.archivedQuests = activeQuests;
        this.notifyDataSetChanged();
    }

    public void clear() {
        this.archivedQuests.clear();
        this.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_card_quest_root) CardView viewRoot;
        @BindView(R.id.view_card_quest_title) TextView questTitle;
        @BindView(R.id.view_card_quest_progress) TextView questProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            viewRoot.setOnClickListener(v -> callbacks.questClicked( archivedQuests.get(getAdapterPosition()) ));
        }
    }
}
