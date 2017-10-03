package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.ui.BaseActivity;


public class OnboardingActivity extends BaseActivity {

    private static final String KEY_POSITION = "position";

    @BindView(R.id.onboarding_root) FrameLayout viewRoot;
    @BindView(R.id.onboarding_stepper) StepperLayout stepperLayout;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        stepperLayout.setAdapter(new StepperAdapter(getSupportFragmentManager(), this));
    }

    @Override protected void onStart() {
        super.onStart();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, OnboardingActivity.class);
    }

    public static class StepperAdapter extends AbstractFragmentStepAdapter {

        public StepperAdapter(FragmentManager fm, Context context) {
            super(fm, context);
        }

        @Override public Step createStep(int position) {
            final StepFragment step = new StepFragment();
            Bundle b = new Bundle();
            b.putInt(KEY_POSITION, position);
            step.setArguments(b);
            return step;
        }

        @Override public int getCount() {
            return 3;
        }

        @NonNull
        @Override public StepViewModel getViewModel(@IntRange(from = 0) int position) {
            //Override this method to set Step title for the Tabs, not necessary for other stepper types
            return new StepViewModel.Builder(context)
                    .setTitle("Test") //can be a CharSequence instead
                    .create();
        }
    }
}
