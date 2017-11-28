package edu.odu.cs.zomp.dietapp.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.net.yummly.models.Match;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;
import edu.odu.cs.zomp.dietapp.ui.recipe.adapters.IngredientAdapter;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class RecipeDetailActivity extends BaseActivity {

    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private static final String ARG_RECIPE = "recipe";

    @BindView(R.id.recipe_appbar) AppBarLayout appBar;
    @BindView(R.id.recipe_headerBg) ImageView headerBg;
    @BindView(R.id.recipe_ratingBar) MaterialRatingBar ratingBar;
    @BindView(R.id.recipe_name) TextView name;
    @BindView(R.id.recipe_ingredientList) ListView ingredientList;
    @BindView(R.id.recipe_fab) FloatingActionButton fab;

    private Match recipe;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        recipe = getIntent().getParcelableExtra(ARG_RECIPE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
    }

    @Override protected void onStart() {
        super.onStart();

        name.setText(recipe.recipeName);
        ratingBar.setRating(recipe.rating);

        IngredientAdapter adapter = new IngredientAdapter(this);
        adapter.addIngredient(recipe.ingredients);
        ingredientList.setAdapter(adapter);

        Glide.with(this)
                .load(recipe.smallImageUrls.get(0))
                .into(headerBg);

        fab.setOnClickListener(view -> Toast.makeText(RecipeDetailActivity.this, "Fab clicked", Toast.LENGTH_SHORT).show());
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(ARG_RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            recipe = savedInstanceState.getParcelable(ARG_RECIPE);
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static Intent createService(Context context, Match recipe) {
        Intent intent = new Intent(context, RecipeDetailActivity.class);
        intent.putExtra(ARG_RECIPE, recipe);
        return intent;
    }
}
