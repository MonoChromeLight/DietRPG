package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Quest implements Parcelable {

    public String id;
    public String name;
    public String description;
    public List<String> prerequisites;
    public List<String> nextQuests;
    public List<String> enemies;
    public List<String> loot;

    public Quest() { }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeStringList(this.prerequisites);
        dest.writeStringList(this.nextQuests);
        dest.writeList(this.enemies);
        dest.writeList(this.loot);
    }

    protected Quest(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.prerequisites = in.createStringArrayList();
        this.nextQuests = in.createStringArrayList();
        this.enemies = new ArrayList<>();
        in.readList(this.enemies, Enemy.class.getClassLoader());
        this.loot = in.createStringArrayList();
    }

    public static final Creator<Quest> CREATOR = new Creator<Quest>() {
        @Override public Quest createFromParcel(Parcel source) { return new Quest(source); }
        @Override public Quest[] newArray(int size) { return new Quest[size]; }
    };
}