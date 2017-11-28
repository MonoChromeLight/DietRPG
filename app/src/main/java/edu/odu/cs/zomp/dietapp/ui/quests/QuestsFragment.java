package edu.odu.cs.zomp.dietapp.ui.quests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import edu.odu.cs.zomp.dietapp.data.models.QuestSummary;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.ui.battle.BattleActivity;
import edu.odu.cs.zomp.dietapp.ui.quests.adapters.ActiveQuestAdapter;
import edu.odu.cs.zomp.dietapp.util.Constants;

import static android.app.Activity.RESULT_OK;

// TODO: Quest history
// TODO: Figure out why quest summary dialog does not appear
public class QuestsFragment extends BaseFragment
        implements ActiveQuestAdapter.IQuestsAdapter {

    private static final String TAG = QuestsFragment.class.getSimpleName();
    private static final String ARG_PLAYER = "player";
    private static final int RC_QUEST_INFO_DIALOG = 123;
    private static final int RC_BATTLE_ACTIVITY = 234;

    @BindView(R.id.view_quests_root) LinearLayout viewRoot;
    @BindView(R.id.view_quests_headerImg) ImageView headerImg;
    @BindView(R.id.view_quests_recycler) RecyclerView questsRecycler;
    @BindView(R.id.view_quests_questHistoryBtn) Button questHistoryBtn;
    @BindView(R.id.view_quests_emptyQuestListText) TextView emptyQuestListText;

    ActiveQuestAdapter activeQuestAdapter = null;
    private Character player;

    public static QuestsFragment newInstance() {
        return new QuestsFragment();
    }

    public QuestsFragment() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_quests, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            InputStream imgStream = getContext().getAssets().open("quest_header_img.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            headerImg.setImageDrawable(bgImg);
            headerImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } catch (IOException e) {
            Log.e("QuestsFragment", e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override public void onStart() {
        super.onStart();

        // Load quests
        activeQuestAdapter = new ActiveQuestAdapter(getContext(), this);
        questsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        questsRecycler.setHasFixedSize(true);
        questsRecycler.setAdapter(activeQuestAdapter);

        loadPlayerData();
    }

    @Override public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "saving instance state...");
        outState.putParcelable(ARG_PLAYER, player);
    }

    @Override public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "view state restored");
        if (savedInstanceState != null)
            player = savedInstanceState.getParcelable(ARG_PLAYER);
    }

    private void loadPlayerData() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    player = documentSnapshot.toObject(Character.class);

                    activeQuestAdapter.clear();
                    List<QuestProgress> questJournal = player.questJournal;
                    if (questJournal != null && questJournal.size() > 0) {
                        for (QuestProgress questProgress : questJournal) {
                            if (questProgress.currentSegment < questProgress.totalSegments)
                                activeQuestAdapter.add(questProgress);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    questsRecycler.setVisibility(View.GONE);
                    emptyQuestListText.setVisibility(View.VISIBLE);
                });
    }

    @Override public void questClicked(QuestProgress questProgressItem) {
        Log.d(TAG, questProgressItem.questId + " clicked");
        QuestInfoDialog dialog = QuestInfoDialog.with(questProgressItem);
        dialog.setTargetFragment(this, RC_QUEST_INFO_DIALOG);
        dialog.show(getFragmentManager(), "quest_info");
    }

    @OnClick(R.id.view_quests_questHistoryBtn)
    void questHistoryClicked() {
        Toast.makeText(getContext(), "Quest history clicked", Toast.LENGTH_SHORT).show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_BATTLE_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                QuestSummary summary = data.getParcelableExtra("summary");
                loadPlayerData();
                QuestSummaryDialog.with(summary).show(getFragmentManager(), "dialog_quest_summary");
            } else {
                // Display error dialog

            }
        } else if (requestCode == RC_QUEST_INFO_DIALOG) {
            if (resultCode == RESULT_OK) {
                QuestProgress questData = data.getParcelableExtra("questProgress");
                Log.d(TAG, "Starting quest " + questData.questId);

                if (player == null)
                    return;

                ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Loading quest: " + questData.questName);
                pd.show();

                FirebaseFirestore.getInstance()
                        .collection(Constants.DATABASE_PATH_QUESTS)
                        .document(questData.questId)
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            Quest quest = documentSnapshot.toObject(Quest.class);
                            pd.dismiss();

                            Log.d(TAG, "Player id: " + player.id);
                            Log.d(TAG, "Player name: " + player.name);
                            Log.d(TAG, "Player gender: " + player.gender);
                            Log.d(TAG, "Player questJournal size: " + player.questJournal.size());
                            startActivityForResult(BattleActivity.createIntent(getContext(), player, quest, questData), RC_BATTLE_ACTIVITY);
                        })
                        .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e.fillInStackTrace()));
            }
        }
    }
}
