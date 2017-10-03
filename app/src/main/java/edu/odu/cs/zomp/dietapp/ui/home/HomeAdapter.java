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


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    // Adapter interface
    public interface IHomeAdapter {
        void itemClicked(String text);
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_list_item_home_root) LinearLayout rootView;
        @BindView(R.id.view_list_item_home_text) TextView itemText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    adapterInterface.itemClicked(data.get( getAdapterPosition() ));
                }
            });
        }
    }

    // Member variables
    private Context context = null;
    private IHomeAdapter adapterInterface = null;
    private List<String> data = new ArrayList<>();

    //Constructor
    public HomeAdapter(Context context, IHomeAdapter adapterInterface) {
        this.context = context;
        this.adapterInterface = adapterInterface;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_home, parent, false);
        return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        String dataItem = data.get(position);
        holder.itemText.setText(dataItem);
    }

    @Override public int getItemCount() {
        return (data != null) ? data.size() : 0;
    }

    public void add(String item) {
        if (data == null)
            data = new ArrayList<>();

        data.add(item);
        this.notifyDataSetChanged();
    }

    public void add(List<String> items) {
        data = items;
        this.notifyDataSetChanged();
    }

    public void clear() {
        data.clear();
        this.notifyDataSetChanged();
    }
}
