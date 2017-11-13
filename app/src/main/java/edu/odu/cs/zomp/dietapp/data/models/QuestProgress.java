package edu.odu.cs.zomp.dietapp.data.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuestProgress implements Parcelable {

    public String id;
    public String questName;
    public int currentSegment;
    public int totalSegments;

    public QuestProgress() { }

    public QuestProgress(String id, String questName, int currentSegment, int totalSegments) {
        this.id = id;
        this.questName = questName;
        this.currentSegment = currentSegment;
        this.totalSegments = totalSegments;
    }


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.questName);
        dest.writeInt(this.currentSegment);
        dest.writeInt(this.totalSegments);
    }

    protected QuestProgress(Parcel in) {
        this.id = in.readString();
        this.questName = in.readString();
        this.currentSegment = in.readInt();
        this.totalSegments = in.readInt();
    }

    public static final Parcelable.Creator<QuestProgress> CREATOR = new Parcelable.Creator<QuestProgress>() {
        @Override public QuestProgress createFromParcel(Parcel source) { return new QuestProgress(source); }
        @Override public QuestProgress[] newArray(int size) { return new QuestProgress[size]; }
    };
}
