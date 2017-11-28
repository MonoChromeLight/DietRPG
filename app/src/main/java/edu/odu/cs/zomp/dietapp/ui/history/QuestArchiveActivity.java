package edu.odu.cs.zomp.dietapp.ui.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;
import edu.odu.cs.zomp.dietapp.ui.history.adapters.QuestArchiveAdapter;

public class QuestArchiveActivity extends BaseActivity implements QuestArchiveAdapter.QuestArchiveInterface {

    private static final String TAG = QuestArchiveActivity.class.getSimpleName();
    private static final String ARG_QUESTS = "quests";

    @BindView(R.id.questArchive_recycler) RecyclerView recycler;

    private List<QuestProgress> quests;
    private QuestArchiveAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        quests = getIntent().getParcelableArrayListExtra(ARG_QUESTS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questarchive);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();
        recycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter = new QuestArchiveAdapter(this, this));
        for (QuestProgress quest : quests) {
            if (quest.currentSegment == quest.totalSegments)
                adapter.add(quest);
        }
    }


    @Override public void questClicked(QuestProgress questProgressItem) {
        QuestArchiveDialog.with(questProgressItem).show(getSupportFragmentManager(), "dialog_questArchive");
    }

    public static Intent createIntent(Context context, List<QuestProgress> quests) {
        Intent intent = new Intent(context, QuestArchiveActivity.class);
        intent.putParcelableArrayListExtra(ARG_QUESTS, (ArrayList<QuestProgress>) quests);
        return intent;
    }
}
