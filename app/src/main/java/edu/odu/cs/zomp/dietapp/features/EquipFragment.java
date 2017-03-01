package edu.odu.cs.zomp.dietapp.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.odu.cs.zomp.dietapp.R;

public class EquipFragment extends BaseFragment {

    public static EquipFragment newInstance() {
        return new EquipFragment();
    }

    public EquipFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_equipment, container, false);
    }
}