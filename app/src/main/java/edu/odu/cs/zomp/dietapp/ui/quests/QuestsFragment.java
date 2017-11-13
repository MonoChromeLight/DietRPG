package edu.odu.cs.zomp.dietapp.ui.quests;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.Quest;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.ui.battle.BattleActivity;
import edu.odu.cs.zomp.dietapp.ui.quests.adapters.ActiveQuestAdapter;
import edu.odu.cs.zomp.dietapp.util.Constants;

public class QuestsFragment extends BaseFragment
        implements ActiveQuestAdapter.IQuestsAdapter {

    @BindView(R.id.view_quests_root) LinearLayout viewRoot;
    @BindView(R.id.view_quests_headerImg) ImageView headerImg;
    @BindView(R.id.view_quests_recycler) RecyclerView questsRecycler;
    @BindView(R.id.view_quests_questHistoryBtn) Button questHistoryBtn;
    @BindView(R.id.view_quests_emptyQuestListText) TextView emptyQuestListText;

    ActiveQuestAdapter activeQuestAdapter = null;

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
        activeQuestAdapter = new ActiveQuestAdapter(getContext(), this);
        questsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        questsRecycler.setHasFixedSize(true);
        questsRecycler.setAdapter(activeQuestAdapter);

        loadQuests();
    }

    private void loadQuests() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Character character = task.getResult().toObject(Character.class);
                        List<QuestProgress> questJournal = character.questJournal;
                        if (questJournal != null && questJournal.size() > 0) {
                            for (QuestProgress questProgress : questJournal) {
                                if (questProgress.currentSegment < questProgress.totalSegments)
                                    activeQuestAdapter.add(questProgress);
                            }
                        }
                    } else {
                        // Display error
                        questsRecycler.setVisibility(View.GONE);
                        emptyQuestListText.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override public void questClicked(QuestProgress questProgressItem) {
        FirebaseFirestore.getInstance()
                .collection(Constants.DATABASE_PATH_QUESTS)
                .document(questProgressItem.id)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Quest quest = task.getResult().toObject(Quest.class);
                        startActivity(BattleActivity.createIntent(getContext(), quest, questProgressItem));
                    }
                });
    }

    @OnClick(R.id.view_quests_questHistoryBtn)
    void questHistoryClicked() {
        Toast.makeText(getContext(), "Quest history clicked", Toast.LENGTH_SHORT).show();
    }
}
