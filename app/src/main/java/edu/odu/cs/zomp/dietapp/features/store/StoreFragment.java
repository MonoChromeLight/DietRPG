package edu.odu.cs.zomp.dietapp.features.store;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.BaseFragment;
import edu.odu.cs.zomp.dietapp.shared.models.StoreItem;

/**
 * Created by David on 2/8/17.
 */

public class StoreFragment extends BaseFragment {

    @BindView(R.id.view_store_categoryGrid) GridView categoryGrid;
    @BindView(R.id.view_store_topItemsRecycler) RecyclerView topItemsRecycler;
    @BindView(R.id.view_store_newItemsRecycler) RecyclerView newItemsRecycler;

    StoreItemsAdapter topItemsAdapter = null;
    StoreItemsAdapter newItemsAdapter = null;

    public static StoreFragment newInstance() {
        return  new StoreFragment();
    }

    public StoreFragment() { }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_store, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        categoryGrid.setAdapter(new GridAdapter(getContext()));
        categoryGrid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);

        topItemsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topItemsRecycler.setHasFixedSize(true);
        topItemsAdapter = new StoreItemsAdapter(getContext());
        topItemsRecycler.setAdapter(topItemsAdapter);
        topItemsAdapter.setData(generateStoreData());


        newItemsRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newItemsRecycler.setHasFixedSize(true);
        newItemsAdapter = new StoreItemsAdapter(getContext());
        newItemsRecycler.setAdapter(newItemsAdapter);
        newItemsAdapter.setData(generateStoreData());
    }

    public List<StoreItem> generateStoreData() {
        List<StoreItem> dummyItems = new ArrayList<>();

        dummyItems.add(new StoreItem("0", "Greatsword", "weapon", 1.99));
        dummyItems.add(new StoreItem("1", "Wood Sheild", "sheild", 0.99));
        dummyItems.add(new StoreItem("2", "Health potion", "potion", 0.99));
        dummyItems.add(new StoreItem("3", "Elixer", "crystal", 1.99));
        dummyItems.add(new StoreItem("4", "Plate Armor", "armor", 1.99));

        return dummyItems;
    }
}
