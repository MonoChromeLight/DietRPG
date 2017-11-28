package edu.odu.cs.zomp.dietapp.ui.battle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.odu.cs.zomp.dietapp.GlideApp;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.Enemy;
import edu.odu.cs.zomp.dietapp.data.models.Quest;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.data.models.QuestSummary;
import edu.odu.cs.zomp.dietapp.util.Constants;

// TODO: Battle flow
// TODO: Magic and item recycler setup
public class BattleActivity extends AppCompatActivity {

    private static final String TAG = BattleActivity.class.getSimpleName();
    private static final String ARG_QUEST = "quest";
    private static final String ARG_PLAYER = "player";
    private static final String ARG_PROGRESS = "progress";
    private static final String ARG_QUEST_SUMMARY = "questSummary";

    @BindView(R.id.battle_root) RelativeLayout viewRoot;
    @BindView(R.id.battle_bg) ImageView background;
    @BindView(R.id.battle_playerSprite) ImageView playerSprite;
    @BindView(R.id.battle_enemySprite) ImageView enemySprite;
    @BindView(R.id.battle_primaryActionFrame) LinearLayout primaryActionFrame;
    @BindView(R.id.battle_action_attack) TextView actionAttack;
    @BindView(R.id.battle_action_magic) TextView actionMagic;
    @BindView(R.id.battle_action_items) TextView attackItems;
    @BindView(R.id.battle_action_flee) TextView attackFlee;
    @BindView(R.id.battle_actionRecycler) RecyclerView actionRecycler;

    private Character player;
    private Quest quest;
    private QuestProgress currentProgress;
    private List<Enemy> enemies;
    private Enemy currentEnemy;
    private QuestSummary questSummary;
    private ProgressDialog pd;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            quest = getIntent().getParcelableExtra(ARG_QUEST);
            player = getIntent().getParcelableExtra(ARG_PLAYER);
            currentProgress = getIntent().getParcelableExtra(ARG_PROGRESS);
        } else if (savedInstanceState != null) {
            quest = savedInstanceState.getParcelable(ARG_QUEST);
            player = savedInstanceState.getParcelable(ARG_PLAYER);
            currentProgress = savedInstanceState.getParcelable(ARG_PROGRESS);
            questSummary = savedInstanceState.getParcelable(ARG_QUEST_SUMMARY);
        }

