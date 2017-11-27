package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;

import java.util.HashMap;
import java.util.Map;

public class ItemEquipment extends Item {

    public static final int TYPE_HELM = 0;
    public static final int TYPE_BODY = 1;
    public static final int TYPE_LEGS = 2;
    public static final int TYPE_WEAPON = 3;
    public static final int TYPE_ACCESSORY = 4;

    public int equipmentType;
    public Map<String, Integer> attributes;

    public ItemEquipment() {
        this.equipmentType = -1;
        this.attributes = null;
    }

    public ItemEquipment(String id, String name, int equipmentType, Map<String, Integer> attributes) {
        super(ITEM_TYPE_EQUIPMENT, id, name);
        this.equipmentType = equipmentType;
        this.attributes = attributes;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.equipmentType);
        dest.writeInt(this.attributes.size());
        for (Map.Entry<String, Integer> entry : this.attributes.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
    }

    protected ItemEquipment(Parcel in) {
        super(in);
        this.equipmentType = in.readInt();
        int attributesSize = in.readInt();
        this.attributes = new HashMap<>(attributesSize);
        for (int i = 0; i < attributesSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.attributes.put(key, value);
        }
    }

    public static final Creator<ItemEquipment> CREATOR = new Creator<ItemEquipment>() {
        @Override public ItemEquipment createFromParcel(Parcel source) { return new ItemEquipment(source); }
        @Override public ItemEquipment[] newArray(int size) { return new ItemEquipment[size]; }
    };
}
