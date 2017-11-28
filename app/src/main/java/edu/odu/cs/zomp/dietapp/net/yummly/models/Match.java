package edu.odu.cs.zomp.dietapp.net.yummly.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Match implements Parcelable {

    public ImageUrlsBySize imageUrlsBySize;
    public String sourceDisplayName;
    public List<String> ingredients = null;
    public String id;
    public List<String> smallImageUrls = null;
    public String recipeName;
    public Integer totalTimeInSeconds;
    public Attributes attributes;
    public Flavors flavors;
    public Integer rating;

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.imageUrlsBySize, flags);
        dest.writeString(this.sourceDisplayName);
        dest.writeStringList(this.ingredients);
        dest.writeString(this.id);
        dest.writeStringList(this.smallImageUrls);
        dest.writeString(this.recipeName);
        dest.writeValue(this.totalTimeInSeconds);
        dest.writeParcelable(this.attributes, flags);
        dest.writeParcelable(this.flavors, flags);
        dest.writeValue(this.rating);
    }

    public Match() { }

    protected Match(Parcel in) {
        this.imageUrlsBySize = in.readParcelable(ImageUrlsBySize.class.getClassLoader());
        this.sourceDisplayName = in.readString();
        this.ingredients = in.createStringArrayList();
        this.id = in.readString();
        this.smallImageUrls = in.createStringArrayList();
        this.recipeName = in.readString();
        this.totalTimeInSeconds = (Integer) in.readValue(Integer.class.getClassLoader());
        this.attributes = in.readParcelable(Attributes.class.getClassLoader());
        this.flavors = in.readParcelable(Flavors.class.getClassLoader());
        this.rating = (Integer) in.readValue(Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>() {
        @Override public Match createFromParcel(Parcel source) { return new Match(source); }
        @Override public Match[] newArray(int size) { return new Match[size]; }
    };
}