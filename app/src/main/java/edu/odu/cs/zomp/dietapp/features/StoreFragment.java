package edu.odu.cs.zomp.dietapp.features;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;

/**
 * Created by David on 2/8/17.
 */

public class StoreFragment extends BaseFragment {

    public static StoreFragment newInstance() {
        return  new StoreFragment();
    }

    public StoreFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_store, container, false);
    }
}
