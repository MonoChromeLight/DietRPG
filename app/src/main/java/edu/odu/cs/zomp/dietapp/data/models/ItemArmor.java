package edu.odu.cs.zomp.dietapp.data.models;


public class ItemArmor extends Item {

    public StatArray stats;

    public ItemArmor() {
        super();
        this.stats = null;
    }

    public ItemArmor(String id, String name, StatArray itemStats) {
        super(ITEM_TYPE_ARMOR, id, name);
        this.stats = itemStats;
    }


}
