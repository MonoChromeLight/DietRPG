package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;

public class Enemy extends Actor  {

    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }

    public Enemy() {
    }

    protected Enemy(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<Enemy> CREATOR = new Creator<Enemy>() {
        @Override
        public Enemy createFromParcel(Parcel source) {
            return new Enemy(source);
        }

        @Override
        public Enemy[] newArray(int size) {
            return new Enemy[size];
        }
    };
}