//        Log.d(TAG, "Player id: " + player.id);
//        Log.d(TAG, "Player name: " + player.name);

        setContentView(R.layout.activity_battle);
        ButterKnife.bind(this);

        primaryActionFrame.setVisibility(View.VISIBLE);
        primaryActionFrame.setEnabled(true);
        actionRecycler.setVisibility(View.GONE);
        actionRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        actionRecycler.setHasFixedSize(true);
    }

    @Override protected void onStart() {
        super.onStart();
        try {
            InputStream imgStream = getAssets().open("backdrop_forest.png");
            Drawable backgroundImgAsset = Drawable.createFromStream(imgStream, null);
            background.setImageDrawable(backgroundImgAsset);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e.fillInStackTrace());
        }

        initPlayer();
        loadEnemies();
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_PLAYER, player);
        outState.putParcelable(ARG_QUEST, quest);
        outState.putParcelable(ARG_PROGRESS, currentProgress);
        outState.putParcelable(ARG_QUEST_SUMMARY, questSummary);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;

        player = savedInstanceState.getParcelable(ARG_PLAYER);
        quest = savedInstanceState.getParcelable(ARG_QUEST);
        currentProgress = savedInstanceState.getParcelable(ARG_PROGRESS);
        questSummary = savedInstanceState.getParcelable(ARG_QUEST_SUMMARY);
    }

    @Override protected void onDestroy() {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
        super.onDestroy();
    }

    private void initPlayer() {
        try {
            String fileName = player.gender == Character.GENDER_MALE ? "character_male.png" : "character_female.png";
            InputStream is = getAssets().open(fileName);
            Drawable d = Drawable.createFromStream(is, fileName);
            playerSprite.setImageDrawable(d);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e.fillInStackTrace());
        }
    }

    private void loadEnemies() {
        enemies = new ArrayList<>();
        String currentEnemyId = quest.enemies.get(currentProgress.currentSegment);
        for (String enemyId : quest.enemies) {
            FirebaseFirestore.getInstance()
                    .collection(Constants.DATABASE_PATH_ENEMIES)
                    .document(enemyId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        Enemy e = documentSnapshot.toObject(Enemy.class);
                        enemies.add(e);
                        if (TextUtils.equals(e.id, currentEnemyId))
                            setEnemy(e);
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, e.getMessage(), e.fillInStackTrace());
                        primaryActionFrame.setEnabled(false);
                    });
        }
    }

    private void setEnemy(Enemy e) {
        currentEnemy = e;
        StorageReference enemySpriteRef = FirebaseStorage.getInstance().getReference()
                .child(Constants.STORAGE_PATH_SPRITES_ENEMIES)
                .child(currentEnemy.id + ".png");

        GlideApp.with(BattleActivity.this)
                .load(enemySpriteRef)
                .into(enemySprite);
        Log.d(TAG, "Successfully loaded enemy");
    }

    private void advanceQuestSegment() {
        // Get the index of the current quest in the user's journal
        int entryIndex = -1;
        for (QuestProgress entry : player.questJournal) {
            if (entry.equals(currentProgress)) {
                entryIndex = player.questJournal.indexOf(entry);
                break;
            }
        }

        if (entryIndex != -1) {
            // Firestore does not support using .update() on Array objects or individual array objects
            // So at present, you have to update the entire player object -_-
            currentProgress.currentSegment++;
            player.questJournal.set(entryIndex, currentProgress);
            // Sync quest progress with Remote database
            FirebaseFirestore.getInstance()
                    .collection(Constants.DATABASE_PATH_CHARACTERS)
                    .document(player.id)
                    .set(player)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "Updated quest journal: Quest (" + currentProgress.questId + "), progress "
                                + (currentProgress.currentSegment - 1) + " -> " + (currentProgress.currentSegment)
                                + " for user " + player.id);
                        if (currentProgress.currentSegment < currentProgress.totalSegments) {
                            setEnemy( enemies.get(currentProgress.currentSegment) );
                        } else {
                            finishQuest();
                        }
                    })
                    .addOnFailureListener(e -> Log.e(TAG,  "Error updating character quest journal", e.fillInStackTrace()));
        } else {
            Log.e(TAG, "Error: quest progress not found in quest journal");
        }
    }

    private void finishQuest() {
        pd = new ProgressDialog(this);
        pd.setMessage("Victory!");
        pd.show();

        questSummary = new QuestSummary();
        summarizeNewQuests();
        summarizeLoot();
        summarizeExperience();
    }

    private void exitCheck() {
        if (player == null || questSummary == null || questSummary.questsUnlocked == null || questSummary.loot == null || questSummary.expGained == 0)
            return;

        Log.d(TAG, "Summary integrity check complete, updating player data...");
        FirebaseFirestore.getInstance()
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(player.id)
                .set(player)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Success");
                    Intent questDataIntent = new Intent();
                    questDataIntent.putExtra(ARG_PLAYER, player);
                    questDataIntent.putExtra(ARG_QUEST, quest);
                    questDataIntent.putExtra(ARG_QUEST_SUMMARY, questSummary);
                    setResult(RESULT_OK, questDataIntent);
                    if (pd != null) {
                        pd.dismiss();
                        pd = null;
                    }
                    finish();
                })
                .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e.fillInStackTrace()));
    }

    private void summarizeNewQuests() {
        questSummary.questsUnlocked = new ArrayList<>();
        if (quest.nextQuests != null && quest.nextQuests.size() > 0) {
            boolean addQuests = true;
            if (quest.prerequisites != null && quest.prerequisites.size() > 0) {
                List<String> completedQuests = new ArrayList<>();
                for (QuestProgress questProgress : player.questJournal) {
                    if (questProgress.currentSegment == questProgress.totalSegments)
                        completedQuests.add(questProgress.questId);
                }

                if (!completedQuests.containsAll(quest.prerequisites)) addQuests = false;
            }

            if (addQuests) {
                final int[] counter = {0};
                for (String questId : quest.nextQuests) {
                    FirebaseFirestore.getInstance()
                            .collection(Constants.DATABASE_PATH_QUESTS)
                            .document(questId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                Quest newQuest = documentSnapshot.toObject(Quest.class);
                                QuestProgress journalEntry = new QuestProgress();
                                journalEntry.questId = newQuest.id;
                                journalEntry.questName = newQuest.name;
                                journalEntry.questDescription = newQuest.description;
                                journalEntry.currentSegment = 0;
                                journalEntry.totalSegments = newQuest.enemies.size();

                                questSummary.questsUnlocked.add(newQuest.name);
                                player.questJournal.add(journalEntry);
                                counter[0]++;

                                if (counter[0] == quest.nextQuests.size())
                                    exitCheck();
                            })
                            .addOnFailureListener(e -> Log.e(TAG, e.getMessage(), e.fillInStackTrace()));
                }
            } else {
                exitCheck();
            }
        }
    }

    private void summarizeLoot() {
        questSummary.loot = new ArrayList<>();
        exitCheck();
    }

    private void summarizeExperience() {
        int exp = 0;
        for (Enemy e : enemies)
            exp += e.stats.get(Constants.STAT_EXP);

        questSummary.userLeveledUp = player.gainExp(exp);
        questSummary.expGained = exp;
        exitCheck();
    }

    public static Intent createIntent(Context context, @NonNull Character player, @NonNull Quest quest, @NonNull QuestProgress progress) {
        Intent intent = new Intent(context, BattleActivity.class);

        Log.d(TAG, "Player id: " + player.id);
        Log.d(TAG, "Player name: " + player.name);
        Log.d(TAG, "Player gender: " + player.gender);
        Log.d(TAG, "Player questJournal size: " + player.questJournal.size());

        intent.putExtra(ARG_PLAYER, player);
        intent.putExtra(ARG_QUEST, quest);
        intent.putExtra(ARG_PROGRESS, progress);
        return intent;
    }

    @OnClick(R.id.battle_action_attack)
    void attackEnemy() {
        advanceQuestSegment();
    }

    @OnClick(R.id.battle_action_magic)
    void displayMagicList() {
        Toast.makeText(this, "View spells", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.battle_action_items)
    void displayItemList() {
        Toast.makeText(this, "View items", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.battle_action_flee)
    void flee() {
        Toast.makeText(this, "Flee", Toast.LENGTH_SHORT).show();
    }
}
