package edu.odu.cs.zomp.dietapp.ui.battle;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.odu.cs.zomp.dietapp.GlideApp;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.Enemy;
import edu.odu.cs.zomp.dietapp.data.models.Item;
import edu.odu.cs.zomp.dietapp.data.models.Quest;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.data.models.QuestSummary;
import edu.odu.cs.zomp.dietapp.ui.battle.adapters.ItemActionAdapter;
import edu.odu.cs.zomp.dietapp.ui.battle.adapters.MagicActionAdapter;
import edu.odu.cs.zomp.dietapp.util.Constants;
import edu.odu.cs.zomp.dietapp.util.ItemLibrary;
import edu.odu.cs.zomp.dietapp.util.SpellLibrary;


public class BattleActivity extends AppCompatActivity
        implements ItemActionAdapter.ItemActionInterface, MagicActionAdapter.MagicActionInterface {

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
    @BindView(R.id.battle_progressBar) ProgressBar progressBar;
    @BindView(R.id.battle_message) TextView message;
    @BindView(R.id.battle_action_recycler) RecyclerView actionRecycler;

    private Character player;
    private Quest quest;
    private QuestProgress currentProgress;
    private List<Enemy> enemies;
    private Enemy currentEnemy;
    private QuestSummary questSummary;
    private ProgressDialog pd;
    private int turn = 0;

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

        displayProgressBar();
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

    @Override public void onBackPressed() {
        if (actionRecycler.getVisibility() == View.VISIBLE) {
            actionRecycler.setAdapter(null);
            displayPrimaryActions();
        } else {
            super.onBackPressed();
        }
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

        turn = 0;
        displayPrimaryActions();
    }

    private void resolveTurn() {
        // Resolve lingering debuffs
//        boolean enemyKilled = false;
//        if (enemyKilled) {
//            turn = 0;
//            Runnable enemyKilledAction = this::advanceQuestSegment;
//            displayMessage(currentEnemy.name + " defeated!", enemyKilledAction);
//        } else {
//            resolveTurn();
//        }

        // Advance turn
        turn++;
        if (turn % 2 == 0) {
            // Enemy Turn
            // TODO: Replace with actual enemy logic
            Runnable enemyAction = this::resolveTurn;
            displayMessage("Enemy misses!", enemyAction);
        } else if (turn % 2 == 1) {
            // Player turn
            displayPrimaryActions();
        }
    }

    private void advanceQuestSegment() {
        displayProgressBar();

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
        progressBar.setVisibility(View.GONE);
        pd = new ProgressDialog(this);
        pd.setMessage("Victory!");
        pd.show();

        questSummary = new QuestSummary();
        summarizeNewQuests();
        summarizeLoot();
        summarizeExperience();
    }

    private void exitCheck() {
        if (player == null || questSummary == null || questSummary.questsUnlocked == null || questSummary.loot == null || questSummary.expMap == null)
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
                .addOnFailureListener(Throwable::printStackTrace);
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
        Map<String, Integer> expTable = new HashMap<>();
        int exp = 0;
        for (Enemy e : enemies) {
            exp += e.stats.get(Constants.STAT_EXP);
            expTable.put(e.name, e.stats.get(Constants.STAT_EXP));
        }

        questSummary.userLeveledUp = player.gainExp(exp);
        questSummary.expMap = expTable;
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
        Log.d(TAG, "attacking enemy");
        boolean enemyKilled = currentEnemy.takeDamage(player.attributes.get(Constants.ATTRIBUTE_STR));
        if (enemyKilled) {
            Runnable action = this::advanceQuestSegment;
            displayMessage(currentEnemy.name + " defeated!", action);
        } else {
            resolveTurn();
        }
    }

    @OnClick(R.id.battle_action_magic)
    void displayMagicList() {
        displayProgressBar();
        MagicActionAdapter adapter = new MagicActionAdapter(this, player.spellbook, this);
        displayActions(adapter);
    }

    @OnClick(R.id.battle_action_items)
    void displayItemList() {
        displayProgressBar();
        ItemActionAdapter adapter = new ItemActionAdapter(this, player.inventory, this);
        displayActions(adapter);
    }

    @OnClick(R.id.battle_action_flee)
    void flee() {
        Toast.makeText(this, "Flee", Toast.LENGTH_SHORT).show();
    }

    @Override public void useItem(Item item) {
        Log.d(TAG, "Using item " + item.name);
        String message = player.name + " uses " + item.name;
        Runnable action = () -> {
            try {
                Object[] params = new Object[] { player };
                Method spell = ItemLibrary.class.getDeclaredMethod(item.name.toLowerCase(), params.getClass());
                spell.invoke(ItemLibrary.class, params);
                resolveTurn();
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Unable to find item", e.fillInStackTrace());
                resolveTurn();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
        displayMessage(message, action);
    }

    @Override public void castSpell(String spellName) {
        Log.d(TAG, "Casting spell " + spellName);
        String message = player.name + " uses " + spellName;
        Runnable action = () -> {
            try {
                Object[] params = new Object[] { player, currentEnemy };
                Method spell = SpellLibrary.class.getDeclaredMethod(spellName.toLowerCase(), Character.class, Enemy.class);
                spell.invoke(SpellLibrary.class, params);

                boolean enemyKilled = currentEnemy.takeDamage(player.attributes.get(Constants.ATTRIBUTE_INT));
                if (enemyKilled) {
                    Runnable enemyKilledAction = this::advanceQuestSegment;
                    displayMessage(currentEnemy.name + " defeated!", enemyKilledAction);
                } else {
                    resolveTurn();
                }
            } catch (NoSuchMethodException e) {
                Log.e(TAG, "Unable to find spell ", e.fillInStackTrace());
                resolveTurn();
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        };
        displayMessage(message, action);
    }

    private void displayPrimaryActions() {
        primaryActionFrame.setVisibility(View.VISIBLE);
        actionRecycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        message.setVisibility(View.GONE);
    }

    private void displayMessage(String messageToDisplay, Runnable postMessageAction) {
        message.setVisibility(View.VISIBLE);
        primaryActionFrame.setVisibility(View.GONE);
        actionRecycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);

        message.setText(messageToDisplay);
        new Handler().postDelayed(postMessageAction, 2000);
    }

    private void displayProgressBar() {
        actionRecycler.setVisibility(View.VISIBLE);
        message.setVisibility(View.GONE);
        primaryActionFrame.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
    }

    private void displayActions(MagicActionAdapter adapter) {
        primaryActionFrame.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        actionRecycler.setAdapter(adapter);
        actionRecycler.setVisibility(View.VISIBLE);
    }

    private void displayActions(ItemActionAdapter adapter) {
        primaryActionFrame.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        message.setVisibility(View.GONE);

        actionRecycler.setAdapter(adapter);
        actionRecycler.setVisibility(View.VISIBLE);
    }
}
