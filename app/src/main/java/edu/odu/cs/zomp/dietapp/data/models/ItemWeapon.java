package edu.odu.cs.zomp.dietapp.data.models;


public class ItemWeapon extends Item {

    public StatArray stats;

    public ItemWeapon() {
        super();
        this.stats = null;
    }

    public ItemWeapon(String id, String name, StatArray itemStats) {
        super(ITEM_TYPE_WEAPON, id, name);
        this.stats = itemStats;
    }


}
