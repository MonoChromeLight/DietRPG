package edu.odu.cs.zomp.dietapp.net.yummly.models;

import java.util.List;

public class RecipeResult {

    public Criteria criteria;
    public List<Match> matches = null;
    public FacetCounts facetCounts;
    public Integer totalMatchCount;
    public Attribution attribution;
}