package edu.odu.cs.zomp.dietapp.ui.character.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.odu.cs.zomp.dietapp.R;
import edu.odu.cs.zomp.dietapp.data.models.ItemEquipment;
import edu.odu.cs.zomp.dietapp.util.Constants;

public class EquipmentAdapter extends BaseAdapter {

    private Context context;
    private Map<String, ItemEquipment> equipment;


    public EquipmentAdapter(Context context, Map<String, ItemEquipment> equipmentList) {
        this.context = context;
        this.equipment = equipmentList;
    }

    @Override public int getCount() { return 6; }

    @Override public Object getItem(int position) {
        ItemEquipment item = null;
        switch (position) {
            case 0:
                item = equipment.get("helm");
                break;
            case 1:
                item = equipment.get("body");
                break;
            case 2:
                item = equipment.get("legs");
                break;
            case 3:
                item = equipment.get("weapon");
                break;
            case 4:
                item = equipment.get("accessory1");
                break;
            case 5:
                item = equipment.get("accessory2");
                break;
        }

        return item;
    }

    @Override public long getItemId(int position) {
        return position;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_equipment, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Load sprite into holder
        ItemEquipment item = (ItemEquipment) getItem(position);
        if (item != null) {
            StorageReference iconRef = FirebaseStorage.getInstance().getReference()
                    .child(Constants.STORAGE_PATH_SPRITES_ITEMS)
                    .child(item.id + Constants.FILE_EXT_PNG);

            Glide.with(context)
                    .load(iconRef)
                    .into(holder.spriteImg);
        }

        return convertView;
    }



    public class ViewHolder {
        @BindView(R.id.item_equipment_icon) ImageView spriteImg;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
