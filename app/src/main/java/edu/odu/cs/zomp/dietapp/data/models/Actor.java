package edu.odu.cs.zomp.dietapp.data.models;

import java.util.Map;

public class Actor {

    public static final String[] statNames = {
            "Health",
            "Max Health",
            "Mana",
            "Max Mana",
            "Level",
            "Experience",
            "ExpToLevel"
    };

    public static final String[] attributeNames = {
            "Constitution",
            "Strength",
            "Intelligence",
            "Wisdom",
            "Agility",
            "Dexterity",
            "Physical Def",
            "Magic Def"
    };

    public String id;
    public String name;
    public Map<String, Integer> stats;
    public Map<String, Integer> attributes;


    public Actor() {
        this.id = null;
    }

    public Actor(String id, String name, Map<String, Integer> stats, Map<String, Integer> attributes) {
        this.id = id;
        this.name = name;
        this.stats = stats;
        this.attributes = attributes;
    }
}
