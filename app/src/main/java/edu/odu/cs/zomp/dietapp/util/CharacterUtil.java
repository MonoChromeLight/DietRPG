package edu.odu.cs.zomp.dietapp.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.odu.cs.zomp.dietapp.data.models.Character;


/**
 * Stats:
 *  Augmented stat = base stat + combined sum of stat from equipment
 */
public class CharacterUtil {

    public static Map<String, Integer> getStartingWarriorAttributes() {
        Map<String, Integer> warriorStats = new HashMap<>();
        warriorStats.put("Constitution", 10);
        warriorStats.put("Strength", 10);
        warriorStats.put("Intelligence", 4);
        warriorStats.put("Wisdom", 3);
        warriorStats.put("Agility", 5);
        warriorStats.put("Dexterity", 5);
        warriorStats.put("Phys. Def", 8);
        warriorStats.put("Magic Def", 3);

        return warriorStats;
    }

    public static Map<String, Integer> getStartingMageAttributes() {
        Map<String, Integer> mageStats = new HashMap<>();
        mageStats.put("Constitution", 5);
        mageStats.put("Strength", 4);
        mageStats.put("Intelligence", 10);
        mageStats.put("Wisdom", 8);
        mageStats.put("Agility", 4);
        mageStats.put("Dexterity", 5);
        mageStats.put("Phys. Def", 3);
        mageStats.put("Magic Def", 8);

        return mageStats;
    }

    public static Map<String, Integer> getStartingRogueAttributes() {
        Map<String, Integer> rogueStats = new HashMap<>();
        rogueStats.put("Constitution", 5);
        rogueStats.put("Strength", 6);
        rogueStats.put("Intelligence", 5);
        rogueStats.put("Wisdom", 5);
        rogueStats.put("Agility", 10);
        rogueStats.put("Dexterity", 8);
        rogueStats.put("Phys. Def", 5);
        rogueStats.put("Magic Def", 5);

        return rogueStats;
    }

    public static Character generateNewCharacter(String id, int gender, int playerClass) {
        Character character = new Character();
        character.id = id;
        character.gender = gender;
        character.playerClass = playerClass;
        character.inventory = new ArrayList<>();

        switch (playerClass) {
            case Character.CLASS_WARRIOR:
                character.attributes = CharacterUtil.getStartingWarriorAttributes();
                break;
            case Character.CLASS_MAGE:
                character.attributes = CharacterUtil.getStartingMageAttributes();
                break;
            case Character.CLASS_ROGUE:
                character.attributes = CharacterUtil.getStartingRogueAttributes();
        }

        // Update stat values to reflect dynamic attributes from player class
        character.stats = new HashMap<>();
        character.stats.put("Health", 50); // Health
        character.stats.put("Max Health", 50); // Max health
        character.stats.put("Mana", 30); // Mana
        character.stats.put("Max Mana", 30); // Max mana
        character.stats.put("Level", 1); // Level
        character.stats.put("Experience", 0); // Exp
        character.stats.put("ExpToLevel", 1000); // Exp to level

        character.equipment = new HashMap<>();
        character.equipment.put("helm", null);
        character.equipment.put("body", null);
        character.equipment.put("legs", null);
        character.equipment.put("weapon", null);
        character.equipment.put("accessory1", null);
        character.equipment.put("accessory2", null);

        return character;
    }
}
