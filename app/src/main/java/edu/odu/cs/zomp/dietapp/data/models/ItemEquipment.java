package edu.odu.cs.zomp.dietapp.data.models;


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
}
