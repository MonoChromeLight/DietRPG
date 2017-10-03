package edu.odu.cs.zomp.dietapp.data.models;

/**
 * Created by cyberpunk on 10/2/17.
 */

public class StatArray {

    // Constitution determines max hitpoints
    public int constitution;

    // Strength determines physical attack damage
    public int strength;

    // Intellect determines magical attack damage
    public int intelligence;

    // Agility determines critical attack chance
    public int agility;

    // Dexterity determines dodge chance
    public int dexterity;

    // Defense against physical attacks
    public int physicalDefense;

    // Defense against magical attacks
    public int magicalDefense;

    public StatArray() {
        this.constitution = 0;
        this.strength = 0;
        this.intelligence = 0;
        this.agility = 0;
        this.dexterity = 0;
        this.physicalDefense = 0;
        this.magicalDefense = 0;
    }

    public StatArray(int constitution, int strength, int intelligence, int agility, int dexterity, int physicalDefense, int magicalDefense) {
        this.constitution = constitution;
        this.strength = strength;
        this.intelligence = intelligence;
        this.agility = agility;
        this.dexterity = dexterity;
        this.physicalDefense = physicalDefense;
        this.magicalDefense = magicalDefense;
    }
}
