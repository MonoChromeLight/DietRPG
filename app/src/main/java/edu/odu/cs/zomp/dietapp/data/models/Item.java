package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    public static final int ITEM_TYPE_USABLE = 0;
    public static final int ITEM_TYPE_EQUIPMENT = 1;

    public int itemType;
    public String id;
    public String name;

    public Item() {
        this.itemType = -1;
        this.id = null;
        this.name = null;
    }

    public Item(int itemType, String id, String name) {
        this.itemType = itemType;
        this.id = id;
        this.name = name;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    protected Item(Parcel in) {
        this.itemType = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
    }

}
