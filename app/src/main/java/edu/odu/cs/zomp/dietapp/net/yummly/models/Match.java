package edu.odu.cs.zomp.dietapp.net.yummly.models;

import java.util.List;

public class Match {

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
}