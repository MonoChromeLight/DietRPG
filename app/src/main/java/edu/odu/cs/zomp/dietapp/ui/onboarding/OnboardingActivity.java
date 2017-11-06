package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.UserPrivate;
import edu.odu.cs.zomp.dietapp.data.models.UserPublic;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;
import edu.odu.cs.zomp.dietapp.ui.MainActivity;
import edu.odu.cs.zomp.dietapp.util.CharacterUtil;
import edu.odu.cs.zomp.dietapp.util.Constants;


public class OnboardingActivity extends BaseActivity implements OnboardingDataManager, StepperLayout.StepperListener {

    private static final String KEY_POSITION = "position";
    private static final String KEY_DATA = "data";

    @BindView(R.id.onboarding_root) FrameLayout viewRoot;
    @BindView(R.id.onboarding_stepper) StepperLayout stepperLayout;

    private int stepPosition = 0;
    private Bundle userData = null;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        ButterKnife.bind(this);
        stepPosition = (savedInstanceState != null) ? savedInstanceState.getInt(KEY_POSITION) : 0;
        userData = (savedInstanceState != null) ? savedInstanceState.getBundle(KEY_DATA) : new Bundle();
        stepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this), stepPosition);
        stepperLayout.setListener(this);
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_POSITION, stepPosition);
        outState.putBundle(KEY_DATA, userData);
        super.onSaveInstanceState(outState);
    }

    @Override public void onBackPressed() {
        super.onBackPressed();
        stepPosition = stepperLayout.getCurrentStepPosition();
        if (stepPosition > 0) {
            stepperLayout.onBackClicked();
        } else {
            finish();
        }
    }

    @Override
    public void saveData(String key, int value) {
        if (userData == null)
            userData = new Bundle();

        if (TextUtils.equals(key, "gender") || TextUtils.equals(key, "class")) {
            userData.putInt(key, value);
            Log.d("Onboarding", "Set user " + key + " info to " + value);
        } else {
            userData.putString("name", key);
            Log.d("Onboarding", "Set user name info to " + key);
        }
    }

    @Override
    public Bundle getData() {
        return userData;
    }

    @Override public void onCompleted(View completeButton) {
        Log.d("Onboarding", "Review complete, uploading user data");
        FirebaseUser authUser = FirebaseAuth.getInstance().getCurrentUser();

        String name = userData.getString("name");
        int gender = userData.getInt("gender");
        int classType = userData.getInt("class");
        String uid = authUser.getUid();
        Log.d("Onboarding", "Retrieved uid: " + uid);

        Character character = CharacterUtil.generateNewCharacter(uid, name, gender, classType);

        // Create userData
        UserPrivate privateData = new UserPrivate(uid, "-KvVBIHEIbRB2hCpQ47n", 0);
        UserPublic publicData = new UserPublic(uid, authUser.getDisplayName());

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference characterRef = firebaseFirestore
                .collection(Constants.DATABASE_PATH_CHARACTERS)
                .document(uid);
        DocumentReference userPublicRef = firebaseFirestore
                .collection(Constants.DATABASE_PATH_USERS_PUBLIC)
                .document(uid);
        DocumentReference userPrivateRef = firebaseFirestore
                .collection(Constants.DATABASE_PATH_USERS_PRIVATE)
                .document(uid);

        WriteBatch batch = firebaseFirestore.batch();
        batch.set(characterRef, character);
        batch.set(userPublicRef, publicData);
        batch.set(userPrivateRef, privateData);
        batch.commit()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("Onboarding", "Successfully written user character and profile data");
                        startActivity(MainActivity.createIntent(OnboardingActivity.this));
                        finish();
                    } else {
                        Log.e("Onboarding", task.getException().getMessage(), task.getException().fillInStackTrace());
                    }
                });
    }

    @Override public void onError(VerificationError verificationError) {

    }

    @Override public void onStepSelected(int newStepPosition) {

    }

    @Override public void onReturn() {
        finish();
    }

    public static class StepperAdapter extends AbstractFragmentStepAdapter {

        public StepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override public Step createStep(int position) {
            switch (position) {
                case 0:
                    return NameStepFragment.newInstance();
                case 1:
                    return GenderStepFragment.newInstance();
                case 2:
                    return PlayerClassStepFragment.newInstance();
                case 3:
                    return ReviewStepFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override public int getCount() {
            return 4;
        }

        @NonNull
        @Override public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            StepViewModel.Builder builder = new StepViewModel.Builder(context);

            switch (position) {
                case 0:
                    // Name step
                    builder
                            .setTitle("Name")
                            .setBackButtonLabel("Cancel")
                            .setBackButtonStartDrawableResId(StepViewModel.NULL_DRAWABLE)
                            .setEndButtonLabel("Gender")
                            .setNextButtonEndDrawableResId(R.drawable.chevron_right);
                    break;
                case 1:
                    // Gender step
                    builder
                            .setTitle("Gender")
                            .setBackButtonLabel("Name")
                            .setBackButtonStartDrawableResId(R.drawable.chevron_left)
                            .setEndButtonLabel("Class")
                            .setNextButtonEndDrawableResId(R.drawable.chevron_right);
                    break;
                case 2:
                    // Class step
                    builder
                            .setTitle("Class")
                            .setBackButtonLabel("Gender")
                            .setBackButtonStartDrawableResId(R.drawable.chevron_left)
                            .setEndButtonLabel("Review")
                            .setNextButtonEndDrawableResId(R.drawable.chevron_right);
                    break;
                case 3:
                    // Review step
                    builder
                            .setTitle("Review")
                            .setBackButtonLabel("Class")
                            .setBackButtonStartDrawableResId(R.drawable.chevron_left)
                            .setEndButtonLabel("Finish")
                            .setNextButtonEndDrawableResId(StepViewModel.NULL_DRAWABLE);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported position: " + position);
            }
            return builder.create();
        }
    }
}
