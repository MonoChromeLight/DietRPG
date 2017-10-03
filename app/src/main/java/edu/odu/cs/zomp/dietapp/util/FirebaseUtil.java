package edu.odu.cs.zomp.dietapp.util;


import java.util.ArrayList;
import java.util.HashMap;

import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.StatArray;

public class FirebaseUtil {

    private static final String TAG = "FirebaseUtil";

    public static Character generateNewCharacter(String id, String name, int gender) {
        Character character = new Character();
        character.id = id;
        character.name = name;
        character.gender = gender;

        character.inventory = new ArrayList<>();
        character.equipment = new HashMap<>();
        character.equipment.put("helm", null);
        character.equipment.put("body", null);
        character.equipment.put("legs", null);
        character.equipment.put("weapon", null);
        character.equipment.put("accessory1", null);
        character.equipment.put("accessory2", null);

        character.stats = new StatArray(20, 5, 5, 5, 5, 5, 5);
        character.level = 1;
        character.currentExperience = 0;

        return character;
    }
}
