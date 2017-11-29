package edu.odu.cs.zomp.dietapp.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.QuestProgress;

import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_AGI;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_CON;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_DEX;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_INT;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_MDEF;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_PDEF;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_STR;
import static edu.odu.cs.zomp.dietapp.util.Constants.ATTRIBUTE_WIS;
import static edu.odu.cs.zomp.dietapp.util.Constants.INITAL_QUEST_DESC;
import static edu.odu.cs.zomp.dietapp.util.Constants.INITIAL_QUEST_ID;
import static edu.odu.cs.zomp.dietapp.util.Constants.INITIAL_QUEST_NAME;
import static edu.odu.cs.zomp.dietapp.util.Constants.INITIAL_QUEST_SEGMENTS;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_ACC1;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_ACC2;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_BODY;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_HELM;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_LEGS;
import static edu.odu.cs.zomp.dietapp.util.Constants.SLOT_WEAPON;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_EXP;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_EXPTOLVL;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_HEALTH;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_LEVEL;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_MANA;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_MAX_HEALTH;
import static edu.odu.cs.zomp.dietapp.util.Constants.STAT_MAX_MANA;


/**
 * Stats:
 *  Augmented stat = base stat + combined sum of stat from equipment
 */
public class CharacterUtil {

    public static Map<String, Integer> getStartingWarriorAttributes() {
        Map<String, Integer> warriorStats = new HashMap<>();
        warriorStats.put(ATTRIBUTE_CON, 10);
        warriorStats.put(ATTRIBUTE_STR, 10);
        warriorStats.put(ATTRIBUTE_INT, 4);
        warriorStats.put(ATTRIBUTE_WIS, 3);
        warriorStats.put(ATTRIBUTE_AGI, 5);
        warriorStats.put(ATTRIBUTE_DEX, 5);
        warriorStats.put(ATTRIBUTE_PDEF, 8);
        warriorStats.put(ATTRIBUTE_MDEF, 3);

        return warriorStats;
    }

    public static Map<String, Integer> getStartingMageAttributes() {
        Map<String, Integer> mageStats = new HashMap<>();
        mageStats.put(ATTRIBUTE_CON, 5);
        mageStats.put(ATTRIBUTE_STR, 4);
        mageStats.put(ATTRIBUTE_INT, 10);
        mageStats.put(ATTRIBUTE_WIS, 8);
        mageStats.put(ATTRIBUTE_AGI, 4);
        mageStats.put(ATTRIBUTE_DEX, 5);
        mageStats.put(ATTRIBUTE_PDEF, 3);
        mageStats.put(ATTRIBUTE_MDEF, 8);

        return mageStats;
    }

    public static Map<String, Integer> getStartingRogueAttributes() {
        Map<String, Integer> rogueStats = new HashMap<>();
        rogueStats.put(ATTRIBUTE_CON, 5);
        rogueStats.put(ATTRIBUTE_STR, 6);
        rogueStats.put(ATTRIBUTE_INT, 5);
        rogueStats.put(ATTRIBUTE_WIS, 5);
        rogueStats.put(ATTRIBUTE_AGI, 10);
        rogueStats.put(ATTRIBUTE_DEX, 8);
        rogueStats.put(ATTRIBUTE_PDEF, 5);
        rogueStats.put(ATTRIBUTE_MDEF, 5);

        return rogueStats;
    }

    public static Character generateNewCharacter(String id, String name, int gender, int playerClass) {
        Character character = new Character();
        character.id = id;
        character.name = name;
        character.gender = gender;
        character.playerClass = playerClass;
        character.inventory = new ArrayList<>();
        character.spellbook = new ArrayList<>();
        character.questJournal = new ArrayList<>();

        QuestProgress starterQuestProgess = new QuestProgress(
                INITIAL_QUEST_ID,
                INITIAL_QUEST_NAME,
                INITAL_QUEST_DESC,
                0, INITIAL_QUEST_SEGMENTS);
        character.questJournal.add(starterQuestProgess);

        switch (playerClass) {
            case Character.CLASS_WARRIOR:
                character.attributes = CharacterUtil.getStartingWarriorAttributes();
                character.spellbook.add("Rage");
                break;
            case Character.CLASS_MAGE:
                character.attributes = CharacterUtil.getStartingMageAttributes();
                character.spellbook.add("Fireball");
                break;
            case Character.CLASS_ROGUE:
                character.attributes = CharacterUtil.getStartingRogueAttributes();
                character.spellbook.add("Evade");
            default:
                Log.e("CharUtil", "Invalid class");
        }

        // Update stat values to reflect dynamic attributes from player class
        character.stats = new HashMap<>();
        character.stats.put(STAT_HEALTH, 50); // Health
        character.stats.put(STAT_MAX_HEALTH, 50); // Max health
        character.stats.put(STAT_MANA, 30); // Mana
        character.stats.put(STAT_MAX_MANA, 30); // Max mana
        character.stats.put(STAT_LEVEL, 1); // Level
        character.stats.put(STAT_EXP, 0); // Exp
        character.stats.put(STAT_EXPTOLVL, 100); // Exp to level

        character.equipment = new HashMap<>();
        character.equipment.put(SLOT_HELM, null);
        character.equipment.put(SLOT_BODY, null);
        character.equipment.put(SLOT_LEGS, null);
        character.equipment.put(SLOT_WEAPON, null);
        character.equipment.put(SLOT_ACC1, null);
        character.equipment.put(SLOT_ACC2, null);

        return character;
    }
}
