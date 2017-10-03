package edu.odu.cs.zomp.dietapp.ui.equip;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.ui.BaseFragment;

public class EquipFragment extends BaseFragment {

    public static EquipFragment newInstance() {
        return new EquipFragment();
    }

    public EquipFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_equipment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
