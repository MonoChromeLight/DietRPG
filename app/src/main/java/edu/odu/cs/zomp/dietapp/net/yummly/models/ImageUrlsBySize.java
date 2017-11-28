package edu.odu.cs.zomp.dietapp.net.yummly.models;


import android.os.Parcel;
import android.os.Parcelable;

public class ImageUrlsBySize implements Parcelable {

    private String _90;

    public String get90() { return _90; }
    public void set90(String _90) { this._90 = _90; }

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) { dest.writeString(this._90); }

    public ImageUrlsBySize() { }

    protected ImageUrlsBySize(Parcel in) { this._90 = in.readString(); }

    public static final Parcelable.Creator<ImageUrlsBySize> CREATOR = new Parcelable.Creator<ImageUrlsBySize>() {
        @Override public ImageUrlsBySize createFromParcel(Parcel source) { return new ImageUrlsBySize(source); }
        @Override public ImageUrlsBySize[] newArray(int size) { return new ImageUrlsBySize[size]; }
    };
}