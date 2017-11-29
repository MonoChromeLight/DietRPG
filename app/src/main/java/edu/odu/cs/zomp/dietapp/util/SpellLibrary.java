package edu.odu.cs.zomp.dietapp.util;


import android.util.Log;

import edu.odu.cs.zomp.dietapp.data.models.Character;
import edu.odu.cs.zomp.dietapp.data.models.Enemy;


public class SpellLibrary {

    public static void rage(Character player, Enemy enemy) {
        Log.d("SpellLibrary", "Rage cast");
    }

    public static void fireball(Character player, Enemy enemy) {
        Log.d("SpellLibrary", "Fireball cast");
    }

    public static void evade(Character player, Enemy enemy) {
        Log.d("SpellLibrary", "Evade cast");
    }
}
