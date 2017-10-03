package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Quest implements Parcelable {

    public String title;
    public int totalSegments;
    public int completedSegments;

    public Quest() { }

    public Quest(String title, int completedSegments, int totalSegments) {
        this.title = title;
        this.completedSegments = completedSegments;
        this.totalSegments = totalSegments;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.totalSegments);
        dest.writeInt(this.completedSegments);
    }

    protected Quest(Parcel in) {
        this.title = in.readString();
        this.totalSegments = in.readInt();
        this.completedSegments = in.readInt();
    }

    public static final Parcelable.Creator<Quest> CREATOR = new Parcelable.Creator<Quest>() {
        @Override
        public Quest createFromParcel(Parcel source) {
            return new Quest(source);
        }

        @Override
        public Quest[] newArray(int size) {
            return new Quest[size];
        }
    };
}
