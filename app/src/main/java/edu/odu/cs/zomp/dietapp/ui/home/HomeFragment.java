package edu.odu.cs.zomp.dietapp.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.Diet;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.net.yummly.YummlyAPI;
import edu.odu.cs.zomp.dietapp.net.yummly.YummlyClient;
import edu.odu.cs.zomp.dietapp.net.yummly.models.Match;
import edu.odu.cs.zomp.dietapp.net.yummly.models.RecipeResult;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;
import edu.odu.cs.zomp.dietapp.ui.recipe.RecipeDetailActivity;
import edu.odu.cs.zomp.dietapp.util.Constants;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends BaseFragment implements RecipeAdapter.IHomeAdapter {

    private static final String TAG =  HomeFragment.class.getSimpleName();

    @BindView(R.id.home_avatar) ImageView avatar;
    @BindView(R.id.home_name) TextView name;
    @BindView(R.id.home_questCompleteCount) TextView completedQuestCount;
    @BindView(R.id.home_health_bar) ProgressBar healthBar;
    @BindView(R.id.home_mana_bar) ProgressBar manaBar;
    @BindView(R.id.home_recycler_frame) LinearLayout recyclerFrame;
    @BindView(R.id.home_empty_list_frame) LinearLayout emptyViewFrame;
    @BindView(R.id.home_empty_view_message) TextView emptyViewMessage;
    @BindView(R.id.home_recipe_loadingIndicator) ProgressBar recipeLoadingProgressBar;
    @BindView(R.id.home_recycler) RecyclerView recycler;

    private RecipeAdapter adapter = null;
    private CompositeDisposable disposables = null;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() { }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize recycler and adapter
        adapter = new RecipeAdapter(getContext(), this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.addItemDecoration(new DividerItemDecoration(recycler.getContext(), DividerItemDecoration.VERTICAL));
        recycler.setHasFixedSize(false);
        recycler.setAdapter(adapter);

        // Retrieve user data
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore
                .collection(Constants.DATABASE_PATH_USERS_PRIVATE)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        UserPrivate userPrivateData = task.getResult().toObject(UserPrivate.class);
                        fetchDiets(userPrivateData.activeDiet);
                    } else {
                        Log.e(TAG, task.getException().getMessage(), task.getException().fillInStackTrace());
                    }
                });

        firestore
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Character character = task.getResult().toObject(Character.class);
                        name.setText(character.name);

                        healthBar.setMax(character.stats.get(Constants.STAT_MAX_HEALTH));
                        healthBar.setProgress(character.stats.get(Constants.STAT_HEALTH));
                        manaBar.setMax(character.stats.get(Constants.STAT_MAX_MANA));
                        manaBar.setProgress(character.stats.get(Constants.STAT_MANA));

                        if (character.questJournal != null && character.questJournal.size() > 0) {
                            int questsCompleted = 0;
                            for (QuestProgress questProgress : character.questJournal)
                                if (questProgress.currentSegment == questProgress.totalSegments)
                                    questsCompleted += 1;

                            completedQuestCount.setText(String.format(Locale.US, "%d / %d", questsCompleted, character.questJournal.size()));
                        } else {
                            completedQuestCount.setText("0 / 0");
                        }

                        // Load avatar
                        try {
                            String avatarFile;
                            if (character.gender == Character.GENDER_MALE)
                                avatarFile = "character_male.png";
                            else
                                avatarFile = "character_female.png";

                            InputStream imgStream = getContext().getAssets().open(avatarFile);
                            Drawable bgImg = Drawable.createFromStream(imgStream, null);
                            avatar.setImageDrawable(bgImg);
                        } catch (IOException e) {
                            Log.e(TAG, e.getMessage(), e.fillInStackTrace());
                        }
                    } else {
                        Log.e(TAG, task.getException().getMessage(), task.getException().fillInStackTrace());
                    }
                });
    }

    @Override public void onDestroy() {
        super.onDestroy();
        disposables.clear();
        disposables.dispose();
    }

    private void fetchDiets(Diet diet) {
        int maxResults = 100;
        YummlyAPI api = YummlyClient.createService(YummlyAPI.class);
        if (disposables == null)
            disposables = new CompositeDisposable();

        recipeLoadingProgressBar.setVisibility(View.VISIBLE);
        if (diet.queryParam == null || diet.queryParam. isEmpty()) {
            disposables.add(
                    api.getRecipes(maxResults, true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<RecipeResult>() {
                                @Override public void onNext(RecipeResult recipeResult) {
                                    adapter.clear();

                                    // Pull 21 random recipes from the results
                                    if (recipeResult.totalMatchCount > 21) {
                                        List<Match> matches = recipeResult.matches;
                                        List<Match> weeklyMatches = new ArrayList<>();

                                        Random random = new Random();
                                        while (weeklyMatches.size() < 21) {
                                            Match match = matches.get(random.nextInt(maxResults));
                                            if (!weeklyMatches.contains(match))
                                                weeklyMatches.add(match);
                                        }
                                        adapter.add(weeklyMatches);

                                        recyclerFrame.setVisibility(View.VISIBLE);
                                        emptyViewFrame.setVisibility(View.GONE);
                                    } else {
                                        recyclerFrame.setVisibility(View.GONE);
                                        emptyViewFrame.setVisibility(View.VISIBLE);
                                        emptyViewMessage.setText("Unable to find recipes");
                                    }
                                }

                                @Override public void onError(Throwable e) {
                                    Log.e(TAG, e.getLocalizedMessage(), e.fillInStackTrace());
                                    recyclerFrame.setVisibility(View.GONE);
                                    emptyViewFrame.setVisibility(View.VISIBLE);
                                    emptyViewMessage.setText("Error loading recipes");
                                }

                                @Override public void onComplete() {
                                    recipeLoadingProgressBar.setVisibility(View.GONE);
                                }
                            })
            );
        } else {
            disposables.add(
                    api.getRecipes(diet.queryParam, maxResults, true)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeWith(new DisposableObserver<RecipeResult>() {
                                @Override public void onNext(RecipeResult recipeResult) {
                                    adapter.clear();

                                    // Pull 21 random recipes from the results
                                    if (recipeResult.totalMatchCount > 21) {
                                        List<Match> matches = recipeResult.matches;
                                        List<Match> weeklyMatches = new ArrayList<>();

                                        Random random = new Random();
                                        while (weeklyMatches.size() < 21) {
                                            Match match = matches.get(random.nextInt(maxResults));
                                            if (!weeklyMatches.contains(match))
                                                weeklyMatches.add(match);
                                        }
                                        adapter.add(weeklyMatches);

                                        recyclerFrame.setVisibility(View.VISIBLE);
                                        emptyViewFrame.setVisibility(View.GONE);
                                    } else {
                                        recyclerFrame.setVisibility(View.GONE);
                                        emptyViewFrame.setVisibility(View.VISIBLE);
                                        emptyViewMessage.setText("Unable to find recipes");
                                    }
                                }

                                @Override public void onError(Throwable e) {
                                    Log.e(TAG, e.getLocalizedMessage(), e.fillInStackTrace());
                                    recyclerFrame.setVisibility(View.GONE);
                                    emptyViewFrame.setVisibility(View.VISIBLE);
                                    emptyViewMessage.setText("Error loading recipes");
                                }

                                @Override public void onComplete() {
                                    recipeLoadingProgressBar.setVisibility(View.GONE);
                                }
                            })
            );
        }
    }

    @Override public void itemClicked(Match recipe) {
        startActivity(RecipeDetailActivity.createService(getActivity(), recipe));
    }
}
