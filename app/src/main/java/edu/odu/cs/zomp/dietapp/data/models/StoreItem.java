package edu.odu.cs.zomp.dietapp.data.models;

/**
 * Created by david on 4/17/17.
 */

public class StoreItem {

    public String id;
    public String itemTitle;
    public String category;
    public double price;

    public StoreItem() { }

    public StoreItem(String id, String itemTitle, String category, double price) {
        this.id = id;
        this.itemTitle = itemTitle;
        this.category = category;
        this.price = price;
    }
}
