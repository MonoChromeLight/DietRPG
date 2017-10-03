package edu.odu.cs.zomp.dietapp.data.models;

public class Diet {

    public String id;
    public String title;

    public Diet() {
        this.id = null;
        this.title = null;
    }

    public Diet(String id, String title) {
        this.id = id;
        this.title = title;
    }
}
