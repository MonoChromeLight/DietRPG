package edu.odu.cs.zomp.dietapp.features.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.features.profile.adapters.DietPickerAdapter;
import edu.odu.cs.zomp.dietapp.shared.models.Diet;


public class DietPickerActivity extends AppCompatActivity implements DietPickerAdapter.DietPickerInterface {

    @BindView(R.id.view_dietpicker_recycler) RecyclerView dietsRecycler;

    private DietPickerAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_picker);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDietList();
    }

    private void initDietList() {
        adapter = new DietPickerAdapter(this, this);
        dietsRecycler.setLayoutManager(new LinearLayoutManager(this));
        dietsRecycler.setAdapter(adapter);

        adapter.add(new Diet("Vegetarian", "bg_vegetarian.jpg"));
        adapter.add(new Diet("No Filter", "bg_nofilterdiet.jpg"));
        adapter.add(new Diet("Paleo", "bg_paleo.jpg"));
        adapter.add(new Diet("Low Carb", "bg_low-carb.jpg"));
        adapter.add(new Diet("Vegan", "bg_vegan.jpg"));
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DietPickerActivity.class);
    }

    @Override public void dietClicked(Diet diet) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString("currentDiet", diet.name)
                .apply();

        finish();
    }
}
