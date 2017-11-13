package edu.odu.cs.zomp.dietapp.ui.battle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Quest;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.ui.battle.adapters.ActionAdapter;


public class BattleActivity extends AppCompatActivity
        implements ActionAdapter.IActionAdapter {

    private static final String TAG = BattleActivity.class.getSimpleName();
    private static final String ARG_QUEST = "quest";
    private static final String ARG_PROGRESS = "user_progress";

    @BindView(R.id.view_battle_root) LinearLayout viewRoot;
    @BindView(R.id.view_battle_bg) ImageView background;
    @BindView(R.id.view_battle_actionRecycler) RecyclerView actionRecycler;
    @BindView(R.id.view_battle_playerSprite) ImageView playerSprite;
    @BindView(R.id.view_battle_enemySprite) ImageView enemySprite;

    private QuestProgress userProgress;
    private Quest quest;
    private RecyclerView.Adapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() != null) {
            quest = getIntent().getParcelableExtra(ARG_QUEST);
            userProgress = getIntent().getParcelableExtra(ARG_PROGRESS);
        } else if (savedInstanceState != null) {
            quest = savedInstanceState.getParcelable(ARG_QUEST);
            userProgress = savedInstanceState.getParcelable(ARG_PROGRESS);
        }

        setContentView(R.layout.activity_battle);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();

        adapter = new ActionAdapter(this, this);
        actionRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        actionRecycler.setHasFixedSize(true);
        actionRecycler.setAdapter(adapter);

        // Load scene sprites
        InputStream imgStream;
        try {
            // Background
            imgStream = getAssets().open("backdrop_forest.png");
            Drawable backgroundImgAsset = Drawable.createFromStream(imgStream, null);
            background.setImageDrawable(backgroundImgAsset);

            // Character sprite

            imgStream = getAssets().open("character_male.png");
            Drawable characterAsset = Drawable.createFromStream(imgStream, null);
            playerSprite.setImageDrawable(characterAsset);

            // Enemy sprite
            imgStream = getAssets().open("sprite_dragon.png");
            Drawable enemyAsset = Drawable.createFromStream(imgStream, null);
            enemySprite.setImageDrawable(enemyAsset);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_QUEST, quest);
        outState.putParcelable(ARG_PROGRESS, userProgress);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState == null)
            return;

        quest = savedInstanceState.getParcelable(ARG_QUEST);
        userProgress = savedInstanceState.getParcelable(ARG_PROGRESS);
    }

    public static Intent createIntent(Context context, Quest quest, QuestProgress userProgress) {
        Intent intent = new Intent(context, BattleActivity.class);
        intent.putExtra(ARG_QUEST, quest);
        intent.putExtra(ARG_PROGRESS, userProgress);
        return intent;
    }

    @Override public void actionClicked(int action) {
        Toast.makeText(this, "Action clicked", Toast.LENGTH_SHORT).show();
    }
}
