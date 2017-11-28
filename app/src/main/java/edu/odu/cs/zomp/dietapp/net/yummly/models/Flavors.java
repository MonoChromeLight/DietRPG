package edu.odu.cs.zomp.dietapp.net.yummly.models;


import android.os.Parcel;
import android.os.Parcelable;

public class Flavors implements Parcelable {

    public Double piquant;
    public Double meaty;
    public Double bitter;
    public Double sweet;
    public Double sour;
    public Double salty;

    @Override public int describeContents() { return 0; }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.piquant);
        dest.writeValue(this.meaty);
        dest.writeValue(this.bitter);
        dest.writeValue(this.sweet);
        dest.writeValue(this.sour);
        dest.writeValue(this.salty);
    }

    public Flavors() { }

    protected Flavors(Parcel in) {
        this.piquant = (Double) in.readValue(Double.class.getClassLoader());
        this.meaty = (Double) in.readValue(Double.class.getClassLoader());
        this.bitter = (Double) in.readValue(Double.class.getClassLoader());
        this.sweet = (Double) in.readValue(Double.class.getClassLoader());
        this.sour = (Double) in.readValue(Double.class.getClassLoader());
        this.salty = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<Flavors> CREATOR = new Parcelable.Creator<Flavors>() {
        @Override public Flavors createFromParcel(Parcel source) { return new Flavors(source); }
        @Override public Flavors[] newArray(int size) { return new Flavors[size]; }
    };
}