package edu.odu.cs.zomp.dietapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Diet;
import edu.odu.cs.zomp.dietapp.ui.profile.adapters.DietPickerAdapter;


public class DietPickerActivity extends AppCompatActivity implements DietPickerAdapter.DietPickerInterface {

    private static final String TAG = DietPickerActivity.class.getSimpleName();

    @BindView(R.id.dietpicker_recycler) RecyclerView dietsRecycler;

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

        FirebaseFirestore.getInstance()
                .collection("diets")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            adapter.add(document.toObject(Diet.class));
                        }
                    }
                });
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DietPickerActivity.class);
    }

    @Override public void dietClicked(Diet diet) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putString(getString(R.string.sharedPref_dietId), diet.id)
                .putString(getString(R.string.sharedPref_dietTitle), diet.title)
                .apply();

        // TODO: Handle update in Firestore

        finish();
    }
}
