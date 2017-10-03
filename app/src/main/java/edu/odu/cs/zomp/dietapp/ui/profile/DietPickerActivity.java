package edu.odu.cs.zomp.dietapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

        DatabaseReference dietsRef = FirebaseDatabase.getInstance().getReference().child("diets");
        dietsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dietSnapshot : dataSnapshot.getChildren()) {
                    Diet d = dietSnapshot.getValue(Diet.class);
                    adapter.add(d);
                }
            }

            @Override public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage(), databaseError.toException().fillInStackTrace());
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

        finish();
    }
}
