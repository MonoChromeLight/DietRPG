package edu.odu.cs.zomp.dietapp.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.net.yummly.models.Match;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;


public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {

    // Adapter interface
    public interface IHomeAdapter {
        void itemClicked(Match match);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.list_item_home_root) LinearLayout rootView;
        @BindView(R.id.list_item_recipe_name) TextView itemText;
        @BindView(R.id.list_item_recipe_ratingBar) MaterialRatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rootView.setOnClickListener(v -> adapterInterface.itemClicked(data.get( getAdapterPosition() )));
        }
    }

    private Context context = null;
    private IHomeAdapter adapterInterface = null;
    private List<Match> data = new ArrayList<>();

    public RecipeAdapter(Context context, IHomeAdapter adapterInterface) {
        this.context = context;
        this.adapterInterface = adapterInterface;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Match recipe = data.get(position);
        holder.itemText.setText(recipe.recipeName);
        holder.ratingBar.setRating(recipe.rating);
    }

    @Override public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }

    public void add(Match recipe) {
        if (data == null)
            data = new ArrayList<>();

        data.add(recipe);
        this.notifyDataSetChanged();
    }

    public void add(List<Match> recipes) {
        data = recipes;
        this.notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        this.notifyDataSetChanged();
    }
}
