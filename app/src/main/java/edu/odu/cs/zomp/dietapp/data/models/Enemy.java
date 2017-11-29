package edu.odu.cs.zomp.dietapp.data.models;


import android.os.Parcel;

import edu.odu.cs.zomp.dietapp.util.Constants;

public class Enemy extends Actor  {

    public String name;

    // Returns true if enemy dies
    public boolean takeDamage(int initialDmg) {
        int defense = this.attributes.get(Constants.ATTRIBUTE_PDEF);
        int hp = this.stats.get(Constants.STAT_HEALTH);
        int damage = (initialDmg - defense) < 0 ? 0 : (initialDmg - defense);
        if (hp - damage <= 0) {
            return true;
        } else {
            hp -= damage;
            this.stats.put(Constants.STAT_HEALTH, hp);
            return false;
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.name);
    }

    public Enemy() {
        super();
    }

    protected Enemy(Parcel in) {
        super(in);
        this.name = in.readString();
    }

    public static final Creator<Enemy> CREATOR = new Creator<Enemy>() {
        @Override public Enemy createFromParcel(Parcel source) {return new Enemy(source);}
        @Override public Enemy[] newArray(int size) {return new Enemy[size];}
    };
}
