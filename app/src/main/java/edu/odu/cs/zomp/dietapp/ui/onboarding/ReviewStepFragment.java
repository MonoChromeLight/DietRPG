package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;

public class ReviewStepFragment extends Fragment implements BlockingStep {

    @BindView(R.id.onboarding_review_name) TextView name;
    @BindView(R.id.onboarding_review_sprite) ImageView sprite;
    @BindView(R.id.onboarding_review_className) TextView className;

    private OnboardingDataManager dataManager;

    public static ReviewStepFragment newInstance() {
        return new ReviewStepFragment();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataManager) {
            dataManager = (OnboardingDataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_step_review, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public VerificationError verifyStep() {
        return null;
    }

    @Override public void onSelected() {
        Bundle userInfo = dataManager.getData();
        name.setText(userInfo.getString("name"));
        int classType = userInfo.getInt("class");
        Log.d("Onboarding", "Retrieved class is " + classType);
        switch (classType) {
            case Character.CLASS_WARRIOR:
                className.setText("Warrior");
                break;
            case Character.CLASS_MAGE:
                className.setText("Mage");
                break;
            case Character.CLASS_ROGUE:
                className.setText("Rogue");
                break;
        }

        int gender = userInfo.getInt("gender");
        String spriteFilename = gender == Character.GENDER_MALE ? "character_male.png" : "character_female.png";
        try {
            InputStream imgStream = getContext().getAssets().open(spriteFilename);
            Drawable d = Drawable.createFromStream(imgStream, null);
            sprite.setImageDrawable(d);
        } catch (IOException e) {
            Log.e("Onboarding", "Error loading character sprite from assets", e.fillInStackTrace());
        }
    }

    @Override public void onError(@NonNull VerificationError error) {

    }

    @Override public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        Log.d("Onboarding", "review on next triggered");
        callback.goToNextStep();
    }

    @Override public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        Log.d("Onboarding", "Review on complete triggered");
        callback.complete();
    }

    @Override public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}