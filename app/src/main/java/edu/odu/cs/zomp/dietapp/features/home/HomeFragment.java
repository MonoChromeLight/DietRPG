package edu.odu.cs.zomp.dietapp.features.home;

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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;


public class HomeFragment extends BaseFragment implements HomeAdapter.IHomeAdapter {

    @BindView(R.id.view_home_charImg) ImageView avatar;
    @BindView(R.id.view_home_username) TextView username;
    @BindView(R.id.view_home_progressText) TextView dailyProgressText;
    @BindView(R.id.view_home_currentQuest) TextView currentQuestText;
    @BindView(R.id.view_home_recycler) RecyclerView recycler;

    HomeAdapter adapter = null;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username.setText("David");

        try {
            InputStream imgStream = getContext().getAssets().open("sprite_char.png");
            Drawable bgImg = Drawable.createFromStream(imgStream, null);
            avatar.setImageDrawable(bgImg);
        } catch (IOException e) {
            Log.e("HomeFragment", e.getMessage(), e.fillInStackTrace());
        }

        dailyProgressText.setText("0 / 5");
        currentQuestText.setText("Hello World");

        adapter = new HomeAdapter(getContext(), this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);
        getDailyPlan();
    }

    private void getDailyPlan() {
        adapter.add("Breakfast smoothie");
        adapter.add("Cereal");
        adapter.add("Salad");
        adapter.add("Salmon with herbs");
    }

    @Override public void itemClicked(String text) {
        Toast.makeText(getContext(), text + " completed", Toast.LENGTH_SHORT).show();
    }
}
