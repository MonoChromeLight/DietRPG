package edu.odu.cs.zomp.dietapp.features.battle;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.features.battle.adapters.ActionAdapter;
import edu.odu.cs.zomp.dietapp.features.battle.adapters.MagicAdapter;
import edu.odu.cs.zomp.dietapp.shared.models.BattleActionItem;


public class BattleActivity extends AppCompatActivity
        implements ActionAdapter.IActionAdapter, MagicAdapter.IMenuAdapter {

    private static final String TAG = BattleActivity.class.getSimpleName();

    @BindView(R.id.view_battle_root) LinearLayout viewRoot;
    @BindView(R.id.view_battle_bg) ImageView background;
    @BindView(R.id.view_battle_actionRecycler) RecyclerView actionRecycler;
    @BindView(R.id.view_battle_playerSprite) ImageView playerSprite;
    @BindView(R.id.view_battle_enemySprite) ImageView enemySprite;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);
        ButterKnife.bind(this);

        try {
            InputStream imgStream = getAssets().open("backdrop_forest.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            background.setImageDrawable(bgImg);
        } catch (IOException e) {
            Log.e("BattleActivity", e.getMessage(), e.fillInStackTrace());
        }
    }

    @Override public void actionClicked(int action) {

    }

    @Override public void itemClicked(BattleActionItem actionItem) {

    }
}
