package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character extends Actor {

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    public static final int CLASS_WARRIOR = 0;
    public static final int CLASS_MAGE = 1;
    public static final int CLASS_ROGUE = 2;

    public int gender;
    public int playerClass;
    public Map<String, ItemEquipment> equipment;
    public List<Item> inventory;
    public List<QuestProgress> questJournal;

    public Character() {
        super();
        this.gender = -1;
        this.stats = null;
        this.attributes = null;
        this.equipment = null;
        this.inventory = null;
        this.questJournal = null;
    }

    public Character(String id, String name, int gender, int playerClass, Map<String, Integer> stats,
                     Map<String, Integer> attributes, Map<String, ItemEquipment> equipment,
                     List<Item> inventory, List<QuestProgress> questJournal) {
        super(id, name, stats, attributes);
        this.gender = gender;
        this.playerClass = playerClass;
        this.equipment = equipment;
        this.inventory = inventory;
        this.questJournal = questJournal;
    }

    public Map<String, Integer> getAugmentedAttributes() {
        Map<String, Integer> augmentedAttributes = attributes;

        for (ItemEquipment item : equipment.values()) {
            if (item == null)
                continue;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                item.attributes.forEach((k, v) -> augmentedAttributes.merge(k, v, Integer::sum));
            } else {
                for (String attr : attributeNames)
                    augmentedAttributes.put(attr, (augmentedAttributes.get(attr) + item.attributes.get(attr)));
            }
        }

        return augmentedAttributes;
    }

    public void levelUp() {
        this.stats.put("Level", this.stats.get("Level") + 1);
        this.stats.put("ExpNextLevel", (int) (this.stats.get("ExpNextLevel") * 1.618));
        this.stats.put("Experience", 0);

        // Increase base attributes according to class tables

        // Update health and mana totals according to new attribute values

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("gender", gender);
        map.put("playerClass", playerClass);
        map.put("stats", stats);
        map.put("attributes", attributes);
        map.put("equipment", equipment);
        map.put("inventory", inventory);
        map.put("questJournal", questJournal);
        return map;
    }
}
