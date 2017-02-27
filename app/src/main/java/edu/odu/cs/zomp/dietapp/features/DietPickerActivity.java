package edu.odu.cs.zomp.dietapp.features;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.shared.models.Diet;


public class DietPickerActivity extends AppCompatActivity {

    @BindView(R.id.view_dietpicker_recycler) RecyclerView dietsRecycler;

    private DietsAdapter adapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_picker);
        ButterKnife.bind(this);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initDietList();
    }

    private void initDietList() {
        adapter = new DietsAdapter();
        dietsRecycler.setLayoutManager(new LinearLayoutManager(this));
        dietsRecycler.setAdapter(adapter);

        adapter.add(new Diet("Vegetarian", "bg_vegetarian.jpg"));
        adapter.add(new Diet("No Filter", "bg_nofilterdiet.jpg"));
        adapter.add(new Diet("Paleo", "bg_paleo.jpg"));
        adapter.add(new Diet("Low Carb", "bg_low-carb.jpg"));
        adapter.add(new Diet("Vegan", "bg_vegan.jpg"));
    }

    class DietsAdapter extends RecyclerView.Adapter<DietsAdapter.DietHolder> {

        private List<Diet> availableDiets = new ArrayList<>();

        @Override public DietHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view =  LayoutInflater.from(DietPickerActivity.this).inflate(R.layout.list_item_card_diet, parent, false);
            return new DietHolder(view);
        }

        @Override public void onBindViewHolder(DietHolder holder, int position) {
            Diet diet = availableDiets.get(position);

            holder.title.setText(diet.name);

            try {
                InputStream imgStream = getAssets().open(diet.backgroundPath);
                Drawable bgImg = Drawable.createFromStream(imgStream, null);
                holder.bg.setImageDrawable(bgImg);
            } catch (IOException e) {
                Log.e("DietsAdapter", e.getMessage(), e.fillInStackTrace());
            }
        }

        @Override public int getItemCount() {
            return availableDiets.size();
        }

        void add(Diet diet) {
            this.availableDiets.add(diet);
            this.notifyDataSetChanged();
        }

        class DietHolder extends RecyclerView.ViewHolder {

            @BindView(R.id.view_list_item_card_diet_bg) ImageView bg;
            @BindView(R.id.view_list_item_card_diet_title) TextView title;

            public DietHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
