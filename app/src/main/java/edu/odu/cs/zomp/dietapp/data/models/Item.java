package edu.odu.cs.zomp.dietapp.data.models;


public abstract class Item {

    public static final int ITEM_TYPE_USABLE = 0;
    public static final int ITEM_TYPE_WEAPON = 1;
    public static final int ITEM_TYPE_ARMOR = 2;
    public static final int ITEM_TYPE_ACCESSORY = 3;

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
}
