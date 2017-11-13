package edu.odu.cs.zomp.dietapp.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Diet;
import edu.odu.cs.zomp.dietapp.ui.profile.adapters.DietPickerAdapter;
import edu.odu.cs.zomp.dietapp.util.Constants;


public class DietPickerActivity extends AppCompatActivity implements DietPickerAdapter.DietPickerInterface {

    private static final String TAG = DietPickerActivity.class.getSimpleName();

    @BindView(R.id.dietpicker_recycler) RecyclerView dietsRecycler;

    FirebaseFirestore firestore = null;
    private DietPickerAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_picker);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override protected void onStart() {
        super.onStart();
        firestore = FirebaseFirestore.getInstance();
        initDietList();
    }

    private void initDietList() {
        adapter = new DietPickerAdapter(this, this);
        dietsRecycler.setLayoutManager(new LinearLayoutManager(this));
        dietsRecycler.setAdapter(adapter);

        firestore
                .collection(Constants.DATABASE_PATH_DIETS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult())
                            adapter.add(document.toObject(Diet.class));
                    }
                });
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, DietPickerActivity.class);
    }

    @Override public void dietClicked(Diet diet) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // update wont marshall Diet class automatically for some reason; must update fields individually
        firestore
                .collection(Constants.DATABASE_PATH_USERS_PRIVATE)
                .document(uid)
                .update("activeDiet.id", diet.id, "activeDiet.title", diet.title, "activeDiet.queryParam", diet.queryParam)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Diet updated for user " + uid);
                    finish();
                })
                .addOnFailureListener(e -> Log.w(TAG, "Error updating user diet", e));
    }
}
