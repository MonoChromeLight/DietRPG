package edu.odu.cs.zomp.dietapp.net.yummly.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Attributes implements Parcelable {

    public List<String> course = null;
    public List<String> holiday = null;


    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.course);
        dest.writeStringList(this.holiday);
    }

    public Attributes() { }

    protected Attributes(Parcel in) {
        this.course = in.createStringArrayList();
        this.holiday = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Attributes> CREATOR = new Parcelable.Creator<Attributes>() {
        @Override public Attributes createFromParcel(Parcel source) { return new Attributes(source); }
        @Override public Attributes[] newArray(int size) { return new Attributes[size]; }
    };
}