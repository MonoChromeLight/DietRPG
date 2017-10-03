package edu.odu.cs.zomp.dietapp.data.models;

import java.util.List;
import java.util.Map;

public class Character extends Actor {

    public static final int GENDER_MALE = 0;
    public static final int GENDER_FEMALE = 1;

    public String name;
    public int gender;
    public int level;
    public int currentExperience;
    public StatArray stats;
    public Map<String, Item> equipment;
    public List<Item> inventory;

    public Character() {
        super();
        this.name = null;
        this.gender = -1;
        this.level = 0;
        this. currentExperience = 0;
        this.stats = null;
        this.equipment = null;
        this.inventory = null;
    }

    public Character(String id, String name, int gender, int level, int currentExperience, StatArray stats, Map<String, Item> equipment, List<Item> inventory) {
        super(id);
        this.name = name;
        this.gender = gender;
        this.level = level;
        this.currentExperience = currentExperience;
        this.stats = stats;
        this.equipment = equipment;
        this.inventory = inventory;
    }
}
