package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class QuestProgress implements Parcelable {

    public String questId;
    public String questName;
    public String questDescription;
    public int currentSegment;
    public int totalSegments;

    public QuestProgress() { }

    public QuestProgress(String questId, String questName, String questDescription, int currentSegment, int totalSegments) {
        this.questId = questId;
        this.questName = questName;
        this.questDescription = questDescription;
        this.currentSegment = currentSegment;
        this.totalSegments = totalSegments;
    }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.questId);
        dest.writeString(this.questName);
        dest.writeString(this.questDescription);
        dest.writeInt(this.currentSegment);
        dest.writeInt(this.totalSegments);
    }

    protected QuestProgress(Parcel in) {
        this.questId = in.readString();
        this.questName = in.readString();
        this.questDescription = in.readString();
        this.currentSegment = in.readInt();
        this.totalSegments = in.readInt();
    }

    public static final Parcelable.Creator<QuestProgress> CREATOR = new Parcelable.Creator<QuestProgress>() {
        @Override public QuestProgress createFromParcel(Parcel source) { return new QuestProgress(source); }
        @Override public QuestProgress[] newArray(int size) { return new QuestProgress[size]; }
    };

    @Override public boolean equals(Object obj) {
        return TextUtils.equals(((QuestProgress) obj).questId, questId);
    }
}
