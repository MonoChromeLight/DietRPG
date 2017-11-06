package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class PlayerClassStepFragment extends Fragment implements BlockingStep {

    @BindView(R.id.stepper_class_warriorBtn) RadioButton warriorBtn;
    @BindView(R.id.stepper_class_mageBtn) RadioButton mageBtn;
    @BindView(R.id.stepper_class_rogueBtn) RadioButton rogueBtn;

    private OnboardingDataManager dataManager;
    private int chosenClass = -1;

    public static PlayerClassStepFragment newInstance() {
        return new PlayerClassStepFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataManager) {
            dataManager = (OnboardingDataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_step_playerclass, container, false);
        ButterKnife.bind(this, v);

        warriorBtn.setOnClickListener(v1 -> warriorBtnClicked());
        mageBtn.setOnClickListener(v1 -> mageBtnClicked());
        rogueBtn.setOnClickListener(v1 -> rogueBtnClicked());
        return v;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override public VerificationError verifyStep() {
        if (chosenClass == -1)
            return new VerificationError("No class chosen");
        return null;
    }

    @Override public void onSelected() {
        //update UI when selected
    }

    @Override public void onError(@NonNull VerificationError error) {
        Toast.makeText(getContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        dataManager.saveData("class", chosenClass);
        callback.goToNextStep();
    }

    @Override public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }

    private void warriorBtnClicked() {
        warriorBtn.setChecked(true);
        mageBtn.setChecked(false);
        rogueBtn.setChecked(false);

        chosenClass = Character.CLASS_WARRIOR;
    }

    private void mageBtnClicked() {
        warriorBtn.setChecked(false);
        mageBtn.setChecked(true);
        rogueBtn.setChecked(false);

        chosenClass = Character.CLASS_MAGE;
    }

    private void rogueBtnClicked() {
        warriorBtn.setChecked(false);
        mageBtn.setChecked(false);
        rogueBtn.setChecked(true);

        chosenClass = Character.CLASS_ROGUE;
    }
}