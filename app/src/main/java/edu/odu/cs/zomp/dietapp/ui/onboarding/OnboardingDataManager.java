package edu.odu.cs.zomp.dietapp.ui.onboarding;

import android.os.Bundle;

public interface OnboardingDataManager {

    void saveData(String key, int value);
    Bundle getData();
}