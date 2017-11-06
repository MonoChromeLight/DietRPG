package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.stepstone.stepper.BlockingStep;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Character;

public class NameStepFragment extends Fragment implements BlockingStep {


    private OnboardingDataManager dataManager;

    @BindView(R.id.stepper_name_input) EditText nameInput;

    public static NameStepFragment newInstance() {
        return new NameStepFragment();
    }

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnboardingDataManager) {
            dataManager = (OnboardingDataManager) context;
        } else {
            throw new IllegalStateException("Activity must implement DataManager interface!");
        }
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frag_step_name, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override public VerificationError verifyStep() {
        if (nameInput.getText().toString().isEmpty())
            return new VerificationError("empty name");
        else
            return null;
    }

    @Override public void onSelected() {
        //update UI when selected
    }


    @Override public void onError(@NonNull VerificationError error) {
        if (TextUtils.equals("empty name", error.getErrorMessage())) {
            nameInput.setError("Please enter a name");
        }
    }

    public void onGenderBtnClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.stepper_gender_femaleBtn:
                if (checked)
                    dataManager.saveData("gender", Character.GENDER_FEMALE);
                    break;
            case R.id.stepper_gender_maleBtn:
                if (checked)
                    dataManager.saveData("gender", Character.GENDER_FEMALE);
                    break;
        }
    }

    @Override public void onNextClicked(StepperLayout.OnNextClickedCallback callback) {
        dataManager.saveData(nameInput.getText().toString().trim(), 0);
        callback.goToNextStep();
    }

    @Override public void onCompleteClicked(StepperLayout.OnCompleteClickedCallback callback) {
        callback.complete();
    }

    @Override public void onBackClicked(StepperLayout.OnBackClickedCallback callback) {
        callback.goToPrevStep();
    }
}