package edu.odu.cs.zomp.dietapp.data.models;

public class Diet {

    public String id;
    public String title;
    public String queryParam;

    public Diet() {
        this.id = null;
        this.title = null;
        this.queryParam = null;
    }

    public Diet(String id, String title, String queryParam) {
        this.id = id;
        this.title = title;
        this.queryParam = queryParam;
    }
}
