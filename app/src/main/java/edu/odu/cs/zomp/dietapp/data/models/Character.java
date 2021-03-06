package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.odu.cs.zomp.dietapp.util.Constants;

public class Character extends Actor implements Parcelable {

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    public static final int CLASS_WARRIOR = 0;
    public static final int CLASS_MAGE = 1;
    public static final int CLASS_ROGUE = 2;

    public int gender;
    public int playerClass;
    public double dietMultiplier;
    public Map<String, ItemEquipment> equipment;
    public List<Item> inventory;
    public List<String> spellbook;
    public List<QuestProgress> questJournal;

    public Character() {
        super();
        this.gender = -1;
        this.dietMultiplier = 1;
        this.spellbook = null;
        this.equipment = null;
        this.inventory = null;
        this.questJournal = null;
    }

    public Character(String id, String name, int gender, int playerClass, double dietMultiplier, Map<String, Integer> stats,
                     Map<String, Integer> attributes, Map<String, ItemEquipment> equipment,
                     List<Item> inventory, List<String> spellbook, List<QuestProgress> questJournal) {
        super(id, name, stats, attributes);
        this.gender = gender;
        this.dietMultiplier = dietMultiplier;
        this.playerClass = playerClass;
        this.equipment = equipment;
        this.inventory = inventory;
        this.spellbook = spellbook;
        this.questJournal = questJournal;
    }

    @Exclude
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

    // Returns true if player dies
    public boolean takeDamage(int initialDmg) {
        int defense = (int) (this.attributes.get(Constants.ATTRIBUTE_PDEF) * dietMultiplier);
        int hp = this.stats.get(Constants.STAT_HEALTH);
        int damage = (initialDmg - defense) < 0 ? 0 : (initialDmg - defense);
        if (hp - damage <= 0) {
            return true;
        } else {
            hp -= damage;
            this.stats.put(Constants.STAT_HEALTH, hp);
            return false;
        }
    }

    public boolean gainExp(int exp) {
        this.stats.put(Constants.STAT_EXP, (int) (this.stats.get(Constants.STAT_EXP) + (exp * dietMultiplier)));
        if (this.stats.get(Constants.STAT_EXP) >= this.stats.get(Constants.STAT_EXPTOLVL)) {
            levelUp();
            return true;
        }

        return false;
    }

    private void levelUp() {
        this.stats.put(Constants.STAT_LEVEL, this.stats.get(Constants.STAT_LEVEL) + 1);
        this.stats.put(Constants.STAT_EXPTOLVL, (int) (this.stats.get(Constants.STAT_EXPTOLVL) * 1.618));
        this.stats.put(Constants.STAT_EXP, 0);

        // Increase base attributes according to class tables

        // Update health and mana totals according to new attribute values

    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("gender", gender);
        map.put("dietMultiplier", dietMultiplier);
        map.put("playerClass", playerClass);
        map.put("stats", stats);
        map.put("attributes", attributes);
        map.put("equipment", equipment);
        map.put("inventory", inventory);
        map.put("questJournal", questJournal);
        return map;
    }


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.gender);
        dest.writeInt(this.playerClass);
        dest.writeDouble(this.dietMultiplier);
        dest.writeInt(this.equipment.size());
        for (Map.Entry<String, ItemEquipment> entry : this.equipment.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeParcelable(entry.getValue(), flags);
        }
        dest.writeTypedList(this.inventory);
        dest.writeStringList(this.spellbook);
        dest.writeTypedList(this.questJournal);
    }

    protected Character(Parcel in) {
        super(in);
        this.gender = in.readInt();
        this.playerClass = in.readInt();
        this.dietMultiplier = in.readDouble();
        int equipmentSize = in.readInt();
        this.equipment = new HashMap<>(equipmentSize);
        for (int i = 0; i < equipmentSize; i++) {
            String key = in.readString();
            ItemEquipment value = in.readParcelable(ItemEquipment.class.getClassLoader());
            this.equipment.put(key, value);
        }
        this.inventory = in.createTypedArrayList(Item.CREATOR);
        this.spellbook = in.createStringArrayList();
        this.questJournal = in.createTypedArrayList(QuestProgress.CREATOR);
    }

    public static final Creator<Character> CREATOR = new Creator<Character>() {
        @Override public Character createFromParcel(Parcel source) { return new Character(source); }
        @Override public Character[] newArray(int size) { return new Character[size]; }
    };
}
