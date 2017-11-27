package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

public class Actor implements Parcelable {

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

    protected Actor(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        int statsSize = in.readInt();
        this.stats = new HashMap<>(statsSize);
        for (int i = 0; i < statsSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.stats.put(key, value);
        }
        int attributesSize = in.readInt();
        this.attributes = new HashMap<>(attributesSize);
        for (int i = 0; i < attributesSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.attributes.put(key, value);
        }
    }

    public static final Creator<Actor> CREATOR = new Creator<Actor>() {
        @Override public Actor createFromParcel(Parcel in) { return new Actor(in); }
        @Override public Actor[] newArray(int size) { return new Actor[size]; }
    };

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.stats.size());
        for (Map.Entry<String, Integer> entry : this.stats.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeInt(this.attributes.size());
        for (Map.Entry<String, Integer> entry : this.attributes.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
    }
}
