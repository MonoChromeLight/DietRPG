package edu.odu.cs.zomp.dietapp.features.quests;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.features.battle.BattleActivity;
import edu.odu.cs.zomp.dietapp.features.quests.adapters.QuestsAdapter;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;
import edu.odu.cs.zomp.dietapp.shared.models.Quest;

public class QuestsFragment extends BaseFragment
        implements QuestsAdapter.IQuestsAdapter {

    @BindView(R.id.view_quests_root) LinearLayout viewRoot;
    @BindView(R.id.view_quests_headerImg) ImageView headerImg;
    @BindView(R.id.view_quests_recycler) RecyclerView questsRecycler;
    @BindView(R.id.view_quests_emptyQuestListText) TextView emptyQuestListText;

    QuestsAdapter questsAdapter = null;

    public static QuestsFragment newInstance() {
        return new QuestsFragment();
    }

    public QuestsFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_quests, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            InputStream imgStream = getContext().getAssets().open("quest_header_img.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            headerImg.setImageDrawable(bgImg);
            headerImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            Log.e("QuestsFragment", e.getMessage(), e.fillInStackTrace());
        }

        // Load quests
        questsAdapter = new QuestsAdapter(getContext(), this);
        questsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        questsRecycler.setHasFixedSize(true);
        questsRecycler.setAdapter(questsAdapter);

        questsAdapter.addQuest(new Quest("Starter Quest", 1, 2));
        questsAdapter.addQuest(new Quest("Damsels in Distress", 0, 3));
        questsAdapter.addQuest(new Quest("Find the Goblin Caravan", 4, 7));
        questsAdapter.addQuest(new Quest("Treasure Caverns", 4, 5));
    }

    @Override public void questClicked(Quest quest) {
        startActivity(BattleActivity.createIntent(getContext(), quest));
    }
}
