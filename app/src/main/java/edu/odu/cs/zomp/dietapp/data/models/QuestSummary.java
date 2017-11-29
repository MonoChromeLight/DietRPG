package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QuestSummary implements Parcelable {

    public String questId = null;
    public boolean userLeveledUp = false;
    public Map<String, Integer> expMap;
    public List<String> loot = null;
    public List<String> questsUnlocked = null;

    public QuestSummary() { }

    public QuestSummary(String questId, boolean userLeveledUp, Map<String, Integer> expMap, List<String> loot, List<String> questsUnlocked) {
        this.questId = questId;
        this.userLeveledUp = userLeveledUp;
        this.expMap = expMap;
        this.loot = loot;
        this.questsUnlocked = questsUnlocked;
    }


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questId);
        dest.writeByte(this.userLeveledUp ? (byte) 1 : (byte) 0);
        dest.writeInt(this.expMap.size());
        for (Map.Entry<String, Integer> entry : this.expMap.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeValue(entry.getValue());
        }
        dest.writeStringList(this.loot);
        dest.writeStringList(this.questsUnlocked);
    }

    protected QuestSummary(Parcel in) {
        this.questId = in.readString();
        this.userLeveledUp = in.readByte() != 0;
        int expMapSize = in.readInt();
        this.expMap = new HashMap<>(expMapSize);
        for (int i = 0; i < expMapSize; i++) {
            String key = in.readString();
            Integer value = (Integer) in.readValue(Integer.class.getClassLoader());
            this.expMap.put(key, value);
        }
        this.loot = in.createStringArrayList();
        this.questsUnlocked = in.createStringArrayList();
    }

    public static final Creator<QuestSummary> CREATOR = new Creator<QuestSummary>() {
        @Override public QuestSummary createFromParcel(Parcel source) { return new QuestSummary(source); }
        @Override public QuestSummary[] newArray(int size) { return new QuestSummary[size]; }
    };
}
