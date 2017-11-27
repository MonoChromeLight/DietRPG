package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;


public class QuestSummary implements Parcelable {

    public String questId = null;
    public int expGained = 0;
    public boolean userLeveledUp = false;
    public List<String> loot = null;
    public List<String> questsUnlocked = null;

    public QuestSummary() { }

    public QuestSummary(String id, int expGained, boolean userLeveledUp, List<String> loot, List<String> questsUnlocked) {
        this.questId = id;
        this.expGained = expGained;
        this.userLeveledUp = userLeveledUp;
        this.loot = loot;
        this.questsUnlocked = questsUnlocked;
    }


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questId);
        dest.writeInt(this.expGained);
        dest.writeByte(this.userLeveledUp ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.loot);
        dest.writeStringList(this.questsUnlocked);
    }

    protected QuestSummary(Parcel in) {
        this.questId = in.readString();
        this.expGained = in.readInt();
        this.userLeveledUp = in.readByte() != 0;
        this.loot = in.createStringArrayList();
        this.questsUnlocked = in.createStringArrayList();
    }

    public static final Parcelable.Creator<QuestSummary> CREATOR = new Parcelable.Creator<QuestSummary>() {
        @Override public QuestSummary createFromParcel(Parcel source) { return new QuestSummary(source); }
        @Override public QuestSummary[] newArray(int size) { return new QuestSummary[size]; }
    };
}
