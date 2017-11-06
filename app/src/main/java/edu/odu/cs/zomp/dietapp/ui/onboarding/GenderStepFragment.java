package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;

public class GenderStepFragment extends Fragment implements BlockingStep {

    @BindView(R.id.stepper_gender_maleBtn) RadioButton maleBtn;
    @BindView(R.id.stepper_gender_femaleBtn) RadioButton femaleBtn;

    private OnboardingDataManager dataManager;
    private int gender = -1;

    public static GenderStepFragment newInstance() {
        return new GenderStepFragment();
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
        View v = inflater.inflate(R.layout.frag_step_gender, container, false);
        ButterKnife.bind(this, v);

        maleBtn.setOnClickListener(v1 -> onMaleRadioChecked());
        femaleBtn.setOnClickListener(v1 -> onFemaleRadioChecked());
        return v;
    }

    @Override public VerificationError verifyStep() {
        if (gender == -1)
            return new VerificationError("No gender selected");
        return null;
    }

    @Override public void onSelected() {
        //update UI when selected
    }

    @Override public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
    }

    public void onMaleRadioChecked() {
        maleBtn.setChecked(true);
        femaleBtn.setChecked(false);
        gender = Character.GENDER_MALE;
        Log.d("GenderStep", "Gender set to male");
    }

    public void onFemaleRadioChecked() {
        maleBtn.setChecked(false);
        femaleBtn.setChecked(true);
        gender = Character.GENDER_FEMALE;
        Log.d("GenderStep", "Gender set to female");
    }

    @Override
    public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        dataManager.saveData("gender", gender);
        callback.goToNextStep();
    }

    @Override
    public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override
    public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}