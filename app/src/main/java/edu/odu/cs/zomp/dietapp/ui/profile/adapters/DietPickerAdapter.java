package edu.odu.cs.zomp.dietapp.ui.profile.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.Diet;


public class DietPickerAdapter extends RecyclerView.Adapter<DietPickerAdapter.DietHolder> {

    private Context context;
    private DietPickerInterface dietPickerInterface;
    private List<Diet> availableDiets = new ArrayList<>();

    public void setDiets(List<Diet> diets) {
        this.availableDiets = diets;
        this.notifyDataSetChanged();
    }

    public interface DietPickerInterface {
        void dietClicked(Diet diet);
    }

    public DietPickerAdapter(Context context, DietPickerInterface dietPickerInterface) {
        this.context = context;
        this.dietPickerInterface = dietPickerInterface;
    }

    @Override public DietHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(context).inflate(R.layout.item_card_diet, parent, false);
        return new DietHolder(view);
    }

    @Override public void onBindViewHolder(DietHolder holder, int position) {
        Diet diet = availableDiets.get(position);

        holder.title.setText(diet.title);

        // Retrieve image from firebase
        StorageReference bgRef = FirebaseStorage.getInstance().getReference()
                .child("diet-backgrounds")
                .child(diet.id + ".png");

        Glide.with(context)
                .load(bgRef)
                .into(holder.bg);
    }

    @Override public int getItemCount() {
        return availableDiets.size();
    }

    public void add(Diet diet) {
        this.availableDiets.add(diet);
        this.notifyDataSetChanged();
    }

    class DietHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_list_item_card_diet_root) CardView root;
        @BindView(R.id.view_list_item_card_diet_bg) ImageView bg;
        @BindView(R.id.view_list_item_card_diet_title) TextView title;

        public DietHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            root.setOnClickListener(view -> dietPickerInterface.dietClicked(availableDiets.get( getAdapterPosition() )));
        }
    }
}